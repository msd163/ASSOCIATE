package simulateLayer;

import _type.TtTrustMethodology;

public class SimulationConfigItem {
    private int method;
    // private boolean isValidateByTrustObservation;
    private float trustForgottenCoeff;
    private float recommendationCoeff;
    private boolean isBidirectionalRecommendationSharing;
    private boolean isSafeUseRecommendation;                // TRUE: if inner trust value is zero, final trust value is equals to recommendation trust value. in otherwise, final trust value is weighted average of inner and recommendation trust value.
    private boolean isUseNegativeRecommendationEffect;      // TRUE: using negative recommended trust value in calculating final trust value. | FALSE: only use positive recommendation in calculating trust value.
    private boolean isUseExperience;
    private boolean isUseIndirectExperience;
    private boolean isBidirectionalExperienceSharing;
    private boolean isUseObservation;
    private boolean isUseIndirectObservation;
    private boolean isBidirectionalObservationSharing;
    private int experienceDepthInRewarding;
    private float ignoringThresholdOfTrustValue;
    private float maximumConsideredRoutingHelpInTrustMechanism;
    private boolean isUseCertification;
    private boolean isUseSharingRecommendationWithInternet;

    //============================//============================//============================
    public String getInfo(int certificationCount) {
        TtTrustMethodology methodology = TtTrustMethodology.getByOrdinal(method);
        if (methodology == TtTrustMethodology.TrustMode_RandomPath || methodology == TtTrustMethodology.TrustMode_ShortPath) {
            return "@Mtd: " + methodology +
                    "@ FrgCf: " + trustForgottenCoeff +
                    " - IgrThrld: " + ignoringThresholdOfTrustValue +
                    " - MaxRtHp: " + maximumConsideredRoutingHelpInTrustMechanism +
                    " @EXP is: " + isUseExperience +
                    " - ind: " + isUseIndirectExperience +
                    " - bid: " + isBidirectionalExperienceSharing +
                    " - dpth: " + experienceDepthInRewarding +
                    " @OBS is: " + isUseObservation +
                    " - ind: " + isUseIndirectObservation +
                    " - bid: " + isBidirectionalObservationSharing +
                    " @RCM cf: " + recommendationCoeff +
                    " - bid: " + isBidirectionalRecommendationSharing +
                    " - sfm: " + isSafeUseRecommendation +
                    " - ngEff: " + isUseNegativeRecommendationEffect +
                    " @CERT is: " + isUseCertification+
                    " @INT rcm: " + isUseSharingRecommendationWithInternet+
                    " - cnt: " + certificationCount
                    ;

        }
        return "Method: " + methodology;

    }

    public boolean isUseRecommendation() {
        return recommendationCoeff > 0;
    }
    //============================//============================//============================


    public boolean isIsUseNegativeRecommendationEffect() {
        return isUseNegativeRecommendationEffect;
    }

    public boolean isIsSafeUseRecommendation() {
        return isSafeUseRecommendation;
    }

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

    public boolean isIsUseCertification() {
        return isUseCertification;
    }

    public boolean isIsUseSharingRecommendationWithInternet() {
        return isUseSharingRecommendationWithInternet;
    }
}
