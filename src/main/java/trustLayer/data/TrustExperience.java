package trustLayer.data;

import societyLayer.environmentSubLayer.StateX;
import societyLayer.agentSubLayer.Agent;

public class TrustExperience extends TrustData {


    public TrustExperience(Agent requester, Agent responder) {
        super(requester, responder, requester.getTrust().getExperienceItemCap());
    }

    //============================//============================//============================

    public void addExperience(Agent requester, StateX source, StateX destination, float reward) {
        super.addDirectItem(requester, requester, source, destination, reward);
    }

    //============================//============================//============================


}
