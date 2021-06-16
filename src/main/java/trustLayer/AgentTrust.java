package trustLayer;

import _type.TtTrustReplaceHistoryMethod;
import com.google.gson.annotations.Expose;
import systemLayer.Agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AgentTrust {

    public AgentTrust(int trustHistoryCap, int historyItemCap, TtTrustReplaceHistoryMethod replaceHistoryMethod, int recommendationCap, int recommendationItemCap) {
        this.historyCap = trustHistoryCap;
        this.historyItemCap = historyItemCap;
        this.trustReplaceHistoryMethod = replaceHistoryMethod;
        this.recommendationCap = recommendationCap;
        this.recommendationItemCap = recommendationItemCap;
    }

    public void setTrustParams(int trustHistoryCap, int historyItemCap, int recommendationCap, int recommendationItemCap, int observationCap) {
        this.historyCap = trustHistoryCap;
        this.historyItemCap = historyItemCap;
        this.recommendationCap = recommendationCap;
        this.recommendationItemCap = recommendationItemCap;
        this.observationCap = observationCap;
    }

    public void init(Agent parentAgent) {

        this.agent = parentAgent;

        historyIndex = -1;
        historySize = 0;


        histories = new TrustHistory[historyCap];
        for (int i = 0; i < historyCap; i++) {
            histories[i] = null;
        }

        historiesSortedIndex = new int[historyCap];
        for (int i = 0; i < historyCap; i++) {
            historiesSortedIndex[i] = i;
        }

        recommendations = new ArrayList<>();

        observations = new ArrayList<>();
    }


    private Agent agent;

    //============================
    private List<TrustRecommendation> recommendations;
    private int recommendationCap;
    private int recommendationItemCap;

    //============================
    // all services received by this agent across world run
    private TrustHistory[] histories;
    // An array of history indic that are sorted based on trustScore
    private int[] historiesSortedIndex;

    private int historyCap;     // maximum size of history
    private int historyItemCap; // max size of services in each history

    private int historyIndex;   // current index of history that will be fill
    private int historySize;    // current capacity of history

    //============================
    @Expose
    private TtTrustReplaceHistoryMethod trustReplaceHistoryMethod;

    //============================
    private int observationCap;
    private List<TrustObservation> observations;

    //============================//============================//============================

    //============================//============================//============================

    public void createNewHistory(Agent helper, float trustScore) {

        histories[historyIndex] = new TrustHistory(helper);
        histories[historyIndex].addHistory(trustScore);

    }


    public void addHistory(float trustScore) {
        ArrayList<TrustHistoryItem> items = histories[historyIndex].getItems();
        if (items.size() >= historyItemCap) {
            // Removing trust score of removed item from finalTrustLevel
            histories[historyIndex].setFinalTrustLevel(
                    histories[historyIndex].getFinalTrustLevel() -
                            items.get(0).getTrustScore()
            );
            histories[historyIndex].getItems().remove(0);
        }
        histories[historyIndex].addHistory(trustScore);
    }

    public int[] getObservationInTargetAndPitfallCount() {
        int[] tarPit = {0, 0};
        if (observations == null || observations.isEmpty()) {
            return tarPit;
        }
        for (TrustObservation obs : observations) {
            if (obs != null) {
                if (obs.isIsFinalTarget()) {
                    tarPit[0]++;
                } else if (obs.isIsFinalPitfall()) {
                    tarPit[1]++;
                }
            }
        }
        return tarPit;
    }

    //============================//============================//============================


    protected AgentTrust clone() {
        AgentTrust trust = new AgentTrust(historyCap, historyItemCap, trustReplaceHistoryMethod, recommendationCap, recommendationItemCap);
        trust.init(agent);
        trust.histories = this.histories;
        trust.historyIndex = this.historyIndex;
        trust.historyItemCap = this.historyItemCap;
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
                ",\n\t\t trustHistoryItemCap=" + historyItemCap +
                '}';
    }

    public int getHistorySize() {
        return historySize;
    }

    public int[] getHistoriesSortedIndex() {
        return historiesSortedIndex;
    }

    public TrustHistory[] getHistories() {
        return histories;
    }

    public int getHistoryCap() {
        return historyCap;
    }

    public int getHistoryIndex() {
        return historyIndex;
    }

    public int getHistoryItemCap() {
        return historyItemCap;
    }

    public void addHistorySizeIfPossible() {
        if (historySize < historyCap) {
            historySize++;
        }
    }

    public void addHistoryIndex() {
        historyIndex++;
    }

    public boolean isIndexExceedFromCap() {
        return historyIndex >= historyCap;
    }

    public void setHistoryIndex(int historyIndex) {
        this.historyIndex = historyIndex;
    }

    public TtTrustReplaceHistoryMethod getTrustReplaceHistoryMethod() {
        return trustReplaceHistoryMethod;
    }

    public void setTrustReplaceHistoryMethod(TtTrustReplaceHistoryMethod trustReplaceHistoryMethod) {
        this.trustReplaceHistoryMethod = trustReplaceHistoryMethod;
    }

    public List<TrustRecommendation> getRecommendations() {
        return recommendations;
    }

    public int getRecommendationCap() {
        return recommendationCap;
    }

    public int getRecommendationItemCap() {
        return recommendationItemCap;
    }

    public int getObservationCap() {
        return observationCap;
    }

    public List<TrustObservation> getObservations() {
        return observations;
    }
}
