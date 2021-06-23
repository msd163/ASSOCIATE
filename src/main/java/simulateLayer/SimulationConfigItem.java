package simulateLayer;

import _type.TtTrustMethodology;

public class SimulationConfigItem {
    private int method;
    // private boolean isValidateByTrustObservation;
    private float trustForgottenCoeff;
    private float trustRecommendationCoeff;
    private boolean isUseExperience;
    private boolean isUseIndirectExperience;
    private boolean isUseObservation;
    private boolean isUseIndirectObservation;
    private int   experienceDepthInRewarding;
    private float ignoringThresholdOfTrustValue;
    private float maximumConsideredRoutingHelpInTrustMechanism;

    //============================//============================//============================
    public String getInfo() {
        TtTrustMethodology methodology = TtTrustMethodology.getByOrdinal(method);
        if (methodology == TtTrustMethodology.TrustMode_RandomPath || methodology == TtTrustMethodology.TrustMode_ShortPath) {
            return "method: " + methodology +
                    " | trustForgottenCoeff: " + trustForgottenCoeff +
                    " | trustRecommendationCoeff: " + trustRecommendationCoeff +
                    " | isUseExperience: " + isUseExperience +
                    " | isUseIndirectExperience: " + isUseIndirectExperience +
                    " | isUseObservation: " + isUseObservation +
                    " | isUseIndirectObservation: " + isUseIndirectObservation +
                    " | experienceDepthInRewarding: " + experienceDepthInRewarding +
                    " | ignoringThresholdOfTrustValue: " + ignoringThresholdOfTrustValue +
                    " | maximumConsideredRoutingHelpInTrustMechanism: " + maximumConsideredRoutingHelpInTrustMechanism;

        }
        return "method: " + methodology;

    }

    public boolean isUseTrustForgottenCoeff() {
        return trustForgottenCoeff > 0;
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

    public float getTrustRecommendationCoeff() {
        return trustRecommendationCoeff;
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
}
