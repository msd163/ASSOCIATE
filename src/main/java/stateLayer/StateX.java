package stateLayer;

import com.google.gson.annotations.Expose;
import systemLayer.Agent;
import systemLayer.WatchedAgent;
import systemLayer.WatchedState;
import utils.Config;
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
    @Expose
    private boolean isPitfall;              // pitfalls have no targets.

    private ArrayList<TransitionX> sourceTrans;
    private ArrayList<TransitionX> targetTrans;

    //============================ Location in Drawing
    private boolean hasLoc;
    private Point location;

    //============================//============================

    public StateX() {
//        this.environment = environment;
        hasLoc = false;
        isPitfall = false;
        agents = new ArrayList<Agent>();
        targetIds = new ArrayList<Integer>();
        targets = new ArrayList<StateX>();
        targetTrans = new ArrayList<TransitionX>();
        sourceTrans = new ArrayList<TransitionX>();
        location = new Point(0, 0);

    }

    //============================//============================

    public int getTraffic() {
        return agents.size();
    }

    /**
     * Updating the target array according targetIds.
     *
     * @param environment
     */
    public void updateTargets(Environment environment) {
        ArrayList<StateX> stateTrans = environment.getStates();
        targets = new ArrayList<>();

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
                (index % bn) * Config.STATE_TILE_WIDTH + location.x - (getWidth() / 2) + (Config.STATE_TILE_WIDTH / 4),
                (index / bn) * Config.STATE_TILE_WIDTH + location.y - (getWidth() / 2) + (Config.STATE_TILE_WIDTH / 4)
        );
    }

    public int getWidth() {
        return getBigness() * Config.STATE_TILE_WIDTH;
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
     * @return
     */
    public void getWatchListOfAgents(List<WatchedAgent> watchedAgents,
                                     List<WatchedState> watchedStates,
                                     int depth,
                                     int maxDepth,
                                     int remainedAgentCount,
                                     Agent sourceAgent,
                                     ArrayList<StateX> parentPath) {

        if (depth < 0 /*|| count < 1*/) {
            return;
        }

        //============================  adding current stated to visited states list
        WatchedState ws = new WatchedState();
        ws.setStateX(this);
        for (StateX stateX : parentPath) {
            ws.addPath(stateX);
        }
        if (depth != maxDepth) {
            ws.addPath(this);
        }
        watchedStates.add(ws);

        //============================  For all agents in this state..., Adding agents of current state to watchedAgents list
        // - If the watchedAgents is filled,adding agents will ignored.
        if (remainedAgentCount > 0) {
            for (Agent agent : agents) {
                //-- If the watch list of the source agent is full...
                if (remainedAgentCount < 1) {
                    break;
                }

                //-- preventing visiting itself
                if (agent.getId() == sourceAgent.getId()) {
                    continue;
                }

                WatchedAgent watchedAgent = new WatchedAgent();
                watchedAgent.setAgent(agent);
                // watchedAgent.setTrust(sourceAgent.getTrust().getTrustScore(agent));

                //-- Adding path from the source state (state of the source agent) to this state (The state of visited agent)
                for (StateX stateX : ws.getPath()) {
                    watchedAgent.addPath(stateX);
                }

                remainedAgentCount--;
                watchedAgents.add(watchedAgent);
            }
        }

        //============================ Navigating to depth

        if (targets == null || targets.isEmpty()) {
            return;
        }

        if (depth > 0) {
            boolean isStateVisited;
            ArrayList<StateX> localParentPath = new ArrayList<>(parentPath);
            if (depth != maxDepth) {
                localParentPath.add(this);
            }

            for (int i = 0; i < targets.size(); i++) {

                //-- Ignoring previously visited states.
                isStateVisited = false;
                for (WatchedState vs : watchedStates) {
                    if (targets.get(i).getId() == vs.getStateX().getId()) {
                        isStateVisited = true;
                        break;
                    }
                }
                if (isStateVisited) {
                    continue;
                }

                //-- Traveling in depth
                targets.get(i).getWatchListOfAgents(
                        watchedAgents,
                        watchedStates,
                        depth - 1, maxDepth, remainedAgentCount, sourceAgent, localParentPath);
            }
        }

    }

    public void getWatchListOfStates(int depth, List<WatchedState> watchedStates, WatchedState currentWatchState) {

        if (depth <= 0) {
            return;
        }

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
                for (StateX stateX : currentWatchState.getPath()) {
                    ws.addPath(stateX);
                }
            }
            ws.addPath(target);
            watchedStates.add(ws);

        }

        if (depth > 1) {
            for (StateX target : targets) {
                //                isAdded = false;
                WatchedState currentWs = null;
                for (WatchedState watchedState : watchedStates) {
                    if (watchedState.getStateX().getId() == target.getId()) {
                        //                        isAdded = true;
                        currentWs = watchedState;
                        break;
                    }
                }
           /*   if (isAdded) {
                continue;
            }*/
                target.getWatchListOfStates(depth - 1, watchedStates, currentWs);
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

    public boolean isIsPitfall() {
        return isPitfall;
    }

    public void setIsPitfall(boolean pitfall) {
        isPitfall = pitfall;
    }
}
