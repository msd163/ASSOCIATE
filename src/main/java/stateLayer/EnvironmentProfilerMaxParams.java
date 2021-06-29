package stateLayer;

import com.google.gson.annotations.Expose;

public class EnvironmentProfilerMaxParams {

    public EnvironmentProfilerMaxParams() {
    }

    //============================//============================//============================
    @Expose
    private int maxExperienceCap = 0;
    @Expose
    private int maxExperienceItemCap = 0;
    @Expose
    private int maxIndirectExperienceCap = 0;
    @Expose
    private int maxIndirectExperienceItemCap = 0;
    @Expose
    private int maxObservationCap = 0;
    @Expose
    private int maxObservationItemCap = 0;
    @Expose
    private int maxIndirectObservationCap = 0;
    @Expose
    private int maxIndirectObservationItemCap = 0;
    @Expose
    private int maxTrustRecommendationItemCap = 0;
    @Expose
    private int maxTrustRecommendationCap = 0;
    @Expose
    private int maxWatchListCap = 0;
    @Expose
    private int maxWatchDepth = 0;
    @Expose
    private int maxTravelHistoryCap = 0;
    @Expose
    private int maxAgentTargetCount = 0;
    @Expose
    private int maxWithInternetCount = 0;

    //============================//============================//============================

    public int getMaxExperienceItemCap() {
        return maxExperienceItemCap;
    }

    public void setMaxExperienceItemCap(int maxExperienceItemCap) {
        this.maxExperienceItemCap = maxExperienceItemCap;
    }

    public int getMaxExperienceCap() {
        return maxExperienceCap;
    }

    public void setMaxExperienceCap(int maxExperienceCap) {
        this.maxExperienceCap = maxExperienceCap;
    }

    public int getMaxTrustRecommendationItemCap() {
        return maxTrustRecommendationItemCap;
    }

    public void setMaxTrustRecommendationItemCap(int maxTrustRecommendationItemCap) {
        this.maxTrustRecommendationItemCap = maxTrustRecommendationItemCap;
    }

    public int getMaxTrustRecommendationCap() {
        return maxTrustRecommendationCap;
    }

    public void setMaxTrustRecommendationCap(int maxTrustRecommendationCap) {
        this.maxTrustRecommendationCap = maxTrustRecommendationCap;
    }

    public int getMaxWatchListCap() {
        return maxWatchListCap;
    }

    public void setMaxWatchListCap(int maxWatchListCap) {
        this.maxWatchListCap = maxWatchListCap;
    }

    public int getMaxWatchDepth() {
        return maxWatchDepth;
    }

    public void setMaxWatchDepth(int maxWatchDepth) {
        this.maxWatchDepth = maxWatchDepth;
    }

    public int getMaxTravelHistoryCap() {
        return maxTravelHistoryCap;
    }

    public void setMaxTravelHistoryCap(int maxTravelHistoryCap) {
        this.maxTravelHistoryCap = maxTravelHistoryCap;
    }

    public int getMaxObservationCap() {
        return maxObservationCap;
    }

    public void setMaxObservationCap(int maxObservationCap) {
        this.maxObservationCap = maxObservationCap;
    }

    public int getMaxAgentTargetCount() {
        return maxAgentTargetCount;
    }

    public void setMaxAgentTargetCount(int maxAgentTargetCount) {
        this.maxAgentTargetCount = maxAgentTargetCount;
    }

    public int getMaxWithInternetCount() {
        return maxWithInternetCount;
    }

    public void setMaxWithInternetCount(int maxWithInternetCount) {
        this.maxWithInternetCount = maxWithInternetCount;
    }

    public int getMaxIndirectExperienceCap() {
        return maxIndirectExperienceCap;
    }

    public void setMaxIndirectExperienceCap(int maxIndirectExperienceCap) {
        this.maxIndirectExperienceCap = maxIndirectExperienceCap;
    }

    public int getMaxIndirectExperienceItemCap() {
        return maxIndirectExperienceItemCap;
    }

    public void setMaxIndirectExperienceItemCap(int maxIndirectExperienceItemCap) {
        this.maxIndirectExperienceItemCap = maxIndirectExperienceItemCap;
    }

    public int getMaxObservationItemCap() {
        return maxObservationItemCap;
    }

    public void setMaxObservationItemCap(int maxObservationItemCap) {
        this.maxObservationItemCap = maxObservationItemCap;
    }

    public int getMaxIndirectObservationCap() {
        return maxIndirectObservationCap;
    }

    public void setMaxIndirectObservationCap(int maxIndirectObservationCap) {
        this.maxIndirectObservationCap = maxIndirectObservationCap;
    }

    public int getMaxIndirectObservationItemCap() {
        return maxIndirectObservationItemCap;
    }

    public void setMaxIndirectObservationItemCap(int maxIndirectObservationItemCap) {
        this.maxIndirectObservationItemCap = maxIndirectObservationItemCap;
    }
}
