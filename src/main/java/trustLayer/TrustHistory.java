package trustLayer;

import systemLayer.Agent;
import utils.Config;
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
        finalTrustLevel += trustScore;
        items.add(new TrustHistoryItem(
                Globals.WORLD_TIMER,
                trustScore
        ));
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

    public void setLastVisitTime(int lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
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
                finalTrustLevel += item.getTrustScore() * Math.pow(agent.getWorld().getSimulationConfig().getTrustForgottenCoeff(), Globals.WORLD_TIMER - item.getVisitTime());
            }
            return finalTrustLevel;
        }
        return finalTrustLevel;
    }

    public void setFinalTrustLevel(float finalTrustLevel) {
        this.finalTrustLevel = finalTrustLevel;
    }

    public ArrayList<TrustHistoryItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<TrustHistoryItem> items) {
        this.items = items;
    }

    public int getLastEpisode() {
        return lastEpisode;
    }
}
