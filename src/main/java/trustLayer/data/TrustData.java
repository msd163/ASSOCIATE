package trustLayer.data;

import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import environmentLayer.StateX;
import systemLayer.Agent;
import utils.Globals;
import utils.OutLog____;

import java.util.ArrayList;

public class TrustData {

    protected Agent owner;
    protected Agent responder;
    protected int lastTime;
    protected int lastEpisode;
    protected int itemCap;
    protected ArrayList<TrustDataItem> items;
    // protected float finalReward;
    // protected int finalRewardUpdateTime;

    protected int positiveRewards;
    protected int negativeRewards;

    private boolean isUpdated;
    //============================//============================//============================

    protected TrustData(Agent owner, Agent responder, int itemCap) {
        this.owner = owner;
        this.responder = responder;
        this.itemCap = itemCap;
        lastTime = Globals.WORLD_TIMER;
        lastEpisode = Globals.EPISODE;
        //  finalReward = 0;
        items = new ArrayList<>();
        positiveRewards = negativeRewards = 0;
        isUpdated = true;
        //  finalRewardUpdateTime = Globals.WORLD_TIMER;
    }

    protected void addItem(Agent issuer, Agent requester, StateX source, StateX destination, float reward) {
        if (itemCap <= 0) {
            return;
        }

        purgeItems();

        lastTime = Globals.WORLD_TIMER;
        lastEpisode = Globals.EPISODE;

        if (reward >= 0) {
            positiveRewards++;
        } else {
            negativeRewards++;
        }

        items.add(new TrustDataItem(
                issuer,
                requester,
                source,
                destination,
                Globals.WORLD_TIMER,
                reward
        ));

        isUpdated = true;
    }

    private void purgeItems() {
        if (items.size() >= itemCap) {
            switch (owner.getTrust().getTrustReplaceMethod()) {
                case Sequential_Circular:
                    removeItem(0);
                    break;

                case RemoveLastUpdated:
                    int historyIndex;
                    TrustDataItem oldHistory = items.get(0);
                    historyIndex = 0;
                    for (int k = 1, historiesLength = items.size(); k < historiesLength; k++) {
                        TrustDataItem tExp = items.get(k);

                        if (oldHistory.getTime() > tExp.getTime()) {
                            historyIndex = k;
                            oldHistory = tExp;
                        }
                    }
                    removeItem(historyIndex);
                    break;
            }
        }
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
        if (itemCap <= 0) {
            return;
        }
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

        purgeItems();

        lastTime = Globals.WORLD_TIMER;
        lastEpisode = Globals.EPISODE;

        if (item.getReward() >= 0) {
            positiveRewards++;
        } else {
            negativeRewards++;
        }
        
        items.add(new TrustDataItem(
                item.getIssuer(),
                item.getRequester(),
                item.getSource(),
                item.getDestination(),
                Globals.WORLD_TIMER,
                item.getReward()
        ));

        isUpdated = true;
    }

    public void removeItem(int index) {
        TrustDataItem remove = items.remove(index);
        if (remove.getReward() >= 0) {
            positiveRewards--;
        } else {
            negativeRewards--;
        }

        isUpdated = false;
    }

    public int getAbstractReward() {
        return Integer.compare(positiveRewards, negativeRewards);
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

    public int getPositiveRewards() {
        return positiveRewards;
    }

    public int getNegativeRewards() {
        return negativeRewards;
    }

    public int getItemCap() {
        return itemCap;
    }

    public void setIsUpdated(boolean updated) {
        isUpdated = updated;
    }

    public boolean isIsUpdated() {
        return isUpdated;
    }
}
