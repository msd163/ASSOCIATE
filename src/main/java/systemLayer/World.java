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
import java.util.ArrayList;
import java.util.List;

public class World {

    public World(Environment environment) throws Exception {
        init(environment);
    }

    private Agent[] agents;
    private int agentsCount;

    private ServiceType[] serviceTypes;     // The services that are accessible in this world

    private int[] traceAgentIds;            // Ids that will be traced in simulation time, in the MainDiagram window

    private List<WorldHistory> histories;

    private Environment environment;

    private WorldStatistics[] statistics;

    private Router router;

    //============================//============================//============================
    private void init(Environment _environment) throws Exception {

        totalServiceCount =
                falseNegative =
                        falsePositive =
                                trueNegative =
                                        truePositive = 0;
        //============================

        // Identifying the agents that we want to trace in Main diagram.
        traceAgentIds = new int[]{1};

        // Resetting the timer of the world.
        Globals.WORLD_TIMER = 0;

        histories = new ArrayList<WorldHistory>();

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

        //============================ Services
        int serviceCount = Globals.profiler.getServiceCount();
        serviceTypes = new ServiceType[serviceCount];
        for (int i = 0; i < serviceCount; i++) {
            serviceTypes[i] = new ServiceType(i + 1);
        }


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

        if (Config.SIMULATION_MODE == TtSimulationMode.SimpleEnvironment) {
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

    private int totalServiceCount;
    private int dishonestServiceCount;
    private int honestServiceCount;
    private int recordedServices;
    private int dontDoneServices;
    private int falsePositive;
    private int falseNegative;
    private int truePositive;
    private int trueNegative;

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
        if (showDiagramWindow) {
            JFrame diagramFrame = new JFrame();
            diagramFrame.add(diagramWindow);
            diagramFrame.setExtendedState(diagramFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            // diagramFrame.setMinimumSize(new Dimension(this.width, this.height));
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

            //============================ Finding Doer of service an doing it
            Agent doerAgent;
            for (Agent requesterAgent : agents) {

                // Ignoring requests of dishonest agents
                if (!Config.DO_REQUEST_SERVICE_BY_DISHONEST_AGENT && !requesterAgent.getBehavior().getIsHonest()) {
                    continue;
                }

                Service requestedService = requesterAgent.selectRequestedService();
                doerAgent = requesterAgent.findDoerOfRequestedService(requestedService);

                //============================ Acting Service by doer
                if (doerAgent != null) {
                    requestedService = doerAgent.doService(requestedService);

                    //============================ TRUST Processes
                    /*//----------  TRUST EXPERIENCE*/
                    if (requestedService.getResult() > 0 && Config.TRUST_LEVEL_RECORD_EXPERIENCES_OF_OTHERS) {
                        requesterAgent.shareExperienceWith(doerAgent);
                    }

                    /*//------------- TRUST OBSERVATION*/
                    for (Agent superAgent : agents) {
                        // Adding the service to the REQUESTER of service and SUPERVISOR AGENTS that can watch the requester or the doer
                        if (
                                (superAgent.getId() == requesterAgent.getId() && Config.TRUST_LEVEL_RECORD_ACTIVITIES_OF_ITSELF)   //  want adding current service experience to history of the requester
                                        ||
                                        (superAgent.getId() != requesterAgent.getId() && Config.TRUST_LEVEL_RECORD_ACTIVITIES_OF_OTHERS
                                                &&
                                                superAgent.getId() != doerAgent.getId()
                                                &&
                                                (
                                                        superAgent.canWatch(requesterAgent)
                                                                ||
                                                                superAgent.canWatch(doerAgent)
                                                )
                                        )

                        ) {
                            /*if (superAgent.getId() == requesterAgent.getId()) {
                                System.out.println("Added to history of requester: " + superAgent.getId());
                            }*/
                            superAgent.getTrust().recordService(requestedService);
                        }
                    }
                } else {
                    // There is no agent to do this action
                }
            }

            dontDoneServices =
                    recordedServices =
                            honestServiceCount =
                                    dishonestServiceCount =
                                            totalServiceCount = 0;

            for (Agent agent : agents) {
                for (Service service : agent.getRequestedServices()) {
                    if (service.getResult() > 0) {
                        honestServiceCount++;
                    } else if (service.getResult() < 0) {
                        dishonestServiceCount++;
                    } else {
                        dontDoneServices++;
                    }
                    totalServiceCount++;
                }
                recordedServices += agent.getTrust().getHistorySize();
            }

         /*   System.out.println("-------------------------\t\t\t\t\tcurrentTime: " + Globals.WORLD_TIMER);
            System.out.println("  totalServiceCount    : " + totalServiceCount);
            System.out.println("  honestServiceCount   : " + honestServiceCount + " >  " + (float) honestServiceCount / totalServiceCount);
            System.out.println("  dishonestServiceCount: " + dishonestServiceCount + " >  " + (float) dishonestServiceCount / totalServiceCount);
            System.out.println("  dontDoneServices     : " + dontDoneServices + " >  " + (float) dontDoneServices / totalServiceCount);
            System.out.println("  recordedService      : " + recordedServices);
*/


            System.out.println(statistic.toString());
            Globals.statGenerator.addStat(statistic);


            histories.add(new WorldHistory(totalServiceCount, dishonestServiceCount, honestServiceCount));

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

        for (Agent agent : agents) {

            for (Service service : agent.getRequestedServices()) {
                if (service.getResult() > 0) {
                    honestServiceCount++;
                } else {
                    dishonestServiceCount++;
                }
                totalServiceCount++;
            }
        }

        System.out.println("========================================");
        System.out.println("  totalServiceCount    : " + totalServiceCount);
        System.out.println("  honestServiceCount   : " + honestServiceCount);
        System.out.println("  dishonestServiceCount: " + dishonestServiceCount);

    }

    //============================//============================//============================

    public String toStringInfo() {
        StringBuilder tx = new StringBuilder("\n");
        StringBuilder ti = new StringBuilder("\n\n\n\t");

        return tx + "World: " +
                ti + " | agentsCount=" + agentsCount +
                ti + " | dishonestSrvCount=" + dishonestServiceCount +
                ti + " * honestSrvCount=" + honestServiceCount +
                ti + " | totalSrvCount=" + totalServiceCount +
                ti + " * recordedSrv=" + recordedServices +
                ti + " * dontDoneSrv=" + dontDoneServices +
                ti + " | falsePositive=" + falsePositive +
                ti + " * falseNegative=" + falseNegative +
                ti + " | truePositive=" + truePositive +
                ti + " * trueNegative=" + trueNegative;
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
                ti + ", totalServiceCount=" + totalServiceCount +
                ti + ", dishonestServiceCount=" + dishonestServiceCount +
                ti + ", honestServiceCount=" + honestServiceCount +
                ti + ", recordedServices=" + recordedServices +
                ti + ", dontDoneServices=" + dontDoneServices +
                ti + ", falsePositive=" + falsePositive +
                ti + ", falseNegative=" + falseNegative +
                ti + ", truePositive=" + truePositive +
                ti + ", trueNegative=" + trueNegative +
                tx + '}';
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public Agent[] getAgents() {
        return agents;
    }

    public ServiceType[] getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(ServiceType[] serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public List<WorldHistory> getHistories() {
        return histories;
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
