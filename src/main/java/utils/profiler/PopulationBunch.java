package utils.profiler;

import _type.TtTrustBehavioralStrategy;
import _type.TtTrustReplaceHistoryMethod;

public class PopulationBunch {
    private String bunchCount;
    private String trustHistoryItemCap;
    private String trustHistoryCap;
    private String trustRecommendationItemCap;
    private String trustRecommendationCap;
    private String watchListCapacity;
    private String watchDepth;
    private String travelHistory;
    private String targetCount;
    private String trustReplaceHistoryMethod;
    private PopulationBunchBehaviorParam behavior;

    //============================
    private DefParameter bunchCountD;
    private DefParameter trustHistoryItemCapD;
    private DefParameter trustHistoryCapD;
    private DefParameter watchListCapacityD;
    private DefParameter trustRecommendationCapD;
    private DefParameter trustRecommendationItemCapD;
    private DefParameter watchDepthD;
    private DefParameter travelHistoryCapD;
    private DefParameter targetCountD;
    private DefParameter trustReplaceHistoryMethodD;


    //============================//============================

    public void initDefParams() {
        bunchCountD = new DefParameter(bunchCount);
        trustHistoryItemCapD = new DefParameter(trustHistoryItemCap);
        trustHistoryCapD = new DefParameter(trustHistoryCap);
        trustRecommendationItemCapD = new DefParameter(trustRecommendationItemCap);
        trustRecommendationCapD = new DefParameter(trustRecommendationCap);
        watchListCapacityD = new DefParameter(watchListCapacity);
        watchDepthD = new DefParameter(watchDepth);
        travelHistoryCapD = new DefParameter(travelHistory);
        targetCountD = new DefParameter(targetCount);
        trustReplaceHistoryMethodD = new DefParameter(trustReplaceHistoryMethod);
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

    public DefParameter getTargetCountD() {
        return targetCountD;
    }

    public TtTrustReplaceHistoryMethod getTrustReplaceHistoryMethod() {
        int i = trustReplaceHistoryMethodD.nextValue();
        return TtTrustReplaceHistoryMethod.getByOrdinal(i);
    }

    public PopulationBunchBehaviorParam getBehavior() {
        return behavior;
    }

    public DefParameter getTrustRecommendationCapD() {
        return trustRecommendationCapD;
    }

    public DefParameter getTrustRecommendationItemCapD() {
        return trustRecommendationItemCapD;
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
                ti + ", targetCountD=" + targetCountD.toString(tabIndex) +
                ti + ", trustReplaceHistoryMethodD=" + trustReplaceHistoryMethodD.toString(tabIndex) +
                tx + '}';
    }

    @Override
    public String toString() {
        return toString(0);
    }
}
