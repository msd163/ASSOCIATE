package stateTransition;

import system.Agent;
import utils.Globals;
import utils.Point;

import java.util.ArrayList;

public class StateX {

    private int id;
    private ArrayList<Integer> targetIds;
    private ArrayList<StateX> targets;
    private ArrayList<Agent> agents;
    private int nodeTrafficCapacity;

    //============================ Location in Drawing
    private boolean hasLoc;
    private Point location;

    //============================//============================

    public StateX() {
        hasLoc = false;
        agents = new ArrayList<Agent>();
        targetIds = new ArrayList<Integer>();
        targets = new ArrayList<StateX>();
        location = new Point(0, 0);
    }

    //============================//============================

    public boolean I_am_in(Agent agent) {
        if (getTraffic() < nodeTrafficCapacity) {
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

    public int getNodeTrafficCapacity() {
        return nodeTrafficCapacity;
    }

    public void setNodeTrafficCapacity(int nodeTrafficCapacity) {
        this.nodeTrafficCapacity = nodeTrafficCapacity;
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

    public ArrayList<StateX> getWatchList(int depth) {

        if (targets == null || targets.isEmpty()) {
            return null;
        }
        ArrayList<StateX> to = null;

        if (depth >= 1) {
            to = new ArrayList<StateX>();
            for (int i = 0; i < targets.size(); i++) {
                to.add(targets.get(i));
                ArrayList<StateX> watchList = getWatchList(depth - 1);
                if (watchList != null) {
                    to.addAll(watchList);
                }
            }
        }
        return to;


    }

    public ArrayList<StateX> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<StateX> targets) {
        this.targets = targets;
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
                ti + ", nodeTrafficCapacity=" + nodeTrafficCapacity +
                ti + ", hasLoc=" + hasLoc +
                ti + ", location=" + location.toString(tabIndex) +
                tx + '}';
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }
}
