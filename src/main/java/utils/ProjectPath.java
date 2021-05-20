package utils;

public class ProjectPath {

    private static ProjectPath _instance = new ProjectPath();

    public static ProjectPath instance() {
        return _instance;
    }

    public String root() {
        String hostPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        return hostPath.substring(0, hostPath.indexOf("/target"));
    }

    public String resourcesDir() {
        return root() + "/src/main/resources";
    }

    public String simDataDir() {
        return root() + "/src/main/resources/simData";
    }

    public String environmentData(int i) {
        return root() + "/src/main/resources/simData/environment_" + i + ".json";
    }

    public String fullEnvironmentData(int i) {
        return root() + "/src/main/resources/simData/full-environment_" + i + ".json";
    }

    public String autoEnvGeneratorData() {
        return root() + "/src/main/resources/simData/autoEnvGen.json";
    }

    public String simulationData(int i) {
        return root() + "/src/main/resources/simData/simulation_" + i + ".json";
    }

}
