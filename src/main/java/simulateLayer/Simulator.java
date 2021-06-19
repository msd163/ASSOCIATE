package simulateLayer;

import com.google.gson.Gson;
import drawingLayer.DrawingWindow;
import drawingLayer.integrated.IntAnalysisOfTrustDrawingWindow;
import drawingLayer.integrated.IntStatsOfEnvDrawingWindow;
import stateLayer.Environment;
import systemLayer.World;
import utils.Config;
import utils.Globals;
import utils.ImageBuilder;
import utils.ProjectPath;

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
    private SimulationConfig simulationConfig;

    public SimulationConfig getSimulationConfigBunch() {
        return simulationConfig;
    }

    //============================//============================

    private FileReader envReader;
    private Gson gson = new Gson();

    //============================//============================ Drawing Windows

    private IntStatsOfEnvDrawingWindow intStatsOfEnvDW;
    private IntAnalysisOfTrustDrawingWindow intAnalysisOfTrustDW;

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
        simulationConfig = gson.fromJson(envReader, SimulationConfig.class);

        if (simulationConfig == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: simulation config file not found.");
            return;
        }

        System.out.println("> Simulation Config file loaded from file.");

        //============================//============================ Initializing worlds
        Globals.SIMULATION_ROUND = simulationConfig.getSimulationRound();
        worlds = new World[Globals.SIMULATION_ROUND];

        for (int i = 0, worldsLength = worlds.length; i < worldsLength; i++) {
            worlds[i] = new World(this, simulationConfig.getNextConfig());
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

    private void reloadEnvironmentFromFile() throws FileNotFoundException {

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
        if (Config.INT_DRAWING_SHOW_ANALYSIS_OF_TRUST_PARAM) {
            intAnalysisOfTrustDW.repaint();
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
        int widthHalf = (int) screenSize.getWidth() / 2;
        int heightHalf = (int) screenSize.getHeight() / 2;

        //============================ Initializing Diagram Drawing Windows
        if (Config.INT_DRAWING_SHOW_STAT_OF_ENV) {
            intStatsOfEnvDW = new IntStatsOfEnvDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intStatsOfEnvDW, widthHalf, heightHalf, "int_s_env", "Integrated Environment Statistics");
        }

        if (Config.INT_DRAWING_SHOW_ANALYSIS_OF_TRUST_PARAM) {
            intAnalysisOfTrustDW = new IntAnalysisOfTrustDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intAnalysisOfTrustDW, widthHalf, heightHalf, "int_t_anl", "Integrated Analysis of Trust (Acc|Sens|Spec)");
        }


        for (World world : worlds) {
            if (Globals.SIMULATION_TIMER > 0) {
                reloadEnvironmentFromFile();
            }
            Globals.reset();
            world.init(loadedEnvironmentFromJson);
            world.run();
            Globals.SIMULATION_TIMER++;
            if (Globals.SIMULATION_ROUND <= Globals.SIMULATION_TIMER) {
                break;
            }
        }

        Globals.SIMULATION_TIMER--;
        new ImageBuilder().generateSimulationImages(intStatsOfEnvDW, intAnalysisOfTrustDW);

        //============================//============================ Closing statistics file
        if (Config.STATISTICS_IS_GENERATE) {
            Globals.statsEnvGenerator.close();
            Globals.statsTrustGenerator.close();
        }
    }

    private void initDrawingWindow(DrawingWindow drawingWindow, int widthHalf, int heightHalf, String name, String title) {
        drawingWindow.setDoubleBuffered(true);
        drawingWindow.setName(name);
        JFrame statsFrame = new JFrame();
        statsFrame.getContentPane().add(drawingWindow);
        statsFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
        statsFrame.setVisible(true);
        statsFrame.setLocation(0, heightHalf);
        statsFrame.setTitle(title);
    }
}
