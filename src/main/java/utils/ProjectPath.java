package utils;

public class ProjectPath {

    public String root() {
        String hostPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        return hostPath.substring(0, hostPath.indexOf("/target"));
    }

    public String resources() {
        return root() + "/src/main/resources";
    }

    public String environmentData() {
        return root() + "/src/main/resources/simData/environment.json";
    }

    public String sim0Data() {
        return root() + "/src/main/resources/simData/sim0.json";
    }

}
