package systemLayer;

import com.google.gson.annotations.Expose;
import utils.Globals;

public class AgentCapacity {

    public AgentCapacity() {
    }

    public AgentCapacity(Agent parentAgent) {

        this.agent = parentAgent;

        watchDepth = Globals.profiler.getCurrentBunch().getWatchRadiusD().nextValue();

        travelHistoryCap = Globals.profiler.getCurrentBunch().getTravelHistoryCapD().nextValue();

        watchListCapacity = Globals.profiler.getCurrentBunch().getWatchListCapacityD().nextValue();



        trustHistoryCap = Globals.profiler.getCurrentBunch().getTrustHistoryCapD().nextValue();
        trustHistoryItemCap = Globals.profiler.getCurrentBunch().getTrustHistoryItemCapD().nextValue();

        // capPower is between 0 and 100
        capPower =
                (int) (60 * ((float) travelHistoryCap / Globals.profiler.getCurrentBunch().getTravelHistoryCapD().getMaxValue())) +
                        (int) (40 * ((float) watchDepth / Globals.profiler.getCurrentBunch().getWatchRadiusD().getMaxValue()))
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

}
