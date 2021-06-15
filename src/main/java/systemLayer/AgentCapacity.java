package systemLayer;

import com.google.gson.annotations.Expose;
import utils.profiler.SimulationProfiler;

public class AgentCapacity {

    public AgentCapacity() {
    }

    public AgentCapacity(Agent parentAgent, SimulationProfiler profiler) {

        this.agent = parentAgent;

        watchDepth = profiler.getCurrentBunch().getWatchRadiusD().nextValue();

        travelHistoryCap = profiler.getCurrentBunch().getTravelHistoryCapD().nextValue();

        watchListCapacity = profiler.getCurrentBunch().getWatchListCapacityD().nextValue();

        trustRecommendationCap = profiler.getCurrentBunch().getTrustRecommendationCapD().nextValue();
        trustRecommendationItemCap = profiler.getCurrentBunch().getTrustRecommendationItemCapD().nextValue();

        trustHistoryCap = profiler.getCurrentBunch().getTrustHistoryCapD().nextValue();
        trustHistoryItemCap = profiler.getCurrentBunch().getTrustHistoryItemCapD().nextValue();

        // capPower is between 0 and 100
        capPower =
                (int) (15 * ((float) trustHistoryCap / profiler.getCurrentBunch().getTrustHistoryCapD().getMaxValue())) +
                        (int) (10 * ((float) trustHistoryItemCap / profiler.getCurrentBunch().getTrustHistoryItemCapD().getMaxValue())) +
                        (int) (10 * ((float) trustRecommendationCap / profiler.getCurrentBunch().getTrustRecommendationCapD().getMaxValue())) +
                        (int) (10 * ((float) trustRecommendationItemCap / profiler.getCurrentBunch().getTrustRecommendationItemCapD().getMaxValue())) +
                        (int) (20 * ((float) travelHistoryCap / profiler.getCurrentBunch().getTravelHistoryCapD().getMaxValue())) +
                        (int) (20 * ((float) watchListCapacity / profiler.getCurrentBunch().getWatchListCapacityD().getMaxValue())) +
                        (int) (15 * ((float) watchDepth / profiler.getCurrentBunch().getWatchRadiusD().getMaxValue()))
        ;

        /*System.out.println(
                " | watchDepth: " + watchDepth
                        + " | watchListCap: " + watchListCapacity
                        + " | travelHistory: " + travelHistory
                        + " | trustHistoryCap: " + trustHistoryCap
                        + " | historySerRecCap: " + trustHistoryItemCap
        );*/

    }

    private Agent agent;

    @Expose
    private int trustHistoryItemCap;        //size of trust items in each trust history. trust items is score of trust for one agent (helper)
    @Expose
    private int trustHistoryCap;
    @Expose
    private int trustRecommendationItemCap;        //size of recommendation items in each trust history. trust items is score of trust for one agent (helper)
    @Expose
    private int trustRecommendationCap;
    @Expose
    private int watchListCapacity;
    @Expose
    private int watchDepth;                              // depth of watch in neighbor states
    @Expose
    private int travelHistoryCap;                        // size of travel history
    @Expose
    private int capPower;                            // it will be calculated by other params

    public int getWatchListCapacity() {
        return watchListCapacity;
    }

    public int getWatchDepth() {
        return watchDepth;
    }

    public int getTravelHistoryCap() {
        return travelHistoryCap;
    }

    public int getCapPower() {
        return capPower;
    }

    @Override
    public String toString() {
        return "\n\tAgentCapacity{" +
                "\n\t\ttrustHistoryItemCap=" + trustHistoryItemCap +
                ",\n\t\ttrustHistoryCap=" + trustHistoryCap +
                ",\n\t\twatchListCapacity=" + watchListCapacity +
                ",\n\t\twatchDepth=" + watchDepth +
                ",\n\t\ttravelHistoryCap=" + travelHistoryCap +
                ",\n\t\tcapPower=" + capPower +
                '}';
    }

    public int getTrustHistoryItemCap() {
        return trustHistoryItemCap;
    }

    public void setTrustHistoryItemCap(int trustHistoryItemCap) {
        this.trustHistoryItemCap = trustHistoryItemCap;
    }

    public int getTrustHistoryCap() {
        return trustHistoryCap;
    }

    public void setTrustHistoryCap(int trustHistoryCap) {
        this.trustHistoryCap = trustHistoryCap;
    }

    public void setWatchListCapacity(int watchListCapacity) {
        this.watchListCapacity = watchListCapacity;
    }

    public void setWatchDepth(int watchDepth) {
        this.watchDepth = watchDepth;
    }

    public void setTravelHistoryCap(int travelHistoryCap) {
        this.travelHistoryCap = travelHistoryCap;
    }

    public int getTrustRecommendationItemCap() {
        return trustRecommendationItemCap;
    }

    public void setTrustRecommendationItemCap(int trustRecommendationItemCap) {
        this.trustRecommendationItemCap = trustRecommendationItemCap;
    }

    public int getTrustRecommendationCap() {
        return trustRecommendationCap;
    }

    public void setTrustRecommendationCap(int trustRecommendationCap) {
        this.trustRecommendationCap = trustRecommendationCap;
    }
}
