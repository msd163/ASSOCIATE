package utils;

public class ProjectPath {

    public String root() {
        String hostPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        return hostPath.substring(0, hostPath.indexOf("/target"));
    }

    public String resources() {
        return root() + "/src/main/resources";
    }

    public String statesData() {
        return root() + "/src/main/resources/simData/states.json";
    }

    public String simulationData() {
        return root() + "/src/main/resources/simData/simulation.json";
    }

}
