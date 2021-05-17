package systemLayer;

import stateLayer.StateX;

import java.util.ArrayList;

public class WatchedAgent {

    private Agent agent;
    private ArrayList<StateX> path;

    //============================//============================

    public int getPathSize(){
        return path==null? 0 : path.size();
    }

    //============================//============================

    public WatchedAgent() {
        path = new ArrayList<>();
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public ArrayList<StateX> getPath() {
        return path;
    }

    public void setPath(ArrayList<StateX> path) {
        this.path = path;
    }

    public void addPath(StateX state) {
        path.add(state);
    }
}
