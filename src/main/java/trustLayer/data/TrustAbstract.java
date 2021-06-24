package trustLayer.data;

import systemLayer.Agent;
import utils.Globals;

public class TrustAbstract {

    public TrustAbstract(Agent responder) {
        this.responder = responder;
    }

    public void update(float trustValue) {
        this.trustValue = trustValue;
        updateTime = Globals.WORLD_TIMER;
    }

    private Agent responder;
    private float trustValue;            // final trust value
    private int updateTime;    // last update time of trust value


    public Agent getResponder() {
        return responder;
    }

    public float getTrustValue() {
        return trustValue;
    }

    public int getUpdateTime() {
        return updateTime;
    }
}
