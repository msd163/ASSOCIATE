package systemLayer;

import drawingLayer.*;
import routingLayer.Router;
import stateLayer.Environment;
import stateLayer.StateX;
import trustLayer.TrustManager;
import trustLayer.TrustMatrix;
import utils.Config;
import utils.Globals;
import utils.ImageBuilder;
import utils.ProjectPath;
import utils.profiler.SimulationConfig;
import utils.statistics.EpisodeStatistics;
import utils.statistics.WorldStatistics;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {

    public World(Simulator simulator, SimulationConfig simulationConfig) {
        this.simulator = simulator;
        this.simulationConfig = simulationConfig;
    }

    private Simulator simulator;

    private Agent[] agents;
    private List<Agent> sortedAgentsByCapPower;

    private int agentsCount;

    private int[] traceAgentIds;            // Ids that will be traced in simulation time, in the MainDiagram window

    private Environment environment;

    private WorldStatistics[] wdStatistics;

    private EpisodeStatistics[] epStatistics;

    private Router router;

    TrustMatrix matrixGenerator = new TrustMatrix();

    private SimulationConfig simulationConfig;

    private TrustManager trustManager;

    //============================//============================//============================
    public void init(Environment _environment) throws Exception {

        initStatistics();

        //-- Identifying the agents that we want to trace in Main diagram.
        //-- Indicating the agent and it's communications in environments with different colors
        traceAgentIds = new int[]{};


        //============================//============================
        trustManager = new TrustManager(simulationConfig);

        router = new Router(this);
        //============================ Setting agents count

        agentsCount = _environment.getAgentsCount();
        agents = new Agent[agentsCount];
        //============================ Initializing Environment
        this.environment = _environment;
        this.environment.initForSimulation(this);

        //============================//============================  Initializing agents
        System.out.println(
                " | agentsCount: " + agentsCount
        );

        ArrayList<StateX> states = environment.getStates();
        sortedAgentsByCapPower = new ArrayList<>();
        int i = 0;
        for (StateX state : states) {
            for (Agent agent : state.getAgents()) {
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

                System.out.println("Full world:::init::agent: " + agent.getId() + " state: " + agent.getState().getId() + " target: " + (agent.getCurrentTarget() != null ? agent.getCurrentTarget().getId() : "NULL"));

                agents[i++] = agent;
                sortedAgentsByCapPower.add(agent);
            }
        }

        sortedAgentsByCapPower.sort((Agent a1, Agent a2) -> {
            if (a1.getCapacity().getCapPower() > a2.getCapacity().getCapPower()) {
                return 1;
            }
            if (a1.getCapacity().getCapPower() < a2.getCapacity().getCapPower()) {
                return -1;
            }
            return 0;
        });

        environment.reassigningStateLocationAndTransPath();

        // Resetting the timer of the world.
        Globals.WORLD_TIMER = 0;

        wdStatistics = new WorldStatistics[Config.WORLD_LIFE_TIME];
        for (i = 0; i < wdStatistics.length; i++) {
            if (i == 0) {
                wdStatistics[i] = new WorldStatistics(null, agentsCount);
            } else {
                wdStatistics[i] = new WorldStatistics(wdStatistics[i - 1], agentsCount);
            }
        }

        int maxEpisodeCount = Math.max(environment.getProMax().getMaxAgentTargetCount(), Config.WORLD_LIFE_TIME / Config.EPISODE_TIMOUT);
        epStatistics = new EpisodeStatistics[maxEpisodeCount];
        for (i = 0; i < maxEpisodeCount; i++) {
            epStatistics[i] = new EpisodeStatistics();
        }

        //============================//============================ Init trust matrix
        initTrustMatrix();
    }


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
        matrixGenerator.init(sortedAgentsByCapPower);

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

    public void run() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        int widthHalf = (int) width / 2;
        int heightHalf = (int) height / 2;
        //============================ Initializing Main Drawing Windows
        StateMachineDrawingWindow stateMachineDW = new StateMachineDrawingWindow(this);
        stateMachineDW.setDoubleBuffered(true);
        stateMachineDW.setName("st_mc");
        if (Config.DRAWING_SHOW_STATE_MACHINE) {
            JFrame mainFrame = new JFrame();
            mainFrame.getContentPane().add(stateMachineDW);
            // mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            mainFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            mainFrame.setVisible(true);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLocation(0, 0);
            mainFrame.setTitle("State Machine Map");
        }
        //============================ Initializing Diagram Drawing Windows
        StatsOfEnvDrawingWindow statsOfEnvDW = new StatsOfEnvDrawingWindow(this);
        statsOfEnvDW.setDoubleBuffered(true);
        statsOfEnvDW.setName("s_env");
        if (Config.DRAWING_SHOW_STAT_OF_ENV) {
            JFrame statsFrame = new JFrame();
            statsFrame.getContentPane().add(statsOfEnvDW);
//            diagramFrame.setExtendedState(diagramFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            statsFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            statsFrame.setVisible(true);
            statsFrame.setLocation(0, heightHalf);
            statsFrame.setTitle("Environment Statistics");
        }

        //============================ Initializing Diagram Drawing Windows
        TrustMatrixDrawingWindow trustMatrixDW = new TrustMatrixDrawingWindow(matrixGenerator);
        trustMatrixDW.setDoubleBuffered(true);
        trustMatrixDW.setName("t_mtx");
        if (Config.DRAWING_SHOW_TRUST_MATRIX) {
            JFrame trustMatFrame = new JFrame();
            trustMatFrame.getContentPane().add(trustMatrixDW);
//            diagramFrame.setExtendedState(diagramFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            trustMatFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            trustMatFrame.setVisible(true);
            trustMatFrame.setLocation(widthHalf, 0);
            trustMatFrame.setTitle("Trust Matrix");
        }

        //============================ Initializing Diagram Drawing Windows
        StatsOfTrustDrawingWindow statsOfTrustDW = new StatsOfTrustDrawingWindow(this);
        statsOfTrustDW.setDoubleBuffered(true);
        statsOfTrustDW.setName("t_stt");
        if (Config.DRAWING_SHOW_STATS_OF_TRUST) {
            JFrame trustStats = new JFrame();
            trustStats.getContentPane().add(statsOfTrustDW);
//            diagramFrame.setExtendedState(diagramFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            trustStats.setMinimumSize(new Dimension(widthHalf, heightHalf));
            trustStats.setVisible(true);
            trustStats.setLocation(widthHalf, heightHalf);
            trustStats.setTitle("Trust Statistics");
        }

        //============================ Initializing Diagram Drawing Windows
        StatsOfFalsePoNeDrawingWindow statsOfFalsePoNeDW = new StatsOfFalsePoNeDrawingWindow(this);
        statsOfFalsePoNeDW.setDoubleBuffered(true);
        statsOfFalsePoNeDW.setName("t_pon");
        if (Config.DRAWING_SHOW_STATS_OF_PO_NE) {
            JFrame poNeStats = new JFrame();
            poNeStats.getContentPane().add(statsOfFalsePoNeDW);
//            diagramFrame.setExtendedState(diagramFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            poNeStats.setMinimumSize(new Dimension(widthHalf, heightHalf));
            poNeStats.setVisible(true);
            poNeStats.setLocation(widthHalf, heightHalf);
            poNeStats.setTitle("(TP | TN | FP | FN) Statistics");
        }

        //============================ Initializing Diagram Drawing Windows
        StatsOfAnalysisOfTrustDrawingWindow analysisOfTrustParamsDW = new StatsOfAnalysisOfTrustDrawingWindow(this);
        analysisOfTrustParamsDW.setDoubleBuffered(true);
        analysisOfTrustParamsDW.setName("t_anl");
        if (Config.DRAWING_SHOW_ANALYSIS_OF_TRUST_PARAM) {
            JFrame anOfTrPrJFrame = new JFrame();
            anOfTrPrJFrame.getContentPane().add(analysisOfTrustParamsDW);
            anOfTrPrJFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            anOfTrPrJFrame.setVisible(true);
            anOfTrPrJFrame.setLocation(widthHalf, heightHalf);
            anOfTrPrJFrame.setTitle("Trust Analyzing (Accuracy | Sensitivity | Specificity)");
        }

        //============================ Initializing Diagram Drawing Windows
        AgentTravelInfoDrawingWindow agentTravelInfoDW = new AgentTravelInfoDrawingWindow(this);
        agentTravelInfoDW.setDoubleBuffered(true);
        agentTravelInfoDW.setName("a_trv");
        if (Config.DRAWING_SHOW_AGENT_TRAVEL_INFO) {
            JFrame agTravelInfoPrJFrame = new JFrame();
            agTravelInfoPrJFrame.getContentPane().add(agentTravelInfoDW);
            agTravelInfoPrJFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            agTravelInfoPrJFrame.setVisible(true);
            agTravelInfoPrJFrame.setLocation(widthHalf, heightHalf);
            agTravelInfoPrJFrame.setTitle("Agent Travel Info");
        }

        //============================ Initializing Diagram Drawing Windows
        AgentTrustDataDrawingWindow agentTrustDW = new AgentTrustDataDrawingWindow(this);
        agentTrustDW.setDoubleBuffered(true);
        agentTrustDW.setName("a_trt");
        if (Config.DRAWING_SHOW_AGENT_TRUST_DATA) {
            JFrame agTravelInfoPrJFrame = new JFrame();
            agTravelInfoPrJFrame.getContentPane().add(agentTrustDW);
            agTravelInfoPrJFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            agTravelInfoPrJFrame.setVisible(true);
            agTravelInfoPrJFrame.setLocation(widthHalf, heightHalf);
            agTravelInfoPrJFrame.setTitle("Agent Trust Data");
        }

        //============================ Initializing Recommendation Drawing Windows
        AgentRecommendationDrawingWindow agentRecommendationDW = new AgentRecommendationDrawingWindow(this);
        agentRecommendationDW.setDoubleBuffered(true);
        agentRecommendationDW.setName("a_rec");
        if (Config.DRAWING_SHOW_AGENT_RECOMMENDATION_DATA) {
            JFrame agTravelInfoPrJFrame = new JFrame();
            agTravelInfoPrJFrame.getContentPane().add(agentRecommendationDW);
            agTravelInfoPrJFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            agTravelInfoPrJFrame.setVisible(true);
            agTravelInfoPrJFrame.setLocation(widthHalf, heightHalf);
            agTravelInfoPrJFrame.setTitle("Agent Recommendation Data");
        }

        //============================ Initializing Observation Drawing Windows
        AgentObservationDrawingWindow agentObservationDW = new AgentObservationDrawingWindow(this);
        agentObservationDW.setDoubleBuffered(true);
        agentObservationDW.setName("a_obs");
        if (Config.DRAWING_SHOW_AGENT_OBSERVATION_DATA) {
            JFrame agTravelInfoPrJFrame = new JFrame();
            agTravelInfoPrJFrame.getContentPane().add(agentObservationDW);
            agTravelInfoPrJFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            agTravelInfoPrJFrame.setVisible(true);
            agTravelInfoPrJFrame.setLocation(widthHalf, heightHalf);
            agTravelInfoPrJFrame.setTitle("Agent Observation Data");
        }


        /* ****************************
         *            MAIN LOOP      *
         * ****************************/

        epStatistics[0].setFromTime(Globals.WORLD_TIMER);

        //============================//============================  Main loop of running in a world
        for (; Globals.WORLD_TIMER < Config.WORLD_LIFE_TIME; Globals.WORLD_TIMER++) {

            WorldStatistics wdStats = wdStatistics[Globals.WORLD_TIMER];
            wdStats.setWorldTime(Globals.WORLD_TIMER);
            wdStats.init(Globals.EPISODE, agents);

            router.setStatistics(wdStats);

            if (Globals.WORLD_TIMER == 0 && Config.STATISTICS_IS_GENERATE) {
                Globals.statsEnvGenerator.addComment(environment);
                Globals.statsEnvGenerator.addHeader();
                Globals.statsTrustGenerator.addHeader();
            }

            System.out.println(Globals.WORLD_TIMER + " : World.run ------------------------------- ");

            //============================//============================  Updating agents statuses
            for (Agent agent : agents) {
                //todo: adding doing service capacity to agents as capacity param
                agent.resetParams();
                //agent.updateTravelHistory();
                agent.updateProfile();
                agent.updateWatchList();
                router.updateNextSteps(agent);
            }

            //============================//============================ Traveling
            for (Agent agent : agents) {
                router.takeAStepTowardTheTarget(agent);
            }

            //============================//============================ Observation
            if (simulationConfig.isIsUseTrustObservation()) {
                for (Agent agent : agents) {
                    if (agent.getCapacity().getObservationCap() > 0) {
                        trustManager.observe(agent);
                    }
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
            updateWindows(stateMachineDW, statsOfEnvDW, trustMatrixDW, statsOfTrustDW, statsOfFalsePoNeDW,
                    analysisOfTrustParamsDW, agentTravelInfoDW, agentTrustDW, agentRecommendationDW, agentObservationDW);

            //============================//============================//============================ Adding Episode of environment
            // and exiting the agents from pitfalls
            if ((Globals.WORLD_TIMER + 1) % Config.EPISODE_TIMOUT == 0 || wdStats.getAllAgentsInTarget() + wdStats.getAllAgentsInPitfall() == agentsCount) {


                if (Globals.WORLD_TIMER > 0) {
                    //-- updating episode statistics
                    epStatistics[Globals.EPISODE].update(wdStatistics);
                    epStatistics[Globals.EPISODE].setFromTime(Globals.WORLD_TIMER);
                }

                //-- Increasing Episode
                Globals.EPISODE++;

                //-- Exiting agents that are in pitfall and taking in new state randomly.
                // ArrayList<StateX> states = environment.getStates();
                //for (int j = 0, statesSize = states.size(); j < statesSize; j++) {
                // StateX state = states.get(j);
                //if (state.isIsPitfall()) {

                for (StateX state : environment.getStates()) {
                    state.getAgents().clear();
                }

                for (int i = 0; i < agentsCount; i++) {
                    Agent agent = agents[i];

                    StateX randomState;
                    int tryCount = 0;
                    boolean isAddedToState;
                    do {
                        randomState = environment.getRandomState();
                        if (randomState.isIsPitfall()) {
                            isAddedToState = false;
                        } else {
                            // checking state capability and adding the agent to it.
                            isAddedToState = randomState.addAgent(agent);
                        }
                        if (isAddedToState) {
                            agent.setState(randomState);
                        }
                    } while (!isAddedToState && tryCount++ < agentsCount * 2);
                }
                //  }
                // }
            }

            while (Globals.PAUSE) {
                updateWindows(stateMachineDW, statsOfEnvDW, trustMatrixDW, statsOfTrustDW, statsOfFalsePoNeDW,
                        analysisOfTrustParamsDW, agentTravelInfoDW, agentTrustDW, agentRecommendationDW, agentObservationDW);
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

        new ImageBuilder().generateStatisticsImages(stateMachineDW, statsOfEnvDW, trustMatrixDW,
                statsOfTrustDW, statsOfFalsePoNeDW, analysisOfTrustParamsDW, agentObservationDW, agentRecommendationDW,
                agentTravelInfoDW, agentTrustDW
        );

        System.out.println("Finished");


        //============================//============================ Running program after finishing lifeTime of the world.

        while (Globals.SIMULATION_TIMER == Globals.SIMULATION_ROUND - 1) {
            updateWindows(stateMachineDW, statsOfEnvDW, trustMatrixDW, statsOfTrustDW, statsOfFalsePoNeDW,
                    analysisOfTrustParamsDW, agentTravelInfoDW, agentTrustDW, agentRecommendationDW, agentObservationDW);
        }
    }  //  End of running


    private void updateWindows(StateMachineDrawingWindow mainWindow,
                               StatsOfEnvDrawingWindow diagramWindow,
                               TrustMatrixDrawingWindow trustMatWindow,
                               StatsOfTrustDrawingWindow trustStatsWindow,
                               StatsOfFalsePoNeDrawingWindow poNeStatsWindow,
                               StatsOfAnalysisOfTrustDrawingWindow paramsDrawingWindow,
                               AgentTravelInfoDrawingWindow agentTravelInfoDW,
                               AgentTrustDataDrawingWindow agentTrustDW,
                               AgentRecommendationDrawingWindow agentRecommendationDataDW,
                               AgentObservationDrawingWindow agentObservationDW
    ) {
        try {
            Thread.sleep(Config.WORLD_SLEEP_MILLISECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Config.DRAWING_SHOW_STATE_MACHINE) {
            mainWindow.repaint();
        }
        if (Config.DRAWING_SHOW_STAT_OF_ENV) {
            diagramWindow.repaint();
        }
        if (Config.DRAWING_SHOW_TRUST_MATRIX) {
            trustMatWindow.repaint();
        }
        if (Config.DRAWING_SHOW_STATS_OF_TRUST) {
            trustStatsWindow.repaint();
        }
        if (Config.DRAWING_SHOW_STATS_OF_PO_NE) {
            poNeStatsWindow.repaint();
        }
        if (Config.DRAWING_SHOW_ANALYSIS_OF_TRUST_PARAM) {
            paramsDrawingWindow.repaint();
        }
        if (Config.DRAWING_SHOW_AGENT_TRAVEL_INFO) {
            agentTravelInfoDW.repaint();
        }
        if (Config.DRAWING_SHOW_AGENT_TRUST_DATA) {
            agentTrustDW.repaint();
        }
        if (Config.DRAWING_SHOW_AGENT_RECOMMENDATION_DATA) {
            agentRecommendationDataDW.repaint();
        }
        if (Config.DRAWING_SHOW_AGENT_OBSERVATION_DATA) {
            agentObservationDW.repaint();
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
        return "E-Code: " + environment.getCode() + " | #Ags: " + agentsCount + " | #Sts: " + environment.getStateCount() +
                " | " + simulator.getSimulationConfigBunch().getByIndex(Globals.SIMULATION_TIMER).getInfo();
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

    public Agent[] getAgents() {
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

    public List<Agent> getSortedAgentsByCapPower() {
        return sortedAgentsByCapPower;
    }

    public SimulationConfig getSimulationConfig() {
        return simulationConfig;
    }

    public TrustManager getTrustManager() {
        return trustManager;
    }
}
