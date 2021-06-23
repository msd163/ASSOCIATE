package trustLayer.data;

import stateLayer.StateX;
import systemLayer.Agent;

public class TrustIndirectObservation extends TrustData {


    public TrustIndirectObservation(Agent responder) {
        super(responder);
    }


    //============================//============================//============================

    public void addObservation(TrustDataItem item) {
        if(lastTime< item.getTime()){
            lastTime = item.getTime();
        }

        super.addItem(item);
    }
    //============================//============================//============================


}
