package trustLayer.consensus;

import _type.TtDaGraContractStatus;
import systemLayer.Agent;
import utils.Config;
import utils.Globals;

import java.util.ArrayList;
import java.util.List;

public class CertContract {

    public CertContract() {
        signs = new ArrayList<>();
        signedContracts = new ArrayList<>();
        verifies = new ArrayList<>();
        verifiedContracts = new ArrayList<>();
        id = Globals.CONTRACT_NEXT_ID++;
    }

    private final long id;

    /* Whether this certification is genesis Node in DAG? */
    private boolean isGenesis;

    private Agent requester;

    private int requestTime;

    /* Received signs from others to this contract */
    private List<CertSign> signs;

    /* Other contracts that is signed by the agent of this contract */
    private List<CertSign> signedContracts;

    /* Received verifies from others to this contract */
    private List<CertVerify> verifies;

    /* Other contracts that is signed by the agent of this contract */
    private List<CertVerify> verifiedContracts;


    /* previous expired certification of the requester */
    private CertContract previousCertification;

    private TtDaGraContractStatus status;
    //============================//============================//============================

    public TtDaGraContractStatus updateStatus() {

        if(isGenesis){
            status = TtDaGraContractStatus.Accept_Accept;
            return status;
        }

        //============================//============================ Request Stage: Checking 'New' and 'Signing' statuses
        int signedContractCount = 0;
        for (CertSign signedContract : signedContracts) {
            if (signedContract.isValid()) {
                signedContractCount++;
            }
        }

        if (signedContractCount == 0) {
            status = TtDaGraContractStatus.Request_New;
            return status;
        }

        if (signedContractCount < Config.DAGRA_REQUEST_STAGE__CERTIFICATIONS_TO_BE_SIGNED) {
            status = TtDaGraContractStatus.Request_Signing;
            return status;
        }

        //============================//============================ Request Stage: Checking 'Verifying' status
        int verifiedContractCount = verifiedContracts.size();

        if (verifiedContractCount < Config.DAGRA_REQUEST_STAGE__CERTIFICATIONS_TO_BE_VERIFIED) {
            status = TtDaGraContractStatus.Request_Verifying;
            return status;
        }

        //============================//============================ Accept Stage: Checking 'New' and 'Signing' statuses
        int notExpiredValidSignCount = 0;
        int validSignCount = 0;

        for (CertSign sign : signs) {
            if (sign.isValid()) {
                validSignCount++;
                if (Globals.WORLD_TIMER - sign.getTime() < Config.DAGRA_ACCEPT_STAGE__EXPIRE_TIME_THRESHOLD /*&& sign.getTrustValue() > 0*/) {
                    notExpiredValidSignCount++;
                }
            }
        }

        if (validSignCount == 0) {
            status = TtDaGraContractStatus.Accept_New;
            return status;
        }
        if (validSignCount < Config.DAGRA_ACCEPT_STAGE__NEEDED_SIGNS) {
            status = TtDaGraContractStatus.Accept_Signing;
            return status;
        }

        //============================//============================ Accept Stage: Checking 'Verifying' status
        int verifiesCount = verifies.size();

        if (verifiesCount < Config.DAGRA_ACCEPT_STAGE__NEEDED_VERIFICATIONS) {
            status = TtDaGraContractStatus.Accept_Verifying;
            return status;
        }

        //============================//============================ Accept Stage: Checking 'Accept' and 'Expired' statuses
        status = notExpiredValidSignCount >= Config.DAGRA_ACCEPT_STAGE__NEEDED_SIGNS
                ? TtDaGraContractStatus.Accept_Accept
                : TtDaGraContractStatus.Expired;

        return status;
    }


    //============================//============================//============================

    public Agent getRequester() {
        return requester;
    }


    public void setRequester(Agent requester) {
        this.requester = requester;
    }

    public int getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(int requestTime) {
        this.requestTime = requestTime;
    }

    public List<CertSign> getSigns() {
        return signs;
    }

    public void setSigns(List<CertSign> signs) {
        this.signs = signs;
    }

    public List<CertSign> getSignedContracts() {
        return signedContracts;
    }

    public void setSignedContracts(List<CertSign> signedContracts) {
        this.signedContracts = signedContracts;
    }

    public boolean isIsGenesis() {
        return isGenesis;
    }

    public void setIsGenesis(boolean genesis) {
        isGenesis = genesis;
    }

    public CertContract getPreviousCertification() {
        return previousCertification;
    }

    public void setPreviousCertification(CertContract previousCertification) {
        this.previousCertification = previousCertification;
    }

    public long getId() {
        return id;
    }

    public TtDaGraContractStatus getStatus() {
        return status;
    }

    public void setStatus(TtDaGraContractStatus status) {
        this.status = status;
    }
}
