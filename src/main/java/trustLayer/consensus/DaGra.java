package trustLayer.consensus;

import _type.TtDaGraContractStatus;
import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import systemLayer.Agent;
import systemLayer.World;
import utils.Config;
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
    }

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
        CertContract contract = new CertContract();
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
                agent.getDaGra().addRequest(contract);
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
            TtDaGraContractStatus status = contract.updateStatus();
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
                if ((Globals.WORLD_TIMER + 1) % Config.DAGRA_REQUEST_STAGE__ALLOWED_REQUEST_PERIOD == 0) {
                    if (Globals.DAGRA_REQUEST_STAGE__REQUESTED_COUNT_IN_CURRENT_PERIOD < Config.DAGRA_REQUEST_STAGE__NUMBER_OF_REQUEST_IN_EACH_PERIOD) {
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

    private CertSign performSign(CertContract toBeSignedContract, float trustValue) {
        CertSign sign = new CertSign();
        sign.setSigner(my);
        sign.setSigned(toBeSignedContract);
        sign.setTime(Globals.WORLD_TIMER);
        sign.setTrustValue(trustValue);
        sign.doSign();
        return sign;
    }

    /**
     * Broadcasting sing to network
     *
     * @param sign
     */
    private void broadcastSign(CertSign sign) {
        for (Agent agent : world.getAgents()) {
            if (agent.getDaGra() != null && agent.getId() != owner.getId()) {
                agent.getDaGra().addSign(sign);
            }
        }
    }

    /**
     * Adding the received sign from the network to the signer and signed contracts
     *
     * @param sign is the received sign from the network
     */
    private void addSign(CertSign sign) {
        boolean isSignedAdded = false;
        boolean isSignerAdded = false;
        boolean isAddedPreviously;
        for (CertContract contract : contracts) {

            /* Adding the input sign to Signs list of the signed contract */
            if (contract.getId() == sign.getSigned().getId()) {

                /* Checking whether previously added to Signs */
                isAddedPreviously = false;
                for (CertSign cs : contract.getSigns()) {
                    if (cs.getSigner().getId() == sign.getSigner().getId() && cs.getSigned().getId() == sign.getSigned().getId()) {
                        isAddedPreviously = true;
                        break;
                    }
                }
                if (!isAddedPreviously) {
                    contract.getSigns().add(sign);
                }
                isSignedAdded = true;
            }

            /* Adding the input sign to SignedContracts list of the signer contract */
            else if (contract.getId() == sign.getSigner().getId()) {

                /* Checking whether previously added to SignedContracts */
                isAddedPreviously = false;
                for (CertSign cs : contract.getSignedContracts()) {
                    if (cs.getSigner().getId() == sign.getSigner().getId() && cs.getSigned().getId() == sign.getSigned().getId()) {
                        isAddedPreviously = true;
                        break;
                    }
                }
                if (!isAddedPreviously) {
                    contract.getSignedContracts().add(sign);
                }
                isSignerAdded = true;
            }
            if (isSignedAdded && isSignerAdded) {
                break;
            }
        }
    }

    public boolean processSigning() {

        if (my == null) {
            return false;
        }

        /* If there is no contract to be signed, the Genesis contract will be signed */
        if (toBeSignedContracts.size() == 0) {

            CertSign sign = performSign(genesis, 1.0f);

            /* Adding sign to Signed contract*/
            genesis.getSigns().add(sign);
            /* Adding sign to its contract */
            my.getSignedContracts().add(sign);

            broadcastSign(sign);

            return true;
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
            for (CertSign sign : my.getSigns()) {
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
                CertSign sign = performSign(contract, trustValue);

                /* Adding sign to Signed contract*/
                contract.getSigns().add(sign);
                /* Adding sign to its contract */
                my.getSignedContracts().add(sign);

                broadcastSign(sign);

                return true;
            }

        }

        return false;
    }

    public void processVerifying() {

    }

    //============================//============================//============================

    public CertContract getGenesis() {
        return genesis;
    }

    public void setGenesis(CertContract genesis) {
        this.genesis = genesis;
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

}
