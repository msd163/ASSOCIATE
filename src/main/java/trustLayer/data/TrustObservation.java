package trustLayer.data;

import stateLayer.StateX;
import systemLayer.Agent;

public class TrustObservation extends TrustData {


    public TrustObservation(Agent responder) {
        super(responder);
    }


    //============================//============================//============================

    public void addObservation(Agent issuer,Agent requester, StateX source, StateX destination, float reward) {
        super.addItem(issuer, requester, source, destination, reward);
    }

    //============================//============================//============================


}
