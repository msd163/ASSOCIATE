package systemLayer;

import _type.TtSimulationMode;
import drawingLayer.DiagramDrawingWindow;
import drawingLayer.MainDrawingWindow;
import routingLayer.Router;
import stateLayer.Environment;
import stateLayer.StateX;
import utils.Config;
import utils.Globals;
import utils.WorldStatistics;

import javax.swing.*;
import java.awt.*;

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

        // Resetting the timer of the world.
        Globals.WORLD_TIMER = 0;

        statistics = new WorldStatistics[Config.WORLD_LIFE_TIME];
        for (int i = 0; i < statistics.length; i++) {
            statistics[i] = new WorldStatistics();
        }

        router = new Router();
        //============================ Setting agents count

        if (Config.SIMULATION_MODE == TtSimulationMode.FullEnvironment) {
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
        this.environment = new Environment();
        this.environment.setStates(_environment.getStates());
        this.environment.setStateCount(_environment.getStateCount());
        this.environment.setStateCapacity(_environment.getStateCapacity());
        this.environment.setAgentsCount(_environment.getAgentsCount());
        this.environment.init(this);


        if (environment.getMaximumAgentCapability() < agentsCount) {
            throw new Exception(">> Error: Agents count is bigger than maximum capability of environment:  " + agentsCount + " > " + environment.getMaximumAgentCapability() + ". simulation_X.json -> \"agentsCount\": " + agentsCount);
        }

        //============================//============================  Initializing agents
        System.out.println(
                " | agentsCount: " + agentsCount
        );

        if (Config.SIMULATION_MODE == TtSimulationMode.PureEnvironment) {
            int id = 0;
            int thisBunchFinished = Globals.profiler.getCurrentBunch().getBunchCount();

            for (int i = 0; i < agentsCount; i++) {
                if (i >= thisBunchFinished) {
                    Globals.profiler.NextBunch();
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
                    // checking state capability and adding the agent to it.
                    isAddedToState = randomState.addAgent(agents[i]);
                } while (!isAddedToState && tryCount++ < agentsCount);

                if (isAddedToState) {
                    agents[i].setState(randomState);
                    boolean isAnyPathTo;
                    //============================ Adding target state to agents
                    do {
                        randomState = environment.getRandomState();
                        //
                        isAnyPathTo = agents[i].getState().isAnyPathTo(randomState);
                    } while (!isAnyPathTo && tryCount++ < agentsCount);

                    if (isAnyPathTo) {
                        agents[i].setTargetState(randomState);
                    }

                }
                //============================  if agentId is in 'traceAgentIds', it will set as traceable
//            Agent agent = agents[i];
                if (isTraceable(i)) {
                    agents[i].setAsTraceable();
                }
                System.out.println("world:::init::agent: " + agents[i].getId() + " state: " + agents[i].getState().getId() + " target: " + (agents[i].getTargetState() != null ? agents[i].getTargetState().getId() : "NULL"));
            }
        }
        //============================  FullEnv
        else {
            StateX[] states = environment.getStates();

            int i = 0;
            for (StateX state : states) {
                for (Agent agent : state.getAgents()) {
                    agent.setState(state);
                    agent.setWorld(this);
                    agent.initVars();

                    agent.setTargetState(environment.getState(agent.getTargetStateId()));
                    //============================  if agentId is in 'traceAgentIds', it will set as traceable
                    if (isTraceable(i)) {
                        agent.setAsTraceable();
                    }

                    System.out.println("Full world:::init::agent: " + agent.getId() + " state: " + agent.getState().getId() + " target: " + (agent.getTargetState() != null ? agent.getTargetState().getId() : "NULL"));

                    agents[i++] = agent;

                }
            }
        }

      /*  for (Agent agent : agents) {
            System.out.println("agent: " + agent.getId() + " state: " + agent.getState().getId() + " target: " + agent.getTargetState().getId());
        }*/

        environment.updateAgentsCount();

        environment.reassigningStateLocationAndTransPath();

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
            }

            System.out.println(Globals.WORLD_TIMER + " : World.run ------------------------------- ");

            //============================//============================  Updating agents statuses
            for (Agent agent : agents) {
                agent.resetParams();
                agent.updateStateMap();
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

            System.out.println(statistic.toString());
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
                ti + " | agentsCount=" + agentsCount ;
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
