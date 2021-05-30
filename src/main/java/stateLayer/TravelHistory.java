package stateLayer;

import systemLayer.Agent;
import utils.Globals;

import java.util.ArrayList;

public class TravelHistory {

    private StateX stateX;
    private int visitTime;
    private ArrayList<StateX> targets;
    private Agent helper;
    private boolean isTarget;
    private int targetIndex;    // in target array
    private boolean isPitfall;
    private boolean isTrustCalculated;
    //============================//============================//============================


    public TravelHistory(StateX stateX, int visitTime, ArrayList<StateX> targets, Agent helper, boolean isTarget, boolean isPitfall,int targetIndex) {
        this.stateX = stateX;
        this.visitTime = visitTime;
        this.targets = targets;
        this.helper = helper;
        this.isTarget = isTarget;
        this.isPitfall = isPitfall;
        this.targetIndex = targetIndex;
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

    public boolean isIsTarget() {
        return isTarget;
    }

    public void setIsTarget(boolean target) {
        isTarget = target;
    }

    public Agent getHelper() {
        return helper;
    }

    public void setHelper(Agent helper) {
        this.helper = helper;
    }

    public void setIsTrustCalculated(boolean trustCalculated) {
        isTrustCalculated = trustCalculated;
    }

    public boolean isTrustCalculated() {
        return isTrustCalculated;
    }
}
