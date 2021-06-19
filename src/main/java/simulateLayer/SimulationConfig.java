package simulateLayer;

public class SimulationConfig {

    private int simulationRound;

    private int currentIndex = 0;

    private SimulationConfigItem configs[];


    public int getSimulationRound() {
        return simulationRound;
    }

    public SimulationConfigItem getNextConfig() {
        if (currentIndex >= simulationRound) {
            currentIndex = 0;
        }
        return configs[currentIndex++];
    }

    public SimulationConfigItem getByIndex(int index){
        return configs[index];
    }
}
