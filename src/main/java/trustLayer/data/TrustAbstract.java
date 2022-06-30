package trustLayer.data;

import societyLayer.agentSubLayer.Agent;
import utils.Globals;

public class TrustAbstract {

    public TrustAbstract(Agent responder) {
        this.responder = responder;
    }

    public void update(float trustValue) {
        this.trustValue = trustValue;
        updateTime = Globals.WORLD_TIMER;
    }

    private Agent responder;                // trustee
    private float trustValue;               // final trust value
    private int updateTime;                 // last update time of trust value
    private String recommendedToId = "|";      // The ID of agents that received a recommendation for this trust value

    public Agent getResponder() {
        return responder;
    }

    public float getTrustValue() {
        return trustValue;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    /**
     * If this trust value is sent to specific receiver agent at 'updateTime'
     *
     * @param receiverAgentId
     * @return
     */
    public boolean isSendThisTrustValueToReceiverAgent(int receiverAgentId) {
        // System.out.println("isContains:  " + recommendedToId.contains("|" + recommendedId + "-" + updateTime + "|") + " " + responder.getId() + "  " + recommendedToId);
        return recommendedToId.contains("|" + receiverAgentId + "-" + updateTime + "|");
    }

    /**
     * @param recommendedId
     */
    public void addInfoOfSentValueToReceiver(int recommendedId) {

        if (recommendedToId.length() > 2) {
            try {
                recommendedToId = recommendedToId.replaceAll("\\d*-(?:(?!" + updateTime + ").)*\\|", "");
            } catch (Exception e) {
                recommendedToId = "";
            }
        }
        recommendedToId += (recommendedId + "-" + updateTime + "|");
        //System.out.println("addRecommended:  " + responder.getId() + "  " + recommendedToId);

    }


}
