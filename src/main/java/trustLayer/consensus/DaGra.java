package trustLayer.consensus;

import _type.TtBehaviorState;
import _type.TtDaGraContractStatus;
import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import societyLayer.agentSubLayer.Agent;
import societyLayer.agentSubLayer.World;
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


    public Float getValidCertificationTrustValue(Agent toBeVerified) {
        for (CertContract contract : contracts) {
            if (!contract.isIsGenesis() && contract.getRequester().getId() == toBeVerified.getId()) {
                if (contract.getStatus() == TtDaGraContractStatus.Accept_Accept) {
                    return contract.getFinalTrustValue();
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    //============================//============================
    public void sendRegisterRequest() {
        CertContract contract = new CertContract(-1, world.getSimulationConfig().getCert().getExpiredTimeOfCert_DaGra());
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

    //todo: this function has error and does not work correctly
    @Deprecated
    public void updatingStatusAndList(DaGra daGra) {

      /*  toBeSignedContracts.clear();
        toBeVerifiedContracts.clear();
        toBeSignedContracts.addAll(daGra.getToBeSignedContracts());
        toBeVerifiedContracts.addAll(daGra.getToBeVerifiedContracts());*/

        // for operating correctly in the function, now, we have used the main function instead of optimized one.
        updatingStatusAndList();
    }

    public void updatingStatusAndList() {
        // Updating the status of all contracts in this DaGra and list
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

    }

    public boolean isHasRegisterRequest() {
        return my == null || my.getStatus() == TtDaGraContractStatus.Expired;
    }

    /**
     * Main method of DaGra
     */
    public void process() {

        // Do work
        TtDaGraContractStatus status = my == null ? TtDaGraContractStatus.NoContract : my.getStatus();
        switch (status) {
            case NoContract:
            case Expired:
                // For these statues, the DaGra process will be done in the World main loop.
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
                    /* If both, the sign of verified and verifier is not equals, there is an error  */
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
        localVerified.setAcceptTime(prevVerify.getTime());
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
                    /* If the sign of signed, and the sign of signer is not equals, there is an error  */
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
            if (Globals.WORLD_TIMER < world.getSimulationConfig().getCert().getExpiredTimeOfCert_DaGra() * 5) {
                OutLog____.pl("$$> performSign(genesis, 1.0f) by " + owner.getId());
                return performSign(genesis, 1.0f);
            }
            return false;
        }

        boolean isSignedPreviously;

        for (int i = 0; i < world.getSimulationConfig().getCert().getNumberOfSignTry_DaGra(); i++) {

            /* Selecting a contract randomly */
            int selectedIndex = Globals.RANDOM.nextInt(toBeSignedContracts.size());
            CertContract contract = toBeSignedContracts.get(selectedIndex);

            /* HONEST BEHAVES */
            if (owner.getBehavior().getCurrentBehaviorState() == TtBehaviorState.Honest) {
                /* HONEST BEHAVES: For preventing self signing */
                if (contract.getId() == my.getId()) {
                    continue;
                }
            }

            /* It is for checking whether the selected contract signed previously via this contract */
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

            /* 1-1 For preventing signing the contracts requested after this contract (MY) */
            if (contract.getRequestTime() >= my.getRequestTime()) {
                continue;
            }

            float trustValue = world.getTrustManager().getTrustValue(owner, contract.getRequester());


            /* NOT MISCHIEF BEHAVES */
            if (owner.getBehavior().getCurrentBehaviorState() != TtBehaviorState.Mischief) {
                // NOT MISCHIEF BEHAVING: Checking the existence of trust history for the target contract.
                if (trustValue != 0.0f) {

                    //HONEST BEHAVES: There is no cycle in the signed graphs
                    //todo: now, only the cycles that are created in presence of this contract (my) is checked. here, all kind of cycles have to be checked and identified.
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
                            if (sign.isValid()) {
                                openList.add(sign.getSigner());
                            }
                        }
                    }
                    // There is a cycle, continue.
                    if (isFindCycle) {
                        continue;
                    }

                    return performSign(contract, trustValue);
                }
            } else {
                // MISCHIEF BEHAVES: Mischief agent do not check if he has trust value of target. Mischief signs in every situation
                return performSign(contract, trustValue);

            }

        }

        return false;
    }

    public boolean processVerifying() {

        if (my == null) {
            return false;
        }

        /* If there is no contract to be verified, return false */
        if (toBeVerifiedContracts.size() == 0) {
            return performVerify(genesis);
        }

        boolean isVerifiedPreviously;

        for (int i = 0; i < world.getSimulationConfig().getCert().getNumberOfVerificationTry_DaGra(); i++) {

            /* Selecting a contract randomly */
            int selectedIndex = Globals.RANDOM.nextInt(toBeVerifiedContracts.size());
            CertContract contract = toBeVerifiedContracts.get(selectedIndex);

            /* 1- For preventing self verifying */
            if (contract.getId() == my.getId()) {
                continue;
            }

            /* 1-1 For preventing verifying the contracts requested after this contract (MY) */
            if (contract.getRequestTime() >= my.getRequestTime()) {
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
                //   continue;
            }

            /* 3-1 For preventing verifying the signed contract by itself */
            //todo: preventing for same verifying and signing: no agent can sign and verify a same contract
          /*  boolean isItsOwn = false;
            for (CertSign sign : contract.getSigns()) {
                if (sign.getSigner().getId() == my.getId()) {
                    isItsOwn = true;
                    break;
                }
            }
            if (isItsOwn) {
                //   continue;
            }*/



            /* 4- Checking sings of the contract do not expired */
            boolean isInvalid = false;
            for (CertSign sign : contract.getSigns()) {
                if (!sign.isValid()) {
                    isInvalid = true;
                    break;
                }
            }
            if (isInvalid) {
                continue;
            }

            /* 5- There is no signer with negative trust */
            int signWithNegativeValCount = 0;
            for (CertSign sign : contract.getSigns()) {
                float trustValue = world.getTrustManager().getTrustValue(owner, sign.getSigner().getRequester());
                if (trustValue < 0) {
                    signWithNegativeValCount++;
                    break;
                }
            }
            if (signWithNegativeValCount > ((float) world.getSimulationConfig().getCert().getNumberOfNeededSing_DaGra())) {
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
                    if (sign.isValid()) {
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
        contract.setAcceptTime(certVerify.getTime());

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

    public void destroy() {

        if (this.contracts != null) {
            for (int i = 0; i < this.contracts.size(); i++) {
                this.contracts.get(i).destroy();
                this.contracts.set(i, null);
            }
            this.contracts.clear();
            this.contracts = null;
        }


    }
}
