package trustLayer.data;

import _type.TtTrustReplaceMethod;
import societyLayer.agentSubLayer.Agent;

public class TrustDataArray {

    private TrustData[] array;
    private Agent owner;
    private int currentIndex;
    private final int length;
    TtTrustReplaceMethod trustReplaceMethod;

    public int size() {
        return array == null ? 0 : length;
    }

    public TrustData get(int i) {
        return array[i];
    }

    public TrustDataArray(int length, TtTrustReplaceMethod trustReplaceMethod, Agent owner) {
        this.length = length;
        this.trustReplaceMethod = trustReplaceMethod;
        this.owner = owner;
        currentIndex = -1;
        array = new TrustData[length];
    }

    public TrustData[] getArray() {
        return array;
    }


    private int getIndexToBeSetIfIsNotInWatchList() {
        TrustData oldHistory;
        boolean isInWatchedList = false;
        int ix = 0;
        do {
            oldHistory = array[ix];
            if (oldHistory == null) {
                return 0;
            }
            ix++;
            //- check if responder is in receiver's watch list
            isInWatchedList = owner.isAgentInWatchList(oldHistory.getResponder());

        } while (isInWatchedList && ix < length);

        if (isInWatchedList) {
            return -1;
        }

        int historyIndex = ix;
        boolean isInWatchedList2;
        for (int k = ix; k < length; k++) {
            TrustData tExp = array[k];
            if (tExp == null) {
                return k;
            }
            //- check if responder is in receiver's watch list
            isInWatchedList2 = owner.isAgentInWatchList(oldHistory.getResponder());

            if (!isInWatchedList2) {
                int oldTime = oldHistory.getLastTime();
                int expTime = tExp.getLastTime();
                if (oldTime > expTime) {
                    historyIndex = k;
                    oldHistory = tExp;
                }
            }
        }

        return historyIndex;
    }

    public int getIndexToBeSet() {

        switch (trustReplaceMethod) {
            case Sequential_Circular:
                return (currentIndex + 1) % length;

            case RemoveLastUpdated:

                if (currentIndex + 1 < length) {
                    return currentIndex + 1;
                } else {

                    int indexToBeSetIfIsNotInWatchList = getIndexToBeSetIfIsNotInWatchList();
                    if (indexToBeSetIfIsNotInWatchList != -1) {
                        return indexToBeSetIfIsNotInWatchList;
                    }

                    TrustData oldHistory = array[0];
                    if (oldHistory == null) {
                        return 0;
                    }

                    int historyIndex = 0;
                    for (int k = 1; k < length; k++) {
                        TrustData tExp = array[k];
                        if (tExp == null) {
                            return k;
                        }
                        int oldTime = oldHistory.getLastTime();
                        int expTime = tExp.getLastTime();
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

    public int getIndexIfExist(Agent responder) {
        for (int k = 0; k < length; k++) {
            TrustData exp = array[k];
            if (exp == null) {
                return -1;
            }
            if (exp.getResponder() != null && responder != null &&
                    exp.getResponder().getId() == responder.getId()) {

                return k;
            }
        }
        return -1;
    }

    public void add(TrustData trustData) {

        if (length <= 0 || array == null) {
            return;
        }

        currentIndex = getIndexToBeSet();

        if (currentIndex < array.length) {
            if (array[currentIndex] != null) {
                array[currentIndex].destroy();
                array[currentIndex] = null;
            }
            array[currentIndex] = trustData;
        }
    }


    public int[] getRewardsCount() {
        int[] tarPit = {0, 0};

        for (int i = 0; i < length; i++) {
            TrustData obs = array[i];
            if (obs == null) {
                break;
            }
            int ar = obs.getAbstractReward();
            if (ar > 0) {
                tarPit[0]++;
            } else if (ar < 0) {
                tarPit[1]++;
            }
        }
        return tarPit;
    }

    public TrustData get(Agent requester, Agent responder) {

        if (requester.getTrust().getExperiences().size() == 0) {
            return null;
        }

        for (int i = 0; i < length; i++) {
            TrustData data = array[i];
            if (data != null && data.getResponder() != null && responder != null &&
                    data.getResponder().getId() == responder.getId()) {
                return data;
            }
        }
        return null;
    }

    public int getFilledSize() {
        if (array[length - 1] != null) {
            return length;
        }
        for (int i = 0; i < length; i++) {
            if (array[i] == null) {
                return i;
            }
        }
        return 0;
    }

}
