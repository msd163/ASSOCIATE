package utils;

import java.io.File;

public class ProjectPath {

    private static ProjectPath _instance = new ProjectPath();

    public static ProjectPath instance() {
        return _instance;
    }

    public String root() {
        String hostPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        return "/" + hostPath.substring(1, hostPath.indexOf("/target"));
    }

    public String resourcesDir() {
        return root() + "/src/main/resources";
    }


    public String societyDir() {
        return root() + "/src/main/resources/society";
    }

    public String societyData(int i) {
        return societyDir() + "/society-" + i + ".json";
    }

    public String societyData() {
        int i = 100;
        for (; i > 0; i--) {
            if (new File(societyDir() + "/society-" + i + ".json").exists()) {
                break;
            }
        }
        System.out.println("|>  Selected FullSociety file: " + societyDir() + "/society-" + i + ".json");
        return societyDir() + "/society-" + i + ".json";
    }

    public String simulationSocietyConfigFile() {
        return resourcesDir() + "/society-config.json";
    }


    public String simulationTrustConfigFile() {
        return resourcesDir() + "/trust-config.json";
    }

    //============================//============================

    public String statisticsDir() {
        return root() + "/statistics";
    }

    public void createDirectoryIfNotExist(String statPath) {
        File file = new File(statPath);

        if (!file.exists()) {
            boolean mkdir = file.mkdir();
            if (mkdir) {
                System.out.println("Statistics directory created: " + statPath);
            }
        }
    }
}
