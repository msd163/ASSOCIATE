package systemLayer;

import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import com.google.gson.annotations.Expose;
import simulateLayer.profiler.SimulationProfiler;
import stateLayer.StateX;
import stateLayer.TravelHistory;
import trustLayer.AgentTrust;
import utils.Globals;
import utils.OutLog____;

import java.util.ArrayList;
import java.util.List;

public class Agent {

    public Agent(World parentWorld, int id) {
        spentTimeAtTheTarget =
                currentTargetStateIndex = 0;
        this.world = parentWorld;
        this.id = id;
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

    private int locX;
    private int locY;
    //============================
    @Expose
    private int id;

    //============================ processing variables

    //============================
    private World world;
    private StateX state;
    // The history of previously visited states
    private ArrayList<TravelHistory> travelHistories;
    @Expose
    private int[] targetStateIds;
    private StateX[] targetStates;
    private int currentTargetStateIndex;    // Index of targetStates array
    private int spentTimeAtTheTarget;            // time spent at the target state.


    // Next steps in order to reach the target state
    private ArrayList<StateX> nextSteps;
    private Agent helper;
    //============================
    @Expose
    private AgentCapacity capacity;

    @Expose
    private AgentTrust trust;

    @Expose
    private AgentBehavior behavior;

    // agents that are watched by this agent
    // watched list must be sorted according to trust level of watched agents.
    // first agent (zero position) is agent with maximum trust level.
    private List<WatchedAgent> watchedAgents;


    // States that will be watched according to watchDepth capacity of this agent.
    // If watchDepth is Zero, watchStates will be empty
    private List<WatchedState> watchedStates;

    //============================//============================//============================

    public void initForGenerator(SimulationProfiler profiler) {
        capacity = new AgentCapacity(this, profiler);
        trust = new AgentTrust(
                capacity.getTrustHistoryCap(),
                capacity.getTrustHistoryItemCap(),
                profiler.getCurrentBunch().getTrustReplaceHistoryMethod(),
                capacity.getTrustRecommendationCap(),
                capacity.getTrustRecommendationItemCap()
        );

        int targetCount = profiler.getCurrentBunch().getTargetCountD().nextValue();
        behavior = new AgentBehavior(profiler.getCurrentBunch().getBehavior());

        targetStateIds = new int[targetCount];
        targetStates = new StateX[targetCount];

        initVars();

    }

    public void initVars() {
        trust.setTrustParams(capacity.getTrustHistoryCap(), capacity.getTrustHistoryItemCap(), capacity.getTrustRecommendationCap(), capacity.getTrustRecommendationItemCap(), capacity.getObservationCap());
        trust.init(this);

        watchedAgents = new ArrayList<>();
        watchedStates = new ArrayList<>();

        helper = null;

        nextSteps = new ArrayList<>();
        travelHistories = new ArrayList<>();

    }

    public void setAsTraceable() {
        simConfigTraceable = true;
        simConfigShowWatchRadius = true;
        simConfigLinkToWatchedAgents = true;
        simConfigShowRequestedService = true;
        simConfigShowTargetState = true;
    }

    public void updateProfile() {

        behavior.updateBehaviorState();
    }


    public TravelHistory getLastTravelHistory() {
        if (travelHistories == null || travelHistories.isEmpty()) {
            return null;
        }
        return travelHistories.get(travelHistories.size() - 1);
    }

    //============================//============================ Travel
    public int addSpentTimeAtTheTarget() {
        return ++spentTimeAtTheTarget;
    }

    public boolean assignNextTargetState() {
        spentTimeAtTheTarget = 0;
        if (currentTargetStateIndex < getTargetCounts() - 1) {
            currentTargetStateIndex++;
        } else {
            currentTargetStateIndex = 0;
        }
        return true;
    }

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
                state.getId() == getCurrentTarget().getId(),
                state.isIsPitfall(),
                currentTargetStateIndex
        ));

        // Printing map
        String sIds = "";
        for (TravelHistory s : travelHistories) {
            sIds += " | " + s.getStateX().getId() /*+ "-" + s.getVisitTime()*/;
        }
        OutLog____.pl(TtOutLogMethodSection.UpdateTravelHistory, TtOutLogStatus.SUCCESS, sIds, this, state, getCurrentTarget());
        //System.out.println("agent:::updateStateMap::agentId: " + id + " [ c: " + state.getId() + " >  t: " + (getCurrentTarget() == null ? "NULL" : getCurrentTarget().getId()) + " ] #  maps: " + sIds);

    }
    //============================//============================ Routing


    public boolean isInFinalTarget() {
        return targetStates != null
                && targetStates.length - 1 == currentTargetStateIndex
                && state.getId() == getCurrentTarget().getId();
    }

    public void clearNextSteps() {
        nextSteps.clear();
        helper = null;
    }

    public StateX getCurrentTarget() {
        return targetStates[currentTargetStateIndex];
    }

    public StateX getNextTarget() {
        if (currentTargetStateIndex < getTargetCounts() - 1) {
            return targetStates[currentTargetStateIndex + 1];
        }
        return targetStates[0];
    }

    public int getCurrentTargetStateIndex() {
        return currentTargetStateIndex;
    }

    public int getTargetCounts() {
        return targetStates.length;
    }

    public boolean isAsTarget(StateX state) {
        for (StateX targetState : targetStates) {
            if (targetState != null && targetState.getId() == state.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInTargetState() {

        return targetStates != null && state.getId() == getCurrentTarget().getId();
    }

    public void addTarget(StateX state) {
        for (int i = 0; i < targetStates.length; i++) {
            if (targetStates[i] == null) {
                targetStates[i] = state;
                targetStateIds[i] = state.getId();
                break;
            }
        }
    }

    public void updateTargets() {
        int length = targetStateIds.length;
        targetStates = new StateX[length];

        for (int i = 0; i < length; i++) {
            int targetStateId = targetStateIds[i];
            targetStates[i] = world.getEnvironment().getState(targetStateId);
        }
    }

    //============================ Watching

    /**
     * Clearing the current watch list and filling it according the current state of the agent.
     * The watch list is depends on the watch radius, that is the capacity of the agent.
     */
    public void updateWatchList() {
        watchedAgents.clear();
        watchedStates.clear();
        ArrayList<StateX> parentPath = new ArrayList<>();

        //============================
        int remainedAgents = state.fillAgentsOfState(
                watchedAgents,
                capacity.getWatchListCapacity(),
                this,
                parentPath);

        //============================  adding current stated to visited states list
        WatchedState ws = new WatchedState();
        ws.setStateX(state);
        watchedStates.add(ws);

        state.fillStateWatchList(
                watchedAgents,
                watchedStates,
                capacity.getWatchDepth(),
                remainedAgents,
                this,
                parentPath
        );

        if (watchedStates.isEmpty()) {
            OutLog____.pl(TtOutLogMethodSection.UpdateWatchList, TtOutLogStatus.ERROR, "state watch list is empty", this, state, getCurrentTarget());
        }
        if (watchedAgents.isEmpty()) {
            OutLog____.pl(TtOutLogMethodSection.UpdateWatchList, TtOutLogStatus.WARN, "agent watch list is empty", this, state, getCurrentTarget());
        }
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

    public int getLocX() {
        return locX;
    }

    public void setLoc(int locX, int locY) {
        this.locX = locX;
        this.locY = locY;
    }

    public int getLocY() {
        return locY;
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

/*    public StateX getTargetState() {
        return targetState;
    }

    public void setTargetState(StateX targetState) {
        this.targetState = targetState;
        this.targetStateId = targetState == null ? -1 : targetState.getId();

    }*/

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

   /* public int getTargetStateId() {
        return targetStateId;
    }*/

    public int getSpentTimeAtTheTarget() {
        return spentTimeAtTheTarget;
    }

    public Agent getHelper() {
        return helper;
    }

    public void setHelper(Agent helper) {
        this.helper = helper;
    }

    public int[] getTargetStateIds() {
        return targetStateIds;
    }

    public boolean hasObservation() {
        return trust.getObservations().size() > 0;
    }
}
