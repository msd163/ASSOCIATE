package trustLayer.consensus;

import _type.TtDaGraContractStatus;
import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import systemLayer.Agent;
import systemLayer.World;
import utils.Globals;
import utils.OutLog____;

import java.util.ArrayList;
import java.util.List;

public class DaGra {

    public DaGra(Agent owner) {
        this.owner = owner;
        this.world = owner.getWorld();
        contracts = new ArrayList<>();
        toBeVerifiedContracts = new ArrayList<>();
        toBeSignedContracts = new ArrayList<>();
        id = Globals.DAGRA_NEXT_ID++;
    }

    private int id;

    private CertContract genesis;

    private List<CertContract> contracts;

    private CertContract my;

    private Agent owner;

    private World world;

    private List<CertContract> toBeVerifiedContracts;

    private List<CertContract> toBeSignedContracts;

    //============================//============================//============================

    public boolean addRequest(CertContract contract) {

        if (contract == null || contract.getRequester() == null) {
            return false;
        }

        // if added previously return
        if (findFirstByContract(contract) != null) {
            return false;
        }

        contract.setRequestTime(Globals.WORLD_TIMER);

        contract.setDagraId(id);

        contracts.add(contract);

        return true;
    }

    public CertContract findFirstByRequester(Agent agent) {
        for (CertContract contract : contracts) {
            if (contract.getRequester() != null && contract.getRequester().getId() == agent.getId()) {
                return contract;
            }
        }
        return null;
    }

    public CertContract findLastByRequester(Agent agent) {
        CertContract cont = null;
        for (CertContract contract : contracts) {
            if (contract.getRequester() != null && contract.getRequester().getId() == agent.getId()) {
                cont = contract;
            }
        }
        return cont;
    }

    public CertContract findFirstByRequester(CertContract inContract) {
        for (CertContract contract : contracts) {
            if (contract.getRequester() != null && contract.getRequester().getId() == inContract.getRequester().getId()) {
                return contract;
            }
        }
        return null;
    }

    public CertContract findFirstByContract(CertContract inContract) {
        for (CertContract contract : contracts) {
            if (contract.getId() == inContract.getId()) {
                return contract;
            }
        }
        return null;
    }

    //============================//============================


    public boolean isValidCertificationFor(Agent toBeVerified) {
        for (CertContract contract : contracts) {
            if (!contract.isIsGenesis() && contract.getRequester().getId() == toBeVerified.getId()) {
                return contract.getStatus() == TtDaGraContractStatus.Accept_Accept;
            }
        }
        return false;
    }

    //============================//============================
    public void sendRegisterRequest() {
        CertContract contract = new CertContract(-1);
        contract.setRequester(owner);
        contract.setRequestTime(Globals.WORLD_TIMER);
        contract.setPreviousCertification(findLastByRequester(owner));
        if (!addRequest(contract)) {
            OutLog____.pl(TtOutLogMethodSection.DaGra, TtOutLogStatus.ERROR, "Can not add contract request to DaGra. agentId: " + owner.getId());
            return;
        }
        my = contract;
        for (Agent agent : world.getAgents()) {
            if (agent.getDaGra() != null && agent.getId() != owner.getId()) {
                agent.getDaGra().addRequest(contract.clone());
            }
        }
    }

    //============================//============================//============================

