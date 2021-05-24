package stateLayer;

import utils.Globals;

import java.util.ArrayList;

public class TravelHistory {

    private StateX stateX;
    private int visitTime;
    private ArrayList<StateX> targets;

    //============================//============================//============================


    public TravelHistory(StateX stateX, int visitTime, ArrayList<StateX> targets) {
        this.stateX = stateX;
        this.visitTime = visitTime;
        this.targets = targets;
    }

    public int isAnyPathTo(StateX targetState) {
        if (stateX.getId() == targetState.getId()) {
            return 0;
        }
        for (StateX target : targets) {
            if (target.getId() == targetState.getId()) {
                return 1;
            }
        }
        return -1;
    }

    public boolean hasPathTo(TravelHistory travelHistory) {
        for (StateX target : targets) {
            if (target.getId() == travelHistory.stateX.getId()) {
                return true;
            }
        }
        return false;
    }

    //============================//============================//============================


    public int getVisitTime() {
        return visitTime;
    }

    public StateX getStateX() {
        return stateX;
    }

    public void updateVisitTime() {
        visitTime = Globals.WORLD_TIMER;
    }

}
