import _type.TtSimulationMode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import stateLayer.Environment;
import stateLayer.StateX;
import systemLayer.World;
import systemLayer.profiler.CapacityProfiler;
import systemLayer.profiler.DefParameter;
import utils.Config;
import utils.Globals;
import utils.ProjectPath;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EnvGenerator {


    public static void main(String[] args) throws Exception {

        Config.SIMULATION_MODE = TtSimulationMode.PureEnvironment;

        Gson gson = new Gson();

        //============================
        FileReader prfReader = new FileReader(Config.SimulatingFile);
        Globals.profiler = gson.fromJson(prfReader, CapacityProfiler.class);

        if (Globals.profiler == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: profiler not found.");
            return;
        }
        Globals.profiler.init();

        //============================
        FileReader envReader = new FileReader(ProjectPath.instance().autoEnvGeneratorData());
        Environment environment = gson.fromJson(envReader, Environment.class);

        if (environment == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: environment not found.");
            return;
        }

        System.out.println("Environment loaded from file.");

        //============================ Initializing Environment
        int stateCount = environment.getStateCount();

        environment.setStates(new StateX[stateCount]);
        environment.setStateTargetCountD(new DefParameter(environment.getStateTargetCount()));
        environment.initPitfallCountValue(new DefParameter(environment.getPitfallCount()));
        environment.setIsAutoGenerated(true);

        //============================ Creating states
        for (int i = 0; i < stateCount; i++) {
            StateX stateX = new StateX();
            stateX.setId(i);
            stateX.setTargetIds(new ArrayList<>());
            environment.getStates()[i] = stateX;
        }

        //============================ Setting pitfalls
        int pitfallCountValue = environment.getPitfallCountValue();
        if (pitfallCountValue > 0) {
            int i = 1;
            for (; i <= pitfallCountValue && i < stateCount; ) {
                int stateId = Globals.RANDOM.nextInt(stateCount);
                if (!environment.getStates()[stateId].isIsPitfall()) {
                    environment.getStates()[stateId].setIsPitfall(true);
                    i++;
                }
            }
            if (i != pitfallCountValue) {
                System.out.println(">> Warning: pitfall count not satisfied. expected pitfalls: " + pitfallCountValue + ". created pitfalls: " + i);
            }
        }


        //============================ Creating target for each state
        for (int i = 0; i < stateCount; i++) {
            StateX stateX = environment.getState(i);

            // Continue if this state is a pitfall
            if (stateX.isIsPitfall()) {
                continue;
            }

            int targetCount = environment.getStateTargetCountValue();

            for (int j = 0; j < targetCount; ) {

                int targetId = Globals.RANDOM.nextInt(stateCount);

                //-- ignoring itself as a target
                if (targetId == stateX.getId()) {
                    continue;
                }

                //-- ignoring previously added target to list
                boolean isExist = false;
                for (Integer tId : stateX.getTargetIds()) {
                    if (tId == targetId) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    j++;
                    stateX.getTargetIds().add(targetId);
                }
            }
            environment.getStates()[i] = stateX;
        }

        //============================ Creating the world and initializing the world and environment
        World world = new World(environment);

        environment = world.getEnvironment();
        environment.setIsAutoGenerated(true);

        //============================ Saving environment
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String envJson = gson.toJson(environment);

        int envFileCounter = 0;
        String envFileName = "f-environment";
        String envFilePath = ProjectPath.instance().fullEnvDir() + "/" + envFileName;

        File file;

        do {
            envFileCounter++;
            file = new File(envFilePath + "-" + envFileCounter + ".json");
        } while (file.exists() && envFileCounter < 10000);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(envJson);
            writer.close();
        } catch (IOException ignored) {
        }

    }
}
