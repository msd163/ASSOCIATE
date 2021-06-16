package utils.profiler;

import _type.TtTrustMethodology;

public class SimulationConfig {
    private int method;
    private boolean isUseTrustObservation;
    private float trustForgottenCoeff;
    private float trustRecommendationCoeff;

    //============================//============================//============================
    public String getInfo() {
        TtTrustMethodology methodology = TtTrustMethodology.getByOrdinal(method);
        if (methodology == TtTrustMethodology.TrustMode_RandomPath || methodology == TtTrustMethodology.TrustMode_ShortPath) {
            return "Method: " + methodology +
                    ", IsUseObservation: " + isUseTrustObservation +
                    ", TrustForgottenCoeff: " + trustForgottenCoeff +
                    ", TrustRecommendationCoeff: " + trustRecommendationCoeff;
        }
        return "Method: " + methodology;

    }

    public boolean isUseTrustRecommendation() {
        return trustRecommendationCoeff > 0;
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

    public boolean isIsUseTrustObservation() {
        return isUseTrustObservation;
    }

    public void setIsUseTrustObservation(boolean useTrustObservation) {
        isUseTrustObservation = useTrustObservation;
    }

    public float getTrustForgottenCoeff() {
        return trustForgottenCoeff;
    }

    public void setTrustForgottenCoeff(float trustForgottenCoeff) {
        this.trustForgottenCoeff = trustForgottenCoeff;
    }

    public float getTrustRecommendationCoeff() {
        return trustRecommendationCoeff;
    }

    public void setTrustRecommendationCoeff(float trustRecommendationCoeff) {
        this.trustRecommendationCoeff = trustRecommendationCoeff;
    }


}
