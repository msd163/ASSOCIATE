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

    public String pureEnvDir() {
        return root() + "/src/main/resources/pure-env";
    }

    public String fullEnvDir() {
        return root() + "/src/main/resources/full-env";
    }

    public String pureEnvData(int i) {
        return pureEnvDir() + "/p-environment-" + i + ".json";
    }

    public String fullEnvData(int i) {
        return fullEnvDir() + "/f-environment-" + i + ".json";
    }

    public String fullEnvData() {
        int i = 100;
        for (; i > 0; i--) {
            if (new File(fullEnvDir() + "/f-environment-" + i + ".json").exists()) {
                break;
            }
        }
        System.out.println("|>  Selected FullEnvironment file: " + fullEnvDir() + "/f-environment-" + i + ".json");
        return fullEnvDir() + "/f-environment-" + i + ".json";
    }

    public String autoEnvGeneratorData() {
        return fullEnvDir() + "/autoEnvGen.json";
    }

    public String simulationData(int i) {
        return resourcesDir() + "/simulation-" + i + ".json";
    }

    //============================//============================

    public String statisticsDir() {
        return root() + "/statistics";
    }
}
