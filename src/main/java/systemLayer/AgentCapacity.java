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

        concurrentDoingServiceCap = Globals.profiler.getCurrentBunch().getConcurrentDoingServiceCapD().nextValue();


        historyCap = Globals.profiler.getCurrentBunch().getHistoryCapD().nextValue();
        historyServiceRecordCap = Globals.profiler.getCurrentBunch().getHistoryServiceRecordCapD().nextValue();

        // capPower is between 0 and 100
        capPower =
                (int) (60 * ((float) travelHistoryCap / Globals.profiler.getCurrentBunch().getTravelHistoryCapD().getMaxValue())) +
                        (int) (40 * ((float) watchDepth / Globals.profiler.getCurrentBunch().getWatchRadiusD().getMaxValue()))
        ;

        /*System.out.println(
                " | watchRadius: " + watchRadius
                        + " | watchListCap: " + watchListCapacity
                        + " | travelHistory: " + travelHistory
                        + " | concurrentDoSerCap: " + concurrentDoingServiceCap
                        + " | historyCap: " + historyCap
                        + " | historySerRecCap: " + historyServiceRecordCap
        );*/

    }

    private Agent agent;

    @Expose
    private int historyServiceRecordCap;    //size of services in each history
    @Expose
    private int historyCap;
    @Expose
    private int watchListCapacity;
    @Expose
    private int watchDepth;                              // depth of watch in neighbor states
    @Expose
    private int travelHistoryCap;                        // size of travel history
    @Expose
    private int concurrentDoingServiceCap;             // number of concurrent services that can do for requester of a service
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
                "\n\t\tagentVisitCap=" + historyServiceRecordCap +
                ",\n\t\tserviceRecordCap=" + historyCap +
                ",\n\t\twatchListCapacity=" + watchListCapacity +
                ",\n\t\twatchDepth=" + watchDepth +
                ",\n\t\ttravelHistory=" + travelHistoryCap +
                ",\n\t\tconcurrentDoingServiceCap=" + concurrentDoingServiceCap +
                ",\n\t\tcapPower=" + capPower +
                '}';
    }

    public int getConcurrentDoingServiceCap() {
        return concurrentDoingServiceCap;
    }

    public int getHistoryServiceRecordCap() {
        return historyServiceRecordCap;
    }

    public void setHistoryServiceRecordCap(int historyServiceRecordCap) {
        this.historyServiceRecordCap = historyServiceRecordCap;
    }


    public int getHistoryCap() {
        return historyCap;
    }

    public void setHistoryCap(int historyCap) {
        this.historyCap = historyCap;
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

    public void setConcurrentDoingServiceCap(int concurrentDoingServiceCap) {
        this.concurrentDoingServiceCap = concurrentDoingServiceCap;
    }
}
