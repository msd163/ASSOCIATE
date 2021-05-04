package profiler;

import utils.DefParameter;

import java.util.ArrayList;

public class CapacityProfiler {

    private String agentsCount;
    private String worldWidth;
    private String worldHeight;
    private String simulationRound;

    private ArrayList<PopulationBunch> bunches;

    private DefParameter agentsCountD;
    private DefParameter worldWidthD;
    private DefParameter worldHeightD;
    private DefParameter simulationRoundD;

    private int currentBunchIndex;

    //============================//============================

    public CapacityProfiler() {
        agentsCount
                = worldWidth
                = worldHeight
                = simulationRound
                = "";
        currentBunchIndex = 0;
        bunches = null;
    }

    public void init() {

        agentsCountD = new DefParameter(agentsCount);
        worldWidthD = new DefParameter(worldWidth);
        worldHeightD = new DefParameter(worldHeight);
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

    public int getWorldWidth() {
        return worldWidthD.nextValue();
    }

    public int getWorldHeight() {
        return worldHeightD.nextValue();
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
                ti + ", worldWidth='" + worldWidth + '\'' +
                ti + ", worldHeight='" + worldHeight + '\'' +
                ti + ", simulationRound='" + simulationRound + '\'' +
                ti + ", agentsCountD=" + agentsCountD.toString(tabIndex) +
                ti + ", worldWidthD=" + worldWidthD.toString(tabIndex) +
                ti + ", worldHeightD=" + worldHeightD.toString(tabIndex) +
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
