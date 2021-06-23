package trustLayer;

import simulateLayer.SimulationConfigItem;
import systemLayer.Agent;
import utils.Globals;

import java.util.ArrayList;
import java.util.List;

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
        SimulationConfigItem simulationConfigItem = master.getWorld().getSimulationConfig();
        //if (simulationConfigItem.isUseTrustForgottenCoeff()) {
        //-- For preventing stack over flow problem and unlimited loop
        if (finalTrustLevelUpdateTime == Globals.WORLD_TIMER) {
            return finalTrustLevel;
        }
        finalTrustLevelUpdateTime = Globals.WORLD_TIMER;
        finalTrustLevel = 0;

        List<Float> itemsWithForgottenFactorEffect = new ArrayList<>();

        //-- Adding forgotten factor effect to trust items
        for (TrustRecommendationItem item : items) {
            float recommenderTrustLevel = master.getWorld().getTrustManager().getTrustValue(master, item.getRecommender());
            //-- If recommender identified as an honest agent...
            if (recommenderTrustLevel > 0) {
                itemsWithForgottenFactorEffect.add(
                        recommenderTrustLevel
                                * item.getRecommendedTrustLevel()
                                * (float) Math.pow(1 - simulationConfigItem.getTrustForgottenCoeff(), Globals.WORLD_TIMER - item.getRecommendTime()));
                //changeFinalTrustLeve(item.getTrustScore() * Math.pow(agent.getWorld().getSimulationConfig().getTrustForgottenCoeff(), Globals.WORLD_TIMER - item.getVisitTime()));
            }
        }

        //-- Sorting items according to abstraction |value| of trusts
        itemsWithForgottenFactorEffect.sort((Float f1, Float f2) -> {
                    if (Math.abs(f1) > Math.abs(f2)) {
                        return -1;
                    }
                    if (Math.abs(f1) < Math.abs(f2)) {
                        return 1;
                    }
                    return 0;
                }
        );

        //-- Calculating trust level value according to Basel Series
        int index = 0;
        for (int i = 0, tsSize = itemsWithForgottenFactorEffect.size(); i < tsSize; i++) {
            Float t = itemsWithForgottenFactorEffect.get(i);
            finalTrustLevel += (t / ((index + 2) * (index + 2)));
            index++;
        }

/*        for (TrustRecommendationItem item : items) {
            float recommenderTrustLevel = master.getWorld().getTrustManager().getTrustLevel(master, item.getRecommender(),true);
            //-- If recommender identified as an honest agent...
            if (recommenderTrustLevel > 0) {
                //todo: has a bug! item.getRecommendedTrustLevel() has to be a proportion of a whole
                finalTrustLevel += recommenderTrustLevel *
                        item.getRecommendedTrustLevel()
                        * Math.pow(1-simulationConfigItem.getTrustForgottenCoeff(), Globals.WORLD_TIMER - item.getRecommendTime());
            }
        }*/
        return finalTrustLevel;
        //}
        //return finalTrustLevel;
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
