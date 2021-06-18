package stateLayer;

import com.google.gson.annotations.Expose;

public class EnvironmentProfilerMaxParams {

    public EnvironmentProfilerMaxParams() {
    }

    //============================//============================//============================
    @Expose
    private int maxTrustHistoryItemCap = 0;
    @Expose
    private int maxTrustHistoryCap = 0;
    @Expose
    private int maxTrustRecommendationItemCap = 0;
    @Expose
    private int maxTrustRecommendationCap = 0;
    @Expose
    private int maxWatchListCapacity = 0;
    @Expose
    private int maxWatchDepth = 0;
    @Expose
    private int maxTravelHistoryCap = 0;
    @Expose
    private int maxObservationCap = 0;
    @Expose
    private int maxAgentTargetCount = 0;

    //============================//============================//============================

    public int getMaxTrustHistoryItemCap() {
        return maxTrustHistoryItemCap;
    }

    public void setMaxTrustHistoryItemCap(int maxTrustHistoryItemCap) {
        this.maxTrustHistoryItemCap = maxTrustHistoryItemCap;
    }

    public int getMaxTrustHistoryCap() {
        return maxTrustHistoryCap;
    }

    public void setMaxTrustHistoryCap(int maxTrustHistoryCap) {
        this.maxTrustHistoryCap = maxTrustHistoryCap;
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

    public int getMaxWatchListCapacity() {
        return maxWatchListCapacity;
    }

    public void setMaxWatchListCapacity(int maxWatchListCapacity) {
        this.maxWatchListCapacity = maxWatchListCapacity;
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
}
