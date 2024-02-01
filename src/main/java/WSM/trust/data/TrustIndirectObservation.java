package WSM.trust.data;

import WSM.society.agent.Agent;

public class TrustIndirectObservation extends TrustData {


    public TrustIndirectObservation(Agent receiver, Agent responder) {
        super(receiver, responder, receiver.getTrust().getIndirectObservationItemCap());
    }
    //============================//============================//============================

    public void addObservation(TrustDataItem item, Agent issuer) {
        if (lastTime < item.getTime()) {
            lastTime = item.getTime();
        }

        super.addItem(item, issuer);
    }
    //============================//============================//============================


}
