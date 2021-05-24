package trustLayer;

import systemLayer.Agent;

import java.util.Arrays;

public class AgentTrust {

    public AgentTrust(Agent parentAgent, int historyCap, int historyServiceRecordCap) {

        this.agent = parentAgent;

        historyIndex = -1;
        historySize = 0;

        this.historyCap = historyCap;
        this.historyServiceRecordCap = historyServiceRecordCap;

        histories = new TrustHistory[historyCap];
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
    private TrustHistory[] histories;
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

       /* boolean sorted;
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
        } while (!sorted);*/

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
     * @param agent
     * @return If there is not the input agent in the history: return 0;
     */
    public float getTrustScore(Agent agent) {
       /* for (AgentHistory history : histories) {
            if (history != null && history.getDoerAgent().getId() == agent.getId()) {
                return history.getEffectiveTrustLevel();
            }
        }*/

        return 0;
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

    public int[] getHistoriesSortedIndex() {
        return historiesSortedIndex;
    }

}
