package WSM.trust.data;

import WSM.society.agent.Agent;

public class TrustRecommendation extends TrustData {


    public TrustRecommendation(Agent receiver, Agent responder) {
        super(receiver, responder, receiver.getTrust().getRecommendationItemCap());
    }


    //============================//============================//============================

    public void addRecommendation(Agent recommender, float trustValue) {
        super.addDirectItem(recommender, null, null,null, trustValue);
    }

    //============================//============================//============================

}
