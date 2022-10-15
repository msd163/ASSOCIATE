package trustLayer.data;

import societyLayer.environmentSubLayer.StateX;
import societyLayer.agentSubLayer.Agent;

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
