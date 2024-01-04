package WSM.trust;

import WSM.trust.data.TrustAbstract;
import WSM.trust.data.TrustDataArray;
import core.enums.TtTrustReplaceMethod;
import com.google.gson.annotations.Expose;
import WSM.society.agent.Agent;

public class AgentTrust {

    public AgentTrust(
            TtTrustReplaceMethod replaceHistoryMethod,
            int experienceCap, int experienceItemCap,
            int indirectExperienceCap, int indirectExperienceItemCap,
            int observationCap, int observationItemCap,
            int indirectObservationCap, int indirectObservationItemCap,
            int recommendationCap, int recommendationItemCap,
            int certificationCandidateCapPowerThreshold, boolean isUseCertificationForDishonestAgents
    ) {
        this.trustReplaceMethod = replaceHistoryMethod;
        this.experienceCap = experienceCap;
        this.experienceItemCap = experienceItemCap;
        this.indirectExperienceCap = indirectExperienceCap;
        this.indirectExperienceItemCap = indirectExperienceItemCap;
        this.observationCap = observationCap;
        this.observationItemCap = observationItemCap;
        this.indirectObservationCap = indirectObservationCap;
        this.indirectObservationItemCap = indirectObservationItemCap;
        this.recommendationCap = recommendationCap;
        this.recommendationItemCap = recommendationItemCap;
        this.certificationCandidateCapPowerThreshold = certificationCandidateCapPowerThreshold;
        this.isUseCertificationForDishonestAgents = isUseCertificationForDishonestAgents;
    }

    public void setTrustParams(
            int experienceCap, int experienceItemCap,
            int indirectExperienceCap, int indirectExperienceItemCap,
            int observationCap, int observationItemCap,
            int indirectObservationCap, int indirectObservationItemCap,
            int recommendationCap, int recommendationItemCap
    ) {

        this.experienceCap = experienceCap;
        this.experienceItemCap = experienceItemCap;
        this.indirectExperienceCap = indirectExperienceCap;
        this.indirectExperienceItemCap = indirectExperienceItemCap;
        this.observationCap = observationCap;
        this.observationItemCap = observationItemCap;
        this.indirectObservationCap = indirectObservationCap;
        this.indirectObservationItemCap = indirectObservationItemCap;
        this.recommendationCap = recommendationCap;
        this.recommendationItemCap = recommendationItemCap;
    }

    public void init(Agent parentAgent) {

        this.agent = parentAgent;

        experiences = new TrustDataArray(experienceCap, trustReplaceMethod, agent);
        indirectExperiences = new TrustDataArray(indirectExperienceCap, trustReplaceMethod, agent);

        recommendations = new TrustDataArray(recommendationCap, trustReplaceMethod, agent);

        observations = new TrustDataArray(observationCap, trustReplaceMethod, agent);
        indirectObservations = new TrustDataArray(indirectObservationCap, trustReplaceMethod, agent);

        /* Selecting candidate for getting certification from the network */
        this.hasCandidateForCertification =
                agent.getCapacity().getCapPower() > certificationCandidateCapPowerThreshold &&
                        (agent.getBehavior().getHasHonestState() ||
                                isUseCertificationForDishonestAgents)
        /*&& agent.getBehavior().getHasHonestState()*/;

    }

    /**
     * After creating agents and filling agents list in the world
     */
    public void postInit() {
        this.trustAbstracts = new TrustAbstract[agent.getWorld().getAgentsCount()];
        for (int i = 0; i < this.trustAbstracts.length; i++) {
            trustAbstracts[i] = new TrustAbstract(agent.getWorld().getAgents().get(i));
        }
    }

    private Agent agent;

    @Expose
    private TtTrustReplaceMethod trustReplaceMethod;

    //-- Last values of trusts for each responder. These data will update after calculating trust values.
    private TrustAbstract trustAbstracts[];

    //============================//============================//============================  Recommendation
    //-- Received recommendation form others
    private TrustDataArray recommendations;
    private int recommendationCap;
    private int recommendationItemCap;
    @Expose
    private int certificationCandidateCapPowerThreshold;
    @Expose
    private boolean isUseCertificationForDishonestAgents;

