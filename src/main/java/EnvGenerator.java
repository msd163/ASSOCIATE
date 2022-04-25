import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import environmentLayer.Environment;
import environmentLayer.StateX;
import systemLayer.Agent;
import utils.Config;
import utils.Globals;
import utils.OutLog____;
import utils.ProjectPath;
import simulateLayer.profiler.SimulationProfiler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class EnvGenerator {

    private static SimulationProfiler profiler = new SimulationProfiler();

    public static void main(String[] args) throws Exception {

        Gson gson = new Gson();

        //============================//============================  Loading Profiler from file
        FileReader prfReader = new FileReader(Config.SimulatingFilePath);
        profiler = gson.fromJson(prfReader, SimulationProfiler.class);

        if (profiler == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: profiler not found.");
            return;
        }
        profiler.init();

        //============================//============================  Initializing Environment
        Environment environment = new Environment();
        int stateCount = profiler.getStateCountValue();
        int agentsCount = profiler.getAgentsCountValue();
        int pitfallCount = profiler.getPitfallCountValue();

        environment.setAgentsCount(agentsCount);
        environment.setStateCount(stateCount);
        environment.setPitfallCount(pitfallCount);
        environment.setStates(new ArrayList<>());

        profiler.calcMaxOfBunchParams();

        //============================//============================ Creating states
        InitializingStates(environment);

        //============================//============================ Creating Agents
        InitializingAgents(environment);

        //============================//============================ Initializing Environment
        environment.initForGenerate();

        //============================//============================  Saving environment
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String envJson = gson.toJson(environment);

        int envFileCounter = 0;
        String envFileName = "environment";
        String envFilePath = ProjectPath.instance().environmentDir() + "/" + envFileName;

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

        //============================//============================ Saving simulation-profiler.json as archived source of environment.json
        try {

            String simProf = readFile(Config.SimulatingFilePath);

            file = new File(envFilePath + "-" + envFileCounter + ".simPro.json");
            FileWriter writer = new FileWriter(file);
            writer.write(simProf);
            writer.close();
        } catch (IOException ignored) {
        }

    }

    private static String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    private static void InitializingAgents(Environment environment) {

        int agentsCount = environment.getAgentsCount();

        Agent[] agents = new Agent[agentsCount];

        int id = 0;
        int thisBunchFinished = profiler.getCurrentBunch().getBunchCount();
        for (int i = 0; i < agentsCount; i++) {
            System.out.println(">> agent index: " + i + " ------------------------------");
            if (i >= thisBunchFinished) {
                profiler.NextBunch();
                thisBunchFinished = thisBunchFinished + profiler.getCurrentBunch().getBunchCount();
            }

            //============================ Creating new state
            agents[i] = new Agent(null, id++);
            agents[i].initForGenerator(profiler);

            //============================  Adding agent to an state
            StateX randomState;
            int tryCount = 0;
            boolean isAddedToState;
            do {
                randomState = environment.getRandomState();
                if (randomState.isIsPitfall()) {
                    isAddedToState = false;
                } else {
                    // checking state capability and adding the agent to it.
                    isAddedToState = randomState.addAgent(agents[i]);
                }
            } while (!isAddedToState && tryCount++ < agentsCount);

            if (isAddedToState) {

                //============================ Adding target states to agent
                agents[i].setState(randomState);

                //============================//============================//============================  Adding Targets to agent
                int targetCounts = agents[i].getTargetCounts();
                StateX prevState = agents[i].getState();
                for (int tc = 0; tc < targetCounts; tc++) {
                    tryCount = 0;
                    //System.out.println("  > Current target index: " + tc);
                    boolean isValidToAddAsTarget;
                    //============================ Adding target state to agents
                    do {
                        //System.out.println("  - Adding target state to agent. try count: " + tryCount);
                        if (tryCount > agentsCount * 0.8 && prevState.hasTarget()) {
                            int i1 = Globals.RANDOM.nextInt(prevState.getTargets().size());
                            randomState = prevState.getTargets().get(i1);
                        } else {
                            randomState = environment.getRandomState();
                        }
                        //
                        if (randomState.isIsPitfall()) {
                            isValidToAddAsTarget = false;
                        } else {
                            //-- checking state capability and adding the agent to it.
                            isValidToAddAsTarget = prevState.isAnyPathTo(randomState);
                        }
                        if (isValidToAddAsTarget) {
                            //-- check if added previously as target
                            //isValidToAddAsTarget = !agents[i].isAsTarget(randomState);

                            //-- Check if this target state is equals to previously added target state
                            isValidToAddAsTarget = randomState.getId() != prevState.getId();
                        }
                    } while (!isValidToAddAsTarget && tryCount++ < agentsCount);


                    if (isValidToAddAsTarget) {
                        agents[i].addTarget(randomState);
                        prevState = randomState;
                    }
                }

                //System.out.println("world:::init::agent: " + agents[i].getId() + " state: " + agents[i].getState().getId() + " target: " + (agents[i].getCurrentTarget() != null ? agents[i].getCurrentTarget().getId() : "NULL"));

                //System.out.println(Arrays.toString(agents[i].getTargetStateIds()));
            }
        }
    }

    private static void InitializingStates(Environment environment) throws Exception {

        int stateCount = environment.getStateCount();

        environment.updateProMaxParams();

        //============================//============================  Creating states
        for (int i = 0; i < stateCount; i++) {
            StateX stateX = new StateX();
            stateX.setId(i);
            stateX.setTargetIds(new ArrayList<>());
            stateX.setCapacity(profiler.getStateCapacityValue());
            environment.getStates().add(stateX);
        }

        //============================ Setting pitfalls
        int pitfallCountValue = environment.getPitfallCount();
        if (pitfallCountValue > 0) {
            int i = 1;
            for (; i <= pitfallCountValue && i < stateCount; ) {
                int stateId = Globals.RANDOM.nextInt(stateCount);
                StateX pitfall = environment.getStates().get(stateId);
                if (!pitfall.isIsPitfall()) {
                    pitfall.setIsPitfall(true);
                    pitfall.setCapacity(profiler.getAgentsCountValue());
                    i++;
                }
            }
            if (i - 1 != pitfallCountValue) {
                OutLog____.pl(TtOutLogMethodSection.TakeAStepTowardTheTarget, TtOutLogStatus.ERROR, "pitfall count not satisfied. expected pitfalls: " + pitfallCountValue + ". created pitfalls: " + i);
            }
        }

        //============================ Creating target for each state
        for (int i = 0; i < stateCount; i++) {
            StateX stateX = environment.getState(i);

            //-- Continue if this state is a pitfall
            if (stateX.isIsPitfall()) {
                continue;
            }

            int targetCount = profiler.getStateTargetCountValue();

            if (targetCount >= stateCount) {
                OutLog____.pl(TtOutLogMethodSection.Generator_InitializingStates, TtOutLogStatus.ERROR, "Target count (" + targetCount + ") is bigger than state count (" + stateCount + ").");
                throw new Exception("Target count (" + targetCount + ") is bigger than state count (" + stateCount + ").");
            }

            for (int j = 0; j < targetCount; ) {

                int targetId = Globals.RANDOM.nextInt(stateCount);

                //-- ignoring itself as a target
                if (targetId == stateX.getId()) {
                    continue;
                }

                //-- For ignoring unsafe targets.
                //-- Preventing to select pitfall states for states that are only on target as output.
                boolean isSafeTarget = true;
                StateX target = environment.getState(targetId);
                if (target.isIsPitfall() && targetCount <= pitfallCountValue) {
                    isSafeTarget = false;
                    for (int k = 0; k < j; k++) {
                        Integer tid = stateX.getTargetIds().get(k);
                        StateX tst = environment.getState(tid);
                        if (tst != null && !tst.isIsPitfall()) {
                            isSafeTarget = true;
                        }
                    }
                }
                if (!isSafeTarget) {
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

            //-- Updating target objects of states according their targetIds.
            stateX.updateTargets(environment);
        }


        //-- Sorting agents according less targetsId size
        //-- Used in Main drawing, for best deployment
        environment.getStates().sort((StateX s1, StateX s2) -> {
            ArrayList<Integer> ts1 = s1.getTargetIds();
            ArrayList<Integer> ts2 = s2.getTargetIds();
            if (ts1 == null) {
                return 1;
            }
            if (ts2 == null) {
                return -1;
            }
            if (ts1.size() > ts2.size()) {
                return -1;
            }
            if (ts1.size() < ts2.size()) {
                return 1;
            }
            return 0;
        });
    }
}
