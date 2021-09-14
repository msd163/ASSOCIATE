package trustLayer.consensus;

import _type.TtDaGraContractStatus;
import systemLayer.Agent;
import systemLayer.World;
import utils.Globals;

import java.util.List;

public class DaGra {

    public DaGra(Agent owner) {
        this.owner = owner;
        this.world = owner.getWorld();
    }

    private CertContract genesis;

    private List<CertContract> contracts;

    private CertContract my;

    private Agent owner;

    private World world;

    private List<CertContract> verifiableContracts;

    //============================//============================//============================
   /* public boolean addRequest(Agent agent) {

        if (find(agent) != null) {
            return false;
        }
        CertContract contract = new CertContract();
        contract.setRequester(agent);
        contract.setRequestTime(Globals.WORLD_TIMER);

        contracts.add(contract);

        return true;
    }*/

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

    public TtDaGraContractStatus hasValidCertification() {
        if (my == null) {
            if (!assignMyContract()) {
                return TtDaGraContractStatus.NoContract;
            }
        }
        return my.updateStatus();
    }

    public boolean assignMyContract() {
        for (CertContract contract : contracts) {
            if (contract.getRequester() != null && contract.getRequester().getId() == owner.getId()) {
                TtDaGraContractStatus contractStatus = contract.updateStatus();
                if (
                        contractStatus != TtDaGraContractStatus.Expired
                ) {
                    my = contract;
                    return true;
                }
            }
        }
        my = null;
        return false;
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
    public void sendRegisterRequest() {
        CertContract contract = new CertContract();
        contract.setRequester(owner);
        contract.setRequestTime(Globals.WORLD_TIMER);
        contract.setPreviousCertification(findLastByRequester(owner));
        addRequest(contract);
        my = contract;
        for (Agent agent : world.getAgents()) {
            if (agent.getDaGra() != null) {
                agent.getDaGra().addRequest(contract);
            }
        }
    }

    /*public void sendValidateRequest() {
        OutLog____.pl(TtOutLogMethodSection.DaGra, TtOutLogStatus.SUCCESS, "Validity Request " + owner.getId());
    }*/

    //============================//============================//============================

    public void processSigning() {

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

}
