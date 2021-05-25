package trustLayer;

import systemLayer.Agent;

public class TrustHistory {

    private Agent agent;
    private int lastVisitTime;
    private float finalTrustLevel;

    //============================//============================//============================


    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public int getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(int lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public float getFinalTrustLevel() {
        return finalTrustLevel;
    }

    public void setFinalTrustLevel(float finalTrustLevel) {
        this.finalTrustLevel = finalTrustLevel;
    }
}
