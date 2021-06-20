package simulateLayer;

import _type.TtTrustMethodology;

public class SimulationConfigItem {
    private int method;
    private boolean isValidateByTrustObservation;
    private float trustForgottenCoeff;
    private float trustRecommendationCoeff;
    private float trustObservationCoeff;
    private int historyDepthInTrustScoring;
    private float ignoringThresholdOfTrustLevelValue;
    private float maximumConsideredRoutingHelpInTrustMechanism;

    //============================//============================//============================
    public String getInfo() {
        TtTrustMethodology methodology = TtTrustMethodology.getByOrdinal(method);
        if (methodology == TtTrustMethodology.TrustMode_RandomPath || methodology == TtTrustMethodology.TrustMode_ShortPath) {
            return "Method: " + methodology +
                    " | IsUseObservation: " + isValidateByTrustObservation +
                    " | TrustForgottenCoeff: " + trustForgottenCoeff +
                    " | TrustRecommendationCoeff: " + trustRecommendationCoeff +
                    " | TrustObservationCoeff: " + trustObservationCoeff +
                    " | HistoryDepthInTrustScoring: " + historyDepthInTrustScoring +
                    " | IgnoringThresholdOfTrt: " + ignoringThresholdOfTrustLevelValue +
                    " | MaxConsRoutingHelpInTrt: " + maximumConsideredRoutingHelpInTrustMechanism
                    ;
        }
        return "Method: " + methodology;

    }

    public boolean isUseTrustRecommendation() {
        return trustRecommendationCoeff > 0;
    }

    public boolean isUseTrustObservation() {
        return trustObservationCoeff > 0;
    }

    public boolean isUseTrustForgottenCoeff() {
        return trustForgottenCoeff < 1;
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

    public boolean isIsValidateByTrustObservation() {
        return isValidateByTrustObservation;
    }


    public float getTrustForgottenCoeff() {
        return trustForgottenCoeff;
    }


    public float getTrustRecommendationCoeff() {
        return trustRecommendationCoeff;
    }


    public float getTrustObservationCoeff() {
        return trustObservationCoeff;
    }

    public int getHistoryDepthInTrustScoring() {
        return historyDepthInTrustScoring;
    }

    public float getIgnoringThresholdOfTrustLevelValue() {
        return ignoringThresholdOfTrustLevelValue;
    }

    public float getMaximumConsideredRoutingHelpInTrustMechanism() {
        return maximumConsideredRoutingHelpInTrustMechanism;
    }
}
