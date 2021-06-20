package trustLayer;

import systemLayer.Agent;

public class TrustObservation {

    private Agent requester;

    private Agent responder;

    private boolean isFinalPitfall;

    private boolean isFinalTarget;

    private float trustScore;

    //============================//============================

    public TrustObservation(Agent requester, Agent responder, boolean isFinalTarget) {
        this.requester = requester;
        this.responder = responder;
        this.isFinalPitfall = !isFinalTarget;
        this.isFinalTarget = isFinalTarget;
        //todo: can be done intelligently with dynamic and adaptive value instead of 1:-1
        this.trustScore = isFinalTarget ? 1 : -1;
    }


    //============================//============================

    public Agent getRequester() {
        return requester;
    }

    public void setRequester(Agent requester) {
        this.requester = requester;
    }

    public Agent getResponder() {
        return responder;
    }

    public void setResponder(Agent responder) {
        this.responder = responder;
    }

    public boolean isIsFinalPitfall() {
        return isFinalPitfall;
    }

    public void setIsFinalPitfall(boolean finalPitfall) {
        isFinalPitfall = finalPitfall;
    }

    public float getTrustScore() {
        return trustScore;
    }

    public void setTrustScore(float trustScore) {
        this.trustScore = trustScore;
    }

    public boolean isIsFinalTarget() {
        return isFinalTarget;
    }

    public void setIsFinalTarget(boolean finalTarget) {
        isFinalTarget = finalTarget;
    }
}
