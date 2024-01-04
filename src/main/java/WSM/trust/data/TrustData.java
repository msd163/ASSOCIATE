package WSM.trust.data;

import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import WSM.society.agent.Agent;
import WSM.society.environment.StateX;
import core.utils.Globals;
import core.utils.OutLog____;

public class TrustData {

    protected Agent owner;
    protected Agent responder;
    protected int lastTime;
    protected int lastEpisode;
    protected int itemCap;
    protected int currentIndex;
    protected TrustDataItem[] items;
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
        items = new TrustDataItem[itemCap];
        positiveRewards = negativeRewards = 0;
        isUpdated = true;
        //  finalRewardUpdateTime = Globals.WORLD_TIMER;
        currentIndex = -1;
    }

    protected synchronized void addDirectItem(Agent issuer, Agent requester, StateX source, StateX destination, float reward) {
        if (itemCap <= 0) {
            return;
        }

        currentIndex = getItemIndexToBeSet();

        lastTime = Globals.WORLD_TIMER;
        lastEpisode = Globals.EPISODE;

        if (reward >= 0) {
            positiveRewards++;
        } else {
            negativeRewards++;
        }

        if (items[currentIndex] == null) {

            items[currentIndex] = (new TrustDataItem(
                    issuer,
                    requester,
                    source,
                    destination,
                    Globals.WORLD_TIMER,
                    reward
            ));
        } else {
            if (items[currentIndex].getScore() >= 0) {
                positiveRewards--;
            } else {
                negativeRewards--;
            }

            items[currentIndex].setIssuer(issuer);
            items[currentIndex].setRequester(requester);
            items[currentIndex].setSource(source);
            items[currentIndex].setDestination(destination);
            items[currentIndex].setTime(Globals.WORLD_TIMER);
            items[currentIndex].setScore(reward);
        }


        isUpdated = true;
    }

    private synchronized int getItemIndexToBeSet() {

        switch (owner.getTrust().getTrustReplaceMethod()) {
            case Sequential_Circular:
                return (currentIndex + 1) % itemCap;

            case RemoveLastUpdated:

                if (currentIndex + 1 < itemCap) {
                    return currentIndex + 1;
                } else {
                    TrustDataItem oldHistory = items[0];
                    if (oldHistory == null) {
                        return 0;
                    }
                    int historyIndex = 0;
                    for (int k = 1; k < itemCap; k++) {
                        TrustDataItem tExp = items[k];
                        if (tExp == null) {
                            return k;
                        }
                        int oldTime = oldHistory.getTime();
                        int expTime = tExp.getTime();
                        if (oldTime > expTime) {
                            historyIndex = k;
                            oldHistory = tExp;
                        }
                    }
                    return historyIndex;
                }
        }

        System.out.println("EEE 231: return default [0] index for Item");
        return 0;

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

    protected synchronized void addItem(TrustDataItem item, Agent issuer) {
        if (itemCap <= 0 || item == null) {
            return;
        }
        boolean isFound = false;
        int itemTime = item.getTime();
        for (int i = 0; i < itemCap; i++) {
            TrustDataItem it = items[i];
            if (it == null) {
                break;
            }
            int itTIme = it.getTime();
            if (
                    itTIme == itemTime &&
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
                    + " Time: " + itemTime
            );

            return;
        }


        currentIndex = getItemIndexToBeSet();

        lastTime = Globals.WORLD_TIMER;
        lastEpisode = Globals.EPISODE;

        if (item.getScore() >= 0) {
            positiveRewards++;
        } else {
            negativeRewards++;
        }

        if (items[currentIndex] == null) {

            items[currentIndex] = (new TrustDataItem(
                    issuer,
                    item.getRequester(),
                    item.getSource(),
                    item.getDestination(),
                    Globals.WORLD_TIMER,
                    item.getScore()
            ));
        } else {
            if (items[currentIndex].getScore() >= 0) {
                positiveRewards--;
            } else {
                negativeRewards--;
            }

            items[currentIndex].setIssuer(issuer);
            items[currentIndex].setRequester(item.getRequester());
            items[currentIndex].setSource(item.getSource());
            items[currentIndex].setDestination(item.getDestination());
            items[currentIndex].setTime(Globals.WORLD_TIMER);
            items[currentIndex].setScore(item.getScore());
        }


        isUpdated = true;
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


    public TrustDataItem[] getItems() {
        return items;
    }

    public int getFilledSize() {
        if (items[itemCap - 1] != null) {
            return itemCap;
        }
        for (int i = 0; i < itemCap; i++) {
            if (items[i] == null) {
                return i;
            }
        }
        return 0;
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

    public void destroy() {
        for (int i = 0, itemsLength = items.length; i < itemsLength; i++) {
            items[i] = null;
        }

    }
}
