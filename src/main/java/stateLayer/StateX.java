package stateLayer;

import com.google.gson.annotations.Expose;
import systemLayer.Agent;
import systemLayer.WatchedAgent;
import systemLayer.WatchedState;
import utils.Globals;
import utils.Point;
import utils.RectangleX;

import java.util.ArrayList;
import java.util.List;

public class StateX {

    @Expose
    private int id;
    @Expose
    private ArrayList<Integer> targetIds;
    private ArrayList<StateX> targets;
    @Expose
    private ArrayList<Agent> agents;
    @Expose
    private int capacity;

    private ArrayList<TransitionX> sourceTrans;
    private ArrayList<TransitionX> targetTrans;

    //============================ Location in Drawing
    private boolean hasLoc;
    private Point location;

    //============================//============================

    public StateX() {
//        this.environment = environment;
        hasLoc = false;
        agents = new ArrayList<Agent>();
        targetIds = new ArrayList<Integer>();
        targets = new ArrayList<StateX>();
        targetTrans = new ArrayList<TransitionX>();
        sourceTrans = new ArrayList<TransitionX>();
        location = new Point(0, 0);

    }

    //============================//============================

    public boolean I_am_in(Agent agent) {
        if (getTraffic() < capacity) {
            boolean add = true;
            for (int i = 0; i < agents.size(); i++) {
                if (agents.get(i).getId() == agent.getId()) {
                    add = false;
                }
            }
            if (add)
                agents.add(agent);
            return true;
        }
        return false;

    }

