package trustLayer;

import systemLayer.Agent;
import utils.Globals;

public class TrustRecommendationItem {

    private Agent recommender;
    private int recommendTime;
    private float recommendedTrustLevel;

    //============================//============================//============================

    public TrustRecommendationItem(Agent recommender, float recommendedTrustLevel) {
        this.recommender = recommender;
        this.recommendTime = Globals.WORLD_TIMER;
        this.recommendedTrustLevel = recommendedTrustLevel;
    }


    //============================//============================//============================

    public int getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(int recommendTime) {
        this.recommendTime = recommendTime;
    }

    public float getRecommendedTrustLevel() {
        return recommendedTrustLevel;
    }

    public Agent getRecommender() {
        return recommender;
    }

    public void setRecommendedTrustLevel(float recommendedTrustLevel) {
        this.recommendedTrustLevel = recommendedTrustLevel;
    }
}