    /**
     * Main method of DaGra
     */
    public void process() {
        // updating the status of all contracts in this DaGra and list
        toBeSignedContracts.clear();
        toBeVerifiedContracts.clear();

        for (CertContract contract : contracts) {
            TtDaGraContractStatus status = contract.updateStatus(world.getSimulationConfig());
            if (status == TtDaGraContractStatus.Accept_New || status == TtDaGraContractStatus.Accept_Signing) {
                toBeSignedContracts.add(contract);
            } else if (status == TtDaGraContractStatus.Accept_Verifying) {
                toBeVerifiedContracts.add(contract);
            }
        }

        // Do work
        TtDaGraContractStatus status = my == null ? TtDaGraContractStatus.NoContract : my.getStatus();
        switch (status) {
            case NoContract:
            case Expired:
                if ((Globals.WORLD_TIMER + 1) % world.getSimulationConfig().getCert().getCertRequestPeriodTime_DaGra() == 0) {
                    if (Globals.DAGRA_REQUEST_STAGE__REQUESTED_COUNT_IN_CURRENT_PERIOD < world.getSimulationConfig().getCert().getNumberOfCertRequestInEachPeriod_DaGra()) {
                        sendRegisterRequest();
                        Globals.DAGRA_REQUEST_STAGE__REQUESTED_COUNT_IN_CURRENT_PERIOD++;
                    }
                }
                break;
            case Request_New:
            case Request_Signing:
                processSigning();
                break;
            case Request_Verifying:
                processVerifying();
                break;
            case Accept_New:
            case Accept_Signing:
            case Accept_Verifying:
                OutLog____.pl(TtOutLogMethodSection.Main, TtOutLogStatus.WARN, "Has Certification in ACCEPTING process. stage: " + status + " | agentId: " + owner.getId());
                break;
            case Accept_Accept:
                OutLog____.pl(TtOutLogMethodSection.Main, TtOutLogStatus.SUCCESS, "Has ACCEPTED Certification. agentId: " + owner.getId());
                break;
        }
    }

    private boolean performSign(CertContract toBeSignedContract, float trustValue) {
        CertSign sign = new CertSign(-1);
        sign.setSigner(my);
        sign.setSigned(toBeSignedContract);
        sign.setTime(Globals.WORLD_TIMER);
        sign.setTrustValue(trustValue);
        sign.doSign();


        /* Adding sign to Signed contract*/
        toBeSignedContract.getSigns().add(sign);
        /* Adding sign to its contract */
        my.getSignedContracts().add(sign);

        broadcastSign(sign);

        return true;
    }

    /**
     * Broadcasting sing to network
     *
     * @param sign
     */
    private void broadcastSign(CertSign sign) {
        for (Agent agent : world.getAgents()) {
            if (agent.getDaGra() != null && agent.getId() != owner.getId()) {
                agent.getDaGra().addSign(sign.clone());
            }
        }
    }

    private void broadcastVerify(CertVerify certVerify) {
        for (Agent agent : world.getAgents()) {
            if (agent.getDaGra() != null && agent.getId() != owner.getId()) {
                agent.getDaGra().addVerify(certVerify.clone());
            }
        }
    }

    private void addVerify(CertVerify verify) {

        /* Finding verifier contract and verified contract in current DaGra */
        CertContract localVerifier = findContractById(verify.getVerifier().getId());
        CertContract localVerified = findContractById(verify.getVerified().getId());

        /* If can not find related verifier or verified contract, there is and error in DaGra */
        if (localVerified == null || localVerifier == null) {
            OutLog____.pl(TtOutLogMethodSection.DaGra, TtOutLogStatus.ERROR, "null contract in certSign: localVerifier" + (localVerifier == null ? "NULL" : localVerifier.getId()) + "  localVerified: " + (localVerified == null ? "NULL" : localVerified.getId()));
            return;
        }
        CertVerify prevVerify = null;

        //============================//============================ Adding Verified 
        /* Checking whether previously added to Signs */
        for (CertVerify cv : localVerified.getVerifies()) {
            if (cv.getId() == verify.getId()) {
                prevVerify = cv;
                break;
            }
        }

        //============================//============================ Adding Verifier
        /* Checking whether previously added to Signs */
        for (CertVerify cv : localVerifier.getVerifiedContracts()) {
            if (cv.getId() == verify.getId()) {
                if (prevVerify != null) {
                    /* If the sign of verified and the sign of verifier is not equals, there is an error  */
                    if (!cv.equals(prevVerify)) {
                        OutLog____.pl(TtOutLogMethodSection.DaGra, TtOutLogStatus.ERROR, "Conflict in previously added verify in process of adding verify to other dags ");
                        return;
                    }
                } else {
                    prevVerify = cv;
                }
                break;
            }
        }

        if (prevVerify == null) {
            prevVerify = verify.clone();
            prevVerify.setVerified(localVerified);
            prevVerify.setVerifier(localVerifier);
        }

        localVerified.getVerifies().add(prevVerify);
        localVerifier.getVerifiedContracts().add(prevVerify);

    }

