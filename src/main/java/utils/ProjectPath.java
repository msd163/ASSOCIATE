package utils;

import _type.TtExecutionTarget;

import java.io.File;

public class ProjectPath {

    private static ProjectPath _instance = new ProjectPath();
    private TtExecutionTarget target;
    private String root;

    public static ProjectPath instance() {
        return _instance;
    }

    private ProjectPath() {
        init();
    }

    private void init() {

        String hostPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        System.out.println("Initializing Target: " + hostPath);
        if (hostPath.contains(":")) {
            target = TtExecutionTarget.Windows;
        } else {
            target = TtExecutionTarget.Linux;
        }

        System.out.println("    Selected Target: " + target);

        root = target.getPath() + hostPath.substring(1, hostPath.indexOf("/target"));

    }

    public String root() {
        return root;
    }

    public String resourcesDir() {
        return root + "/src/main/resources";
    }


    public String societyDir() {
        return root + "/src/main/resources/society";
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

    public String societyDataSimPro() {
        int i = 100;
        for (; i > 0; i--) {
            if (new File(societyDir() + "/society-" + i + ".json").exists()) {
                break;
            }
        }
        System.out.println("|>  Selected FullSociety file: " + societyDir() + "/society-" + i + ".json");
        return societyDir() + "/society-" + i + ".simPro.json";
    }

    public String societyConfigFile() {
        return resourcesDir() + "/society-config.json";
    }

    public String alertStartFile() {
        return resourcesDir() + "/alert-str.wav";
    }

    public String alertMidFile() {
        return resourcesDir() + "/alert-mid-2.wav";
    }

    public String alertEndFile() {
        return resourcesDir() + "/alert-end.wav";
    }


    public String trustConfigFile() {
        return resourcesDir() + "/trust-config.json";
    }
    public String simulationConfigFile() {
        return resourcesDir() + "/simulation-config.json";
    }

    //============================//============================

    public String statisticsDir() {
        return root + "/statistics";
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
