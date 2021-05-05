package system;

import stateTransition.StateX;
import utils.Config;
import utils.Globals;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Agent {
    private StateX state;


    public Agent(World parentWorld, int id, StateX stateX) {
        this.world = parentWorld;
        this.id = id;
        this.state = stateX;
        currentDoingServiceSize = 0;
        simConfigTraceable =
                simConfigShowWatchRadius =
                        simConfigShowRequestedService =
                                simConfigLinkToWatchedAgents = false;


    }

    //============================
    private boolean simConfigShowWatchRadius;
    private boolean simConfigLinkToWatchedAgents;
    private boolean simConfigTraceable;
    private boolean simConfigShowRequestedService;

    //============================

    private int id;


    //============================ processing variables
    private int currentDoingServiceSize;

    //============================
    private World world;
    //============================

    private AgentCapacity capacity;

    private AgentTrust trust;

    private AgentBehavior behavior;

    // agents that are watched by this agent
    private List<Agent> watchedAgents;

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
        watchedAgents = new ArrayList<Agent>();


        //todo: [policy] : assigning requested services
        requestingServiceTypes = new ArrayList<ServiceType>();
        requestingServiceTypes.addAll(Arrays.asList(world.getServiceTypes()));

        //todo: [policy] : assigning possible services
        doingServiceTypes = new ArrayList<ServiceType>();
        doingServiceTypes.addAll(Arrays.asList(world.getServiceTypes()));

        requestedServices = new ArrayList<Service>();
        doneServices = new ArrayList<Service>();

    }

    public void setAsTraceable() {
        simConfigTraceable = true;
        simConfigShowWatchRadius = true;
        simConfigLinkToWatchedAgents = true;
        simConfigShowRequestedService = true;
    }


    public void updateProfile() {

        behavior.updateHonestState();
    }

    public void resetParams() {
        currentDoingServiceSize = 0;
    }


 /*   public void setCurrentState(int state) {
        if (Globals.environment.getTransition(state).I_am_in(this)) {
            if (currentState != -1) {
                Globals.environment.getTransition(currentState).I_am_out(this);
            }
            this.currentState = state;
        }

    }*/

    //============================ Watching

    /**
     * Clearing the current watch list and filling it according the current state of the agent.
     * The watch list is depends on the watch radius, that is the capacity of the agent.
     */
    public void updateWatchList() {
        watchedAgents.clear();
        ArrayList<StateX> seenStates = state.getWatchList(capacity.getWatchRadius());

        if (seenStates == null) {
            return;
        }

        for (StateX st : seenStates) {
            ArrayList<Agent> ags = st.getAgents();
            for (Agent ag : ags) {
                if (watchedAgents.size() <= capacity.getWatchListCapacity()) {
                    watchedAgents.add(ag);
                }
            }
        }
    }


//    public boolean canWatch(int x, int y) {
////        TODO: here should be updated.
//        return Math.sqrt(Math.pow((double) x - (double) location.getX(), 2) + Math.pow((double) y - (double) location.getY(), 2)) < (double) capacity.getWatchRadius();
//    }


    public boolean canWatch(Agent agent) {
        for (int i = 0; i < watchedAgents.size(); i++) {
            if (watchedAgents.get(i).id == agent.id) {
                return true;
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

            for (Agent watchedAgent : watchedAgents) {
                if (history != null
                        && history.getDoerAgent().getId() == watchedAgent.getId()  // if the watched agent is in history
                        //todo: [policy] : set threshold to trustee selection
                        && history.getEffectiveTrustLevel() > 0  // if the watched agent is not dishonest
                ) {

                    boolean serviceAcceptance = watchedAgent.canDoService(this, service);
                    if (serviceAcceptance) {
                        return watchedAgent;
                    }
                }
            }
        }

        //============================  Random selection if there is no agent in trust history

        int i;
        int tryCount = 0;

        while (++tryCount < 10) {
            i = Globals.RANDOM.nextInt(watchSize);
            if (trust.getTrustScore(watchedAgents.get(i)) >= 0) {
                return watchedAgents.get(i);
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
    BasicStroke stroke1 = new BasicStroke(1);
    Font font = new Font("Tahoma", Font.PLAIN, 20);
    Color honestColor;

    private boolean isCapCandid = false;

    public void draw(Graphics2D g) {

        int loc_x;
        int loc_y;

        loc_x = state.getLocation().getX();
        loc_y = state.getLocation().getY();

        honestColor = behavior.getIsHonest() ? Color.GREEN : Color.RED;
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
            for (Agent wa : watchedAgents) {
                g.drawLine(loc_x, loc_y, wa.getLoc_x(), wa.getLoc_y());
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

        // Set color of node with honest strategy
        g.setColor(honestColor);
        // Draw node according to it's capacity
        int agentBound = capacity.getCapPower() / 5;
        g.fillOval(loc_x - agentBound, loc_y - agentBound, agentBound * 2, agentBound * 2);


        // Drawing id of the node
        g.setFont(font);

        // Set color of node with honest strategy
        g.drawString(id + "", loc_x, loc_y + capacity.getCapPower() + 10);

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
                ",\n\t honestColor=" + honestColor +
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

    public List<Agent> getWatchedAgents() {
        return watchedAgents;
    }

    public boolean isSimConfigLinkToWatchedAgents() {
        return simConfigLinkToWatchedAgents;
    }

    public boolean isSimConfigShowWatchRadius() {
        return simConfigShowWatchRadius;
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
}
