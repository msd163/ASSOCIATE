package WSM.society.agent;

import SiM.report.ImageBuilder;
import _type.*;
import SiM.monitor.DrawingWindowRunner;
import WSM.internet.Internet;
import SiM.engine.Engine;
import SiM.profiler.config.TrustConfigItem;
import SiM.statistics.EpisodeStatistics;
import SiM.statistics.WorldStatistics;
import WSM.society.environment.Environment;
import WSM.society.environment.StateX;
import WSM.transition.Router;
import WSM.trust.TrustManager;
import WSM.trust.TrustMatrix;
import WSM.trust.consensus.CertContract;
import WSM.trust.consensus.DaGra;
import core.utils.*;
import core.parellel.AgentObservationRunner;
import core.parellel.AgentUpdaterRunner;
import core.parellel.ShareObservationRunner;

import java.util.ArrayList;
import java.util.List;

public class World {

    public World(int id, Engine engine, TrustConfigItem trustConfigItem) {
        this.id = id;
        this.engine = engine;
        this.trustConfigItem = trustConfigItem;
        if (Config.DRAWING_SHOW_ENABLED()) {
            this.drawingWindowRunner = new DrawingWindowRunner(this);
        }

    }

    private int id;
    private DrawingWindowRunner drawingWindowRunner;

    private Engine engine;

    private List<Agent> agents;             // Sorted agents by capPower

    private int agentsCount;

    private int[] traceAgentIds;            // Ids that will be traced in simulation time, in the MainDiagram window

    private Environment environment;

    private WorldStatistics[] wdStatistics;

    private EpisodeStatistics[] epStatistics;

    private Router router;

    public TrustMatrix matrixGenerator = new TrustMatrix();

    private TrustConfigItem trustConfigItem;

    private TrustManager trustManager;

    private Internet internet;

    //============================//============================//============================
    public void init(Environment _environment) throws Exception {

        internet = new Internet(this);

        initStatistics();

        //-- Identifying the agents that we want to trace in Main diagram.
        //-- Indicating the agent and it's communications in environments with different colors
        traceAgentIds = new int[]{};

        //============================//============================
        wdStatistics = new WorldStatistics[Config.WORLD_LIFE_TIME];

        trustManager = new TrustManager(trustConfigItem, wdStatistics);

        router = new Router(this);
        //============================ Setting agents count

        agentsCount = _environment.getAgentsCount();

        //============================ Initializing Environment
        this.environment = _environment;
        this.environment.initForSimulation(this);

        //============================//============================  Initializing agents
        System.out.println(
                " | agentsCount: " + agentsCount
        );

        ArrayList<StateX> states = environment.getStates();
        agents = new ArrayList<>();
        int i;
        for (StateX state : states) {
            System.out.println(">>    State: " + state.getId());
            for (Agent agent : state.getAgents()) {
                System.out.println("                    Agent: " + agent.getId());
                agent.setState(state);
                agent.setWorld(this);
                agent.initVars();

                //============================ filling state array according to stateId array
                agent.updateTargets();

                //-- First updating travel history as initialization state
                if (agent.getState() != null) {
                    agent.updateTravelHistory();
                }

                //============================  if agentId is in 'traceAgentIds', it will set as traceable
                if (isTraceable(agent.getId())) {
                    agent.setAsTraceable();
                }

                //System.out.println("Full world:::init::agent: " + agent.getId() + " state: " + agent.getState().getId() + " target: " + (agent.getCurrentTarget() != null ? agent.getCurrentTarget().getId() : "NULL"));

                agents.add(agent);
            }
        }

        System.out.println("> Sorting is stated...");
        agents.sort((Agent a1, Agent a2) -> {
            if (a1.getCapacity().getCapPower() > a2.getCapacity().getCapPower()) {
                return -1;
            }
            if (a1.getCapacity().getCapPower() < a2.getCapacity().getCapPower()) {
                return 1;
            }
            return 0;
        });

        System.out.println("Indexing is started...");
        //-- Setting index of agent in sorted list
        int indexInSortedList = 0;
        for (Agent agent : agents) {
            agent.setIndex(indexInSortedList++);
            agent.getTrust().postInit();
        }

        System.out.println("Updating agent count...");
        environment.updateAgentsCount();

        System.out.println("Reassigning state location...");
        environment.reassigningStateLocationAndTransPath();

        // Resetting the timer of the world.
        Globals.WORLD_TIMER = 0;

        for (i = 0; i < wdStatistics.length; i++) {
            if (i == 0) {
                wdStatistics[i] = new WorldStatistics(null, null, environment);
            } else {
                if (i >= Config.STATISTICS_AVERAGE_TIME_WINDOW) {
                    wdStatistics[i] = new WorldStatistics(wdStatistics[i - 1], wdStatistics[i - Config.STATISTICS_AVERAGE_TIME_WINDOW], environment);
                } else {
                    wdStatistics[i] = new WorldStatistics(wdStatistics[i - 1], null, environment);
                }
            }
        }

   /*     if (Config.SIMULATION_MODE == TtSimulationMode.Episodic) {
            int maxEpisodeCount = Math.max(environment.getProMax().getMaxAgentTargetCount(), Config.WORLD_LIFE_TIME / Config.EPISODE_TIMOUT);
            epStatistics = new EpisodeStatistics[maxEpisodeCount];
            for (i = 0; i < maxEpisodeCount; i++) {
                epStatistics[i] = new EpisodeStatistics();
            }
        }*/

        //============================//============================ Init trust matrix
        System.out.println("Initializing TrustMatrix...");
        initTrustMatrix();

        //============================//============================ Init DaGra
        System.out.println("Initializing DaGra...");
        initDaGra();

        if (Config.RUNTIME_THREAD_COUNT > 1) {
            AgentUpdaterRunner.init(agents, router);
            AgentObservationRunner.init(agents, trustManager);
            if (trustConfigItem.isIsShareObservationWithNeighbors()) {
                ShareObservationRunner.init(agents, trustManager);
            }
        }
    }