    public void I_am_out(Agent agent) {
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).getId() == agent.getId()) {
                agents.remove(i);
                break;
            }
        }
    }

    public int getTraffic() {
        return agents.size();
    }

    /**
     * Updating the target array according targetIds.
     *
     * @param environment
     */
    public void updateTargets(Environment environment) {
        StateX[] stateTrans = environment.getStates();
        targets = new ArrayList<StateX>();

        if (targetIds != null && stateTrans != null) {
            boolean isAdded;
            boolean isFound;
            for (Integer ti : targetIds) {
                isFound = false;
                for (StateX st : stateTrans) {
                    if (st.getId() == ti) {
                        isAdded = false;
                        for (StateX ta : targets) {
                            if (st.getId() == ta.getId()) {
                                isAdded = true;
                                break;
                            }
                        }
                        if (!isAdded) {
                            targets.add(st);
                        }
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    System.out.println(">> Failed: State with ID '" + ti + "' is not in States.");
                }
            }
        }
    }

    public int getBigness() {
        return (int) (Math.sqrt(agents.size()) + 1);
    }

    public boolean isNeighborState(StateX stateX) {
        if (targets.isEmpty()) {
            return false;
        }

        for (StateX target : targets) {
            if (stateX.getId() == target.getId()) {
                return true;
            }
        }
        return false;
    }

    //============================ Transition

    public void addTargetTrans(TransitionX transition) {
        targetTrans.add(transition);
    }

    public void addSourceTrans(TransitionX transition) {
        sourceTrans.add(transition);
    }

    public TransitionX getSourceTrans(StateX sourceState) {
        for (TransitionX st : sourceTrans) {
            if (sourceState.getId() == st.getFrom().getId()) {
                return st;
            }
        }
        return null;
    }

    public TransitionX getTargetTrans(StateX targetState) {
        for (TransitionX st : targetTrans) {
            if (targetState.getId() == st.getTo().getId()) {
                return st;
            }
        }
        return null;
    }

    //============================ Agents
    public boolean addAgent(Agent agent) {
        if (getTraffic() < capacity) {
            boolean add = true;
            for (int i = 0; i < agents.size(); i++) {
                if (agents.get(i).getId() == agent.getId()) {
                    add = false;
                    break;
                }
            }
            if (add) {
                int bigness = getBigness();

                agents.add(agent);

                // for updating transition paths if size of state box is changed in drawing area
                if (bigness != getBigness()) {
                    for (TransitionX tt : targetTrans) {
                        tt.updatePath();
                    }
                    for (TransitionX st : sourceTrans) {
                        st.updatePath();
                    }
                }

            }
            return true;
        }
        System.out.println(">> StateX.addAgent:: [Warning] No capability to adding agent (" + agent.getId() + ") to state (" + id + ")");
        return false;
    }

    public boolean leave(Agent agent) {
        int bigness = getBigness();
        boolean remove = false;
        if (agents != null && !agents.isEmpty()) {
            remove = agents.remove(agent);
            // for updating transition paths if size of state box is changed in drawing area
            if (bigness != getBigness()) {
                for (TransitionX tt : targetTrans) {
                    tt.updatePath();
                }
                for (TransitionX st : sourceTrans) {
                    st.updatePath();
                }
            }
        }
        return remove;
    }

    //============================ Area
    public Point getTileLocation(int index) {
        int bn = getBigness();
        return new Point(
                (index % bn) * Globals.STATE_TILE_WIDTH + location.x - (getWidth() / 2) + (Globals.STATE_TILE_WIDTH / 4),
                (index / bn) * Globals.STATE_TILE_WIDTH + location.y - (getWidth() / 2) + (Globals.STATE_TILE_WIDTH / 4)
        );
    }

    public int getWidth() {
        return getBigness() * Globals.STATE_TILE_WIDTH;
    }

    public RectangleX getBoundedRectangle() {
        int w = getWidth();
        return new RectangleX(location.x - (w / 2), location.y - (w / 2), w, w);
    }

    //============================//============================

    public boolean isAnyPathTo(StateX goalState) {
        ArrayList<StateX> visitedStates = new ArrayList<>();
        return isAnyPathTo(goalState, visitedStates);
    }

    public boolean isAnyPathTo(StateX goalState, ArrayList<StateX> visitedStates) {

        if (this.getId() == goalState.getId()) {
            return true;
        }

        if (targets == null) {
            return false;
        }

        for (StateX target : targets) {

            if (visitedStates.contains(target)) {
                continue;
            }

            visitedStates.add(target);

            if (target.isAnyPathTo(goalState, visitedStates)) {
                return true;
            }


        }

        return false;

    }
    //============================//============================


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public void setAgents(ArrayList<Agent> agents) {
        this.agents = agents;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isHasLoc() {
        return hasLoc;
    }

    public void setHasLoc(boolean hasLoc) {
        this.hasLoc = hasLoc;
    }

    public boolean hasTarget() {
        return targets.size() > 0;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * Getting Watched agents list with specific depth
     *
     * @param depth
     * @param maxDepth
     * @param count
     * @param sourceAgent
     * @param visitedStates
     * @param parentPath
     * @return
     */
    public ArrayList<WatchedAgent> getWatchListOfAgents(int depth, int maxDepth, int count, Agent sourceAgent, ArrayList<StateX> visitedStates, ArrayList<StateX> parentPath) {

        if (depth < 0 || count < 1) {
            return new ArrayList<>();
        }

        // adding current stated lo visited states list
        visitedStates.add(this);

        ArrayList<WatchedAgent> was = new ArrayList<>();

        // For all agents in this state...
        for (Agent agent : agents) {
            // preventing visiting itself
            if (agent.getId() == sourceAgent.getId()) {
                continue;
            }
            WatchedAgent watchedAgent = new WatchedAgent();
            watchedAgent.setAgent(agent);
            watchedAgent.setTrust(sourceAgent.getTrust().getTrustScore(agent));

            // Adding path from the source state (state of the source agent) to this state (The state of visited agent)
            if (depth < maxDepth) {
                for (int i = 0, len = parentPath.size(); i < len; i++) {
                    watchedAgent.addPath(parentPath.get(i));
                }
                watchedAgent.addPath(this);
            }
            count--;
            was.add(watchedAgent);

            // If the watch list of the source agent is full...
            if (count < 1) {
                return was;
            }
        }

        if (targets == null || targets.isEmpty()) {
            return was;
        }

        if (depth > 0) {
            boolean isStateVisited;

            ArrayList<StateX> localParentPath = new ArrayList<>(parentPath);
            if (depth < maxDepth) {
                localParentPath.add(this);
            }

            for (int i = 0; i < targets.size(); i++) {

                // Ignoring previously visited states.
                isStateVisited = false;
                for (StateX vs : visitedStates) {
                    if (targets.get(i).getId() == vs.getId()) {
                        isStateVisited = true;
                        break;
                    }
                }
                if (isStateVisited) {
                    continue;
                }

                // Traveling in depth
                ArrayList<WatchedAgent> watchList = targets.get(i).getWatchListOfAgents(depth - 1, maxDepth, count, sourceAgent, visitedStates, localParentPath);

                if (watchList != null) {
                    was.addAll(watchList);
                    count -= watchList.size();

                    if (count < 1) {
                        return was;
                    }
                }
            }
        }

        return was;
    }

    public void getWatchListOfStates(int depth, List<WatchedState> watchedStates, WatchedState currentWatchState) {

        if (depth < 0) {
            return;
        }

        if (depth > 0) {

            boolean isAdded;
            // First adding children of current state, BFS navigation
            for (StateX target : targets) {
                isAdded = false;
                for (WatchedState watchedState : watchedStates) {
                    if (watchedState.getStateX().getId() == target.getId()) {
                        isAdded = true;
                        break;
                    }
                }
                if (isAdded) {
                    continue;
                }

                WatchedState ws = new WatchedState();
                ws.setStateX(target);
                if (currentWatchState != null) {
                    ws.setPath(currentWatchState.getPath());
                }
                ws.addPath(target);
                watchedStates.add(ws);

            }

            for (StateX target : targets) {
                isAdded = false;
                for (WatchedState watchedState : watchedStates) {
                    if (watchedState.getStateX().getId() == target.getId()) {
                        isAdded = true;
                        break;
                    }
                }
                if (isAdded) {
                    continue;
                }
                target.getWatchListOfStates(depth - 1, watchedStates, null);
            }
        }

    }

    public ArrayList<StateX> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<StateX> targets) {
        this.targets = targets;
    }

    public ArrayList<Integer> getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(ArrayList<Integer> targetIds) {
        this.targetIds = targetIds;
    }

    //============================//============================

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

        return tx + "StateTrans{" +
                ti + "  id=" + id +
                ti + ", nodeTrafficCapacity=" + capacity +
                ti + ", hasLoc=" + hasLoc +
                ti + ", location=" + location.toString(tabIndex) +
                tx + '}';
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public boolean isFullCapability() {
        return capacity <= agents.size();
    }
}
