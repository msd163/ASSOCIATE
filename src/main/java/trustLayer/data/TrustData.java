package trustLayer.data;

import stateLayer.StateX;
import systemLayer.Agent;
import utils.Globals;

import java.util.ArrayList;

public class TrustData {

    protected Agent responder;
    protected int lastTime;
    protected int lastEpisode;
    protected ArrayList<TrustDataItem> items;
   // protected float finalReward;
   // protected int finalRewardUpdateTime;
    //============================//============================//============================


    public TrustData() {
    }

    public TrustData(Agent responder) {
        this.responder = responder;
        lastTime = Globals.WORLD_TIMER;
        lastEpisode = Globals.EPISODE;
      //  finalReward = 0;
        items = new ArrayList<>();
      //  finalRewardUpdateTime = Globals.WORLD_TIMER;
    }

    protected void addItem(Agent issuer, Agent requester, StateX source, StateX destination, float reward) {
        lastTime = Globals.WORLD_TIMER;
        lastEpisode = Globals.EPISODE;
        items.add(new TrustDataItem(
                issuer,
                requester,
                source,
                destination,
                Globals.WORLD_TIMER,
                reward
        ));
    }


    protected void addItem(TrustDataItem item) {
        lastTime = Globals.WORLD_TIMER;
        lastEpisode = Globals.EPISODE;
        items.add(new TrustDataItem(
                item.getIssuer(),
                item.getRequester(),
                item.getSource(),
                item.getDestination(),
                Globals.WORLD_TIMER,
                item.getReward()
        ));
    }


    //============================//============================//============================


    public Agent getResponder() {
        return responder;
    }

    public void setResponder(Agent responder) {
        this.responder = responder;
    }

    public int getLastTime() {
        return lastTime;
    }

    public int getLastEpisode() {
        return lastEpisode;
    }


    public ArrayList<TrustDataItem> getItems() {
        return items;
    }
}
