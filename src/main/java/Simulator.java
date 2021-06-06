import com.google.gson.Gson;
import stateLayer.Environment;
import systemLayer.World;
import systemLayer.profiler.CapacityProfiler;
import utils.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Simulator {

    private World[] worlds;

    private void init() throws Exception {

        Gson gson = new Gson();

        Environment environment;

        //============================
        FileReader prfReader = new FileReader(Config.SimulatingFile);
        Globals.profiler = gson.fromJson(prfReader, CapacityProfiler.class);

        if (Globals.profiler == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: profiler not found.");
            return;
        }
        Globals.profiler.init();

        FileReader envReader = new FileReader(Config.FullEnvironmentDataFile);
        environment = gson.fromJson(envReader, Environment.class);

        if (environment == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: environment not found.");
            return;
        }

        System.out.println("Environment loaded from file.");

        //============================ Initializing wrolds
        worlds = new World[Globals.profiler.getSimulationRoundValue()];

        for (int i = 0, worldsLength = worlds.length; i < worldsLength; i++) {
            worlds[i] = new World(environment);
        }

        //============================//============================ Initializing statistics report file
        if (Config.STATISTICS_IS_GENERATE) {
            String statName = ParsCalendar.getInstance().getShortDateTime();
            statName = statName
                    .replaceAll("[ ]", "-")
                    .replaceAll("[:/]", "")
            ;

            statName += "_" + Config.FullEnvironmentDataFile.substring(Config.FullEnvironmentDataFile.lastIndexOf("/") + 1,Config.FullEnvironmentDataFile.lastIndexOf("."));

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
            Globals.profiler.ResetBunch();
            world.run();
        }

        //============================//============================ Closing statistics file
        if (Config.STATISTICS_IS_GENERATE) {
            Globals.statsEnvGenerator.close();
            Globals.statsTrustGenerator.close();
        }
    }
}
