package WSM.trust.consensus;

import _type.TtDaGraContractStatus;
import SiM.profiler.config.TrustConfigItem;
import WSM.society.agent.Agent;
import core.utils.Globals;

import java.util.ArrayList;
import java.util.List;

public class CertContract {

    public CertContract(int id, int expireTimeRange) {
        signs = new ArrayList<>();
        signedContracts = new ArrayList<>();
        verifies = new ArrayList<>();
        verifiedContracts = new ArrayList<>();
        if (id == -1) {
            this.id = Globals.DAGRA_CONTRACT_NEXT_ID++;
        } else {
            this.id = id;
        }
        acceptTime = -1;
        lastUpdateTime = Globals.WORLD_TIMER;
        this.expireTimeRange = expireTimeRange;
        this.status = TtDaGraContractStatus.NoContract;
        this.isOldExpired = false;
    }

    //=======================================================
    int drawX;
    int drawY;
    //=======================================================
    private int dagraId;

    private final int id;

    /* Whether this certification is genesis Node in DAG? */
    private boolean isGenesis;

    private Agent requester;

    private int requestTime;

    /* The time that this certification status is Accept_Accept */
    private int acceptTime;

    /* when the state of the contract is changed, this time will be updated by the current time */
    private int lastUpdateTime;
    /* The time range that the certification will be expired  */
    private int expireTimeRange;


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

    private float finalTrustValue;

    private boolean isOldExpired;
    //============================//============================//============================

    public boolean isExpired() {
        if (status == TtDaGraContractStatus.Accept_Accept)
            return (Globals.WORLD_TIMER - lastUpdateTime) >= expireTimeRange;
        else
            return ( status ==  TtDaGraContractStatus.Expired) || (Globals.WORLD_TIMER - lastUpdateTime) >= (expireTimeRange * 4);
    }


    public TtDaGraContractStatus updateStatus(TrustConfigItem simulationConfig) {
        TtDaGraContractStatus prevStat = status;
        if (isGenesis) {
            status = TtDaGraContractStatus.Accept_Accept;
            return status;
        }

        /* Checking expiring of contract */
        if (isExpired()) {
            status = TtDaGraContractStatus.Expired;
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
            lastUpdateTime = (status == prevStat) ? lastUpdateTime : Globals.WORLD_TIMER;
            return status;
        }

        if (signedContractCount < simulationConfig.getCert().getNumberOfCertToBeSigned_DaGra()) {
            status = TtDaGraContractStatus.Request_Signing;
            lastUpdateTime = (status == prevStat) ? lastUpdateTime : Globals.WORLD_TIMER;
            return status;
        }

        //============================//============================ Request Stage: Checking 'Verifying' status
        int verifiedContractCount = verifiedContracts.size();

        if (verifiedContractCount < simulationConfig.getCert().getNumberOfCertToBeVerified_DaGra()) {
            status = TtDaGraContractStatus.Request_Verifying;
            lastUpdateTime = (status == prevStat) ? lastUpdateTime : Globals.WORLD_TIMER;
            return status;
        }

        //============================//============================ Accept Stage: Checking 'New' and 'Signing' statuses
//        int notExpiredValidSignCount = 0;
        int validSignCount = 0;
        finalTrustValue = 0;
        for (CertSign sign : signs) {
            if (sign.isValid()) {
                validSignCount++;
                /*  if (!sign.isExpired(simulationConfig.getCert().getExpiredTimeOfCert_DaGra()) *//*&& sign.getTrustValue() > 0*//*) {
                    notExpiredValidSignCount++;
                }*/
                finalTrustValue += sign.getTrustValue();
            }
        }

        if (validSignCount == 0) {
            status = TtDaGraContractStatus.Accept_New;
            lastUpdateTime = (status == prevStat) ? lastUpdateTime : Globals.WORLD_TIMER;
            return status;
        }
        /* Calculating finalTrustValue */
        finalTrustValue /= validSignCount;

        if (validSignCount < simulationConfig.getCert().getNumberOfNeededSing_DaGra()) {
            status = TtDaGraContractStatus.Accept_Signing;
            lastUpdateTime = (status == prevStat) ? lastUpdateTime : Globals.WORLD_TIMER;
            return status;
        }

        //============================//============================ Accept Stage: Checking 'Verifying' status
        int verifiesCount = verifies.size();

        if (verifiesCount < simulationConfig.getCert().getNumberOfNeededVerify_DaGra()) {
            status = TtDaGraContractStatus.Accept_Verifying;
            lastUpdateTime = (status == prevStat) ? lastUpdateTime : Globals.WORLD_TIMER;
            return status;
        }

        //============================//============================ Accept Stage: Checking 'Accept' status
        status = TtDaGraContractStatus.Accept_Accept;
        lastUpdateTime = (status == prevStat) ? lastUpdateTime : Globals.WORLD_TIMER;
        return status;
    }

