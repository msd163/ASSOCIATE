package WSM.trust.data;

import WSM.society.environment.StateX;
import WSM.society.agent.Agent;

public class TrustDataItem {

    private Agent issuer;       // The agent that issued this data as indirect experience or indirect observation

    private Agent requester;    // The agent that is requested this service from responder.

    private StateX source;
    private StateX destination;

    private int time;
    private float score;

    //============================//============================//============================

    public TrustDataItem(Agent issuer, Agent requester, StateX source, StateX destination, int time,float score) {
        this.issuer = issuer;
        this.requester = requester;
        this.source = source;
        this.destination = destination;
        this.time = time;
        this.score = score;
    }

    //============================//============================//============================

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Agent getIssuer() {
        return issuer;
    }

    public void setIssuer(Agent issuer) {
        this.issuer = issuer;
    }

    public Agent getRequester() {
        return requester;
    }

    public void setRequester(Agent requester) {
        this.requester = requester;
    }

    public StateX getSource() {
        return source;
    }

    public void setSource(StateX source) {
        this.source = source;
    }

    public StateX getDestination() {
        return destination;
    }

    public void setDestination(StateX destination) {
        this.destination = destination;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
