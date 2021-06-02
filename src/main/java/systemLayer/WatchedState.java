package systemLayer;

import stateLayer.StateX;

import java.util.ArrayList;

public class WatchedState {

    private StateX stateX;
    private ArrayList<StateX> path;

    //============================//============================

    public int getPathSize() {
        return path == null ? 0 : path.size();
    }

    //============================//============================

    public WatchedState() {
        path = new ArrayList<>();
    }

    public StateX getStateX() {
        return stateX;
    }

    public void setStateX(StateX stateX) {
        this.stateX = stateX;
    }

    public ArrayList<StateX> getPath() {
        return path;
    }

   /* public void setPath(ArrayList<StateX> path) {
        this.path = path;
    }*/

    public void addPath(StateX state) {
        path.add(state);
    }


    public void addPathInFirst(StateX stateX) {
        path.add(0, stateX);
    }
}
