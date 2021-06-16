package utils.profiler;

import utils.Globals;

import java.util.ArrayList;

public class SimulationProfiler {


    private String simulationRound;
    private DefParameter simulationRoundD;
    private String sleepInTarget;
    private DefParameter sleepInTargetD;

    //============================
    private String stateCount;
    private DefParameter stateCountD;
    private String stateCapacity;
    private DefParameter stateCapacityD;
    private String stateTargetCount;
    private DefParameter stateTargetCountD;
    private String pitfallCount;
    private DefParameter pitfallCountD;

    //============================
    private String agentsCount;
    private DefParameter agentsCountD;

    //============================
    private ArrayList<PopulationBunch> bunches;


    private int currentBunchIndex;

    //============================//============================

    public SimulationProfiler() {
        pitfallCount
                = stateTargetCount
                = stateCapacity
                = stateCount
                = sleepInTarget
                = agentsCount
                = simulationRound
                = "";
        currentBunchIndex = 0;
        bunches = null;
    }

    public void init() {

        agentsCountD = new DefParameter(agentsCount);
        simulationRoundD = new DefParameter(simulationRound);
        pitfallCountD = new DefParameter(pitfallCount);
        stateTargetCountD = new DefParameter(stateTargetCount);
        stateCapacityD = new DefParameter(stateCapacity);
        stateCountD = new DefParameter(stateCount);
        sleepInTargetD = new DefParameter(sleepInTarget);

        if (bunches == null || bunches.isEmpty()) {
            System.out.println(">> ERROR: Profiler bunch is empty.");
            return;
        }

        for (int i = 0, len = bunches.size(); i < len; i++) {
            bunches.get(i).initDefParams();
        }
    }

    public void NextBunch() {
        currentBunchIndex++;
        if (currentBunchIndex >= bunches.size()) {
            currentBunchIndex = 0;
        }
    }

    public void PrevBunch() {
        currentBunchIndex--;
        if (currentBunchIndex < 0)
            currentBunchIndex = bunches.size() - 1;
    }

    public void ResetBunch() {
        currentBunchIndex = 0;
    }

    public PopulationBunch getCurrentBunch() {
        return bunches.get(currentBunchIndex);
    }

    public void calcMaxOfBunchParams() {
        for (PopulationBunch bunch : bunches) {
            if (Globals.ProfileBunchMax.maxTravelHistoryCap < bunch.getTravelHistoryCapD().getMaxValue()) {
                Globals.ProfileBunchMax.maxTravelHistoryCap = bunch.getTravelHistoryCapD().getMaxValue();
            }
            if (Globals.ProfileBunchMax.maxTrustHistoryCap < bunch.getTrustHistoryCapD().getMaxValue()) {
                Globals.ProfileBunchMax.maxTrustHistoryCap = bunch.getTrustHistoryCapD().getMaxValue();
            }
            if (Globals.ProfileBunchMax.maxTrustHistoryItemCap < bunch.getTrustRecommendationItemCapD().getMaxValue()) {
                Globals.ProfileBunchMax.maxTrustHistoryItemCap = bunch.getTrustRecommendationItemCapD().getMaxValue();
            }
            if (Globals.ProfileBunchMax.maxWatchDepth < bunch.getWatchRadiusD().getMaxValue()) {
                Globals.ProfileBunchMax.maxWatchDepth = bunch.getWatchRadiusD().getMaxValue();
            }
            if (Globals.ProfileBunchMax.maxTrustRecommendationCap < bunch.getTrustRecommendationCapD().getMaxValue()) {
                Globals.ProfileBunchMax.maxTrustRecommendationCap = bunch.getTrustRecommendationCapD().getMaxValue();
            }
            if (Globals.ProfileBunchMax.maxTrustRecommendationItemCap < bunch.getTrustRecommendationItemCapD().getMaxValue()) {
                Globals.ProfileBunchMax.maxTrustRecommendationItemCap = bunch.getTrustRecommendationItemCapD().getMaxValue();
            }
            if (Globals.ProfileBunchMax.maxWatchListCapacity < bunch.getWatchListCapacityD().getMaxValue()) {
                Globals.ProfileBunchMax.maxWatchListCapacity = bunch.getWatchListCapacityD().getMaxValue();
            }
            if (Globals.ProfileBunchMax.maxObservationCap < bunch.getObservationCapD().getMaxValue()) {
                Globals.ProfileBunchMax.maxObservationCap = bunch.getObservationCapD().getMaxValue();
            }
        }
    }
    //============================//============================

    public int getSimulationRoundValue() {
        return simulationRoundD.nextValue();
    }

    public int getAgentsCountValue() {
        return agentsCountD.nextValue();
    }

    public int getSleepInTargetValue() {
        return sleepInTargetD.nextValue();
    }

    public int getStateCountValue() {
        return stateCountD.nextValue();
    }

    public int getStateCapacityValue() {
        return stateCapacityD.nextValue();
    }

    public int getStateTargetCountValue() {
        return stateTargetCountD.nextValue();
    }

    public int getPitfallCountValue() {
        return pitfallCountD.nextValue();
    }

    //============================//============================

    public String toString(int tabIndex) {
        tabIndex++;
        StringBuilder tx = new StringBuilder("\n");
        StringBuilder ti = new StringBuilder("\n");
        for (int i = 0; i <= tabIndex; i++) {
            if (i > 1) {
                tx.append("\t");
            }
            ti.append("\t");
        }
        tabIndex++;

        StringBuilder bs = new StringBuilder(ti + "[");
        if (bunches != null) {
            for (PopulationBunch b : bunches) {
                bs.append(b.toString(tabIndex)).append(",");
            }
        }
        bs.append(ti).append("]");

        return tx + "CapacityProfiler{" +
                ti + "  agentsCount='" + agentsCount + '\'' +
                ti + ", simulationRound='" + simulationRound + '\'' +
                ti + ", agentsCountD=" + agentsCountD.toString(tabIndex) +
                ti + ", simulationRoundD=" + simulationRoundD.toString(tabIndex) +
                ti + ", currentBunch=" + currentBunchIndex +
                ti + ", bunches=" + bs +
                tx + '}';
    }

    @Override
    public String toString() {
        return toString(0);
    }
}
