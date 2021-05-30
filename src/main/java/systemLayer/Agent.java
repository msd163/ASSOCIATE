package systemLayer;

import com.google.gson.annotations.Expose;
import stateLayer.StateX;
import stateLayer.TravelHistory;
import trustLayer.AgentTrust;
import utils.Globals;

import java.util.ArrayList;
import java.util.List;

public class Agent {

    public Agent(World parentWorld, int id) {
        targetStateId = -1;
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
    @Expose
    private int id;

    //============================ processing variables
    private int currentDoingServiceSize;

    //============================
    private World world;
    private StateX state;
    // The history of previously visited states
    private ArrayList<TravelHistory> travelHistories;
    @Expose
    private int targetStateId;
    private StateX targetState;
    // Next steps in order to reach the target state
    private ArrayList<StateX> nextSteps;
    private Agent helper;
    //============================
    @Expose
    private AgentCapacity capacity;

    private AgentTrust trust;

    private AgentBehavior behavior;

    // agents that are watched by this agent
    // watched list must be sorted according to trust level of watched agents.
    // first agent (zero position) is agent with maximum trust level.
    private List<WatchedAgent> watchedAgents;

    // States that will be watched according to watchDepth capacity of this agent.
    // If watchDepth is Zero, watchStates will be empty
    private List<WatchedState> watchedStates;

    //============================//============================//============================

    public void init() {
        capacity = new AgentCapacity(this);
        initVars();

    }

    public void initVars() {

        trust = new AgentTrust(this, capacity.getTrustHistoryCap(), capacity.getTrustHistoryItemCap());
        behavior = new AgentBehavior();
        watchedAgents = new ArrayList<>();
        watchedStates = new ArrayList<>();

        helper = null;

        nextSteps = new ArrayList<>();
        travelHistories = new ArrayList<>();
        if (state != null) {
            updateTravelHistory();
        }

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

    /**
     *
     */
    public void updateTravelHistory() {

        int size = travelHistories.size();
        // If the agent do not moved and it's state doesnt changed, only the visit time will to be updated.
        if (size > 0 && travelHistories.get(size - 1).getStateX().getId() == state.getId()) {
            travelHistories.get(size - 1).updateVisitTime();
            return;
        }

        // If travelHistory if full, remove old (first) state in map
        if (size >= capacity.getTravelHistoryCap()) {
            travelHistories.remove(0);
        }

        // Adding new state to the map
        travelHistories.add(new TravelHistory(
                state,
                Globals.WORLD_TIMER,
                state.getTargets(),
                helper,
                state.getId() == targetStateId,
                state.isIsPitfall()
        ));

        // Printing map
        String sIds = "";
        for (TravelHistory s : travelHistories) {
            sIds += " | " + s.getStateX().getId() /*+ "-" + s.getVisitTime()*/;
        }
        System.out.println("agent:::updateStateMap::agentId: " + id + " [ c: " + state.getId() + " >  t: " + (targetState == null ? "NULL" : targetState.getId()) + " ] #  maps: " + sIds);

    }
    //============================//============================ Routing


    public void clearNextSteps() {
        nextSteps.clear();
        helper = null;
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
        watchedAgents = state.getWatchListOfAgents(capacity.getWatchDepth(), capacity.getWatchDepth(), capacity.getWatchListCapacity(), this, visitedStates, parentPath);
        watchedStates.clear();
        state.getWatchListOfStates(capacity.getWatchDepth(), watchedStates, null);

   /*     // Sorting watched agents according trust level of this agent to them.
        watchedAgents.sort((WatchedAgent w1, WatchedAgent w2) -> {
            float t1 = w1.getTrust();
            float t2 = w2.getTrust();

            if (t1 > t2) {
                return 1;
            } else if (t1 < t2) {
                return -1;
            }
            return 0;
        });*/
    }

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

    //============================ Doing

    public void shareExperienceWith(Agent agent) {
/*        for (AgentHistory history : trust.getHistories()) {
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
        }*/
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
        this.targetStateId = targetState == null ? -1 : targetState.getId();

    }

    public ArrayList<TravelHistory> getTravelHistories() {
        return travelHistories;
    }

    public ArrayList<StateX> getNextSteps() {
        return nextSteps;
    }

    public List<WatchedState> getWatchedStates() {
        return watchedStates;
    }

    public boolean isSimConfigShowRequestedService() {
        return simConfigShowRequestedService;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getTargetStateId() {
        return targetStateId;
    }

    public Agent getHelper() {
        return helper;
    }

    public void setHelper(Agent helper) {
        this.helper = helper;
    }
}
