package utils;

import java.io.File;

public class ProjectPath {

    private static ProjectPath _instance = new ProjectPath();

    public static ProjectPath instance() {
        return _instance;
    }

    public String root() {
        String hostPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        return hostPath.substring(1, hostPath.indexOf("/target"));
    }

    public String resourcesDir() {
        return root() + "/src/main/resources";
    }


    public String environmentDir() {
        return root() + "/src/main/resources/environment";
    }

    public String environmentData(int i) {
        return environmentDir() + "/environment-" + i + ".json";
    }

    public String environmentData() {
        int i = 100;
        for (; i > 0; i--) {
            if (new File(environmentDir() + "/environment-" + i + ".json").exists()) {
                break;
            }
        }
        System.out.println("|>  Selected FullEnvironment file: " + environmentDir() + "/environment-" + i + ".json");
        return environmentDir() + "/environment-" + i + ".json";
    }

    public String simulationProfileData(int i) {
        return resourcesDir() + "/simulation-profile-" + i + ".json";
    }

    //============================//============================

    public String statisticsDir() {
        return root() + "/statistics";
    }
}
