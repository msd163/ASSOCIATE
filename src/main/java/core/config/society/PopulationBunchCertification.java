package core.config.society;

public class PopulationBunchCertification {

    private int candidateCapPowerThreshold;                      // [0-100]
    private boolean isUsedForDishonestAgents;

    //============================//============================//============================


    public int getCandidateCapPowerThreshold() {
        return candidateCapPowerThreshold;
    }

    public void setCandidateCapPowerThreshold(int candidateCapPowerThreshold) {
        this.candidateCapPowerThreshold = candidateCapPowerThreshold;
    }

    public boolean getIsUseCertificationForDishonestAgents() {
        return isUsedForDishonestAgents;
    }

    public void setIsUsedForDishonestAgents(boolean usedForDishonestAgents) {
        isUsedForDishonestAgents = usedForDishonestAgents;
    }
}
