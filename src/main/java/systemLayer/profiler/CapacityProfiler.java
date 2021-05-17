package systemLayer.profiler;

import java.util.ArrayList;

public class CapacityProfiler {

    private String agentsCount;
    private String serviceCount;
    private String simulationRound;

    private ArrayList<PopulationBunch> bunches;

    private DefParameter agentsCountD;
    private DefParameter serviceCountD;
    private DefParameter simulationRoundD;

    private int currentBunchIndex;

    //============================//============================

    public CapacityProfiler() {
        agentsCount
                = simulationRound
                = serviceCount
                = "";
        currentBunchIndex = 0;
        bunches = null;
    }

    public void init() {

        agentsCountD = new DefParameter(agentsCount);
        serviceCountD = new DefParameter(serviceCount);
        simulationRoundD = new DefParameter(simulationRound);

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

    public int getSimulationRound() {
        return simulationRoundD.nextValue();
    }

    public int getAgentsCount() {
        return agentsCountD.nextValue();
    }

    public int getServiceCount() {
        return serviceCountD.nextValue();
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
                ti + ", serviceCount='" + serviceCount + '\'' +
                ti + ", simulationRound='" + simulationRound + '\'' +
                ti + ", agentsCountD=" + agentsCountD.toString(tabIndex) +
                ti + ", serviceCountD=" + serviceCountD.toString(tabIndex) +
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
