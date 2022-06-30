package trustLayer.data;

import societyLayer.agentSubLayer.Agent;

public class TrustIndirectObservation extends TrustData {


    public TrustIndirectObservation(Agent receiver, Agent responder) {
        super(receiver, responder, receiver.getTrust().getIndirectObservationItemCap());
    }
    //============================//============================//============================

    public void addObservation(TrustDataItem item) {
        if (lastTime < item.getTime()) {
            lastTime = item.getTime();
        }

        super.addItem(item);
    }
    //============================//============================//============================


}
