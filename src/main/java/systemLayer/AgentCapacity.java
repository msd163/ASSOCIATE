package systemLayer;

import com.google.gson.annotations.Expose;
import utils.Globals;

public class AgentCapacity {

    public AgentCapacity() {
    }

    public AgentCapacity(Agent parentAgent) {

        this.agent = parentAgent;

        watchRadius = Globals.profiler.getCurrentBunch().getWatchRadiusD().nextValue();

        stateMapCap = Globals.profiler.getCurrentBunch().getStateMapCapD().nextValue();

        watchListCapacity = Globals.profiler.getCurrentBunch().getWatchListCapacityD().nextValue();

        concurrentDoingServiceCap = Globals.profiler.getCurrentBunch().getConcurrentDoingServiceCapD().nextValue();


        historyCap = Globals.profiler.getCurrentBunch().getHistoryCapD().nextValue();
        historyServiceRecordCap = Globals.profiler.getCurrentBunch().getHistoryServiceRecordCapD().nextValue();

        // capPower is between 0 and 100
        capPower =
                (int) (60 * ((float) stateMapCap / Globals.profiler.getCurrentBunch().getStateMapCapD().getMaxValue())) +
                        (int) (40 * ((float) watchRadius / Globals.profiler.getCurrentBunch().getWatchRadiusD().getMaxValue()))
        ;

        /*System.out.println(
                " | watchRadius: " + watchRadius
                        + " | watchListCap: " + watchListCapacity
                        + " | stateMapCap: " + stateMapCap
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
    private int watchRadius;                        // radius of watch
    @Expose
    private int stateMapCap;                        // radius of watch
    @Expose
    private int concurrentDoingServiceCap;             // number of concurrent services that can do for requester of a service
    @Expose
    private int capPower;                            // it will be calculated by other params

    public int getWatchListCapacity() {
        return watchListCapacity;
    }

    public int getWatchRadius() {
        return watchRadius;
    }

    public int getStateMapCap() {
        return stateMapCap;
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
                ",\n\t\twatchRadius=" + watchRadius +
                ",\n\t\tstateMapCap=" + stateMapCap +
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

    public void setWatchRadius(int watchRadius) {
        this.watchRadius = watchRadius;
    }

    public void setStateMapCap(int stateMapCap) {
        this.stateMapCap = stateMapCap;
    }

    public void setConcurrentDoingServiceCap(int concurrentDoingServiceCap) {
        this.concurrentDoingServiceCap = concurrentDoingServiceCap;
    }
}
