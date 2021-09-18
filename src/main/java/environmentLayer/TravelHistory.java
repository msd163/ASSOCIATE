package environmentLayer;

import systemLayer.Agent;
import utils.Globals;

import java.util.ArrayList;

public class TravelHistory {

    private StateX stateX;
    private int visitTime;
    private ArrayList<StateX> targets;
    private Agent responder;
    private boolean isTarget;
    private int targetIndex;    // in target array
    private boolean isPitfall;
    private boolean isTrustCalculated;
    //============================//============================//============================


    public TravelHistory(StateX stateX, int visitTime, ArrayList<StateX> targets, Agent responder, boolean isTarget, boolean isPitfall, int targetIndex) {
        this.stateX = stateX;
        this.visitTime = visitTime;
        this.targets = targets;
        this.responder = responder;
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

    public StateX getPitfallIfExist() {
        if (stateX.isIsPitfall()) {
            return stateX;
        }
        if (targets != null) {
            for (StateX target : targets) {
                if (target.isIsPitfall()) {
                    return target;
                }
            }
        }
        return null;
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

    public Agent getResponder() {
        return responder;
    }

    public void setResponder(Agent responder) {
        this.responder = responder;
    }

    public void setIsTrustCalculated(boolean trustCalculated) {
        isTrustCalculated = trustCalculated;
    }

    public boolean isTrustCalculated() {
        return isTrustCalculated;
    }

    public boolean isIsPitfall() {
        return isPitfall;
    }
}
