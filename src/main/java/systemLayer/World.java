package systemLayer;

import _type.TtSimulationMode;
import drawingLayer.DiagramDrawingWindow;
import drawingLayer.MainDrawingWindow;
import routingLayer.Router;
import stateLayer.Environment;
import stateLayer.StateX;
import trustLayer.TrustHistory;
import trustLayer.TrustHistoryItem;
import utils.Config;
import utils.Globals;
import utils.WorldStatistics;

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

    //============================//============================//============================
    private void init(Environment _environment) throws Exception {
        //============================

        // Identifying the agents that we want to trace in Main diagram.
        traceAgentIds = new int[]{1};

        // Initializing the timer of the world.
        // Setting -1 for registering first history of travel time to -1;
        // it used in initVar() of agent
        Globals.WORLD_TIMER = -1;

        statistics = new WorldStatistics[Config.WORLD_LIFE_TIME];
        for (int i = 0; i < statistics.length; i++) {
            statistics[i] = new WorldStatistics();
        }

        router = new Router(this);
        //============================ Setting agents count

        if (Config.SIMULATION_MODE == TtSimulationMode.SimulateMode) {
            agentsCount = 0;
            for (StateX state : _environment.getStates()) {
                if (state.getAgents() != null) {
                    agentsCount += state.getAgents().size();
                }
            }
        } else {
            agentsCount = Globals.profiler.getAgentsCount();
        }
        agents = new Agent[agentsCount];

        //============================ Initializing Environment
        this.environment = new Environment(_environment);
        this.environment.init(this);


        if (environment.getMaximumAgentCapability() < agentsCount) {
            throw new Exception(">> Error: Agents count is bigger than maximum capability of environment:  " + agentsCount + " > " + environment.getMaximumAgentCapability() + ". simulation_X.json -> \"agentsCount\": " + agentsCount);
        }

        //============================//============================  Initializing agents
        System.out.println(
                " | agentsCount: " + agentsCount
        );

        if (Config.SIMULATION_MODE == TtSimulationMode.GenerateMode) {
            int id = 0;
            int thisBunchFinished = Globals.profiler.getCurrentBunch().getBunchCount();

            for (int i = 0; i < agentsCount; i++) {
                if (i >= thisBunchFinished) {
                    thisBunchFinished = thisBunchFinished + Globals.profiler.getCurrentBunch().getBunchCount();
                }

                //============================ Creating new state
                agents[i] = new Agent(this, ++id);
                agents[i].init();

                //============================  Adding agent to an state
                StateX randomState;
                int tryCount = 0;
                boolean isAddedToState;
                do {
                    randomState = environment.getRandomState();
                    if (randomState.isIsPitfall()) {
                        isAddedToState = false;
                    } else {
                        // checking state capability and adding the agent to it.
                        isAddedToState = randomState.addAgent(agents[i]);
                    }
                } while (!isAddedToState && tryCount++ < agentsCount);

                if (isAddedToState) {

                    //============================ Adding state to agent
                    agents[i].setState(randomState);

                    int targetCounts = agents[i].getTargetCounts();
                    StateX prevState = agents[i].getState();
                    for (int tc = 0; tc < targetCounts; tc++) {
                        boolean isValidToAddAsTarget;
                        //============================ Adding target state to agents
                        do {
                            randomState = environment.getRandomState();
                            //
                            if (randomState.isIsPitfall()) {
                                isValidToAddAsTarget = false;
                            } else {
                                //-- checking state capability and adding the agent to it.
                                isValidToAddAsTarget = prevState.isAnyPathTo(randomState);
                            }
                            if (isValidToAddAsTarget) {
                                //-- check if added previously as target
                                isValidToAddAsTarget = !agents[i].isAsTarget(randomState);
                            }
                        } while (!isValidToAddAsTarget && tryCount++ < agentsCount);

                        if (isValidToAddAsTarget) {
                            agents[i].addTarget(randomState);
                            prevState = randomState;
                        }
                    }
                }

                //============================  if agentId is in 'traceAgentIds', it will set as traceable
//            Agent agent = agents[i];
                if (isTraceable(i)) {
                    agents[i].setAsTraceable();
                }
                System.out.println("world:::init::agent: " + agents[i].getId() + " state: " + agents[i].getState().getId() + " target: " + (agents[i].getCurrentTarget() != null ? agents[i].getCurrentTarget().getId() : "NULL"));
            }
        }
        //============================  FullEnv
        else {
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
                    if (isTraceable(i)) {
                        agent.setAsTraceable();
                    }

                    System.out.println("Full world:::init::agent: " + agent.getId() + " state: " + agent.getState().getId() + " target: " + (agent.getCurrentTarget() != null ? agent.getCurrentTarget().getId() : "NULL"));

                    agents[i++] = agent;

                }
            }
        }

      /*  for (Agent agent : agents) {
            System.out.println("agent: " + agent.getId() + " state: " + agent.getState().getId() + " target: " + agent.getTargetState().getId());
        }*/

        environment.updateAgentsCount();

        environment.reassigningStateLocationAndTransPath();

        // Resetting the timer of the world.
        Globals.WORLD_TIMER = 0;
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

        boolean showMainWindow = Config.DRAWING_SHOW_MAIN_WINDOW;           // Whether show MainWindow or not.
        boolean showDiagramWindow = Config.DRAWING_SHOW_DIAGRAM_WINDOW;     // Whether show DrawingWindow or not.

        //============================ Initializing Main Drawing Windows
        MainDrawingWindow mainWindow = new MainDrawingWindow(this);
        mainWindow.setDoubleBuffered(true);
        if (showMainWindow) {
            JFrame mainFrame = new JFrame();
            mainFrame.getContentPane().add(mainWindow);
            mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            mainFrame.setMinimumSize(new Dimension(1500, 800));
            mainFrame.setVisible(true);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // boolean doubleBuffered = mainFrame.isDoubleBuffered();
        }
        //============================ Initializing Diagram Drawing Windows
        DiagramDrawingWindow diagramWindow = new DiagramDrawingWindow(this);
        diagramWindow.setDoubleBuffered(true);
        if (showDiagramWindow) {
            JFrame diagramFrame = new JFrame();
            diagramFrame.getContentPane().add(diagramWindow);
            diagramFrame.setExtendedState(diagramFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            diagramFrame.setMinimumSize(new Dimension(1500, 800));
            diagramFrame.setVisible(true);
        }

        /* ****************************
         *            MAIN LOOP      *
         * ****************************/

        //============================//============================  Main loop of running in a world
        for (; Globals.WORLD_TIMER < Config.WORLD_LIFE_TIME; Globals.WORLD_TIMER++) {
            WorldStatistics statistic = statistics[Globals.WORLD_TIMER];
            statistic.setWorldTime(Globals.WORLD_TIMER);
            router.setStatistics(statistic);

            if (Globals.WORLD_TIMER == 0) {
                Globals.statGenerator.addHeader(statistic);
                Globals.statGenerator.addComment();
            }

            System.out.println(Globals.WORLD_TIMER + " : World.run ------------------------------- ");

            //============================//============================  Updating agents statuses
            for (Agent agent : agents) {
                agent.resetParams();
                //agent.updateTravelHistory();
                agent.updateWatchList();
                agent.updateProfile();
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

            // System.out.println(statistic.toString());
            Globals.statGenerator.addStat(statistic);

            if (showMainWindow) {
                mainWindow.repaint();
            }

            if (showDiagramWindow) {
                diagramWindow.repaint();
            }
            try {
                Thread.sleep(Config.WORLD_SLEEP_MILLISECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Agent agent : agents) {
            TrustHistory[] histories = agent.getTrust().getHistories();
            String hiss = "";
            System.out.println(agent.getId() + "------------------------------");
            for (int i = 0; i < histories.length; i++) {
                TrustHistory history = histories[i];
                if (history != null) {
                    hiss += " | " + history.getAgent().getId() + " > " + history.getFinalTrustLevel() + " : ";
                    for (TrustHistoryItem item : history.getItems()) {
                        if (item != null) {
                            hiss += item.getTrustScore() + ", ";
                        }
                    }
                }
            }
            System.out.println(hiss);

        }

        try {
            Thread.sleep(Config.WORLD_SLEEP_MILLISECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mainWindow.repaint();

        System.out.println("Finished");


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
