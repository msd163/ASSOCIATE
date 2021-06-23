package trustLayer;

import _type.TtTrustReplaceMethod;
import com.google.gson.annotations.Expose;
import systemLayer.Agent;
import trustLayer.data.TrustExperience;
import trustLayer.data.TrustIndirectExperience;
import trustLayer.data.TrustIndirectObservation;
import trustLayer.data.TrustObservation;

import java.util.ArrayList;
import java.util.List;

public class AgentTrust {

    public AgentTrust(
            TtTrustReplaceMethod replaceHistoryMethod,
            int experienceCap, int experienceItemCap,
            int indirectExperienceCap, int indirectExperienceItemCap,
            int observationCap, int observationItemCap,
            int indirectObservationCap, int indirectObservationItemCap,
            int recommendationCap, int recommendationItemCap
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

    public void init(Agent parentAgent, int agentsCount) {

        this.agent = parentAgent;

        experiences = new ArrayList<>();
        indirectExperiences = new ArrayList<>();

        recommendations = new ArrayList<>();

        observations = new ArrayList<>();
        indirectObservations = new ArrayList<>();

        this.trustValues = new float[agentsCount];
        this.lastUpdateTrustValues = new int[agentsCount];

    }

    private Agent agent;

    @Expose
    private TtTrustReplaceMethod trustReplaceMethod;

    private float trustValues[];            // final trust values
    private int lastUpdateTrustValues[];    // last update time of trust values

    //============================//============================//============================  Recommendation
    //-- Received recommendation form others
    private List<TrustRecommendation> recommendations;
    private int recommendationCap;
    private int recommendationItemCap;

    //============================//============================//============================ Direct Experience
    //-- All experience tuples that calculated across world run
    private List<TrustExperience> experiences;
    private int experienceCap;     // maximum size of history
    private int experienceItemCap; // max size of services in each history

    //============================//============================//============================ Direct Experience
    //-- All indirect experience tuples that calculated across world run
    private List<TrustIndirectExperience> indirectExperiences;
    private int indirectExperienceCap;     // maximum size of indirectExperience
    private int indirectExperienceItemCap; // max size of items in each indirectExperience

    //============================//============================//============================ Direct Observation
    //-- All observation tuples that calculated across world run
    private List<TrustObservation> observations;
    private int observationCap;     // maximum size of observations
    private int observationItemCap; // max size of items in each observation

    //============================//============================//============================ Indirect Observation
    //-- All indirect observation tuples that calculated across world run
    private List<TrustIndirectObservation> indirectObservations;
    private int indirectObservationCap;     // maximum size of indirect observations
    private int indirectObservationItemCap; // max size of items in each indirect observation

    //============================//============================//============================

    public int[] getObservationRewardsCount() {
        int[] tarPit = {0, 0};

        for (TrustObservation obs : observations) {
            int ar = obs.getAbstractReward();
            if (ar > 0) {
                tarPit[0]++;
            } else if (ar < 0) {
                tarPit[1]++;
            }
        }
        return tarPit;
    }

    public int[] getIndirectObservationRewardsCount() {
        int[] tarPit = {0, 0};

        for (TrustIndirectObservation obs : indirectObservations) {
            int ar = obs.getAbstractReward();
            if (ar > 0) {
                tarPit[0]++;
            } else if (ar < 0) {
                tarPit[1]++;
            }
        }
        return tarPit;
    }


    public int[] getExperienceRewardsCount() {
        int[] tarPit = {0, 0};

        for (TrustExperience exp : experiences) {
            int ar = exp.getAbstractReward();
            if (ar > 0) {
                tarPit[0]++;
            } else if (ar < 0) {
                tarPit[1]++;
            }
        }
        return tarPit;
    }

    public int[] getIndirectExperienceRewardsCount() {
        int[] tarPit = {0, 0};

        for (TrustIndirectExperience obs : indirectExperiences) {
            int ar = obs.getAbstractReward();
            if (ar > 0) {
                tarPit[0]++;
            } else if (ar < 0) {
                tarPit[1]++;
            }
        }
        return tarPit;
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
                "\n\t\tagent=" + agent +
                ",\n\t\t experienceCap=" + experienceCap +
                ",\n\t\t experienceItemCap=" + experienceItemCap +
                '}';
    }

    //============================//============================//============================


    public Agent getAgent() {
        return agent;
    }

    public List<TrustExperience> getExperiences() {
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

    public List<TrustRecommendation> getRecommendations() {
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

    public List<TrustIndirectExperience> getIndirectExperiences() {
        return indirectExperiences;
    }

    public int getIndirectExperienceCap() {
        return indirectExperienceCap;
    }

    public int getIndirectExperienceItemCap() {
        return indirectExperienceItemCap;
    }

    public List<TrustObservation> getObservations() {
        return observations;
    }

    public int getObservationItemCap() {
        return observationItemCap;
    }

    public List<TrustIndirectObservation> getIndirectObservations() {
        return indirectObservations;
    }

    public int getIndirectObservationCap() {
        return indirectObservationCap;
    }

    public int getIndirectObservationItemCap() {
        return indirectObservationItemCap;
    }

    public float[] getTrustValues() {
        return trustValues;
    }

    public int[] getLastUpdateTrustValues() {
        return lastUpdateTrustValues;
    }
}
