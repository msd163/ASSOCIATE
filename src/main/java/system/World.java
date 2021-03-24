package system;

import utils.Config;
import utils.DiagramDrawingWindow;
import utils.Globals;
import utils.MainDrawingWindow;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {

    public World() {
        init();
    }

    private int width;                      // The width of this world. it will defined randomly in Initializing time of the world.
    private int height;                     // The height of this world. it will defined randomly in Initializing time of the world.
    private int maxVelocityOfAgents_x;      //
    private int maxVelocityOfAgents_y;
    private int bignessFactor;
    private Agent[] agents;
    private int agentsCount;

    private ServiceType[] serviceTypes;     // The services that are accessible in this world

    private int[] traceAgentIds;            // Ids that will be traced in simulation time, in the MainDiagram window

    private List<WorldHistory> histories;

    //============================//============================//============================
    private void init() {


        totalServiceCount =
                falseNegative =
                        falsePositive =
                                trueNegative =
                                        truePositive = 0;
        //============================

        // Identifying the agents that we want to trace in Main diagram.
        traceAgentIds = new int[]{1, 4, 9, 10, 11, 12, 13};

        // Resetting the timer of the world.
        Globals.WORLD_TIMER = 0;

        histories = new ArrayList<WorldHistory>();

        //============================

        width = Globals.profiler.getWorld_width();
        height = Globals.profiler.getWorld_width();

        bignessFactor = Math.max(width, height);

        maxVelocityOfAgents_x = Globals.profiler.getMaxVelocityX();

        agentsCount = Globals.profiler.getPopulationCount();
        agents = new Agent[agentsCount];

        //============================ Services

        serviceTypes = new ServiceType[Config.WORLD_SERVICES_COUNT];
        for (int i = 0; i < Config.WORLD_SERVICES_COUNT; i++) {
            serviceTypes[i] = new ServiceType(i + 1);
        }

        //============================ Initializing agents

        System.out.println(
                " | bignessFactor: " + bignessFactor
                        + " | agentsCount: " + agentsCount

        );

        int id = 0;
        int thisBunchFinished = Globals.profiler.CurrentBunch().getBunchCount();
        int stateCount = Globals.environment.getStateCount();
        for (int i = 0 ; i < Globals.profiler.getPopulationCount(); i++) {
            if(i >= thisBunchFinished)
            {
                Globals.profiler.NextBunch();
                thisBunchFinished = thisBunchFinished + Globals.profiler.CurrentBunch().getBunchCount();
            }
            agents[i] = new Agent(this, ++id);
            agents[i].init();

            // if agentId is in 'traceAgentIds', it will set as traceable
            if (isTraceable(i)) {
                agents[i].setAsTraceable();
            }
            agents[i].my_national_code = i;
            agents[i].setAgent_Current_State( Globals.RANDOM.nextInt(stateCount) );
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

        if (showMainWindow) {
            JFrame mainFrame = new JFrame();
            mainFrame.add(mainWindow);
            mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            mainFrame.setMinimumSize(new Dimension(this.width, this.height));
            mainFrame.setVisible(true);
        }
        //============================ Initializing Diagram Drawing Windows
        DiagramDrawingWindow diagramWindow = new DiagramDrawingWindow(this);
        if (showDiagramWindow) {
            JFrame diagramFrame = new JFrame();
            diagramFrame.add(diagramWindow);
            diagramFrame.setExtendedState(diagramFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            diagramFrame.setMinimumSize(new Dimension(this.width, this.height));
            diagramFrame.setVisible(true);
        }

        // Main loop of running in a world
        for (; Globals.WORLD_TIMER < Config.WORLD_LIFE_TIME; Globals.WORLD_TIMER++) {

            for (Agent agent : agents) {
                agent.updateCurrentState();
            }

            for (Agent agent : agents) {
                agent.resetParams();
                agent.updateWatchList();
                agent.updateProfile();
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

            System.out.println("-------------------------\t\t\t\t\tcurrentTime: " + Globals.WORLD_TIMER);
            System.out.println("  totalServiceCount    : " + totalServiceCount);
            System.out.println("  honestServiceCount   : " + honestServiceCount + " >  " + (float) honestServiceCount / totalServiceCount);
            System.out.println("  dishonestServiceCount: " + dishonestServiceCount + " >  " + (float) dishonestServiceCount / totalServiceCount);
            System.out.println("  dontDoneServices     : " + dontDoneServices + " >  " + (float) dontDoneServices / totalServiceCount);
            System.out.println("  recordedService      : " + recordedServices);

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
    @Override
    public String toString() {
        return "World{" +
                "width=" + width +
                ", \nheight=" + height +
                ", \nmaxVelocityOfAgents_x=" + maxVelocityOfAgents_x +
                ", \nmaxVelocityOfAgents_y=" + maxVelocityOfAgents_y +
                ", \nagents length=" + agents.length +
                '}';
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxVelocityOfAgents_x() {
        return maxVelocityOfAgents_x;
    }

    public int getMaxVelocityOfAgents_y() {
        return maxVelocityOfAgents_y;
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

    public int getBignessFactor() {
        return bignessFactor;
    }

    public int getAgentsCount() {
        return agentsCount;
    }

}
