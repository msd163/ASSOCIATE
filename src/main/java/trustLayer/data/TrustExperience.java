package trustLayer.data;

import stateLayer.StateX;
import systemLayer.Agent;

public class TrustExperience extends TrustData {


    public TrustExperience(Agent requester, Agent responder) {
        super(requester, responder, requester.getTrust().getExperienceItemCap());
    }

    //============================//============================//============================

    public void addExperience(Agent requester, StateX source, StateX destination, float reward) {
        super.addItem(requester, requester, source, destination, reward);
    }

    //============================//============================//============================


}