    private void initDaGra() {
        /* Creating Genesis Certification and broadcasting */
        CertContract genesis = new CertContract(-1, -1);
        genesis.setRequestTime(Globals.WORLD_TIMER);
        genesis.setIsGenesis(true);
        genesis.setStatus(TtDaGraContractStatus.Accept_Accept);
        Agent genesisAgent = new Agent(this, -99);
        genesisAgent.setIndex(-99);
        genesis.setRequester(genesisAgent);
        for (int i = 0, j = 0, agentsSize = agents.size(); i < agentsSize; i++) {
            Agent agent = agents.get(i);
            if (agent.getTrust().isHasCandidateForCertification()) {
                System.out.println("| " + j++ + "  " + i);
                if (j % 20 == 0) {
                    System.out.println();
                }
                agent.setDaGra(new DaGra(agent));
                agent.getDaGra().setGenesis(genesis.clone());
                //agent.getDaGra().assignMyContract();
            }
        }

    }

    //============================//============================//============================


    private void initStatistics() {
        //============================//============================ Initializing statistics report file
        if (Config.STATISTICS_IS_GENERATE) {

            String statName = Globals.STATS_FILE_NAME;

            System.out.println(statName);

            //-- Creating environment statistics file
            Globals.statsEnvGenerator.init(ProjectPath.instance().statisticsDir() + "/" + statName, statName + ".csv");

            //-- Creating trust statistics file
            Globals.statsTrustGenerator.init(ProjectPath.instance().statisticsDir() + "/" + statName, statName + ".trust.csv");

        }
    }

    private void initTrustMatrix() {
        if (Config.TRUST_MATRIX_IS_ON) {
            matrixGenerator.init(agents);
        }
    }

