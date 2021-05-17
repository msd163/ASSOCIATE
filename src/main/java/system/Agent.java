package system;

import stateTransition.RoutingHelp;
import stateTransition.StateMap;
import stateTransition.StateX;
import stateTransition.TransitionX;
import utils.Config;
import utils.Globals;
import utils.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Agent {

    public Agent(World parentWorld, int id) {
        this.world = parentWorld;
        this.id = id;
        currentDoingServiceSize = 0;
        simConfigTraceable =
                simConfigShowWatchRadius =
                        simConfigShowRequestedService =
                                simConfigShowTargetState =
                                        simConfigLinkToWatchedAgents = false;
    }


    //============================
    private boolean simConfigShowWatchRadius;
    private boolean simConfigLinkToWatchedAgents;
    private boolean simConfigTraceable;
    private boolean simConfigShowRequestedService;
    private boolean simConfigShowTargetState;

    //============================
    private int id;

    //============================ processing variables
    private int currentDoingServiceSize;

    //============================
    private World world;
    private StateX state;
    private ArrayList<StateMap> stateMaps;
    private StateX targetState;
    private ArrayList<StateX> nextStates;
    //============================
    private AgentCapacity capacity;

    private AgentTrust trust;

    private AgentBehavior behavior;

    // agents that are watched by this agent
    // watched list must be sorted according to trust level of watched agents.
    // first agent (zero position) is agent with maximum trust level.
    private List<WatchedAgent> watchedAgents;

    // States that will be watched according to watchRadius capacity of this agent.
    // If watchRadius is Zero, watchStates will be empty
    private List<WatchedState> watchedStates;

    // list of services that can request with this agent
    private List<ServiceType> requestingServiceTypes;

    // list of services that can done with this agent
    private List<ServiceType> doingServiceTypes;

    // list of services that requested by agent
    private List<Service> requestedServices;

    // list of services that done by agent
    private List<Service> doneServices;

    //============================//============================//============================

    public void init() {
        capacity = new AgentCapacity(this);
        trust = new AgentTrust(this, capacity.getHistoryCap(), capacity.getHistoryServiceRecordCap());
        behavior = new AgentBehavior();
        watchedAgents = new ArrayList<>();


        //todo: [policy] : assigning requested services
        requestingServiceTypes = new ArrayList<ServiceType>();
        requestingServiceTypes.addAll(Arrays.asList(world.getServiceTypes()));

        //todo: [policy] : assigning possible services
        doingServiceTypes = new ArrayList<ServiceType>();
        doingServiceTypes.addAll(Arrays.asList(world.getServiceTypes()));

        requestedServices = new ArrayList<Service>();
        doneServices = new ArrayList<Service>();

        stateMaps = new ArrayList<>();
        nextStates = new ArrayList<>();

    }

    public void setAsTraceable() {
        simConfigTraceable = true;
        simConfigShowWatchRadius = true;
        simConfigLinkToWatchedAgents = true;
        simConfigShowRequestedService = true;
        simConfigShowTargetState = true;
    }


    public void updateProfile() {

        behavior.updateHonestState();
    }

    public void resetParams() {
        currentDoingServiceSize = 0;
    }


    //============================//============================ State Map

    private int getOldStateMapIndex() {
        int minVal = 99999999;
        int oldIndex = 0, ix = 0;
        for (StateMap map : stateMaps) {
            if (map.getVisitTime() < minVal) {
                minVal = map.getVisitTime();
                oldIndex = ix;
            }
            ix++;
        }
        return oldIndex;
    }

    private int getStateMapIndex(StateX stateX) {
        int ix = 0;
        for (StateMap map : stateMaps) {
            if (map.getStateX().getId() == stateX.getId()) {
                return ix;
            }
            ix++;
        }
        return -1;
    }

    /**
     *
     */
    public void updateStateMap() {

        int size = stateMaps.size();
        // If the agent do not moved and it's state doesnt changed, only the visit time will to be updated.
        if (size > 0 && stateMaps.get(size - 1).getStateX().getId() == state.getId()) {
            stateMaps.get(size - 1).updateVisitTime();
            return;
        }

        // If stateMap if full, remove old (first) state in map
        if (size >= capacity.getStateMapCap()) {
            stateMaps.remove(0);
        }

        // Adding new state to the map
        stateMaps.add(new StateMap(state, Globals.WORLD_TIMER, state.getTargets()));

        // Printing map
        String sIds = "";
        for (StateMap s : stateMaps) {
            sIds += " | " + s.getStateX().getId() /*+ "-" + s.getVisitTime()*/;
        }
        System.out.println("agent:::updateStateMap::agentId: " + id + " [ c: " + state.getId() + " >  t: " + (targetState == null ? "NULL" : targetState.getId()) + " ] #  maps: " + sIds);

    }
    //============================//============================ Routing


    public void clearNextStates() {
        nextStates.clear();
    }

    public boolean isInTargetState() {

        return targetState != null && state.getId() == targetState.getId();
    }

    //============================ Watching

    /**
     * Clearing the current watch list and filling it according the current state of the agent.
     * The watch list is depends on the watch radius, that is the capacity of the agent.
     */
    public void updateWatchList() {
        watchedAgents.clear();
        ArrayList<StateX> visitedStates = new ArrayList<>();    // list of visited states in navigation of states, this list is for preventing duplicate visiting.
        ArrayList<StateX> parentPath = new ArrayList<>();
        watchedAgents = state.getWatchListOfAgents(capacity.getWatchRadius(), capacity.getWatchRadius(), capacity.getWatchListCapacity(), this, visitedStates, parentPath);
        watchedStates = state.getWatchListOfStates(capacity.getWatchRadius());

       /* watchedAgents.clear();
        // List<StateX> seenStates = state.getWatchList(capacity.getWatchRadius());


        int watchCount = 0;
        int depth = 0;
        StateX watchedState = state;
        while (watchCount <= capacity.getWatchListCapacity()) {

            for (Agent agent : watchedState.getAgents()) {
                watchCount++;
                WatchedAgent watchedAgent = new WatchedAgent();
                watchedAgent.setAgent(agent);
                if (depth > 0) {
                    watchedAgent.addPath(state);
                }
                watchedAgents.add(watchedAgent);
                if (watchCount > capacity.getWatchListCapacity()) {
                    break;
                }
            }

            if(depth< capacity.getWatchRadius()){

            }

        }


        if (seenStates == null) {
            return;
        }

        for (StateX st : seenStates) {
            ArrayList<Agent> ags = st.getAgents();
            for (Agent ag : ags) {
                if (watchedAgents.size() <= capacity.getWatchListCapacity() &&
                        !watchedAgents.contains(ag)
                ) {
                    watchedAgents.add(ag);
                }
            }
        }

        watchedAgents.sort((o1, o2) -> {

            float t1 = trust.getTrustScore(o1);
            float t2 = trust.getTrustScore(o2);

            return Float.compare(t1, t2);
        });*/

    }


//    public boolean canWatch(int x, int y) {
////        TODO: here should be updated.
//        return Math.sqrt(Math.pow((double) x - (double) location.getX(), 2) + Math.pow((double) y - (double) location.getY(), 2)) < (double) capacity.getWatchRadius();
//    }


    public boolean canWatch(Agent agent) {
        if (watchedAgents != null) {
            for (int i = 0; i < watchedAgents.size(); i++) {
                if (watchedAgents.get(i).getAgent().id == agent.id) {
                    return true;
                }
            }
        }
        return false;
    }

    //============================ Requesting

    public Service selectRequestedService() {

        ServiceType st = requestingServiceTypes.get(Globals.RANDOM.nextInt(requestingServiceTypes.size()));

        Service rs = new Service();
        rs.setRequester(this);
        rs.setServiceType(st);

        requestedServices.add(rs);

        return rs;
    }

    public Agent findDoerOfRequestedService(Service service) {
        //todo: [policy] : selecting doer of service

        int watchSize = watchedAgents.size();
        if (watchSize == 0) {
            return null;
        }

        trust.sortHistoryByTrustLevel();

        for (int index : trust.getHistoriesSortedIndex()) {
            AgentHistory history = trust.getHistories()[index];

            for (WatchedAgent watchedAgent : watchedAgents) {
                if (history != null
                        && history.getDoerAgent().getId() == watchedAgent.getAgent().getId()  // if the watched agent is in history
                        //todo: [policy] : set threshold to trustee selection
                        && history.getEffectiveTrustLevel() > 0  // if the watched agent is not dishonest
                ) {

                    boolean serviceAcceptance = watchedAgent.getAgent().canDoService(this, service);
                    if (serviceAcceptance) {
                        return watchedAgent.getAgent();
                    }
                }
            }
        }

        //============================  Random selection if there is no agent in trust history

        int i;
        int tryCount = 0;

        while (++tryCount < 10) {
            i = Globals.RANDOM.nextInt(watchSize);
            if (trust.getTrustScore(watchedAgents.get(i).getAgent()) >= 0) {
                return watchedAgents.get(i).getAgent();
            }
        }

        return null;

    }


    //============================ Doing

    public boolean canDoService(Agent requester, Service service) {

        //todo: adding limit to count of concurrent doing service
        if (currentDoingServiceSize < capacity.getConcurrentDoingServiceCap() && watchedAgents.contains(requester)) {
            if (doingServiceTypes.contains(service.getServiceType())) {
                //todo: [policy] : bidirectional trust
                return true;
            }
        }
        return false;
    }

    public Service doService(Service service) {

        currentDoingServiceSize++;

        service.setDoer(this);
        /*Globals.random.nextFloat() * */
        float res = capacity.getCapPower() * (behavior.getIsHonest() ? 0.1f : -0.1f);
        service.setResult(res);

        doneServices.add(service);

        return service;

    }

    public void shareExperienceWith(Agent agent) {
        for (AgentHistory history : trust.getHistories()) {
            if (history != null) {
                for (ServiceMetaInfo info : history.getServiceMetaInfos()) {
                    if (info != null) {
                        // Only direct observation
                        if (info.getPublisher().getId() == this.id) {
                            agent.getTrust().recordExperience(info);
                        }
                    }
                }
            }
        }
    }

    //============================//============================//============================

    BasicStroke stroke3 = new BasicStroke(3);
    BasicStroke stroke2 = new BasicStroke(2);
    BasicStroke stroke1 = new BasicStroke(1);
    Font font = new Font("Tahoma", Font.PLAIN, 9);
    Color honestBackColor;
    Color honestForeColor;
    Color honestBorderColor;

    private boolean isCapCandid = false;

    public void draw(Graphics2D g, int index) {

        int loc_x;
        int loc_y;

        Point tileIndex = state.getTileLocation(index);

        loc_x = tileIndex.x;
        loc_y = tileIndex.y;


        honestBackColor = behavior.getIsHonest() ? new Color(0, 255, 85) : new Color(255, 71, 71);
        honestForeColor = behavior.getIsHonest() ? new Color(36, 151, 9) : new Color(255, 196, 166);
        honestBorderColor = behavior.getIsHonest() ? new Color(29, 102, 0) : new Color(160, 0, 0);
        isCapCandid = Config.DRAWING_SHOW_POWERFUL_AGENTS_RADIUS && capacity.getCapPower() > Config.DRAWING_POWERFUL_AGENTS_THRESHOLD;
        // Drawing watch radius
        if (isCapCandid || isSimConfigShowWatchRadius()) {
            g.setColor(isCapCandid ? (behavior.getIsHonest() ? Color.GREEN : Color.RED) : simConfigTraceable ? Color.CYAN : Color.lightGray);
            g.drawOval(
                    loc_x - capacity.getWatchRadius(),
                    loc_y - capacity.getWatchRadius(),
                    capacity.getWatchRadius() * 2,
                    capacity.getWatchRadius() * 2
            );
        }

        // Drawing links to watched agents
        if (simConfigLinkToWatchedAgents) {
            g.setColor(Color.GRAY);
            for (WatchedAgent wa : watchedAgents) {
                g.drawLine(loc_x, loc_y, wa.getAgent().getLoc_x(), wa.getAgent().getLoc_y());
            }
        }

        if (simConfigShowRequestedService && requestedServices.size() > 0) {
            Service service = requestedServices.get(requestedServices.size() - 1);
            if (service != null) {
                g.setStroke(stroke3);
                if (service.getDoer() != null) {
                    g.setColor(service.getDoer().getBehavior().getIsHonest() ? Color.GREEN : Color.RED);
                    g.drawLine(loc_x, loc_y, service.getDoer().getLoc_x(), service.getDoer().getLoc_y());
                } else {
                    g.setColor(Color.GREEN);
                    g.drawLine(loc_x, loc_y, loc_x + 40, loc_y + 40);
                }
                g.setStroke(stroke1);
            }
        }

/*        if (simConfigShowTargetState) {
            RectangleX rec = targetState.getBoundedRectangle();
            g.setColor(Color.RED);
            g.draw(new Rectangle.Float(rec.x, rec.y, rec.with, rec.height));
        }*/

        // Set color of node with honest strategy
        g.setColor(honestBackColor);

        // Draw node according to it's capacity
        int agentBound = capacity.getCapPower() / 10;
        g.fillOval(loc_x - agentBound, loc_y - agentBound, agentBound * 2, agentBound * 2);

        if (isInTargetState()) {
            // If agent is in target state draw a circle around it.
            g.setColor(honestBorderColor);
            g.setStroke(stroke2);
            g.drawArc(loc_x - agentBound, loc_y - agentBound, agentBound * 2, agentBound * 2, 0, 270);
        }

        // Drawing id of the node
        g.setColor(honestForeColor);
        g.setFont(font);
        g.drawString(id + "", loc_x - 5, loc_y + 5 /*+ capacity.getCapPower() + 10*/);

    }
    //============================//============================//============================


    @Override
    public String toString() {
        return "Agent{" +
                "\n\tstate=" + state +
                ",\n\t simConfigShowWatchRadius=" + simConfigShowWatchRadius +
                ",\n\t simConfigLinkToWatchedAgents=" + simConfigLinkToWatchedAgents +
                ",\n\t simConfigTraceable=" + simConfigTraceable +
                ",\n\t simConfigShowRequestedService=" + simConfigShowRequestedService +
                ",\n\t id=" + id +
                ",\n\t currentDoingServiceSize=" + currentDoingServiceSize +
                ",\n\t world=" + world +
                ",\n\t capacity=" + capacity +
                ",\n\t trust=" + trust +
                ",\n\t behavior=" + behavior +
                ",\n\t watchedAgents=" + watchedAgents +
                ",\n\t requestingServiceTypes=" + requestingServiceTypes +
                ",\n\t doingServiceTypes=" + doingServiceTypes +
                ",\n\t requestedServices=" + requestedServices +
                ",\n\t doneServices=" + doneServices +
                ",\n\t stroke3=" + stroke3 +
                ",\n\t stroke1=" + stroke1 +
                ",\n\t font=" + font +
                ",\n\t honestColor=" + honestBackColor +
                ",\n\t isCapCandid=" + isCapCandid +
                '}';
    }

    public int getId() {
        return id;
    }

    public int getLoc_x() {
        return (int) state.getLocation().getX();
    }

    public int getLoc_y() {
        return (int) state.getLocation().getY();
    }


    public World getWorld() {
        return world;
    }

    public AgentCapacity getCapacity() {
        return capacity;
    }

    public List<WatchedAgent> getWatchedAgents() {
        return watchedAgents;
    }

    public boolean isSimConfigLinkToWatchedAgents() {
        return simConfigLinkToWatchedAgents;
    }

    public boolean isSimConfigShowWatchRadius() {
        return simConfigShowWatchRadius;
    }

    public boolean isSimConfigShowTargetState() {
        return simConfigShowTargetState;
    }

    public boolean isSimConfigTraceable() {
        return simConfigTraceable;
    }

    public List<Service> getDoneServices() {
        return doneServices;
    }

    public void setDoneServices(List<Service> doneServices) {
        this.doneServices = doneServices;
    }

    public List<Service> getRequestedServices() {
        return requestedServices;
    }

    public void setRequestedServices(List<Service> requestedServices) {
        this.requestedServices = requestedServices;
    }


    public List<ServiceType> getRequestingServiceTypes() {
        return requestingServiceTypes;
    }

    public void setRequestingServiceTypes(List<ServiceType> requestingServiceTypes) {
        this.requestingServiceTypes = requestingServiceTypes;
    }

    public List<ServiceType> getDoingServiceTypes() {
        return doingServiceTypes;
    }

    public void setDoingServiceTypes(List<ServiceType> doingServiceTypes) {
        this.doingServiceTypes = doingServiceTypes;
    }

    public AgentBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(AgentBehavior behavior) {
        this.behavior = behavior;
    }

    public AgentTrust getTrust() {
        return trust;
    }

    public StateX getState() {
        return state;
    }

    public void setState(StateX state) {
        this.state = state;
    }

    public StateX getTargetState() {
        return targetState;
    }

    public void setTargetState(StateX targetState) {
        this.targetState = targetState;
    }

    public ArrayList<StateMap> getStateMaps() {
        return stateMaps;
    }

    public ArrayList<StateX> getNextStates() {
        return nextStates;
    }

    public List<WatchedState> getWatchedStates() {
        return watchedStates;
    }
}