    //============================//============================//============================ Direct Experience
    //-- All experience tuples that calculated across world run
    private TrustDataArray experiences;
    private int experienceCap;     // maximum size of history
    private int experienceItemCap; // max size of services in each history

    //============================//============================//============================ Direct Experience
    //-- All indirect experience tuples that calculated across world run
    private TrustDataArray indirectExperiences;
    private int indirectExperienceCap;     // maximum size of indirectExperience
    private int indirectExperienceItemCap; // max size of items in each indirectExperience

    //============================//============================//============================ Direct Observation
    //-- All observation tuples that calculated across world run
    private TrustDataArray observations;
    private int observationCap;     // maximum size of observations
    private int observationItemCap; // max size of items in each observation

    //============================//============================//============================ Indirect Observation
    //-- All indirect observation tuples that calculated across world run
    private TrustDataArray indirectObservations;
    private int indirectObservationCap;     // maximum size of indirect observations
    private int indirectObservationItemCap; // max size of items in each indirect observation

    //============================//============================//============================ Certification

    private boolean hasCandidateForCertification;

    //============================//============================//============================

    public int[] getObservationRewardsCount() {
        return observations.getRewardsCount();
    }

    public int[] getIndirectObservationRewardsCount() {
        return indirectObservations.getRewardsCount();
    }

    public int[] getExperienceRewardsCount() {
        return experiences.getRewardsCount();
    }

    public int[] getRecommendationRewardsCount() {
        return recommendations.getRewardsCount();

    }

    public int[] getIndirectExperienceRewardsCount() {
        return indirectExperiences.getRewardsCount();
    }

    //============================//============================//============================


/*    protected AgentTrust clone() {
        AgentTrust trust = new AgentTrust(
                trustReplaceMethod,
                experienceCap, experienceItemCap,
                indirectExperienceCap, indirectExperienceItemCap,
                observationCap, observationItemCap,
                indirectObservationCap, indirectObservationItemCap,
                recommendationCap, recommendationItemCap);

        trust.init(agent);
        trust.experiences = this.experiences;
        trust.experienceItemCap = this.experienceItemCap;
        return trust;
    }*/

    @Override
    public String toString() {
        return "AgentTrust{" +
//                "\n\t\tagent=" + agent +
                ",\n\t\t experienceCap=" + experienceCap +
                ",\n\t\t experienceItemCap=" + experienceItemCap +
                '}';
    }

    //============================//============================//============================


    public Agent getAgent() {
        return agent;
    }

    public TrustDataArray getExperiences() {
        return experiences;
    }

    public int getExperienceItemCap() {
        return experienceItemCap;
    }

    public int getExperienceCap() {
        return experienceCap;
    }

    public TtTrustReplaceMethod getTrustReplaceMethod() {
        return trustReplaceMethod;
    }

    public void setTrustReplaceMethod(TtTrustReplaceMethod trustReplaceMethod) {
        this.trustReplaceMethod = trustReplaceMethod;
    }

    public TrustDataArray getRecommendations() {
        return recommendations;
    }

    public int getRecommendationCap() {
        return recommendationCap;
    }

    public int getRecommendationItemCap() {
        return recommendationItemCap;
    }

    public int getObservationCap() {
        return observationCap;
    }

    public TrustDataArray getIndirectExperiences() {
        return indirectExperiences;
    }

    public int getIndirectExperienceCap() {
        return indirectExperienceCap;
    }

    public int getIndirectExperienceItemCap() {
        return indirectExperienceItemCap;
    }

    public TrustDataArray getObservations() {
        return observations;
    }

    public int getObservationItemCap() {
        return observationItemCap;
    }

    public TrustDataArray getIndirectObservations() {
        return indirectObservations;
    }

    public int getIndirectObservationCap() {
        return indirectObservationCap;
    }

    public int getIndirectObservationItemCap() {
        return indirectObservationItemCap;
    }

    public TrustAbstract[] getTrustAbstracts() {
        return trustAbstracts;
    }

    public boolean isHasCandidateForCertification() {
        return hasCandidateForCertification;
    }

    public void destroy() {
        this.trustAbstracts = null;
        this.recommendations = null;
        this.experiences = null;
        this.indirectExperiences = null;
        this.indirectObservations = null;
        this.observations = null;
    }
}
