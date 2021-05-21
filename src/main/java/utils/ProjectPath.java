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

    public String pureEnvDir() {
        return root() + "/src/main/resources/pure-env";
    }

    public String fullEnvDir() {
        return root() + "/src/main/resources/full-env";
    }

    public String pureEnvData(int i) {
        return pureEnvDir() + "/p-environment_" + i + ".json";
    }

    public String fullEnvData(int i) {
        return fullEnvDir() + "/f-environment_" + i + ".json";
    }

    public String autoEnvGeneratorData() {
        return fullEnvDir() + "/autoEnvGen.json";
    }

    public String simulationData(int i) {
        return resourcesDir() + "/simulation_" + i + ".json";
    }

}
