package systemLayer.profiler;

public class PopulationBunch {
    private String bunchCount;
    private String trustHistoryItemCap;
    private String trustHistoryCap;
    private String watchListCapacity;
    private String watchDepth;
    private String travelHistory;

    //============================
    private DefParameter bunchCountD;
    private DefParameter trustHistoryItemCapD;
    private DefParameter trustHistoryCapD;
    private DefParameter watchListCapacityD;
    private DefParameter watchDepthD;
    private DefParameter travelHistoryCapD;


    //============================//============================

    public void initDefParams() {
        bunchCountD = new DefParameter(bunchCount);
        trustHistoryItemCapD = new DefParameter(trustHistoryItemCap);
        trustHistoryCapD = new DefParameter(trustHistoryCap);
        watchListCapacityD = new DefParameter(watchListCapacity);
        watchDepthD = new DefParameter(watchDepth);
        travelHistoryCapD = new DefParameter(travelHistory);
    }

    //============================//============================


    public int getBunchCount() {
        return bunchCountD.nextValue();
    }


    public DefParameter getBunchCountD() {
        return bunchCountD;
    }

    public DefParameter getTrustHistoryItemCapD() {
        return trustHistoryItemCapD;
    }

    public DefParameter getTrustHistoryCapD() {
        return trustHistoryCapD;
    }

    public DefParameter getWatchListCapacityD() {
        return watchListCapacityD;
    }

    public DefParameter getWatchRadiusD() {
        return watchDepthD;
    }

    public DefParameter getTravelHistoryCapD() {
        return travelHistoryCapD;
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
                ti + ", trustHistoryItemCap='" + trustHistoryItemCap + '\'' +
                ti + ", trustHistoryCap='" + trustHistoryCap + '\'' +
                ti + ", watchListCapacity='" + watchListCapacity + '\'' +
                ti + ", watchDepth='" + watchDepth + '\'' +
                ti + ", travelHistory='" + travelHistory + '\'' +
                ti + ", bunchCountD=" + bunchCountD.toString(tabIndex) +
                ti + ", trustHistoryItemCapD=" + trustHistoryItemCapD.toString(tabIndex) +
                ti + ", trustHistoryCapD=" + trustHistoryCapD.toString(tabIndex) +
                ti + ", watchListCapacityD=" + watchListCapacityD.toString(tabIndex) +
                ti + ", watchDepthD=" + watchDepthD.toString(tabIndex) +
                ti + ", travelHistoryD=" + travelHistoryCapD.toString(tabIndex) +
                tx + '}';
    }

    @Override
    public String toString() {
        return toString(0);
    }
}
