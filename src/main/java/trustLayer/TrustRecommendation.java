package trustLayer;

import systemLayer.Agent;
import utils.Config;
import utils.Globals;

import java.util.ArrayList;

public class TrustRecommendation {

    private Agent master;
    private Agent trustee;   // trustee agent that others recommend it's trust level
    private int lastRecommendTime;
    private int lastEpisode;
    private float finalTrustLevel;
    private int finalTrustLevelUpdateTime;
    private ArrayList<TrustRecommendationItem> items;

    //============================//============================//============================

    public TrustRecommendation(Agent master, Agent trustee, TrustRecommendationItem item) {
        this.master = master;
        this.trustee = trustee;
        this.lastEpisode = Globals.EPISODE;
        this.lastRecommendTime = Globals.WORLD_TIMER;
        items = new ArrayList<>();
        items.add(item);
    }


    //============================//============================//============================


    public Agent getTrustee() {
        return trustee;
    }

    public void setTrustee(Agent trustee) {
        this.trustee = trustee;
    }

    public int getLastRecommendTime() {
        return lastRecommendTime;
    }

    public void setLastRecommendTime(int lastRecommendTime) {
        this.lastRecommendTime = lastRecommendTime;
    }

    public int getLastEpisode() {
        return lastEpisode;
    }

    public void setLastEpisode(int lastEpisode) {
        this.lastEpisode = lastEpisode;
    }

    public float getFinalRecommendedTrustLevel() {
        if (Config.TRUST_USE_FORGOTTEN_COEFF) {
            //-- For preventing stack over flow problem and unlimited loop
            if (finalTrustLevelUpdateTime == Globals.WORLD_TIMER) {
                return finalTrustLevel;
            }
            finalTrustLevelUpdateTime = Globals.WORLD_TIMER;
            finalTrustLevel = 0;
            for (TrustRecommendationItem item : items) {
                float recommenderTrustLevel = Globals.trustManager.getTrustLevel(master, item.getRecommender());
                //-- If recommender identified as an honest agent...
                if (recommenderTrustLevel > 0) {
                    finalTrustLevel += item.getRecommendedTrustLevel() * Math.pow(Config.TRUST_FORGOTTEN_COEFF, Globals.WORLD_TIMER - item.getRecommendTime());
                }
            }
            return finalTrustLevel;
        }
        return finalTrustLevel;
    }

    public void setFinalTrustLevel(float finalTrustLevel) {
        this.finalTrustLevel = finalTrustLevel;
    }

    public ArrayList<TrustRecommendationItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<TrustRecommendationItem> items) {
        this.items = items;
    }

}
