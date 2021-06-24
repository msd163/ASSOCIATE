package simulateLayer;

import _type.TtTrustMethodology;

public class SimulationConfigItem {
    private int method;
    // private boolean isValidateByTrustObservation;
    private float trustForgottenCoeff;
    private float recommendationCoeff;
    private boolean isBidirectionalRecommendationSharing;
    private boolean isUseExperience;
    private boolean isUseIndirectExperience;
    private boolean isBidirectionalExperienceSharing;
    private boolean isUseObservation;
    private boolean isUseIndirectObservation;
    private boolean isBidirectionalObservationSharing;
    private int experienceDepthInRewarding;
    private float ignoringThresholdOfTrustValue;
    private float maximumConsideredRoutingHelpInTrustMechanism;

    //============================//============================//============================
    public String getInfo() {
        TtTrustMethodology methodology = TtTrustMethodology.getByOrdinal(method);
        if (methodology == TtTrustMethodology.TrustMode_RandomPath || methodology == TtTrustMethodology.TrustMode_ShortPath) {
            return "Method: " + methodology +
                    " | FrgCoeff: " + trustForgottenCoeff +
                    " || RcmCoeff: " + recommendationCoeff +
                    " | IsRcmBid: " + isBidirectionalRecommendationSharing +
                    " || UseExp: " + isUseExperience +
                    " | UseIndrExp: " + isUseIndirectExperience +
                    " | IsExpBid: " + isBidirectionalExperienceSharing +
                    " || UseObs: " + isUseObservation +
                    " | UseIndrObs: " + isUseIndirectObservation +
                    " | IsObsBid: " + isBidirectionalObservationSharing +
                    " || ExpDepthInRwd: " + experienceDepthInRewarding +
                    " | IgrThrldOfTutV: " + ignoringThresholdOfTrustValue +
                    " | MaxRtHpInTut: " + maximumConsideredRoutingHelpInTrustMechanism;

        }
        return "Method: " + methodology;

    }

    public boolean isUseRecommendation() {
        return recommendationCoeff > 0;
    }
    //============================//============================//============================

    public TtTrustMethodology getTtMethod() {
        return TtTrustMethodology.getByOrdinal(method);
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }


    public float getTrustForgottenCoeff() {
        return trustForgottenCoeff;
    }

    public float getRecommendationCoeff() {
        return recommendationCoeff;
    }

    public boolean isIsUseExperience() {
        return isUseExperience;
    }

    public boolean isIsUseIndirectExperience() {
        return isUseIndirectExperience;
    }

    public boolean isIsUseObservation() {
        return isUseObservation;
    }

    public boolean isIsUseIndirectObservation() {
        return isUseIndirectObservation;
    }

    public int getExperienceDepthInRewarding() {
        return experienceDepthInRewarding;
    }

    public float getIgnoringThresholdOfTrustValue() {
        return ignoringThresholdOfTrustValue;
    }

    public float getMaximumConsideredRoutingHelpInTrustMechanism() {
        return maximumConsideredRoutingHelpInTrustMechanism;
    }

    public boolean isIsBidirectionalRecommendationSharing() {
        return isBidirectionalRecommendationSharing;
    }

    public boolean isIsBidirectionalExperienceSharing() {
        return isBidirectionalExperienceSharing;
    }

    public boolean isIsBidirectionalObservationSharing() {
        return isBidirectionalObservationSharing;
    }
}
