package trustLayer;

import systemLayer.Agent;
import utils.Globals;

import java.util.ArrayList;

public class TrustHistory {

    private Agent agent;
    private int lastVisitTime;
    private int lastEpisode;
    private float finalTrustLevel;
    private ArrayList<TrustHistoryItem> items;
    private int finalTrustLevelUpdateTime;
    //============================//============================//============================


    public TrustHistory(Agent agent) {
        this.agent = agent;
        lastVisitTime = Globals.WORLD_TIMER;
        lastEpisode = Globals.EPISODE;
        finalTrustLevel = 0;
        items = new ArrayList<>();
    }

    public void addHistory(float trustScore) {
        lastVisitTime = Globals.WORLD_TIMER;
        lastEpisode = Globals.EPISODE;
        changeFinalTrustLeve(trustScore);
        items.add(new TrustHistoryItem(
                Globals.WORLD_TIMER,
                trustScore
        ));
    }

    public void changeFinalTrustLeve(double trustScore) {
        finalTrustLevel += trustScore;
        if (finalTrustLevel > 1) {
            finalTrustLevel = 1;
        } else if (finalTrustLevel < -1) {
            finalTrustLevel = -1;
        }
    }

    //============================//============================//============================


    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public int getLastVisitTime() {
        return lastVisitTime;
    }

    public float getFinalTrustLevel() {
        if (agent.getWorld().getSimulationConfig().isUseTrustForgottenCoeff()) {
            //-- For preventing stack over flow problem and unlimited loop
            if (finalTrustLevelUpdateTime == Globals.WORLD_TIMER) {
                return finalTrustLevel;
            }
            finalTrustLevelUpdateTime = Globals.WORLD_TIMER;
            finalTrustLevel = 0;
            for (TrustHistoryItem item : items) {
                changeFinalTrustLeve(item.getTrustScore() * Math.pow(agent.getWorld().getSimulationConfig().getTrustForgottenCoeff(), Globals.WORLD_TIMER - item.getVisitTime()));
            }
            //-- tuning final trust level
            if (finalTrustLevel < agent.getWorld().getSimulationConfig().getIgnoringThresholdOfTrustLevelValue() && finalTrustLevel > -agent.getWorld().getSimulationConfig().getIgnoringThresholdOfTrustLevelValue()) {
                finalTrustLevel = 0;
            }
            return finalTrustLevel;
        }
        return finalTrustLevel;
    }

    public ArrayList<TrustHistoryItem> getItems() {
        return items;
    }

    public int getLastEpisode() {
        return lastEpisode;
    }
}
