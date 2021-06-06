package systemLayer.profiler;

import java.util.ArrayList;

public class CapacityProfiler {


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

    public CapacityProfiler() {
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
        if (currentBunchIndex >= bunches.size())
            currentBunchIndex = 0;
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
