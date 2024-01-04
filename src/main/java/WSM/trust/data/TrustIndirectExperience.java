package WSM.trust.data;

import WSM.society.agent.Agent;

public class TrustIndirectExperience extends TrustData {


    public TrustIndirectExperience(Agent receiver, Agent responder) {
        super(receiver, responder, receiver.getTrust().getIndirectExperienceItemCap());
    }


    //============================//============================//============================

    public void addExperience(TrustDataItem item, Agent issuer) {
        if (lastTime < item.getTime()) {
            lastTime = item.getTime();
        }

        super.addItem(item, issuer);
    }


    //============================//============================//============================


}
