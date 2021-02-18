package system;

import utils.Globals;

public class AgentCapacity {

    public AgentCapacity(Agent parentAgent) {

        this.agent = parentAgent;
        int worldBF = parentAgent.getWorld().getBignessFactor() / 4;
        int worldAC = parentAgent.getWorld().getAgentsCount();

        int randPowerFactor;
        int i = Globals.random.nextInt(100);
        // %70 : 1-20
        // %20 : 21-50
        // %10 : 51-100
        i = i < 70 ? 20 : i < 90 ? 50 : 100;

        randPowerFactor = Globals.random.nextInt(i) + 1;

        capPower = randPowerFactor;

        float capCoeff = (float) capPower / 100;

        watchRadius = (int) (capCoeff * worldBF);

        watchListCapacity = (int) (capCoeff * worldAC);

        concurrentDoingServiceCap = watchListCapacity / 10;

        historyCap = watchListCapacity * 2;
        historyServiceRecordCap = watchListCapacity;

        System.out.println(
                         " | capCoeff: " + capCoeff
                        + " | watchRadius: " + watchRadius
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
