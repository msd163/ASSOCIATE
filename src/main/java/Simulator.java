import _type.TtSimulationMode;
import com.google.gson.Gson;
import stateLayer.Environment;
import systemLayer.World;
import systemLayer.profiler.CapacityProfiler;
import utils.Config;
import utils.Globals;
import utils.ParsCalendar;
import utils.ProjectPath;

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


        if (Config.SIMULATION_MODE == TtSimulationMode.PureEnvironment) {

            //============================
            FileReader envReader = new FileReader(Config.PureEnvironmentDataFile);
            environment = gson.fromJson(envReader, Environment.class);

            if (environment == null) {
                System.out.println(">> Simulator.init");
                System.out.println("> Error: environment not found.");
                return;
            }

            System.out.println("Environment loaded from file.");
            // System.out.println(environment.toString());

        } else {
            FileReader envReader = new FileReader(Config.FullEnvironmentDataFile);
            environment = gson.fromJson(envReader, Environment.class);

            if (environment == null) {
                System.out.println(">> Simulator.init");
                System.out.println("> Error: environment not found.");
                return;
            }

            System.out.println("Environment loaded from file.");
            // System.out.println(environment.toString());

        }

        //============================
        worlds = new World[Globals.profiler.getSimulationRound()];

        for (int i = 0, worldsLength = worlds.length; i < worldsLength; i++) {
            worlds[i] = new World(environment);
        }

        //============================
        if (Config.STATISTICS_IS_GENERATE) {
            String statName = ParsCalendar.getInstance().getShortDateTime();
            statName = statName
                    .replaceAll("[ ]", "-")
                    .replaceAll("[:/]", "")
            ;

            if (Config.SIMULATION_MODE == TtSimulationMode.FullEnvironment) {
                statName += "_" + Config.FullEnvironmentDataFile.substring(Config.FullEnvironmentDataFile.lastIndexOf("/") + 1);
            } else {
                statName += "_" + Config.PureEnvironmentDataFile.substring(Config.PureEnvironmentDataFile.lastIndexOf("/") + 1);

            }

            System.out.println(statName);
            statName = ProjectPath.instance().statisticsDir() + "/" + statName + ".csv";

            Globals.statGenerator.init(statName);
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

        if (Config.STATISTICS_IS_GENERATE) {
            Globals.statGenerator.close();
        }
    }
}
