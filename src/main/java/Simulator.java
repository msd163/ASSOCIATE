import com.google.gson.Gson;
import profiler.CapacityProfiler;
import stateTransition.Environment;
import system.World;
import utils.Globals;
import utils.ProjectPath;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Simulator {

    private World[] worlds;

    private void init() throws FileNotFoundException {

        ProjectPath projectPath = new ProjectPath();

        Gson gson = new Gson();

        //============================
        FileReader prfReader = new FileReader(projectPath.simulationData());
        Globals.profiler = gson.fromJson(prfReader, CapacityProfiler.class);

        if (Globals.profiler == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: profiler not found.");
            return;
        }
        Globals.profiler.init();

        System.out.println(Globals.profiler.toString());

        //============================
        FileReader envReader = new FileReader(projectPath.statesData());
        Environment environment = gson.fromJson(envReader, Environment.class);

        if (environment == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: environment not found.");
            return;
        }

        System.out.println("Environment loaded from file.");
        System.out.println(environment.toString());

        //============================
        worlds = new World[Globals.profiler.getSimulationRound()];

        for (int i = 0, worldsLength = worlds.length; i < worldsLength; i++) {
            worlds[i] = new World(environment);
        }
    }

    public void simulate() {

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

    }
}
