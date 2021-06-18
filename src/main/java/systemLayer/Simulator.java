package systemLayer;

import com.google.gson.Gson;
import drawingLayer.integrated.IntStatsOfEnvDrawingWindow;
import stateLayer.Environment;
import utils.Config;
import utils.Globals;
import utils.ProjectPath;
import utils.profiler.SimulationConfigBunch;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Simulator {

    private World[] worlds;
    private Environment loadedEnvironmentFromJson;
    private SimulationConfigBunch simulationConfigBunch;

    //============================//============================

    private FileReader envReader;
    private Gson gson = new Gson();

    //============================//============================

    private IntStatsOfEnvDrawingWindow intStatsOfEnvDW;

    //============================//============================//============================
    private void init() throws Exception {

        //============================//============================ Loading Environment from file
        envReader = new FileReader(Config.EnvironmentDataFilePath);
        loadedEnvironmentFromJson = gson.fromJson(envReader, Environment.class);

        if (loadedEnvironmentFromJson == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: environment not found.");
            return;
        }

        System.out.println("> Environment loaded from file.");

        //============================//============================ Loading Simulation Config file from file
        envReader = new FileReader(Config.SimulationConfigFilePath);
        simulationConfigBunch = gson.fromJson(envReader, SimulationConfigBunch.class);

        if (simulationConfigBunch == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: simulation config file not found.");
            return;
        }

        System.out.println("> Simulation Config file loaded from file.");

        //============================ Initializing worlds
        Globals.SIMULATION_ROUND = simulationConfigBunch.getSimulationRound();
        worlds = new World[Globals.SIMULATION_ROUND];

        for (int i = 0, worldsLength = worlds.length; i < worldsLength; i++) {
            worlds[i] = new World(this, simulationConfigBunch.getNextConfig());
        }


        //============================//============================ Initializing statistics report file
        if (Config.STATISTICS_IS_GENERATE) {

            String statName = Globals.STATS_FILE_NAME;

            System.out.println("Statistics file name: " + statName);

            ProjectPath.instance().createDirectoryIfNotExist(ProjectPath.instance().statisticsDir() + "/" + statName);

            //-- Copying environment-x.json to statistics directory
            Path sourcePath = Paths.get(Config.EnvironmentDataFilePath);
            Path targetPath = Paths.get(ProjectPath.instance().statisticsDir() + "/" + statName + "/" + Config.EnvironmentDataFileName);
            Files.copy(sourcePath, targetPath);

            //-- Copying simulation-x.json to statistics directory
            sourcePath = Paths.get(Config.SimulatingFilePath);
            targetPath = Paths.get(ProjectPath.instance().statisticsDir() + "/" + statName + "/" + Config.SimulatingFileName);
            Files.copy(sourcePath, targetPath);

        }
    }

    private void reInit() throws FileNotFoundException {

        envReader = new FileReader(Config.EnvironmentDataFilePath);
        loadedEnvironmentFromJson = gson.fromJson(envReader, Environment.class);

        if (loadedEnvironmentFromJson == null) {
            System.out.println(">> Simulator.reInit");
            System.out.println("> Error: environment not found.");
            return;
        }

        System.out.println("> Environment reloaded from file. simulationTimer");
    }

    public void updateWindows() {
        if (Config.INT_DRAWING_SHOW_STAT_OF_ENV) {
            intStatsOfEnvDW.repaint();
        }
    }

    public void simulate() throws Exception {

        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        int widthHalf = (int) width / 2;
        int heightHalf = (int) height / 2;

        //============================ Initializing Diagram Drawing Windows
        if (Config.INT_DRAWING_SHOW_STAT_OF_ENV) {
            intStatsOfEnvDW = new IntStatsOfEnvDrawingWindow(worlds, simulationConfigBunch);
            intStatsOfEnvDW.setDoubleBuffered(true);
            intStatsOfEnvDW.setName("int_s_env");
            JFrame statsFrame = new JFrame();
            statsFrame.getContentPane().add(intStatsOfEnvDW);
            statsFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            statsFrame.setVisible(true);
            statsFrame.setLocation(0, heightHalf);
            statsFrame.setTitle("Integrated Environment Statistics");
        }


        for (World world : worlds) {
            if (Globals.SIMULATION_TIMER > 0) {
                reInit();
            }
            Globals.reset();
            world.init(loadedEnvironmentFromJson);
            world.run();
            Globals.SIMULATION_TIMER++;
            if (Globals.SIMULATION_ROUND <= Globals.SIMULATION_TIMER) {
                break;
            }
        }

        //============================//============================ Closing statistics file
        if (Config.STATISTICS_IS_GENERATE) {
            Globals.statsEnvGenerator.close();
            Globals.statsTrustGenerator.close();
        }
    }
}
