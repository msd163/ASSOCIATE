package systemLayer;

import drawingLayer.*;
import routingLayer.Router;
import stateLayer.Environment;
import stateLayer.StateX;
import trustLayer.TrustManager;
import trustLayer.TrustMatrix;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class World {

    public World(Environment environment) throws Exception {
        init(environment);
    }

    private Agent[] agents;
    private int agentsCount;

    private int[] traceAgentIds;            // Ids that will be traced in simulation time, in the MainDiagram window

    private Environment environment;

    private WorldStatistics[] statistics;

    private Router router;


    TrustMatrix matrixGenerator = new TrustMatrix();

    //============================//============================//============================
    private void init(Environment _environment) throws Exception {


        //-- Identifying the agents that we want to trace in Main diagram.
        //-- Indicating the agent and it's communications in environments with different colors
        traceAgentIds = new int[]{};

        //-- Initializing the timer of the world.
        //-- Setting -1 for registering first history of travel time to -1;
        //-- it used in initVar() of agent
        Globals.WORLD_TIMER = -1;

        statistics = new WorldStatistics[Config.WORLD_LIFE_TIME];
        for (int i = 0; i < statistics.length; i++) {
            if (i == 0) {
                statistics[i] = new WorldStatistics(null);
            } else {
                statistics[i] = new WorldStatistics(statistics[i - 1]);
            }
        }

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

            }
        }

        environment.reassigningStateLocationAndTransPath();

        // Resetting the timer of the world.
        Globals.WORLD_TIMER = 0;

        Globals.trustManager = new TrustManager();

        //============================//============================ Init trust matrix
        initTrustMatrix();
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

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        int widthHalf = (int) width / 2;
        int heightHalf = (int) height / 2;
        //============================ Initializing Main Drawing Windows
        StateMachineDrawingWindow stateMachineWindow = new StateMachineDrawingWindow(this);
        stateMachineWindow.setDoubleBuffered(true);
        stateMachineWindow.setName("st_mc");
        if (Config.DRAWING_SHOW_STATE_MACHINE) {
            JFrame mainFrame = new JFrame();
            mainFrame.getContentPane().add(stateMachineWindow);
            // mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            mainFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            mainFrame.setVisible(true);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLocation(0, 0);
            mainFrame.setTitle("State Machine Map");
        }
        //============================ Initializing Diagram Drawing Windows
        StatsOfEnvDrawingWindow statsOfEnvWindow = new StatsOfEnvDrawingWindow(this);
        statsOfEnvWindow.setDoubleBuffered(true);
        statsOfEnvWindow.setName("s_env");
        if (Config.DRAWING_SHOW_STAT_OF_ENV) {
            JFrame statsFrame = new JFrame();
            statsFrame.getContentPane().add(statsOfEnvWindow);
//            diagramFrame.setExtendedState(diagramFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            statsFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            statsFrame.setVisible(true);
            statsFrame.setLocation(0, heightHalf);
            statsFrame.setTitle("Environment Statistics");
        }

        //============================ Initializing Diagram Drawing Windows
        TrustMatrixDrawingWindow trustMatrixWindow = new TrustMatrixDrawingWindow(matrixGenerator);
        trustMatrixWindow.setDoubleBuffered(true);
        trustMatrixWindow.setName("t_mtx");
        if (Config.DRAWING_SHOW_TRUST_MATRIX) {
            JFrame trustMatFrame = new JFrame();
            trustMatFrame.getContentPane().add(trustMatrixWindow);
//            diagramFrame.setExtendedState(diagramFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            trustMatFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            trustMatFrame.setVisible(true);
            trustMatFrame.setLocation(widthHalf, 0);
            trustMatFrame.setTitle("Trust Matrix");
        }

        //============================ Initializing Diagram Drawing Windows
        StatsOfTrustDrawingWindow trustStatsWindow = new StatsOfTrustDrawingWindow(this);
        trustStatsWindow.setDoubleBuffered(true);
        trustStatsWindow.setName("t_stt");
        if (Config.DRAWING_SHOW_STATS_OF_TRUST) {
            JFrame trustStats = new JFrame();
            trustStats.getContentPane().add(trustStatsWindow);
//            diagramFrame.setExtendedState(diagramFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            trustStats.setMinimumSize(new Dimension(widthHalf, heightHalf));
            trustStats.setVisible(true);
            trustStats.setLocation(widthHalf, heightHalf);
            trustStats.setTitle("Trust Statistics");
        }

        //============================ Initializing Diagram Drawing Windows
        StatsOfFalsePoNeDrawingWindow poNeStatsWindow = new StatsOfFalsePoNeDrawingWindow(this);
        poNeStatsWindow.setDoubleBuffered(true);
        poNeStatsWindow.setName("t_pon");
        if (Config.DRAWING_SHOW_STATS_OF_PO_NE) {
            JFrame poNeStats = new JFrame();
            poNeStats.getContentPane().add(poNeStatsWindow);
//            diagramFrame.setExtendedState(diagramFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            poNeStats.setMinimumSize(new Dimension(widthHalf, heightHalf));
            poNeStats.setVisible(true);
            poNeStats.setLocation(widthHalf, heightHalf);
            poNeStats.setTitle("False Positive and False Negative Statistics");
        }

        //============================ Initializing Diagram Drawing Windows
        StatsOfTrustParamsDrawingWindow trustParamsDrawingWindow = new StatsOfTrustParamsDrawingWindow(this);
        trustParamsDrawingWindow.setDoubleBuffered(true);
        trustParamsDrawingWindow.setName("t_prm");
        if (Config.DRAWING_SHOW_STATS_OF_TRUST_PARAM) {
            JFrame paramStats = new JFrame();
            paramStats.getContentPane().add(trustParamsDrawingWindow);
            paramStats.setMinimumSize(new Dimension(widthHalf, heightHalf));
            paramStats.setVisible(true);
            paramStats.setLocation(widthHalf, heightHalf);
            paramStats.setTitle("Trust Params (Accuracy | Sensitivity | Specificity)");
        }


        /* ****************************
         *            MAIN LOOP      *
         * ****************************/

        //============================//============================  Main loop of running in a world
        for (; Globals.WORLD_TIMER < Config.WORLD_LIFE_TIME; Globals.WORLD_TIMER++) {

            WorldStatistics statistic = statistics[Globals.WORLD_TIMER];
            statistic.setWorldTime(Globals.WORLD_TIMER);
            statistic.init(Globals.EPISODE);
            router.setStatistics(statistic);

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

            //============================//============================  updating full state statistics
            for (StateX state : environment.getStates()) {
                if (state.isFullCapability()) {
                    statistic.addFullStateCount();
                }
                if (state.getTargets().size() == 0) {
                    statistic.addStatesWithNoTarget();
                }
            }

            matrixGenerator.update(statistic);

            if (Config.STATISTICS_IS_GENERATE) {
                Globals.statsEnvGenerator.addStat(statistic);
                Globals.statsTrustGenerator.addStat(statistic);
            }
            //============================//============================ Repainting
            updateWindows(stateMachineWindow, statsOfEnvWindow, trustMatrixWindow, trustStatsWindow, poNeStatsWindow, trustParamsDrawingWindow);

            //============================//============================//============================ Adding Episode of environment
            // and exiting the agents from pitfalls
            if (Globals.WORLD_TIMER % Config.EPISODE_TIMOUT == 0 || statistic.getAllAgentsInTarget() + statistic.getAllAgentsInPitfall() == agentsCount) {

                //-- Increasing Episode
                Globals.EPISODE++;

                //-- Exiting agents that are in pitfall and taking in new state randomly.
                ArrayList<StateX> states = environment.getStates();
                for (int j = 0, statesSize = states.size(); j < statesSize; j++) {
                    StateX state = states.get(j);
                    if (state.isIsPitfall()) {

                        for (int i = 0, aSize = state.getAgents().size(); i < aSize; i++) {
                            Agent agent = state.getAgents().get(i);

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
                                    state.getAgents().get(i).setState(randomState);
                                }
                            } while (!isAddedToState && tryCount++ < agentsCount * 2);
                        }
                        state.getAgents().clear();
                    }
                }
            }

            while (Globals.PAUSE) {
                updateWindows(stateMachineWindow, statsOfEnvWindow, trustMatrixWindow, trustStatsWindow, poNeStatsWindow, trustParamsDrawingWindow);
            }

        }

        //============================//============================ Creating trust matrix and saving in csv file

        if (Config.TRUST_MATRIX_IS_GENERATE) {
            System.out.println("Generating Trust Matrix");
            String matrixPath = Globals.STATS_FILE_NAME;

            matrixPath = ProjectPath.instance().statisticsDir() + "/" + matrixPath + "/" + matrixPath + ".mat.csv";

            //matrixGenerator.update(null);
            matrixGenerator.write(matrixPath);
            matrixGenerator.close();
            System.out.println("Trust Matrix Generated.");
        }

        ImageBuilder.generateStatisticsImages(stateMachineWindow, statsOfEnvWindow, trustMatrixWindow, trustStatsWindow, poNeStatsWindow, trustParamsDrawingWindow);

        System.out.println("Finished");


        //============================//============================ Running program after finishing lifeTime of the world.

        while (true) {
            updateWindows(stateMachineWindow, statsOfEnvWindow, trustMatrixWindow, trustStatsWindow, poNeStatsWindow, trustParamsDrawingWindow);
        }
    }  //  End of running


    private void updateWindows(StateMachineDrawingWindow mainWindow, StatsOfEnvDrawingWindow diagramWindow, TrustMatrixDrawingWindow trustMatWindow, StatsOfTrustDrawingWindow trustStatsWindow, StatsOfFalsePoNeDrawingWindow poNeStatsWindow, StatsOfTrustParamsDrawingWindow paramsDrawingWindow) {
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
        if (Config.DRAWING_SHOW_STATS_OF_TRUST_PARAM) {
            paramsDrawingWindow.repaint();
        }
    }

    //============================//============================//============================

    public String toStringInfo() {
        StringBuilder tx = new StringBuilder("\n");
        StringBuilder ti = new StringBuilder("\n\n\n\t");

        return tx + "World: " +
                ti + " | agentsCount=" + agentsCount;
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

    public WorldStatistics[] getStatistics() {
        return statistics;
    }
}
