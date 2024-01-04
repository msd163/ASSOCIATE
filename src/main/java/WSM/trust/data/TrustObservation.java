package WSM.trust.data;

import WSM.society.environment.StateX;
import WSM.society.agent.Agent;

public class TrustObservation extends TrustData {

    public TrustObservation(Agent observer, Agent responder) {
        super(observer, responder, observer.getTrust().getObservationItemCap());
    }
    //============================//============================//============================

    public void addObservation(Agent issuer, Agent requester, StateX source, StateX destination, float reward) {
        super.addDirectItem(issuer, requester, source, destination, reward);
    }

    //============================//============================//============================

}