    public CertContract clone() {
        CertContract obj = new CertContract(this.id, expireTimeRange);
        obj.setDagraId(getDagraId());
        obj.setRequester(requester);
        obj.setStatus(status);
        obj.setRequestTime(requestTime);
        obj.setIsGenesis(isGenesis);
        obj.setPreviousCertification(previousCertification);
        obj.setExpireTimeRange(expireTimeRange);
        obj.setFinalTrustValue(finalTrustValue);
        obj.setAcceptTime(acceptTime);
     /*   obj.setVerifies();
        obj.setVerifiedContracts();

        obj.setSigns();
        obj.setSignedContracts();*/

        return obj;
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

    public int getId() {
        return id;
    }

    public TtDaGraContractStatus getStatus() {
        return status;
    }

    public void setStatus(TtDaGraContractStatus status) {
        this.status = status;
    }

    public List<CertVerify> getVerifies() {
        return verifies;
    }

    public void setVerifies(List<CertVerify> verifies) {
        this.verifies = verifies;
    }

    public List<CertVerify> getVerifiedContracts() {
        return verifiedContracts;
    }

    public void setVerifiedContracts(List<CertVerify> verifiedContracts) {
        this.verifiedContracts = verifiedContracts;
    }

    public int getDagraId() {
        return dagraId;
    }

    public void setDagraId(int dagraId) {
        this.dagraId = dagraId;
    }

    public float getFinalTrustValue() {
        return finalTrustValue;
    }

    public int getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(int acceptTime) {
        this.acceptTime = acceptTime;
    }

    public int getExpireTimeRange() {
        return expireTimeRange;
    }

    public void setExpireTimeRange(int expireTimeRange) {
        this.expireTimeRange = expireTimeRange;
    }

    public boolean isIsOldExpired() {
        return isOldExpired;
    }

    public void setIsOldExpired(boolean oldExpired) {
        isOldExpired = oldExpired;
    }

    public void setFinalTrustValue(float finalTrustValue) {
        this.finalTrustValue = finalTrustValue;
    }

    public int getDrawX() {
        return drawX;
    }

    public void setDrawX(int drawX) {
        this.drawX = drawX;
    }

    public int getDrawY() {
        return drawY;
    }

    public void setDrawY(int drawY) {
        this.drawY = drawY;
    }

    public void destroy() {

        if (this.signedContracts != null) {
            List<CertSign> contracts = this.signedContracts;
            for (int i = 0; i < contracts.size(); i++) {
                contracts.set(i, null);
            }
            this.signedContracts.clear();
            this.signedContracts = null;
        }
        if (this.verifiedContracts != null) {
            for (int i = 0; i < this.verifiedContracts.size(); i++) {
                this.verifiedContracts.set(i, null);
            }
            this.verifiedContracts.clear();
            this.verifiedContracts = null;
        }

        if (this.signs != null) {
            for (int i = 0; i < this.signs.size(); i++) {
                this.signs.set(i, null);
            }
            this.signs.clear();
            this.signs = null;
        }

        if (this.verifies != null) {
            for (int i = 0; i < this.verifies.size(); i++) {
                this.verifies.set(i, null);
            }
            this.verifies.clear();
            this.verifies = null;
        }
    }
}
