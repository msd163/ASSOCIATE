package systemLayer;

import _type.TtDrawingWindowLocation;
import drawingLayer.*;
import routingLayer.Router;
import simulateLayer.SimulationConfigItem;
import simulateLayer.Simulator;
import stateLayer.Environment;
import stateLayer.StateX;
import trustLayer.TrustManager;
import trustLayer.TrustMatrix;
import utils.Config;
import utils.Globals;
import utils.ImageBuilder;
import utils.ProjectPath;
import utils.statistics.EpisodeStatistics;
import utils.statistics.WorldStatistics;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {

    public World(Simulator simulator, SimulationConfigItem simulationConfigItem) {
        this.simulator = simulator;
        this.simulationConfigItem = simulationConfigItem;
    }

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

    //============================//============================//============================
    public void init(Environment _environment) throws Exception {

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
            for (Agent agent : state.getAgents()) {
                agent.setState(state);
                agent.setWorld(this);
                agent.initVars(agentsCount);

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

                agents.add(agent);
            }
        }

        agents.sort((Agent a1, Agent a2) -> {
            if (a1.getCapacity().getCapPower() > a2.getCapacity().getCapPower()) {
                return 1;
            }
            if (a1.getCapacity().getCapPower() < a2.getCapacity().getCapPower()) {
                return -1;
            }
            return 0;
        });

        //-- Setting index of agent in sorted list
        int indexInSortedList = 0;
        for (Agent agent : agents) {
            agent.setIndex(indexInSortedList++);
        }

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


    private StateMachineDrawingWindow stateMachineDW;
    private StatsOfEnvDrawingWindow statsOfEnvDW;
    private TrustMatrixDrawingWindow trustMatrixDW;
    private StatsOfTrustDrawingWindow statsOfTrustDW;
    private AgentTrustDataDrawingWindow agentTrustDW;
    private AgentTravelInfoDrawingWindow agentTravelInfoDW;
    private StatsOfAnalysisOfTrustDrawingWindow analysisOfTrustParamsDW;
    private StatsOfFalsePoNeDrawingWindow statsOfFalsePoNeDW;
    private AgentObservationDrawingWindow agentObservationDW;
    private AgentRecommendationDrawingWindow agentRecommendationDW;


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

    public void run() {

        initDrawingWindows();

        /* ****************************
         *            MAIN LOOP      *
         * ****************************/

        epStatistics[0].setFromTime(Globals.WORLD_TIMER);

        //============================//============================  Main loop of running in a world
        for (; Globals.WORLD_TIMER < Config.WORLD_LIFE_TIME; Globals.WORLD_TIMER++) {

            WorldStatistics wdStats = wdStatistics[Globals.WORLD_TIMER];
            wdStats.setWorldTime(Globals.WORLD_TIMER);
            wdStats.init(Globals.EPISODE);

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
                agent.updateProfile();
                agent.updateWatchList();
                router.updateNextSteps(agent);
            }

            //============================//============================ Traveling
            for (Agent agent : agents) {
                router.takeAStepTowardTheTarget(agent);
            }

            //============================//============================ Observation
            if (simulationConfigItem.isIsValidateByTrustObservation()) {
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

        new ImageBuilder().generateStatisticsImages(stateMachineDW, statsOfEnvDW, trustMatrixDW,
                statsOfTrustDW, statsOfFalsePoNeDW, analysisOfTrustParamsDW, agentObservationDW, agentRecommendationDW,
                agentTravelInfoDW, agentTrustDW
        );

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
        stateMachineDW = new StateMachineDrawingWindow(this);
        if (Config.DRAWING_SHOW_STATE_MACHINE) {
            initDrawingWindow(widthHalf, heightHalf, stateMachineDW, "st_mc", "State Machine Map", TtDrawingWindowLocation.TopLeft, true);
        }
        //============================ Initializing Diagram Drawing Windows
        statsOfEnvDW = new StatsOfEnvDrawingWindow(this);
        if (Config.DRAWING_SHOW_STAT_OF_ENV) {
            initDrawingWindow(widthHalf, heightHalf, statsOfEnvDW, "s_env", "Environment Statistics", TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        trustMatrixDW = new TrustMatrixDrawingWindow(matrixGenerator);
        if (Config.DRAWING_SHOW_TRUST_MATRIX) {
            initDrawingWindow(widthHalf, heightHalf, trustMatrixDW, "t_mtx", "Trust Matrix", TtDrawingWindowLocation.TopLeft);
        }

        //============================ Initializing Diagram Drawing Windows
        statsOfTrustDW = new StatsOfTrustDrawingWindow(this);
        if (Config.DRAWING_SHOW_STATS_OF_TRUST) {
            initDrawingWindow(widthHalf, heightHalf, statsOfTrustDW, "t_stt", "Trust Statistics", TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        statsOfFalsePoNeDW = new StatsOfFalsePoNeDrawingWindow(this);
        if (Config.DRAWING_SHOW_STATS_OF_PO_NE) {
            initDrawingWindow(widthHalf, heightHalf, statsOfFalsePoNeDW, "t_pon", "E(TP | TN | FP | FN) Statistics", TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        analysisOfTrustParamsDW = new StatsOfAnalysisOfTrustDrawingWindow(this);
        if (Config.DRAWING_SHOW_ANALYSIS_OF_TRUST_PARAM) {
            initDrawingWindow(widthHalf, heightHalf, analysisOfTrustParamsDW, "t_anl", "Trust Analyzing (Accuracy | Sensitivity | Specificity)", TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        agentTravelInfoDW = new AgentTravelInfoDrawingWindow(this);
        if (Config.DRAWING_SHOW_AGENT_TRAVEL_INFO) {
            initDrawingWindow(widthHalf, heightHalf, agentTravelInfoDW, "a_trv", "Agent Travel Info", TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Diagram Drawing Windows
        agentTrustDW = new AgentTrustDataDrawingWindow(this);
        if (Config.DRAWING_SHOW_AGENT_TRUST_DATA) {
            initDrawingWindow(widthHalf, heightHalf, agentTrustDW, "a_trt", "Agent Trust Data", TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Recommendation Drawing Windows
        agentRecommendationDW = new AgentRecommendationDrawingWindow(this);
        if (Config.DRAWING_SHOW_AGENT_RECOMMENDATION_DATA) {
            initDrawingWindow(widthHalf, heightHalf, agentRecommendationDW, "a_rec", "Agent Recommendation Data", TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Observation Drawing Windows
        agentObservationDW = new AgentObservationDrawingWindow(this);
        if (Config.DRAWING_SHOW_AGENT_OBSERVATION_DATA) {
            initDrawingWindow(widthHalf, heightHalf, agentObservationDW, "a_obs", "gent Observation Data", TtDrawingWindowLocation.BottomRight);
        }
    }

    private void initDrawingWindow(int widthHalf, int heightHalf, DrawingWindow stateMachineDW, String name, String title, TtDrawingWindowLocation location) {
        initDrawingWindow(widthHalf, heightHalf, stateMachineDW, name, title, location, false);
    }

    private void initDrawingWindow(int widthHalf, int heightHalf, DrawingWindow stateMachineDW, String name, String title, TtDrawingWindowLocation location, boolean exitAppOnCLose) {
        stateMachineDW.setDoubleBuffered(true);
        stateMachineDW.setName(name);
        JFrame mainFrame = new JFrame();
        mainFrame.getContentPane().add(stateMachineDW);
        mainFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
        mainFrame.setVisible(true);
        if (exitAppOnCLose) {
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        mainFrame.setTitle(title);
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
        if (Config.DRAWING_SHOW_STATE_MACHINE) {
            stateMachineDW.repaint();
        }
        if (Config.DRAWING_SHOW_STAT_OF_ENV) {
            statsOfEnvDW.repaint();
        }
        if (Config.DRAWING_SHOW_TRUST_MATRIX) {
            trustMatrixDW.repaint();
        }
        if (Config.DRAWING_SHOW_STATS_OF_TRUST) {
            statsOfTrustDW.repaint();
        }
        if (Config.DRAWING_SHOW_STATS_OF_PO_NE) {
            statsOfFalsePoNeDW.repaint();
        }
        if (Config.DRAWING_SHOW_ANALYSIS_OF_TRUST_PARAM) {
            analysisOfTrustParamsDW.repaint();
        }
        if (Config.DRAWING_SHOW_AGENT_TRAVEL_INFO) {
            agentTravelInfoDW.repaint();
        }
        if (Config.DRAWING_SHOW_AGENT_TRUST_DATA) {
            agentTrustDW.repaint();
        }
        if (Config.DRAWING_SHOW_AGENT_RECOMMENDATION_DATA) {
            agentRecommendationDW.repaint();
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
}
