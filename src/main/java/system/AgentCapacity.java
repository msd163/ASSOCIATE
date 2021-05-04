package system;

import utils.Globals;

public class AgentCapacity {

    public AgentCapacity(Agent parentAgent) {

        this.agent = parentAgent;
        int worldBF = parentAgent.getWorld().getBignessFactor() / 4;
        int worldAC = parentAgent.getWorld().getAgentsCount();

        watchRadius = Globals.profiler.getCurrentBunch().getWatchRadiusD().nextValue() * worldBF;

        watchListCapacity = Globals.profiler.getCurrentBunch().getWatchListCapacityD().nextValue() * worldAC;

        concurrentDoingServiceCap = Globals.profiler.getCurrentBunch().getConcurrentDoingServiceCapD().nextValue();


        historyCap = Globals.profiler.getCurrentBunch().getHistoryCapD().nextValue();
        historyServiceRecordCap = Globals.profiler.getCurrentBunch().getHistoryServiceRecordCapD().nextValue();

        System.out.println(
                " | watchRadius: " + watchRadius
                        + " | watchListCap: " + watchListCapacity
                        + " | concurrentDoSerCap: " + concurrentDoingServiceCap
                        + " | historyCap: " + historyCap
                        + " | historySerRecCap: " + historyServiceRecordCap
        );

    }

    private Agent agent;

    private int historyServiceRecordCap;    //size of services in each history
    private int historyCap;


    private int watchListCapacity;
    private int watchRadius;                        // radius of watch

    private int concurrentDoingServiceCap;             // number of concurrent services that can do for requester of a service

    private int capPower;                            // it will be calculated by other params

    public int getWatchListCapacity() {
        return watchListCapacity;
    }

    public int getWatchRadius() {
        return watchRadius;
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

}
