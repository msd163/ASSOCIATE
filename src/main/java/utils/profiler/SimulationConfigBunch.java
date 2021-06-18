package utils.profiler;

public class SimulationConfigBunch {

    private int simulationRound;

    private int currentIndex = 0;

    private SimulationConfig configs[];


    public int getSimulationRound() {
        return simulationRound;
    }

    public SimulationConfig getNextConfig() {
        if (currentIndex >= simulationRound) {
            currentIndex = 0;
        }
        return configs[currentIndex++];
    }

    public SimulationConfig getByIndex(int index){
        return configs[index];
    }
}