    private boolean isTraceable(int i) {

        if (traceAgentIds != null) {
            for (int t : traceAgentIds) {
                if (t == i) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * ============================
     * RUN
     * ==============================
     **/
    public void run() throws InterruptedException {

        if (Config.DRAWING_SHOW_ENABLED()) {
            drawingWindowRunner.initDrawingWindows(matrixGenerator);
            drawingWindowRunner.start();
        }

        /* ****************************
         *            MAIN LOOP      *
         * ****************************/

        if (Config.SIMULATION_MODE == TtSimulationMode.Episodic) {
            epStatistics[0].setFromTime(Globals.WORLD_TIMER);
        }
        //============================//============================  Main loop of running in a world
        for (; Globals.WORLD_TIMER < Config.WORLD_LIFE_TIME; Globals.WORLD_TIMER++) {

            Globals.DAGRA_REQUEST_STAGE__REQUESTED_COUNT_IN_CURRENT_PERIOD = 0;

            WorldStatistics wdStats = wdStatistics[Globals.WORLD_TIMER];
            wdStats.setWorldTime(Globals.WORLD_TIMER);
            wdStats.init(Globals.EPISODE);

            router.setStatistics(wdStats);

            if (Globals.WORLD_TIMER == 0 && Config.STATISTICS_IS_GENERATE) {
                Globals.statsEnvGenerator.addComment(environment);
                Globals.statsEnvGenerator.addHeader();
                Globals.statsTrustGenerator.addHeader();
            }

            System.out.println("World: " + Globals.SIMULATION_TIMER + " Time: " + Globals.WORLD_TIMER + " > run ------------------------------- ");

            //============================//============================  Updating agents statuses
            System.out.println("> updating agents' profile, watched list, and next steps...");


            if (Config.RUNTIME_THREAD_COUNT > 1) {
                AgentUpdaterRunner.execute();
            } else {
                for (int i = 0, agentsSize = agents.size(); i < agentsSize; i++) {
                    Agent agent = agents.get(i);
                    // System.out.print("World: " + Globals.SIMULATION_TIMER + " Time: " + Globals.WORLD_TIMER + "  | " + i );
                    //todo: adding doing service capacity to agents as capacity param
                    //System.out.print(" | 1 > profile...");
                    agent.updateProfile();
                    // System.out.print(" | 2 > watchList...");
                    agent.updateWatchList();
                    // System.out.print(" | 3 > nextStep...");
                    router.updateNextSteps(agent);
                }
            }


            System.out.println("> go to next step...");
            //============================//============================ Traveling
            for (Agent agent : agents) {
                router.takeAStepTowardTheTarget(agent);
            }

            //============================//============================ Observation
            if (trustConfigItem.isIsUseObservation() || trustConfigItem.isIsUseIndirectObservation()) {
                System.out.println("> observing...");
                if (Config.RUNTIME_THREAD_COUNT > 1) {
                    AgentObservationRunner.execute();
                } else {
                    for (Agent agent : agents) {
                        if (agent.getCapacity().getObservationCap() > 0) {
                            trustManager.observe(agent);
                        }
                    }
                }
            }

            //============================//============================ Sharing Observation

            if (Config.RUNTIME_THREAD_COUNT > 1 && trustConfigItem.isIsShareObservationWithNeighbors()) {
                ShareObservationRunner.execute();
            }

            //============================//============================ Sharing With Internet

            if (
                    (
                            trustConfigItem.getTtMethod() == TtTrustMethodology.TrustMode_ShortPath ||
                                    trustConfigItem.getTtMethod() == TtTrustMethodology.TrustMode_RandomPath
                    )
                            && trustConfigItem.isIsUseSharingRecommendationWithInternet()
                            && (Globals.WORLD_TIMER % trustConfigItem.getSharingRecommendationWithInternetPeriod() == 0)
            ) {
                System.out.println("> sending recommendation through Internet...");
                trustManager.sendRecommendationsWithInternet(internet.getAgentList());
            }


            //============================//============================ DaGra processes

            /* Updating all contracts status and filling toBeSignedContracts and toBeVerifiedContracts lists  */
            if (
                    (
                            trustConfigItem.getTtMethod() == TtTrustMethodology.TrustMode_ShortPath ||
                                    trustConfigItem.getTtMethod() == TtTrustMethodology.TrustMode_RandomPath
                    )
                            && trustConfigItem.getCert().isIsUseCertification()
                            && trustConfigItem.getCert().isIsUseDaGra()) {
                System.out.println("> DaGra: updating status and list...");

                Agent baseCandidateAgent = null;                         // for saving the first agent that is candidate for certification, and its trust data is updated.
                boolean isFirstCandidateAgentIsFoundAsBase = false;
                for (int i = 0, agentsSize = agents.size(); i < agentsSize; i++) {
                    Agent agent = agents.get(i);
                    if (agent.getTrust().isHasCandidateForCertification()) {
                        if (Config.TURBO_CERTIFIED_DAGRA_SINGLE_UPDATE_MULTIPLE_CLONE) {
                            if (!isFirstCandidateAgentIsFoundAsBase) {
                                agent.getDaGra().updatingStatusAndList();
                                isFirstCandidateAgentIsFoundAsBase = true;
                                baseCandidateAgent = agent;
                            } else {
                                agent.getDaGra().updatingStatusAndList(baseCandidateAgent.getDaGra());
                            }
                        } else {
                            agent.getDaGra().updatingStatusAndList();
                        }
                    }
                }

                for (Agent selectedAgent : agents) {
                    if (selectedAgent.getTrust().isHasCandidateForCertification()) {

                        if (selectedAgent.getDaGra().getContracts() != null) {
                            for (CertContract contract : selectedAgent.getDaGra().getContracts()) {
                                if (contract.getStatus() == TtDaGraContractStatus.Request_New) {
                                    wdStats.getStatisticsDagra().add_requestNewCount();
                                } else if (contract.getStatus() == TtDaGraContractStatus.Request_Signing) {
                                    wdStats.getStatisticsDagra().add_requestSingingCount();
                                } else if (contract.getStatus() == TtDaGraContractStatus.Request_Verifying) {
                                    wdStats.getStatisticsDagra().add_requestVerifyingCount();
                                } else if (contract.getStatus() == TtDaGraContractStatus.Accept_New) {
                                    wdStats.getStatisticsDagra().add_acceptNewCount();
                                } else if (contract.getStatus() == TtDaGraContractStatus.Accept_Signing) {
                                    wdStats.getStatisticsDagra().add_acceptSigningCount();
                                } else if (contract.getStatus() == TtDaGraContractStatus.Accept_Verifying) {
                                    wdStats.getStatisticsDagra().add_acceptVerifyingCount();
                                } else if (contract.getStatus() == TtDaGraContractStatus.Accept_Accept) {
                                    wdStats.getStatisticsDagra().add_acceptAcceptCount();
                                } else if (contract.getStatus() == TtDaGraContractStatus.Expired) {
                                    if (!contract.isIsOldExpired()) {
                                        wdStats.getStatisticsDagra().add_expiredContractCount();
                                        contract.setIsOldExpired(true);
                                    }
                                } else if (contract.getStatus() == TtDaGraContractStatus.NoContract) {
                                    wdStats.getStatisticsDagra().add_noContractCount();
                                }
                            }
                            break;
                        }
                    }
                }

                /* Creating a list for agents that have registration request, and sent a certain request to the DaGra randomly*/
                /* If the request period has arrived */
                if ((Globals.WORLD_TIMER + 1) % trustConfigItem.getCert().getCertRequestPeriodTime_DaGra() == 0) {
                    System.out.println("> DaGra: sending new requests...");
                    /* If the maximum allowed number of requests is not consumed */
                    if (Globals.DAGRA_REQUEST_STAGE__REQUESTED_COUNT_IN_CURRENT_PERIOD <= trustConfigItem.getCert().getNumberOfCertRequestInEachPeriod_DaGra()) {

                        /* Creating register request list */
                        List<Agent> registerRequestList = new ArrayList<>();
                        for (Agent agent : agents) {
                            if (agent.getTrust().isHasCandidateForCertification()) {
                                if (agent.getDaGra().isHasRegisterRequest()) {
                                    registerRequestList.add(agent);
                                }
                            }
                        }

                        /* As long as there is a request, and the capacity of request registration is not full. */
                        while (registerRequestList.size() > 0 &&
                                Globals.DAGRA_REQUEST_STAGE__REQUESTED_COUNT_IN_CURRENT_PERIOD
                                        <= trustConfigItem.getCert().getNumberOfCertRequestInEachPeriod_DaGra()) {

                            /* Selecting a requester randomly */
                            //todo: IDEA: adding a random algorithm for selecting 'NumberOfCertRequestInEachPeriod_DaGra' agents randomly.
                            int nextInt = Globals.RANDOM.nextInt(registerRequestList.size());
                            Agent selectedAgent = registerRequestList.remove(nextInt);
                            /* Sending a request to DaGra */
                            selectedAgent.getDaGra().sendRegisterRequest();
                            Globals.DAGRA_REQUEST_STAGE__REQUESTED_COUNT_IN_CURRENT_PERIOD++;
                        }
                    }
                }

                /* Processing DaGra for all statues EXCEPT 'NoContract' and 'Expired' statuses */
                System.out.println("> DaGra: processing...");
                for (Agent agent : agents) {
                    if (agent.getTrust().isHasCandidateForCertification()) {
                        OutLog____.pl(TtOutLogMethodSection.Main, TtOutLogStatus.SUCCESS, ">> Agents with certification Cap. agentId: " + agent.getId());
                        agent.getDaGra().process();
                    }
                }
            }
            //============================//============================  updating full state statistics
            System.out.println("> updating statistics");
            for (StateX state : environment.getStates()) {
                if (state.isFullCapability()) {
                    wdStats.addFullStateCount();
                }
                if (state.getTargets().size() == 0) {
                    wdStats.addStatesWithNoTarget();
                }
            }

            if (Config.TRUST_MATRIX_IS_ON) {
                System.out.println("> updating trust matrix");
                matrixGenerator.update(wdStats);
            }
            if (Config.STATISTICS_IS_GENERATE) {
                System.out.println("> adding statistics to generator");
                Globals.statsEnvGenerator.addStat(wdStats);
                Globals.statsTrustGenerator.addStat(wdStats);
            }
            //============================//============================ Repainting


            //============================//============================//============================ Adding Episode of environment
            // and exiting the agents from pitfalls
            /*if ((Globals.WORLD_TIMER + 1) % Config.EPISODE_TIMOUT == 0 || wdStats.getAllAgentsInTarget() + wdStats.getAllAgentsInPitfall() == agentsCount) {


                if (Globals.WORLD_TIMER > 0) {
                    //-- updating episode statistics
                    epStatistics[Globals.EPISODE].update(wdStatistics);
                    epStatistics[Globals.EPISODE].setFromTime(Globals.WORLD_TIMER);
                }

                for (StateX state : environment.getStates()) {
                    state.getAgents().clear();
                }

                //-- Increasing Episode
                Globals.EPISODE++;

                for (Agent agent : agents) {

                    if (agent.getState().isIsPitfall()) {
                        agent.setState(agent.getCurrentTarget());
                    }
                    int ct = agent.getCurrentTarget().getId();
                    agent.assignNextTargetState();
                    OutLog____.pl(TtOutLogMethodSection.TakeAStepTowardTheTarget, TtOutLogStatus.SUCCESS,
                            "Assigning new target to agent (" + agent.getId() + "). current target: " + ct + " | new target: " + agent.getCurrentTarget().getId());

                }

            }*/

            while (Globals.PAUSE) {
                Thread.sleep(Config.WORLD_SLEEP_MILLISECOND_IN_PAUSE);
            }

            if (Config.WORLD_SLEEP_MILLISECOND > 0) {
                try {
                    Thread.sleep(Config.WORLD_SLEEP_MILLISECOND);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        //============================//============================ Creating trust matrix and saving in csv file

        if (Config.TRUST_MATRIX_IS_GENERATE) {
            System.out.println("Generating Trust Matrix");
            String matrixPath = Globals.STATS_FILE_NAME;

            String simDir = "/sim-" + (Globals.SIMULATION_TIMER < 10 ? "0" + Globals.SIMULATION_TIMER : Globals.SIMULATION_TIMER);

            matrixPath = ProjectPath.instance().statisticsDir() + "/" + matrixPath + simDir + "/" + matrixPath + ".mat.csv";

            //matrixGenerator.update(null);
            matrixGenerator.write(matrixPath);
            matrixGenerator.close();
            System.out.println("Trust Matrix Generated.");
        }

        if (Config.STATISTICS_IS_GENERATE && Config.STATISTICS_IS_SAVE_IMAGE && Config.DRAWING_SHOW_ENABLED()) {
            new ImageBuilder().generateStatisticsImages(
                    drawingWindowRunner.getStateMachineDrawingWindow(),
                    drawingWindowRunner.getTravelStatsLinearDrawingWindow(),
                    drawingWindowRunner.getTrustMatrixDrawingWindow(),
                    drawingWindowRunner.getTrustStatsLinearDrawingWindow(),
                    drawingWindowRunner.getTrustRecogniseLinearDrawingWindow(),
                    drawingWindowRunner.getTrustAnalysisLinearDrawingWindow(),
                    drawingWindowRunner.getObservationBarDrawingWindow(),
                    drawingWindowRunner.getRecommendationBarDrawingWindow(),
                    drawingWindowRunner.getTravelHistoryBarDrawingWindow(),
                    drawingWindowRunner.getExperienceBarDrawingWindow(),
                    drawingWindowRunner.getIndirectExperienceBarDrawingWindow(),
                    drawingWindowRunner.getIndirectObservationBarDrawingWindow()
            );
        }
        System.out.println("Finished");


        //============================//============================ Running program after finishing lifeTime of the world.


        /*while (Globals.SIMULATION_TIMER == Globals.SIMULATION_ROUND - 1) {
            updateWindows(stateMachineDW, statsOfEnvDW, trustMatrixDW, statsOfTrustDW, statsOfFalsePoNeDW,
                    analysisOfTrustParamsDW, agentTravelInfoDW, agentTrustDW, agentRecommendationDW, agentObservationDW);
        }*/
    }  //  End of running


    //============================//============================//============================

    public String toStringInfo() {
        StringBuilder tx = new StringBuilder("\n");
        StringBuilder ti = new StringBuilder("\n\n\n\t");

        return tx + "World: " +
                ti + " | agentsCount=" + agentsCount;
    }

    public String getDrawingTitle() {
        return "E-Code: " + environment.getCode() + " | #Ags: " + agentsCount + " | #Sts: " + environment.getStateCount();
    }

    public String getSimulationConfigInfo() {
        return engine.getTrustConfigBunch().getByIndex(id).getInfo(environment.getCertifiedCount());
    }

    public String getSimulationConfigInfo(int i) {
        switch (i) {
            case 1:
                return engine.getTrustConfigBunch().getByIndex(id).getInfo_1();
            case 2:
                return engine.getTrustConfigBunch().getByIndex(id).getInfo_2();
            case 3:
                return engine.getTrustConfigBunch().getByIndex(id).getInfo_3(environment.getCertifiedCount());
            case 4:
                return engine.getTrustConfigBunch().getByIndex(id).getInfo_4();
        }
        return engine.getTrustConfigBunch().getByIndex(id).getInfo_1();
    }

    public String toString(int tabIndex) {
        tabIndex++;
        StringBuilder tx = new StringBuilder("\n");
        StringBuilder ti = new StringBuilder("\n");
        for (int i = 0; i <= tabIndex; i++) {
            if (i > 1) {
                tx.append("\t");
            }
            ti.append("\t");
        }
        return tx + "World{" +
                ti + ", agentsCount=" + agentsCount +
                tx + '}';
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public int getAgentsCount() {
        return agentsCount;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public WorldStatistics[] getWdStatistics() {
        return wdStatistics;
    }

    public EpisodeStatistics[] getEpStatistics() {
        return epStatistics;
    }

    public TrustConfigItem getSimulationConfig() {
        return trustConfigItem;
    }

    public TrustManager getTrustManager() {
        return trustManager;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void destroy() {
        if (Config.OPTIMIZE_MEMORY) {
            for (int i = 0; i < agents.size(); i++) {
                agents.get(i).destroy();
                agents.set(i, null);
            }
            if (this.matrixGenerator != null) {
                this.matrixGenerator.destroy();
                this.matrixGenerator = null;
            }

            if (environment != null) {
                environment.destroy();
            }
        }
    }
}
