package simulateLayer;

import com.google.gson.Gson;
import drawingLayer.DrawingWindow;
import drawingLayer.integrated.IntTrustAnalysisLinearDrawingWindow;
import drawingLayer.integrated.IntTravelStatsLinearDrawingWindow;
import drawingLayer.integrated.IntTrustStatsLinearDrawingWindow;
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

    private IntTravelStatsLinearDrawingWindow intTravelStatsLinearDrawingWindow;
    private IntTrustAnalysisLinearDrawingWindow intTrustAnalysisLinearDrawingWindow;
    private IntTrustStatsLinearDrawingWindow intTrustStatsLinearDrawingWindow;

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
            worlds[i] = new World(i,this, simulationConfig.getNextConfig());
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

            //-- Copying simulation-profile-x.json to statistics directory
            sourcePath = Paths.get(Config.SimulatingFilePath);
            targetPath = Paths.get(ProjectPath.instance().statisticsDir() + "/" + statName + "/" + Config.SimulatingFileName);
            Files.copy(sourcePath, targetPath);

            //-- Copying simulation-config-x.json to statistics directory
            sourcePath = Paths.get(Config.SimulationConfigFilePath);
            targetPath = Paths.get(ProjectPath.instance().statisticsDir() + "/" + statName + "/" + Config.SimulationConfigFileName);
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
        if (Config.INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow) {
            intTravelStatsLinearDrawingWindow.repaint();
        }
        if (Config.INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow) {
            intTrustAnalysisLinearDrawingWindow.repaint();
        }
        if (Config.INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow){
            intTrustStatsLinearDrawingWindow.repaint();
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
        if (Config.INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow) {
            intTravelStatsLinearDrawingWindow = new IntTravelStatsLinearDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intTravelStatsLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow) {
            intTrustAnalysisLinearDrawingWindow = new IntTrustAnalysisLinearDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intTrustAnalysisLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow) {
            intTrustStatsLinearDrawingWindow = new IntTrustStatsLinearDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intTrustStatsLinearDrawingWindow, widthHalf, heightHalf);
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
        new ImageBuilder().generateSimulationImages(
                intTravelStatsLinearDrawingWindow,
                intTrustAnalysisLinearDrawingWindow,
                intTrustStatsLinearDrawingWindow
        );

        //============================//============================ Closing statistics file
        if (Config.STATISTICS_IS_GENERATE) {
            Globals.statsEnvGenerator.close();
            Globals.statsTrustGenerator.close();
        }

        while (true) {
            for (World world : worlds) {
                world.updateWindows();
            }
        }
    }

    private void initDrawingWindow(DrawingWindow drawingWindow, int widthHalf, int heightHalf) {
        drawingWindow.setDoubleBuffered(true);
        JFrame statsFrame = new JFrame();
        statsFrame.getContentPane().add(drawingWindow);
        statsFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
        statsFrame.setVisible(true);
        statsFrame.setLocation(0, heightHalf);
        statsFrame.setTitle(drawingWindow.getHeaderTitle());
    }
}
