package systemLayer;

import utils.Config;

import java.util.Arrays;

public class AgentTrust {

    public AgentTrust(Agent parentAgent, int historyCap, int historyServiceRecordCap) {

        this.agent = parentAgent;

        historyIndex = -1;
        historySize = 0;

        this.historyCap = historyCap;
        this.historyServiceRecordCap = historyServiceRecordCap;

        histories = new AgentHistory[historyCap];
        for (int i = 0; i < historyCap; i++) {
            histories[i] = null;
        }

        historiesSortedIndex = new int[historyCap];
        for (int i = 0; i < historyCap; i++) {
            historiesSortedIndex[i] = i;
        }
    }

    private final Agent agent;

    // all services received by this agent across world run
    private AgentHistory[] histories;
    // An array of history indic that are sorted based on trustScore
    private int[] historiesSortedIndex;
    private int historyCap;
    private int historyIndex;
    private int historySize;
    private int historyServiceRecordCap; // max size of services in each history

    //============================//============================//============================

    public boolean canTrustToAgent(Agent agent) {
        return false;
    }

    /***
     * Sorting Indexing array of the history.
     * according to the values of history array, indexing array will be rearranged
     */
    public void sortHistoryByTrustLevel() {
        final int size = historyCap;

        for (int i = 0; i < size; i++)
            historiesSortedIndex[i] = i;

        boolean sorted;
        do {
            sorted = true;
            int bubble = historiesSortedIndex[0];
            for (int i = 0; i < size - 1; i++) {
                if (
                        (histories[bubble] != null
                                && histories[historiesSortedIndex[i + 1]] != null
                                && histories[bubble].getEffectiveTrustLevel() < histories[historiesSortedIndex[i + 1]].getEffectiveTrustLevel()
                        ) || (
                                histories[bubble] == null
                                        && histories[historiesSortedIndex[i + 1]] != null
                        )
                ) {
                    historiesSortedIndex[i] = historiesSortedIndex[i + 1];
                    historiesSortedIndex[i + 1] = bubble;
                    sorted = false;
                } else {
                    bubble = historiesSortedIndex[i + 1];
                }
            }
        } while (!sorted);

   /*     System.out.println("======= sorted list for " + agent.getId());
        for (int index : historiesSortedIndex) {
            System.out.println(index + ": " + (histories[index] == null ? "NUL" : histories[index].getTrustScore()));
        }*/
        /*for (AgentHistory history : histories) {
            System.out.println(history == null ? "NULL" : history.getTrustScore());
        }*/
    }


    //============================//============================//============================

    /**
     * Adding visited service to it's history
     *
     * @param service
     * @return
     */
    public int recordService(Service service) {
        return recordService(service, false);
    }

    public int recordService(Service service, boolean isExperience) {

        Agent doer = service.getDoer();

        if (doer == null) {
            System.out.println("Error: Doer is null in recodeService");
            return -1;
        }

        int selectedHistoryIndex = -1;

        for (int i = 0, historyLen = histories.length; i < historyLen; i++) {
            AgentHistory ah = histories[i];
            if (ah != null && ah.getDoerAgent().getId() == doer.getId()) {
                selectedHistoryIndex = i;
                break;
            }
        }
        if (selectedHistoryIndex == -1) {
            if (historySize < historyCap) {
                historySize++;
            }

            // Replacing new history item with an exist one according selected method.
            switch (Config.TRUST_REPLACE_HISTORY_METHOD) {

                case Sequential_Circular:
                    historyIndex++;
                    if (historyIndex >= historyCap) {
                        historyIndex = 0;
                    }
                    break;

                case RemoveLastUpdated:
                    AgentHistory oldHistory = histories[0];
                    if (oldHistory == null) {
                        historyIndex = 0;
                    } else {
                        for (int i = 1, historiesLength = histories.length; i < historiesLength; i++) {
                            AgentHistory history = histories[i];

                            if (history == null) {
                                historyIndex = i;
                                break;
                            }
                            if (oldHistory.getLastUpdateTime() > history.getLastUpdateTime()) {
                                historyIndex = i;
                                oldHistory = history;
                            }
                        }
                    }
                    break;
            }
            selectedHistoryIndex = historyIndex;
            histories[selectedHistoryIndex] = new AgentHistory(doer, historyServiceRecordCap);
        }

        histories[selectedHistoryIndex].addToHistory(service, agent, isExperience);

        return selectedHistoryIndex;
    }

    /**
     * @param agent
     * @return If there is not the input agent in the history: return 0;
     */
    public float getTrustScore(Agent agent) {
        for (AgentHistory history : histories) {
            if (history != null && history.getDoerAgent().getId() == agent.getId()) {
                return history.getEffectiveTrustLevel();
            }
        }

        return 0;
    }

    public void recordExperience(ServiceMetaInfo info) {
        recordService(info.getService(), true);
    }
    //============================//============================//============================


    protected AgentTrust clone() {
        AgentTrust trust = new AgentTrust(agent, historyCap, historyServiceRecordCap);
        trust.histories = this.histories;
        trust.historyIndex = this.historyIndex;
        trust.historyServiceRecordCap = this.historyServiceRecordCap;
        return trust;
    }

    @Override
    public String toString() {
        return "AgentTrust{" +
                "\n\t\tagent=" + agent +
                ",\n\t\t histories=" + Arrays.toString(histories) +
                ",\n\t\t historyCap=" + historyCap +
                ",\n\t\t historyIndex=" + historyIndex +
                ",\n\t\t historySize=" + historySize +
                ",\n\t\t historyServiceRecordCap=" + historyServiceRecordCap +
                '}';
    }

    public int getHistorySize() {
        return historySize;
    }

    public AgentHistory[] getHistories() {
        return histories;
    }

    public int[] getHistoriesSortedIndex() {
        return historiesSortedIndex;
    }

}
