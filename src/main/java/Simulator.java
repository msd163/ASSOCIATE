import system.World;
import utils.Config;
import utils.Globals;

public class Simulator {

    private World[] worlds;


    private void init() {
        worlds = new World[Globals.profiler.simulationRound];
        for (int i = 0, worldsLength = worlds.length; i < worldsLength; i++) {
           worlds[i] = new World();
        }

    }

    public void simulate() {

        init();

        for (World world : worlds) {

            world.run();

        }

    }
}
