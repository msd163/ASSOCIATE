package systemLayer;

import _type.*;
import drawingLayer.DrawingWindow;
import drawingLayer.routing.StateMachineDrawingWindow;
import drawingLayer.routing.TravelHistoryBarDrawingWindow;
import drawingLayer.routing.TravelStatsLinearDrawingWindow;
import drawingLayer.trust.*;
import environmentLayer.Environment;
import environmentLayer.StateX;
import internetLayer.Internet;
import simulateLayer.SimulationConfigItem;
import simulateLayer.Simulator;
import simulateLayer.statistics.EpisodeStatistics;
import simulateLayer.statistics.WorldStatistics;
import transitionLayer.Router;
import trustLayer.TrustManager;
import trustLayer.TrustMatrix;
import trustLayer.consensus.CertContract;
import trustLayer.consensus.DaGra;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {

    public World(int id, Simulator simulator, SimulationConfigItem simulationConfigItem) {
        this.id = id;
        this.simulator = simulator;
        this.simulationConfigItem = simulationConfigItem;
    }

    private int id;

    private Simulator simulator;

    private List<Agent> agents;             // Sorted agents by capPower

    private int agentsCount;

    private int[] traceAgentIds;            // Ids that will be traced in simulation time, in the MainDiagram window

    private Environment environment;

    private WorldStatistics[] wdStatistics;

    private EpisodeStatistics[] epStatistics;

    private Router router;

    TrustMatrix matrixGenerator = new TrustMatrix();

    private SimulationConfigItem simulationConfigItem;

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
        trustManager = new TrustManager(simulationConfigItem);

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

        wdStatistics = new WorldStatistics[Config.WORLD_LIFE_TIME];
        for (i = 0; i < wdStatistics.length; i++) {
            if (i == 0) {
                wdStatistics[i] = new WorldStatistics(null, agentsCount, null);
            } else {
                if (i >= Config.STATISTICS_AVERAGE_TIME_WINDOW) {
                    wdStatistics[i] = new WorldStatistics(wdStatistics[i - 1], agentsCount, wdStatistics[i - Config.STATISTICS_AVERAGE_TIME_WINDOW]);
                } else {
                    wdStatistics[i] = new WorldStatistics(wdStatistics[i - 1], agentsCount, null);
                }
            }
        }

        if (Config.SIMULATION_MODE == TtSimulationMode.Episodic) {
            int maxEpisodeCount = Math.max(environment.getProMax().getMaxAgentTargetCount(), Config.WORLD_LIFE_TIME / Config.EPISODE_TIMOUT);
            epStatistics = new EpisodeStatistics[maxEpisodeCount];
            for (i = 0; i < maxEpisodeCount; i++) {
                epStatistics[i] = new EpisodeStatistics();
            }
        }

        //============================//============================ Init trust matrix
        System.out.println("Initializing TrustMatrix...");
        initTrustMatrix();

        //============================//============================ Init DaGra
        System.out.println("Initializing DaGra...");
        initDaGra();

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
        for (Agent agent : agents) {
            if (agent.getTrust().isHasCandidateForCertification()) {
                agent.setDaGra(new DaGra(agent));
                agent.getDaGra().setGenesis(genesis.clone());
                //agent.getDaGra().assignMyContract();
            }
        }

    }

    //============================//============================//============================
    private StateMachineDrawingWindow stateMachineDrawingWindow;
    private TravelStatsLinearDrawingWindow travelStatsLinearDrawingWindow;
    private TravelHistoryBarDrawingWindow travelHistoryBarDrawingWindow;

    private TrustMatrixDrawingWindow trustMatrixDrawingWindow;

    private TrustStatsLinearDrawingWindow trustStatsLinearDrawingWindow;
    private TrustRecogniseLinearDrawingWindow trustRecogniseLinearDrawingWindow;
    private TrustAnalysisLinearDrawingWindow trustAnalysisLinearDrawingWindow;

    private ExperienceBarDrawingWindow experienceBarDrawingWindow;
    private IndirectExperienceBarDrawingWindow indirectExperienceBarDrawingWindow;

    private ObservationBarDrawingWindow observationBarDrawingWindow;
    private IndirectObservationBarDrawingWindow indirectObservationBarDrawingWindow;


    private RecommendationBarDrawingWindow recommendationBarDrawingWindow;


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
        matrixGenerator.init(agents);
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
    public void run() {

        initDrawingWindows();

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
            for (Agent agent : agents) {
                //todo: adding doing service capacity to agents as capacity param
                agent.updateProfile();
                agent.updateWatchList();
                router.updateNextSteps(agent);
            }

            //============================//============================ Traveling
            for (Agent agent : agents) {
                router.takeAStepTowardTheTarget(agent);
            }

            //============================//============================ Observation
            if (simulationConfigItem.isIsUseObservation() || simulationConfigItem.isIsUseIndirectObservation()) {
                for (Agent agent : agents) {
                    if (agent.getCapacity().getObservationCap() > 0) {
                        trustManager.observe(agent);
                    }
                }
            }

            //============================//============================ Sharing With Internet

            if (simulationConfigItem.isIsUseSharingRecommendationWithInternet()) {
                trustManager.sendRecommendationsWithInternet(internet.getAgentList());
            }


            //============================//============================ DaGra processes
            /* Updating all contracts status and filling toBeSignedContracts and toBeVerifiedContracts lists  */
            for (Agent agent : agents) {
                if (agent.getTrust().isHasCandidateForCertification()) {
                    agent.getDaGra().updatingStatusAndList();
                }
            }

            /* Creating a list for agents that have register request, and sent a certain request to the DaGra randomly*/
            /* If the request period has arrived */
            if ((Globals.WORLD_TIMER + 1) % simulationConfigItem.getCert().getCertRequestPeriodTime_DaGra() == 0) {
                /* If the maximum allowed number of requests is not consumed */
                if (Globals.DAGRA_REQUEST_STAGE__REQUESTED_COUNT_IN_CURRENT_PERIOD <= simulationConfigItem.getCert().getNumberOfCertRequestInEachPeriod_DaGra()) {

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
                                    <= simulationConfigItem.getCert().getNumberOfCertRequestInEachPeriod_DaGra()) {

                        /* Selecting a requester randomly */
                        int nextInt = Globals.RANDOM.nextInt(registerRequestList.size());
                        Agent selectedAgent = registerRequestList.remove(nextInt);
                        /* Sending a request to DaGra */
                        selectedAgent.getDaGra().sendRegisterRequest();
                        Globals.DAGRA_REQUEST_STAGE__REQUESTED_COUNT_IN_CURRENT_PERIOD++;
                    }
                }
            }

            /* Processing DaGra for all statues EXCEPT 'NoContract' and 'Expired' statuses */
            for (Agent agent : agents) {
                if (agent.getTrust().isHasCandidateForCertification()) {
                    OutLog____.pl(TtOutLogMethodSection.Main, TtOutLogStatus.SUCCESS, ">> Agents with certification Cap. agentId: " + agent.getId());
                    agent.getDaGra().process();
                }
            }

            //============================//============================  updating full state statistics
            for (StateX state : environment.getStates()) {
                if (state.isFullCapability()) {
                    wdStats.addFullStateCount();
                }
                if (state.getTargets().size() == 0) {
                    wdStats.addStatesWithNoTarget();
                }
            }

            matrixGenerator.update(wdStats);

            if (Config.STATISTICS_IS_GENERATE) {
                Globals.statsEnvGenerator.addStat(wdStats);
                Globals.statsTrustGenerator.addStat(wdStats);
            }
            //============================//============================ Repainting
            updateWindows();

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
                updateWindows();
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

        if (Config.STATISTICS_IS_GENERATE) {
            new ImageBuilder().generateStatisticsImages(
                    stateMachineDrawingWindow,
                    travelStatsLinearDrawingWindow,
                    trustMatrixDrawingWindow,
                    trustStatsLinearDrawingWindow,
                    trustRecogniseLinearDrawingWindow,
                    trustAnalysisLinearDrawingWindow,
                    observationBarDrawingWindow,
                    recommendationBarDrawingWindow,
                    travelHistoryBarDrawingWindow,
                    experienceBarDrawingWindow,
                    indirectExperienceBarDrawingWindow,
                    indirectObservationBarDrawingWindow
            );
        }
        System.out.println("Finished");


        //============================//============================ Running program after finishing lifeTime of the world.


        /*while (Globals.SIMULATION_TIMER == Globals.SIMULATION_ROUND - 1) {
            updateWindows(stateMachineDW, statsOfEnvDW, trustMatrixDW, statsOfTrustDW, statsOfFalsePoNeDW,
                    analysisOfTrustParamsDW, agentTravelInfoDW, agentTrustDW, agentRecommendationDW, agentObservationDW);
        }*/
    }  //  End of running

    private void initDrawingWindows() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthHalf = (int) screenSize.getWidth() / 2;
        int heightHalf = (int) screenSize.getHeight() / 2;
        //============================ Initializing Main Drawing Windows
        stateMachineDrawingWindow = new StateMachineDrawingWindow(this);
        if (Config.DRAWING_SHOW_stateMachineWindow) {
            initDrawingWindow(widthHalf, heightHalf, stateMachineDrawingWindow, TtDrawingWindowLocation.TopLeft, true);
        }
        //============================ Initializing Diagram Drawing Windows
        travelStatsLinearDrawingWindow = new TravelStatsLinearDrawingWindow(this);
        if (Config.DRAWING_SHOW_travelStatsLinearDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, travelStatsLinearDrawingWindow, TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        trustMatrixDrawingWindow = new TrustMatrixDrawingWindow(matrixGenerator, this);
        if (Config.DRAWING_SHOW_trustMatrixDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, trustMatrixDrawingWindow, TtDrawingWindowLocation.TopLeft);
        }

        //============================ Initializing Diagram Drawing Windows
        trustStatsLinearDrawingWindow = new TrustStatsLinearDrawingWindow(this);
        if (Config.DRAWING_SHOW_trustStatsLinearDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, trustStatsLinearDrawingWindow, TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        trustRecogniseLinearDrawingWindow = new TrustRecogniseLinearDrawingWindow(this);
        if (Config.DRAWING_SHOW_trustRecogniseLinearDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, trustRecogniseLinearDrawingWindow, TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        trustAnalysisLinearDrawingWindow = new TrustAnalysisLinearDrawingWindow(this);
        if (Config.DRAWING_SHOW_trustAnalysisLinearDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, trustAnalysisLinearDrawingWindow, TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        travelHistoryBarDrawingWindow = new TravelHistoryBarDrawingWindow(this);
        if (Config.DRAWING_SHOW_travelHistoryBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, travelHistoryBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Diagram Drawing Windows
        experienceBarDrawingWindow = new ExperienceBarDrawingWindow(this);
        if (Config.DRAWING_SHOW_experienceBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, experienceBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Diagram Drawing Windows
        indirectExperienceBarDrawingWindow = new IndirectExperienceBarDrawingWindow(this);
        if (Config.DRAWING_SHOW_indirectExperienceBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, indirectExperienceBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Recommendation Drawing Windows
        recommendationBarDrawingWindow = new RecommendationBarDrawingWindow(this);
        if (Config.DRAWING_SHOW_recommendationBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, recommendationBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Observation Drawing Windows
        observationBarDrawingWindow = new ObservationBarDrawingWindow(this);
        if (Config.DRAWING_SHOW_observationBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, observationBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }
        //============================ Initializing Observation Drawing Windows
        indirectObservationBarDrawingWindow = new IndirectObservationBarDrawingWindow(this);
        if (Config.DRAWING_SHOW_indirectObservationBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, indirectObservationBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }
    }

    private void initDrawingWindow(int widthHalf, int heightHalf, DrawingWindow stateMachineDW, TtDrawingWindowLocation location) {
        initDrawingWindow(widthHalf, heightHalf, stateMachineDW, location, false);
    }

    private void initDrawingWindow(int widthHalf, int heightHalf, DrawingWindow drawingWindow, TtDrawingWindowLocation location, boolean exitAppOnCLose) {
        drawingWindow.setDoubleBuffered(true);
        JFrame mainFrame = new JFrame();
        mainFrame.getContentPane().add(drawingWindow);
        mainFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
        mainFrame.setVisible(true);
        if (exitAppOnCLose) {
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        mainFrame.setTitle(drawingWindow.getHeaderTitle());
        switch (location) {
            case TopRight:
                mainFrame.setLocation(widthHalf, 0);
                break;
            case BottomLeft:
                mainFrame.setLocation(0, heightHalf);
                break;
            case BottomRight:
                mainFrame.setLocation(widthHalf, heightHalf);
                break;
            case TopLeft:
            default:
                mainFrame.setLocation(0, 0);
                break;
        }
    }


    public void updateWindows() {
        try {
            Thread.sleep(Config.WORLD_SLEEP_MILLISECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Config.DRAWING_SHOW_stateMachineWindow) {
            stateMachineDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_travelStatsLinearDrawingWindow) {
            travelStatsLinearDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_trustMatrixDrawingWindow) {
            trustMatrixDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_trustStatsLinearDrawingWindow) {
            trustStatsLinearDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_trustRecogniseLinearDrawingWindow) {
            trustRecogniseLinearDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_trustAnalysisLinearDrawingWindow) {
            trustAnalysisLinearDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_travelHistoryBarDrawingWindow) {
            travelHistoryBarDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_experienceBarDrawingWindow) {
            experienceBarDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_indirectExperienceBarDrawingWindow) {
            indirectExperienceBarDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_recommendationBarDrawingWindow) {
            recommendationBarDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_observationBarDrawingWindow) {
            observationBarDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_indirectObservationBarDrawingWindow) {
            indirectObservationBarDrawingWindow.repaint();
        }

        simulator.updateWindows();
    }

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
        return simulator.getSimulationConfigBunch().getByIndex(id).getInfo(environment.getCertifiedCount());
    }

    public String getSimulationConfigInfo(int i) {
        switch (i) {
            case 1:
                return simulator.getSimulationConfigBunch().getByIndex(id).getInfo_1();
            case 2:
                return simulator.getSimulationConfigBunch().getByIndex(id).getInfo_2();
            case 3:
                return simulator.getSimulationConfigBunch().getByIndex(id).getInfo_3(environment.getCertifiedCount());
            case 4:
                return simulator.getSimulationConfigBunch().getByIndex(id).getInfo_4();
        }
        return simulator.getSimulationConfigBunch().getByIndex(id).getInfo_1();
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

    public SimulationConfigItem getSimulationConfig() {
        return simulationConfigItem;
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
}
