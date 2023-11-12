package simulateLayer.config.trust;

public class TrustConfigItemCertification {

    private boolean isUseCertification;
    private boolean isUseDaGra;
    private int certRequestPeriodTime_DaGra;                      // The new certification request is sent in this period times

    private boolean isUseAllDagraAgentToVerify_DaGra;                   // When an agent needs to verify a certification, the agent can use Dagra capabilities in the network
                                                                  // , however the agent has no DagDar itself and don't access any Dagdar directly.
                                                                  // It is for accessing all agents to Dagra's verifications mechanism all over the network
    private int numberOfCertRequestInEachPeriod_DaGra;            // The number of certification request which is sent in each period times
    private int expiredTimeOfCert_DaGra;                          // After this time, the sign that done in a certification will be expired.
    private int numberOfCertToBeSigned_DaGra;                     // The number of certifications which any candidate agent has to sign in order to go to 'Request_Verifying' stage
    private int numberOfCertToBeVerified_DaGra;                   // The number of certifications which any candidate agent has to verify in order to go to 'Accept_New' stage
    private int numberOfNeededSing_DaGra;                         // The number of signs which any candidate agent has to gain in order to go to 'Accept_Verifying' stage
    private int numberOfNeededVerify_DaGra;                       // The number of verification which any candidate agent has to gain in order to go to 'Accept_Accept' stage
    private int numberOfSignTry_DaGra;                            //
    private int numberOfVerificationTry_DaGra;                    //

    //============================//============================//============================

    public String getInfo() {
        if (isUseCertification) {
            if (isUseDaGra) {
                return
                        "     @CERT is: " + true +
                                "  - dag: " + true +
                                "  - alDag: " + isUseAllDagraAgentToVerify_DaGra +
                                "  - prd: " + certRequestPeriodTime_DaGra +
                                "  - req: " + numberOfCertRequestInEachPeriod_DaGra +
                                "  - exp: " + expiredTimeOfCert_DaGra +
                                "  - sig: " + numberOfCertToBeSigned_DaGra +
                                "  - ver: " + numberOfCertToBeVerified_DaGra +
                                "  - nSi: " + numberOfNeededSing_DaGra +
                                "  - nVe: " + numberOfNeededVerify_DaGra +
                                "  - str: " + numberOfSignTry_DaGra +
                                "  - vtr: " + numberOfVerificationTry_DaGra
                        ;
            } else {
                return
                        "   @CERT is: " + true +
                                "  - dag: " + false
                        ;
            }
        }
        return
                "    @CERT is: " + false
                ;
    }

    public boolean isIsUseCertification() {
        return isUseCertification;
    }


    public int getCertRequestPeriodTime_DaGra() {
        return certRequestPeriodTime_DaGra;
    }

    public boolean getIsUseAllDagraAgentToVerify_DaGra() {
        return isUseAllDagraAgentToVerify_DaGra;
    }

    public int getNumberOfCertRequestInEachPeriod_DaGra() {
        return numberOfCertRequestInEachPeriod_DaGra;
    }

    public int getExpiredTimeOfCert_DaGra() {
        return expiredTimeOfCert_DaGra;
    }

    public int getNumberOfCertToBeSigned_DaGra() {
        return numberOfCertToBeSigned_DaGra;
    }

    public int getNumberOfCertToBeVerified_DaGra() {
        return numberOfCertToBeVerified_DaGra;
    }

    public int getNumberOfNeededSing_DaGra() {
        return numberOfNeededSing_DaGra;
    }

    public int getNumberOfNeededVerify_DaGra() {
        return numberOfNeededVerify_DaGra;
    }

    public boolean isIsUseDaGra() {
        return isUseDaGra;
    }

    public int getNumberOfSignTry_DaGra() {
        return numberOfSignTry_DaGra;
    }

    public int getNumberOfVerificationTry_DaGra() {
        return numberOfVerificationTry_DaGra;
    }
}
