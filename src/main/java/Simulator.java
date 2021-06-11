import com.google.gson.Gson;
import stateLayer.Environment;
import systemLayer.World;
import utils.Config;
import utils.Globals;
import utils.ProjectPath;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Simulator {

    private World[] worlds;

    private void init() throws Exception {

        Gson gson = new Gson();

        Environment environment;

        //============================//============================
        //-- Loading Environment from file
        FileReader envReader = new FileReader(Config.FullEnvironmentDataFile);
        environment = gson.fromJson(envReader, Environment.class);

        if (environment == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: environment not found.");
            return;
        }

        System.out.println("Environment loaded from file.");

        //============================ Initializing worlds
        worlds = new World[environment.getSimulationRound()];

        for (int i = 0, worldsLength = worlds.length; i < worldsLength; i++) {
            worlds[i] = new World(environment);
        }


        //============================//============================ Initializing statistics report file
        if (Config.STATISTICS_IS_GENERATE) {
            String statName = Globals.STATS_FILE_NAME;

            System.out.println(statName);

            Globals.statsEnvGenerator.init(ProjectPath.instance().statisticsDir() + "/" + statName + ".csv");

            Globals.statsTrustGenerator.init(ProjectPath.instance().statisticsDir() + "/" + statName + ".trust.csv");
        }

    }

    public void simulate() throws Exception {

        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        for (World world : worlds) {
            // Globals.profiler.ResetBunch();
            world.run();
        }

        //============================//============================ Closing statistics file
        if (Config.STATISTICS_IS_GENERATE) {
            Globals.statsEnvGenerator.close();
            Globals.statsTrustGenerator.close();
        }
    }
}
