package trustLayer.data;

import stateLayer.StateX;
import systemLayer.Agent;

public class TrustIndirectExperience extends TrustData {


    public TrustIndirectExperience(Agent responder) {
        super(responder);
    }


    //============================//============================//============================

    public void addExperience(TrustDataItem item) {
        if(lastTime< item.getTime()){
            lastTime = item.getTime();
        }

        super.addItem(item);
    }


    //============================//============================//============================


}
