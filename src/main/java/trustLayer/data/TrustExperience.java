package trustLayer.data;

import stateLayer.StateX;
import systemLayer.Agent;

public class TrustExperience extends TrustData {


    public TrustExperience(Agent responder) {
        super(responder);
    }


    //============================//============================//============================

    public void addExperience(Agent requester, StateX source, StateX destination, float reward) {
        super.addItem(requester, requester, source, destination, reward);
    }

    //============================//============================//============================


}
