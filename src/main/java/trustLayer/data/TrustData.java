package trustLayer.data;

import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import stateLayer.StateX;
import systemLayer.Agent;
import utils.Globals;
import utils.OutLog____;

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

    private boolean isAgentEquals(Agent a1, Agent a2) {
        if (
                (a1 == null && a2 == null) ||
                        (a1 != null && a2 != null && a1.getId() == a2.getId())
        ) {
            return true;
        }
        return false;
    }

    protected void addItem(TrustDataItem item) {

        if (items.size() > 0) {
            boolean isFound = false;
            for (TrustDataItem it : items) {
                if (
                        it.getTime() == item.getTime() &&
                                isAgentEquals(it.getIssuer(), item.getIssuer()) &&
                                isAgentEquals(it.getRequester(), item.getRequester())
                ) {
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                OutLog____.pl(TtOutLogMethodSection.TrustData_AddItem, TtOutLogStatus.WARN, "This item added previously"
                        + " Issuer: " + (item.getIssuer() == null ? "-" : item.getIssuer().getId())
                        + " Requester: " + (item.getRequester() == null ? "-" : item.getRequester().getId())
                        + " Time: " + item.getTime()
                );

                return;
            }
        }

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
