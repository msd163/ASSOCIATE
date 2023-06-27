package simulateLayer.config.society;

import _type.TtTrustReplaceMethod;
import utils.DefParameter;

public class PopulationBunch {
    private String bunchCount;

    private String experienceCap;
    private String experienceItemCap;

    private String indirectExperienceCap;
    private String indirectExperienceItemCap;

    private String trustRecommendationCap;
    private String trustRecommendationItemCap;

    private String observationCap;
    private String observationItemCap;

    private String indirectObservationCap;
    private String indirectObservationItemCap;

    private String watchListCap;
    private String watchDepth;
    private String travelHistoryCap;
    private String targetCount;
    private String trustReplaceHistoryMethod;
    private String withInternetCapPowerThreshold;      // Minimum capPower of agents that have internet.
    //private String withInternetCapPowerThreshold;      // Minimum capPower of agents that have internet.
    private PopulationBunchBehaviorParam behavior;

    private PopulationBunchCertification cert;

    //============================
    private DefParameter bunchCountD;

    private DefParameter experienceCapD;
    private DefParameter experienceItemCapD;

    private DefParameter indirectExperienceCapD;
    private DefParameter indirectExperienceItemCapD;


    private DefParameter observationCapD;
    private DefParameter observationItemCapD;

    private DefParameter indirectObservationCapD;
    private DefParameter indirectObservationItemCapD;

    private DefParameter watchListCapD;
    private DefParameter trustRecommendationCapD;
    private DefParameter trustRecommendationItemCapD;
    private DefParameter watchDepthD;
    private DefParameter travelHistoryCapD;
    private DefParameter targetCountD;
    private DefParameter trustReplaceHistoryMethodD;

    private DefParameter withInternetCapPowerThresholdD;
    private DefParameter isCandidateForCertificationD;

    //============================//============================

    public void initDefParams() {
        bunchCountD = new DefParameter(bunchCount);

        experienceCapD = new DefParameter(experienceCap);
        experienceItemCapD = new DefParameter(experienceItemCap);

        indirectExperienceCapD = new DefParameter(indirectExperienceCap);
        indirectExperienceItemCapD = new DefParameter(indirectExperienceItemCap);

        trustRecommendationCapD = new DefParameter(trustRecommendationCap);
        trustRecommendationItemCapD = new DefParameter(trustRecommendationItemCap);
        watchListCapD = new DefParameter(watchListCap);
        watchDepthD = new DefParameter(watchDepth);
        travelHistoryCapD = new DefParameter(travelHistoryCap);
        targetCountD = new DefParameter(targetCount);
        trustReplaceHistoryMethodD = new DefParameter(trustReplaceHistoryMethod);
        withInternetCapPowerThresholdD = new DefParameter(withInternetCapPowerThreshold);
        //isCandidateForCertificationD = new DefParameter(isCandidateForCertification);

        observationCapD = new DefParameter(observationCap);
        observationItemCapD = new DefParameter(observationItemCap);

        indirectObservationCapD = new DefParameter(indirectObservationCap);
        indirectObservationItemCapD = new DefParameter(indirectObservationItemCap);

    }

    //============================//============================


    public int getNextBunchCount() {
        return bunchCountD.nextValue();
    }
    public String getBunchCount() {
        return bunchCount;
    }

    public DefParameter getIndirectExperienceCapD() {
        return indirectExperienceCapD;
    }

    public DefParameter getIndirectExperienceItemCapD() {
        return indirectExperienceItemCapD;
    }

    public DefParameter getObservationItemCapD() {
        return observationItemCapD;
    }

    public DefParameter getIndirectObservationCapD() {
        return indirectObservationCapD;
    }

    public DefParameter getIndirectObservationItemCapD() {
        return indirectObservationItemCapD;
    }

    public DefParameter getBunchCountD() {
        return bunchCountD;
    }

    public DefParameter getExperienceItemCapD() {
        return experienceItemCapD;
    }

    public DefParameter getExperienceCapD() {
        return experienceCapD;
    }

    public DefParameter getWatchListCapD() {
        return watchListCapD;
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

    public TtTrustReplaceMethod getTrustReplaceHistoryMethod() {
        int i = trustReplaceHistoryMethodD.nextValue();
        return TtTrustReplaceMethod.getByOrdinal(i);
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

    public DefParameter getObservationCapD() {
        return observationCapD;
    }

    public DefParameter getWithInternetCapPowerThresholdD() {
        return withInternetCapPowerThresholdD;
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
                ti + ", experienceItemCap='" + experienceItemCap + '\'' +
                ti + ", experienceCap='" + experienceCap + '\'' +
                ti + ", watchListCap='" + watchListCap + '\'' +
                ti + ", watchDepth='" + watchDepth + '\'' +
                ti + ", travelHistoryCap='" + travelHistoryCap + '\'' +
                ti + ", bunchCountD=" + bunchCountD.toString(tabIndex) +
                ti + ", experienceItemCapD=" + experienceItemCapD.toString(tabIndex) +
                ti + ", experienceCapD=" + experienceCapD.toString(tabIndex) +
                ti + ", watchListCapD=" + watchListCapD.toString(tabIndex) +
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

    public PopulationBunchCertification getCert() {
        return cert;
    }
}
