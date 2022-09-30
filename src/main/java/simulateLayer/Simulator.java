package simulateLayer;

import com.google.gson.Gson;
import drawingLayer.IntDrawingWindowRunner;
import societyLayer.agentSubLayer.World;
import societyLayer.environmentSubLayer.Environment;
import utils.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Simulator {

    private World[] worlds;
    private Environment loadedEnvironmentFromJson;
    private SimulationConfig simulationConfig;
    private IntDrawingWindowRunner intDrawingWindowRunner;

    public SimulationConfig getSimulationConfigBunch() {
        return simulationConfig;
    }

    //============================//============================

    private FileReader envReader;
    private Gson gson = new Gson();

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
        envReader = new FileReader(Config.TrustConfigFilePath);
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
            worlds[i] = new World(i, this, simulationConfig.getNextConfig());
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

            //-- Copying society-config-x.json to statistics directory
            sourcePath = Paths.get(Config.SocietyConfigFilePath);
            targetPath = Paths.get(ProjectPath.instance().statisticsDir() + "/" + statName + "/" + Config.SocietyConfigFileName);
            Files.copy(sourcePath, targetPath);

            //-- Copying trust-config-x.json to statistics directory
            sourcePath = Paths.get(Config.TrustConfigFilePath);
            targetPath = Paths.get(ProjectPath.instance().statisticsDir() + "/" + statName + "/" + Config.TrustConfigFileName);
            Files.copy(sourcePath, targetPath);

        }

        intDrawingWindowRunner = new IntDrawingWindowRunner(worlds, simulationConfig);
        intDrawingWindowRunner.initDrawingWindows();
        intDrawingWindowRunner.start();

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


    public void simulate() throws Exception {

        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
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
            world.destroy();
            OutLog____.pl("s");
        }

        Globals.SIMULATION_TIMER--;
        if (Config.STATISTICS_IS_GENERATE && Config.STATISTICS_IS_SAVE_IMAGE) {
            System.out.print("Simulator images are generating...");
            new ImageBuilder().generateSimulationImages(
                    intDrawingWindowRunner.getIntTravelStatsLinearDrawingWindow(),
                    intDrawingWindowRunner.getIntTrustAnalysisLinearDrawingWindow(),
                    intDrawingWindowRunner.getIntTrustStatsLinearDrawingWindow()
            );
            System.out.print("Simulator images are generated.");
        }
        //============================//============================ Closing statistics file
        if (Config.STATISTICS_IS_GENERATE) {
            Globals.statsEnvGenerator.close();
            Globals.statsTrustGenerator.close();
        }

        System.out.print("\n");
        System.out.print("Simulator run is finished.");

    }

}
