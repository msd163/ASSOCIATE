package profiler;

import utils.DefParameter;

public class PopulationBunch {
    private String bunchCount;
    private String historyServiceRecordCap;
    private String historyCap;
    private String watchListCapacity;
    private String watchRadius;
    private String concurrentDoingServiceCap;

    //============================
    private DefParameter bunchCountD;
    private DefParameter historyServiceRecordCapD;
    private DefParameter historyCapD;
    private DefParameter watchListCapacityD;
    private DefParameter watchRadiusD;
    private DefParameter concurrentDoingServiceCapD;


    //============================//============================

    public void initDefParams() {
        bunchCountD = new DefParameter(bunchCount);
        historyServiceRecordCapD = new DefParameter(historyServiceRecordCap);
        historyCapD = new DefParameter(historyCap);
        watchListCapacityD = new DefParameter(watchListCapacity);
        watchRadiusD = new DefParameter(watchRadius);
        concurrentDoingServiceCapD = new DefParameter(concurrentDoingServiceCap);
    }

    //============================//============================


    public int getBunchCount() {
        return bunchCountD.nextValue();
    }


    public DefParameter getBunchCountD() {
        return bunchCountD;
    }

    public DefParameter getHistoryServiceRecordCapD() {
        return historyServiceRecordCapD;
    }

    public DefParameter getHistoryCapD() {
        return historyCapD;
    }

    public DefParameter getWatchListCapacityD() {
        return watchListCapacityD;
    }

    public DefParameter getWatchRadiusD() {
        return watchRadiusD;
    }

    public DefParameter getConcurrentDoingServiceCapD() {
        return concurrentDoingServiceCapD;
    }

    //============================//============================
    public String toString(int tabIndex) {
        tabIndex++;
        StringBuilder tx = new StringBuilder("\n");
        StringBuilder ti = new StringBuilder("\n");
        for (int i = 0; i <= tabIndex; i++) {
            if (i > 1) {
                tx.append("\t");
            }
            ti.append("\t");
        }
        tabIndex++;
        return tx + "PopulationBunch{" +
                ti + "  bunchCount='" + bunchCount + '\'' +
                ti + ", historyServiceRecordCap='" + historyServiceRecordCap + '\'' +
                ti + ", historyCap='" + historyCap + '\'' +
                ti + ", watchListCapacity='" + watchListCapacity + '\'' +
                ti + ", watchRadius='" + watchRadius + '\'' +
                ti + ", concurrentDoingServiceCap='" + concurrentDoingServiceCap + '\'' +
                ti + ", bunchCountD=" + bunchCountD.toString(tabIndex) +
                ti + ", historyServiceRecordCapD=" + historyServiceRecordCapD.toString(tabIndex) +
                ti + ", historyCapD=" + historyCapD.toString(tabIndex) +
                ti + ", watchListCapacityD=" + watchListCapacityD.toString(tabIndex) +
                ti + ", watchRadiusD=" + watchRadiusD.toString(tabIndex) +
                ti + ", concurrentDoingServiceCapD=" + concurrentDoingServiceCapD.toString(tabIndex) +
                tx + '}';
    }

    @Override
    public String toString() {
        return toString(0);
    }
}
