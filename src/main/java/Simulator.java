import system.World;
import utils.Config;

public class Simulator {

    private World[] worlds;


    private void init() {
        worlds = new World[Config.SIMULATION_WORLD_COUNT];

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
