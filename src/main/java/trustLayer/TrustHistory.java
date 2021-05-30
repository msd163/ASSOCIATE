package trustLayer;

import systemLayer.Agent;
import utils.Globals;

import java.util.ArrayList;

public class TrustHistory {

    private Agent agent;
    private int lastVisitTime;
    private float finalTrustLevel;
    private ArrayList<TrustHistoryItem> items;

    //============================//============================//============================


    public TrustHistory(Agent agent) {
        this.agent = agent;
        lastVisitTime = Globals.WORLD_TIMER;
        finalTrustLevel = 0;
        items = new ArrayList<>();
    }

    public void addHistory(float trustScore) {
        lastVisitTime = Globals.WORLD_TIMER;
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
}
