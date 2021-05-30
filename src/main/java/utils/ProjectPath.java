package utils;

import java.io.File;

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


    public String envtDir() {
        return root() + "/src/main/resources/environment";
    }

    public String envData(int i) {
        return envtDir() + "/environment-" + i + ".json";
    }

    public String envData() {
        int i = 100;
        for (; i > 0; i--) {
            if (new File(envtDir() + "/environment-" + i + ".json").exists()) {
                break;
            }
        }
        System.out.println("|>  Selected FullEnvironment file: " + envtDir() + "/environment-" + i + ".json");
        return envtDir() + "/environment-" + i + ".json";
    }

    public String autoEnvGeneratorData() {
        return resourcesDir() + "/autoEnvGen.json";
    }

    public String simulationData(int i) {
        return resourcesDir() + "/simulation-" + i + ".json";
    }

    //============================//============================

    public String statisticsDir() {
        return root() + "/statistics";
    }
}