    /**
     * Adding the received sign from the network to the signer and signed contracts
     *
     * @param sign is the received sign from the network
     */
    private void addSign(CertSign sign) {

        /* Finding signer contract and signed contract in current DaGra */
        CertContract localSigner = findContractById(sign.getSigner().getId());
        CertContract localSigned = findContractById(sign.getSigned().getId());

        /* If can not find related signer or signed contract, there is and error in DaGra */
        if (localSigned == null || localSigner == null) {
            OutLog____.pl(TtOutLogMethodSection.DaGra, TtOutLogStatus.ERROR, "null contract in certSign: localSigner" + (localSigner == null ? "NULL" : localSigner.getId()) + "  localSigned: " + (localSigned == null ? "NULL" : localSigned.getId()));
            return;
        }
        CertSign prevSign = null;

        //============================//============================ Adding Signed 
        /* Checking whether previously added to Signs */
        for (CertSign cs : localSigned.getSigns()) {
            if (cs.getId() == sign.getId()) {
                prevSign = cs;
                break;
            }
        }

        //============================//============================ Adding Signer
        /* Checking whether previously added to Signs */
        for (CertSign cs : localSigner.getSignedContracts()) {
            if (cs.getId() == sign.getId()) {
                if (prevSign != null) {
                    /* If the sign of signed and the sign of signer is not equals, there is an error  */
                    if (!cs.equals(prevSign)) {
                        OutLog____.pl(TtOutLogMethodSection.DaGra, TtOutLogStatus.ERROR, "Conflict in previously added sign in process of adding sign to other dags ");
                        return;
                    }
                } else {
                    prevSign = cs;
                }
                break;
            }
        }

        if (prevSign == null) {
            prevSign = sign.clone();
            prevSign.setSigned(localSigned);
            prevSign.setSigner(localSigner);
        }

        localSigned.getSigns().add(prevSign);
        localSigner.getSignedContracts().add(prevSign);

    }

    private CertContract findContractById(int id) {
        for (CertContract contract : contracts) {
            if (contract.getId() == id) {
                return contract;
            }
        }
        return null;
    }

    public boolean processSigning() {

        if (my == null) {
            return false;
        }

        /* If there is no contract to be signed, the Genesis contract will be signed */
        if (toBeSignedContracts.size() == 0) {
            return performSign(genesis, 1.0f);
        }

        boolean isSignedPreviously;

        for (int i = 0; i < 10; i++) {

            /* Selecting a contract randomly */
            int selectedIndex = Globals.RANDOM.nextInt(toBeSignedContracts.size());
            CertContract contract = toBeSignedContracts.get(selectedIndex);

            /* For preventing self signing */
            if (contract.getId() == my.getId()) {
                continue;
            }

            /* For checking if selected contract is signed previously via this contract */
            isSignedPreviously = false;
            for (CertSign sign : my.getSignedContracts()) {
                if (sign.getSigned().getId() == contract.getId()) {
                    isSignedPreviously = true;
                    break;
                }
            }
            /* For preventing double signing */
            if (isSignedPreviously) {
                continue;
            }

            float trustValue = world.getTrustManager().getTrustValue(owner, contract.getRequester());
            if (trustValue != 0.0f) {
                return performSign(contract, trustValue);
            }

        }

        return false;
    }

