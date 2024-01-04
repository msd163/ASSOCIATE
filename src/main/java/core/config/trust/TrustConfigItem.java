package core.config.trust;

import _type.TtTrustFormula;
import _type.TtTrustMethodology;

public class TrustConfigItem {
    private int method;
    private int trustFormula;
    private float trustFormula2MaclaurinAlpha;
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
    private boolean isShareObservationWithNeighbors;
    private int experienceDepthInScoring;
    private float ignoringThresholdOfTrustValue;
    private float maximumConsideredRoutingHelpInTrustMechanism;
    private boolean isUseSharingRecommendationWithInternet;
    private int sharingRecommendationWithInternetPeriod;
    private TrustConfigItemCertification cert;

    //============================//============================//============================
    public String getInfo(int certificationCount) {
        TtTrustMethodology methodology = TtTrustMethodology.getByOrdinal(method);
        if (methodology == TtTrustMethodology.TrustMode_RandomPath || methodology == TtTrustMethodology.TrustMode_ShortPath) {
            TtTrustFormula trustFormula = TtTrustFormula.getByOrdinal(this.trustFormula);
            return "@Mtd: " + methodology +
                    " @ TrFrml: " + trustFormula +
                    (trustFormula == TtTrustFormula.Formula_2_Maclaurin ?
                            " (" + trustFormula2MaclaurinAlpha + ")"
                            : "") +
                    " @ FrgCf: " + trustForgottenCoeff +
                    " - IgrThrld: " + ignoringThresholdOfTrustValue +
                    " - MaxRtHp: " + maximumConsideredRoutingHelpInTrustMechanism +
                    " @EXP is: " + isUseExperience +
                    " - ind: " + isUseIndirectExperience +
                    " - bid: " + isBidirectionalExperienceSharing +
                    " - dpth: " + experienceDepthInScoring +
                    " @OBS is: " + isUseObservation +
                    " - ind: " + isUseIndirectObservation +
                    " - bid: " + isBidirectionalObservationSharing +
                    " - shr: " + isShareObservationWithNeighbors +
                    " @RCM cf: " + recommendationCoeff +
                    " - bid: " + isBidirectionalRecommendationSharing +
                    " - sfm: " + isSafeUseRecommendation +
                    " - ngEff: " + isUseNegativeRecommendationEffect +
                    " @CERT is: " + cert.isIsUseCertification() +
                    " @INT is: " + isUseSharingRecommendationWithInternet + "[" + sharingRecommendationWithInternetPeriod + "]" +
                    " - cnt: " + certificationCount
                    ;

        }
        return "Method: " + methodology;

    }

    public String getInfo_1() {
        TtTrustMethodology methodology = TtTrustMethodology.getByOrdinal(method);
        if (methodology == TtTrustMethodology.TrustMode_RandomPath || methodology == TtTrustMethodology.TrustMode_ShortPath) {
            TtTrustFormula trustFormula = TtTrustFormula.getByOrdinal(this.trustFormula);
            return "Method: " + methodology +
                    " @ TrFrml: " + trustFormula +
                    (trustFormula == TtTrustFormula.Formula_2_Maclaurin ?
                            " (" + trustFormula2MaclaurinAlpha + ")"
                            : "") +
                    "  @ FrgCf: " + trustForgottenCoeff +
                    " - IgrThrld: " + ignoringThresholdOfTrustValue +
                    " - MaxRtHp: " + maximumConsideredRoutingHelpInTrustMechanism
                    ;
        }
        return "Method: " + methodology;

    }

    public String getInfo_2() {
        TtTrustMethodology methodology = TtTrustMethodology.getByOrdinal(method);
        if (methodology == TtTrustMethodology.TrustMode_RandomPath || methodology == TtTrustMethodology.TrustMode_ShortPath) {
            return
                    "         @EXP is: " + isUseExperience +
                            " - ind: " + isUseIndirectExperience +
                            " - bid: " + isBidirectionalExperienceSharing +
                            " - dpth: " + experienceDepthInScoring +
                            "  @OBS is: " + isUseObservation +
                            " - ind: " + isUseIndirectObservation +
                            " - bid: " + isBidirectionalObservationSharing+
                            " - shr: " + isShareObservationWithNeighbors
                    ;
        }
        return "";

    }

    public String getInfo_3(int certificationCount) {
        TtTrustMethodology methodology = TtTrustMethodology.getByOrdinal(method);
        if (methodology == TtTrustMethodology.TrustMode_RandomPath || methodology == TtTrustMethodology.TrustMode_ShortPath) {
            return
                    "         @RCM cf: " + recommendationCoeff +
                            " - bid: " + isBidirectionalRecommendationSharing +
                            " - sfm: " + isSafeUseRecommendation +
                            " - ngEff: " + isUseNegativeRecommendationEffect +
                            "  @INT is: " + isUseSharingRecommendationWithInternet + "[" + sharingRecommendationWithInternetPeriod + "]" +
                            " - cnt: " + certificationCount
                    ;
        }
        return "";
    }

    public String getInfo_4() {
        return cert.getInfo();
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

    public int getTrustFormula() {
        return trustFormula;
    }

    public float getTrustForgottenCoeff() {
        return trustForgottenCoeff;
    }

    public TtTrustFormula getTtTrustFormula() {
        return TtTrustFormula.getByOrdinal(trustFormula);
    }

    public float getTrustFormula2MaclaurinAlpha() {
        return trustFormula2MaclaurinAlpha;
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

    public int getExperienceDepthInScoring() {
        return experienceDepthInScoring;
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

    public boolean isIsShareObservationWithNeighbors() {
        return isShareObservationWithNeighbors;
    }

    public boolean isIsUseSharingRecommendationWithInternet() {
        return isUseSharingRecommendationWithInternet;
    }

    public int getSharingRecommendationWithInternetPeriod() {
        return sharingRecommendationWithInternetPeriod;
    }

    public TrustConfigItemCertification getCert() {
        return cert;
    }
}
