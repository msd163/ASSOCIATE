package stateTransition;

import com.sun.javafx.geom.Point2D;
import system.Agent;

import java.util.ArrayList;

public class StateTrans {

    private int id;
    private ArrayList<Integer> targetIds;
    private ArrayList<StateTrans> targets;
    private ArrayList<Agent> agents;
    private int nodeTrafficCapacity;

    private boolean hasLoc;
    private Point2D location;

    //============================//============================

    public StateTrans() {
        hasLoc = false;
        agents = new ArrayList<Agent>();
        targetIds = new ArrayList<Integer>();
        targets = new ArrayList<StateTrans>();
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
     * @param environment
     */
    public void updateTargets(Environment environment) {
        StateTrans[] stateTrans = environment.getStateTrans();
        targets = new ArrayList<StateTrans>();

        if (targetIds != null && stateTrans != null) {
            boolean isAdded;
            boolean isFound;
            for (Integer ti : targetIds) {
                isFound = false;
                for (StateTrans st : stateTrans) {
                    if (st.getId() == ti) {
                        isAdded = false;
                        for (StateTrans ta : targets) {
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

    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public ArrayList<StateTrans> getWatchList(int depth) {

        if (targets == null || targets.isEmpty()) {
            return null;
        }
        ArrayList<StateTrans> to = null;

        if (depth >= 1) {
            to = new ArrayList<StateTrans>();
            for (int i = 0; i < targets.size(); i++) {
                to.add(targets.get(i));
                ArrayList<StateTrans> watchList = getWatchList(depth - 1);
                if (watchList != null) {
                    to.addAll(watchList);
                }
            }
        }
        return to;


    }

    public ArrayList<StateTrans> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<StateTrans> targets) {
        this.targets = targets;
    }
}
