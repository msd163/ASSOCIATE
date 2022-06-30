package societyLayer.agentSubLayer;

import com.google.gson.annotations.Expose;
import simulateLayer.profiler.SimulationProfiler;
import utils.Globals;

public class AgentCapacity {

    public AgentCapacity() {
    }

    public AgentCapacity(Agent parentAgent, SimulationProfiler profiler) {

        this.agent = parentAgent;

        watchDepth = profiler.getCurrentBunch().getWatchRadiusD().nextValue();

        travelHistoryCap = profiler.getCurrentBunch().getTravelHistoryCapD().nextValue();

        watchListCap = profiler.getCurrentBunch().getWatchListCapD().nextValue();

        trustRecommendationCap = profiler.getCurrentBunch().getTrustRecommendationCapD().nextValue();
        trustRecommendationItemCap = profiler.getCurrentBunch().getTrustRecommendationItemCapD().nextValue();

        experienceCap = profiler.getCurrentBunch().getExperienceCapD().nextValue();
        experienceItemCap = profiler.getCurrentBunch().getExperienceItemCapD().nextValue();

        indirectExperienceCap = profiler.getCurrentBunch().getIndirectExperienceCapD().nextValue();
        indirectExperienceItemCap = profiler.getCurrentBunch().getIndirectExperienceItemCapD().nextValue();

        observationCap = profiler.getCurrentBunch().getObservationCapD().nextValue();
        observationItemCap = profiler.getCurrentBunch().getObservationItemCapD().nextValue();

        indirectObservationCap = profiler.getCurrentBunch().getIndirectObservationCapD().nextValue();
        indirectObservationItemCap = profiler.getCurrentBunch().getIndirectObservationItemCapD().nextValue();

        // capPower is between 0 and 100
        capPower =
                (int) (10 * ((float) experienceCap / Globals.ProfileBunchMax.maxExperienceCap)) +
                        (int) (5 * ((float) experienceItemCap / Globals.ProfileBunchMax.maxExperienceItemCap)) +
                        (int) (3 * ((float) indirectExperienceCap / Globals.ProfileBunchMax.maxIndirectExperienceCap)) +
                        (int) (2 * ((float) indirectExperienceItemCap / Globals.ProfileBunchMax.maxIndirectExperienceItemCap)) +

                        (int) (10 * ((float) observationCap / Globals.ProfileBunchMax.maxObservationCap)) +
                        (int) (5 * ((float) observationItemCap / Globals.ProfileBunchMax.maxObservationItemCap)) +
                        (int) (3 * ((float) indirectObservationCap / Globals.ProfileBunchMax.maxIndirectObservationCap)) +
                        (int) (2 * ((float) indirectObservationItemCap / Globals.ProfileBunchMax.maxIndirectObservationItemCap)) +

                        (int) (10 * ((float) trustRecommendationCap / Globals.ProfileBunchMax.maxTrustRecommendationCap)) +
                        (int) (5 * ((float) trustRecommendationItemCap / Globals.ProfileBunchMax.maxTrustRecommendationItemCap)) +
                        (int) (15 * ((float) travelHistoryCap / Globals.ProfileBunchMax.maxTravelHistoryCap)) +
                        (int) (15 * ((float) watchListCap / Globals.ProfileBunchMax.maxWatchListCap)) +
                        (int) (15 * ((float) watchDepth / Globals.ProfileBunchMax.maxWatchDepth))
        ;

        hasInternet = (capPower >= profiler.getCurrentBunch().getWithInternetCapPowerThresholdD().nextValue());

    }

    private Agent agent;

    @Expose
    private int experienceCap;
    @Expose
    private int experienceItemCap;        //size of experience items in each trust Experience history. experience items is tuple of trust for one agent (responder)
    @Expose
    private int indirectExperienceCap;
    @Expose
    private int indirectExperienceItemCap;        //size of experience items in each trust Experience history. experience items is tuple of trust for one agent (responder)

    @Expose
    private int trustRecommendationItemCap;        //size of recommendation items in each trust history. trust items is score of trust for one agent (responder)
    @Expose
    private int trustRecommendationCap;
    @Expose
    private int watchListCap;
    @Expose
    private int watchDepth;                              // depth of watch in neighbor states
    @Expose
    private int travelHistoryCap;                        // size of travel history
    @Expose
    private int observationCap;
    @Expose
    private int observationItemCap;
    @Expose
    private int indirectObservationCap;
    @Expose
    private int indirectObservationItemCap;

    @Expose
    private boolean hasInternet;

    @Expose
    private int capPower; // it will be calculated by other params

    public int getWatchListCap() {
        return watchListCap;
    }

    public int getWatchDepth() {
        return watchDepth;
    }

    public int getTravelHistoryCap() {
        return travelHistoryCap;
    }

    public int getCapPower() {
        return capPower;
    }

    @Override
    public String toString() {
        return "\n\tAgentCapacity{" +
                "\n\t\texperienceItemCap=" + experienceItemCap +
                ",\n\t\texperienceCap=" + experienceCap +
                ",\n\t\twatchListCap=" + watchListCap +
                ",\n\t\twatchDepth=" + watchDepth +
                ",\n\t\ttravelHistoryCap=" + travelHistoryCap +
                ",\n\t\tcapPower=" + capPower +
                '}';
    }

    public int getIndirectExperienceCap() {
        return indirectExperienceCap;
    }

    public int getIndirectExperienceItemCap() {
        return indirectExperienceItemCap;
    }

    public int getObservationItemCap() {
        return observationItemCap;
    }

    public int getIndirectObservationCap() {
        return indirectObservationCap;
    }

    public int getIndirectObservationItemCap() {
        return indirectObservationItemCap;
    }

    public int getExperienceItemCap() {
        return experienceItemCap;
    }

    public void setExperienceItemCap(int experienceItemCap) {
        this.experienceItemCap = experienceItemCap;
    }

    public int getExperienceCap() {
        return experienceCap;
    }

    public void setExperienceCap(int experienceCap) {
        this.experienceCap = experienceCap;
    }

    public void setWatchListCap(int watchListCap) {
        this.watchListCap = watchListCap;
    }

    public void setWatchDepth(int watchDepth) {
        this.watchDepth = watchDepth;
    }

    public void setTravelHistoryCap(int travelHistoryCap) {
        this.travelHistoryCap = travelHistoryCap;
    }

    public int getTrustRecommendationItemCap() {
        return trustRecommendationItemCap;
    }

    public void setTrustRecommendationItemCap(int trustRecommendationItemCap) {
        this.trustRecommendationItemCap = trustRecommendationItemCap;
    }

    public int getTrustRecommendationCap() {
        return trustRecommendationCap;
    }

    public void setTrustRecommendationCap(int trustRecommendationCap) {
        this.trustRecommendationCap = trustRecommendationCap;
    }

    public int getObservationCap() {
        return observationCap;
    }

    public void setObservationCap(int observationCap) {
        this.observationCap = observationCap;
    }

    public boolean isHasInternet() {
        return hasInternet;
    }
}