    public boolean processVerifying() {

        if (my == null) {
            return false;
        }

        if (my.getStatus() == TtDaGraContractStatus.Request_Signing) {
            OutLog____.pl("sss");
        }
        /* If there is no contract to be verified, return false */
        if (toBeVerifiedContracts.size() == 0) {
            return performVerify(genesis);
        }

        boolean isVerifiedPreviously;

        for (int i = 0; i < 10; i++) {

            /* Selecting a contract randomly */
            int selectedIndex = Globals.RANDOM.nextInt(toBeVerifiedContracts.size());
            CertContract contract = toBeVerifiedContracts.get(selectedIndex);

            /* 1- For preventing self verifying */
            if (contract.getId() == my.getId()) {
                continue;
            }

            /* 2-  For checking if selected contract is verified previously via this contract */
            isVerifiedPreviously = false;
            for (CertVerify verify : my.getVerifiedContracts()) {
                if (verify.getVerifier().getId() == contract.getId()) {
                    isVerifiedPreviously = true;
                    break;
                }
            }
            /* For preventing double signing */
            if (isVerifiedPreviously) {
                continue;
            }

            /* 3- For preventing verifying the signed contract by itself */
            boolean isItsOwn = false;
            for (CertSign sign : contract.getSigns()) {
                if (sign.getSigner().getId() == my.getId()) {
                    isItsOwn = true;
                    break;
                }
            }
            if (isItsOwn) {
                continue;
            }

            /* 4- Checking sings of the contract do not expired */
            boolean isExpiredOrInvalid = false;
            for (CertSign sign : contract.getSigns()) {
                if (sign.isExpired(world.getSimulationConfig().getCert().getExpiredTimeOfCert_DaGra()) || !sign.isValid()) {
                    isExpiredOrInvalid = true;
                    break;
                }
            }
            if (isExpiredOrInvalid) {
                continue;
            }

            /* 5- There is no signer with negative trust */
            boolean isSignWithNegativeVal = false;
            for (CertSign sign : contract.getSigns()) {
                float trustValue = world.getTrustManager().getTrustValue(owner, sign.getSigner().getRequester());
                if (trustValue < 0) {
                    isSignWithNegativeVal = true;
                    break;
                }
            }
            if (isSignWithNegativeVal) {
                continue;
            }

            /* 6- There is no cycle in the signed graphs */
            List<CertContract> openList = new ArrayList<>();
            openList.add(contract);
            boolean isFindCycle;
            isFindCycle = false;
            while (!openList.isEmpty() && !isFindCycle) {
                CertContract cnt = openList.remove(0);
                for (CertSign sign : cnt.getSigns()) {
                    if (sign.getSigner().getId() == my.getId()) {
                        isFindCycle = true;
                        break;
                    }
                    if (sign.isValid() && !sign.isExpired(world.getSimulationConfig().getCert().getExpiredTimeOfCert_DaGra())) {
                        openList.add(sign.getSigner());
                    }
                }
            }
            if (isFindCycle) {
                continue;
            }

            return performVerify(contract);
        }

        return false;
    }

    private boolean performVerify(CertContract contract) {
        CertVerify certVerify = new CertVerify(-1);
        certVerify.setTime(Globals.WORLD_TIMER);
        certVerify.setVerifier(my);
        certVerify.setVerified(contract);
        certVerify.setResult(true);

        my.getVerifiedContracts().add(certVerify);
        contract.getVerifies().add(certVerify);

        broadcastVerify(certVerify);

        return true;
    }

    //============================//============================//============================

    public CertContract getGenesis() {
        return genesis;
    }

    public void setGenesis(CertContract genesis) {
        this.genesis = genesis;
        this.genesis.setDagraId(id);
        contracts.add(genesis);
    }

    public List<CertContract> getContracts() {
        return contracts;
    }

    public void setContracts(List<CertContract> contracts) {
        this.contracts = contracts;
    }

    public List<CertContract> getToBeSignedContracts() {
        return toBeSignedContracts;
    }

    public void setToBeSignedContracts(List<CertContract> toBeSignedContracts) {
        this.toBeSignedContracts = toBeSignedContracts;
    }

    public List<CertContract> getToBeVerifiedContracts() {
        return toBeVerifiedContracts;
    }

    public void setToBeVerifiedContracts(List<CertContract> toBeVerifiedContracts) {
        this.toBeVerifiedContracts = toBeVerifiedContracts;
    }

    public CertContract getMy() {
        return my;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
