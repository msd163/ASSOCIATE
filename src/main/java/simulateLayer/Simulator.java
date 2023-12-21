package simulateLayer;

import com.google.gson.Gson;
import drawingLayer.IntDrawingWindowRunner;
import simulateLayer.config.simulation.SimulationConfig;
import simulateLayer.config.society.PopulationBunch;
import simulateLayer.config.society.SimulationProfiler;
import simulateLayer.config.trust.TrustConfig;
import simulateLayer.statistics.WorldStatistics;
import societyLayer.agentSubLayer.World;
import societyLayer.environmentSubLayer.Environment;
import utils.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Simulator {

    private World[] worlds;
    private Environment loadedSocietyFromJson;
    private SimulationProfiler loadedSocietyProfileFromJson;
    private TrustConfig trustConfig;
    private SimulationConfig simulationConfig;
    private IntDrawingWindowRunner intDrawingWindowRunner;

    public TrustConfig getTrustConfigBunch() {
        return trustConfig;
    }

    //============================//============================

    private FileReader fileReader;
    private final Gson gson = new Gson();

    long startTime;
    long endTime;

    //============================//============================//============================
    private void init() throws Exception {
        //============================//============================ Loading Environment from file
        fileReader = new FileReader(Globals.SocietyDataFilePath);
        loadedSocietyFromJson = gson.fromJson(fileReader, Environment.class);

        if (loadedSocietyFromJson == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: environment not found.");
            return;
        }

        fileReader = new FileReader(Globals.SocietyDataSimProfileFilePath);
        loadedSocietyProfileFromJson = gson.fromJson(fileReader, SimulationProfiler.class);

        System.out.println("> Society loaded from file.");

        //============================//============================ Loading Trust Config file from file
        fileReader = new FileReader(Globals.TrustConfigFilePath);
        trustConfig = gson.fromJson(fileReader, TrustConfig.class);

        if (trustConfig == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: trust config file not found.");
            return;
        }
        System.out.println("> Trust Config file loaded from file.");

        //============================//============================ Loading Trust Config file from file
        fileReader = new FileReader(Globals.SimulationConfigFilePath);
        simulationConfig = gson.fromJson(fileReader, SimulationConfig.class);

        if (simulationConfig == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: simulation config file not found.");
            return;
        }

        System.out.println("> Simulation Config file loaded from file.");

        updateSimulationConfig();

        //============================//============================ Initializing worlds
        Globals.SIMULATION_ROUND = trustConfig.getValidConfigCount();
        worlds = new World[Globals.SIMULATION_ROUND];

        for (int i = 0, worldsLength = worlds.length; i < worldsLength; i++) {
            worlds[i] = new World(i, this, trustConfig.getNextConfig());
        }

        //============================//============================ Initializing statistics report file
        if (Config.STATISTICS_IS_GENERATE) {

            String statName = Globals.STATS_FILE_NAME;

            System.out.println("Statistics file name: " + statName);

            ProjectPath.instance().createDirectoryIfNotExist(ProjectPath.instance().statisticsDir() + "/" + statName);

            //-- Copying environment-x.json to statistics directory
            Path sourcePath = Paths.get(Globals.SocietyDataFilePath);
            Path targetPath = Paths.get(ProjectPath.instance().statisticsDir() + "/" + statName + "/" + Globals.SocietyDataFileName);
            Files.copy(sourcePath, targetPath);

            //-- Copying society-config-x.json to statistics directory
            sourcePath = Paths.get(Globals.SocietyConfigFilePath);
            targetPath = Paths.get(ProjectPath.instance().statisticsDir() + "/" + statName + "/" + Globals.SocietyConfigFileName);
            Files.copy(sourcePath, targetPath);

            //-- Copying trust-config-x.json to statistics directory
            sourcePath = Paths.get(Globals.TrustConfigFilePath);
            targetPath = Paths.get(ProjectPath.instance().statisticsDir() + "/" + statName + "/" + Globals.TrustConfigFileName);
            Files.copy(sourcePath, targetPath);

            //-- Copying simulation-config-x.json to statistics directory
            sourcePath = Paths.get(Globals.SimulationConfigFilePath);
            targetPath = Paths.get(ProjectPath.instance().statisticsDir() + "/" + statName + "/" + Globals.SimulationConfigFileName);
            Files.copy(sourcePath, targetPath);

        }

        if (Config.INT_DRAWING_SHOW_ENABLED()) {
            intDrawingWindowRunner = new IntDrawingWindowRunner(worlds, trustConfig);
            intDrawingWindowRunner.initDrawingWindows();
            intDrawingWindowRunner.start();
        }

    }

    private void updateSimulationConfig() {
        Config.WORLD_LIFE_TIME = simulationConfig.getWorldLifeTime();
        Config.RUNTIME_THREAD_COUNT = simulationConfig.getRuntimeThreadCount();
        Config.THEME_MODE = simulationConfig.getThemeMode();

        Config.WORLD_SLEEP_MILLISECOND = simulationConfig.getWorldSleepMillisecond();
        Config.WORLD_SLEEP_MILLISECOND_IN_PAUSE = simulationConfig.getWorldSleepMillisecondInPause();
        Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING = simulationConfig.getWorldSleepMillisecondForDrawing();
        Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING_IN_PAUSE = simulationConfig.getWorldSleepMillisecondForDrawingInPause();

        Config.IMPROVEMENT_ANALYSIS_COEFFICIENT = simulationConfig.getImprovementAnalysisCoefficient();

        Config.STATISTICS_AVERAGE_TIME_WINDOW = simulationConfig.getStatisticsAverageTimeWindow();
        Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_RESISTANCE = simulationConfig.getStatisticsAverageTimeWindowForResistance();
        Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION = simulationConfig.getStatisticsAverageTimeWindowForCollaboration();
        Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_DOMINANCE = simulationConfig.getStatisticsAverageTimeWindowForDominance();
        Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_ROC = simulationConfig.getStatisticsAverageTimeWindowForRoc();
        Config.STATISTICS_HYPOCRITE_DIAGNOSIS_THRESHOLD = simulationConfig.getStatisticsHypocriteDiagnosisThreshold();
        Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT = simulationConfig.getStatisticsHypocriteResistanceCount();

        Config.STATISTICS_SCALE_UP_Y_AXIS_NUMBER = simulationConfig.getStatisticsScaleUpYAxisNumber();

        Config.ROUTING_STAY_IN_TARGET_TIME = simulationConfig.getRoutingStayInTargetTime();
        Config.ROUTING_STAY_IN_PITFALL_TIME = simulationConfig.getRoutingStayInPitfallTime();

        Config.TRUST_MATRIX_IS_ON = simulationConfig.isOptimizeMemory();

        Config.TURBO_CERTIFIED_DAGRA_SINGLE_UPDATE_MULTIPLE_CLONE = simulationConfig.isTurboCertifiedDagraSingleUpdateMultipleClone();


        Config.DRAWING_WINDOWS_MAXIMIZING = simulationConfig.isDrawingWindowsMaximizing();
        Config.DRAWING_WINDOWS_DEFAULT_PAINT_VISIBILITY = simulationConfig.isDrawingWindowsDefaultPaintVisibility();


        Config.DRAWING_SHOW_stateMachineWindow = simulationConfig.isDrawingShowStateMachineWindow();
        Config.DRAWING_SHOW_travelStatsLinearDrawingWindow = simulationConfig.isDrawingShowTravelStatsLinearDrawingWindow();
        Config.DRAWING_SHOW_travelHistoryBarDrawingWindow = simulationConfig.isDrawingShowTravelHistoryBarDrawingWindow();

        Config.DRAWING_SHOW_trustMatrixDrawingWindow = simulationConfig.isDrawingShowTrustMatrixDrawingWindow();
        Config.DRAWING_SHOW_trustStatsLinearDrawingWindow = simulationConfig.isDrawingShowTrustStatsLinearDrawingWindow();
        Config.DRAWING_SHOW_trustRecogniseLinearDrawingWindow = simulationConfig.isDrawingShowTrustRecogniseLinearDrawingWindow();
        Config.DRAWING_SHOW_trustAnalysisLinearDrawingWindow = simulationConfig.isDrawingShowTrustAnalysisLinearDrawingWindow();

        Config.DRAWING_SHOW_experienceBarDrawingWindow = simulationConfig.isDrawingShowExperienceBarDrawingWindow();
        Config.DRAWING_SHOW_indirectExperienceBarDrawingWindow = simulationConfig.isDrawingShowIndirectExperienceBarDrawingWindow();

        Config.DRAWING_SHOW_observationBarDrawingWindow = simulationConfig.isDrawingShowObservationBarDrawingWindow();
        Config.DRAWING_SHOW_indirectObservationBarDrawingWindow = simulationConfig.isDrawingShowIndirectObservationBarDrawingWindow();

        Config.DRAWING_SHOW_recommendationBarDrawingWindow = simulationConfig.isDrawingShowRecommendationBarDrawingWindow();

        Config.DRAWING_SHOW_dagGraphDrawingWindow = simulationConfig.isDrawingShowDagGraphDrawingWindow();

        Config.INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow = simulationConfig.isIntDrawingShowIntTravelStatsLinearDrawingWindow();
        Config.INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow = simulationConfig.isIntDrawingShowIntTrustAnalysisLinearDrawingWindow();
        Config.INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow = simulationConfig.isIntDrawingShowIntTrustStatsLinearDrawingWindow();
        Config.INT_DRAWING_SHOW_IntResistancePerNumberLinearDrawingWindow = simulationConfig.isIntDrawingShowIntResistancePerNumberLinearDrawingWindow();
        Config.INT_DRAWING_SHOW_RocPointDrawingWindow = simulationConfig.isIntDrawingShowRocPointDrawingWindow();
        Config.INT_DRAWING_SHOW_HonestCollaborationLinearDrawingWindow = simulationConfig.isIntDrawingShowHonestCollaborationLinearDrawingWindow();
        Config.INT_DRAWING_SHOW_DishonestCollaborationLinearDrawingWindow = simulationConfig.isIntDrawingShowDishonestCollaborationLinearDrawingWindow();
        Config.INT_DRAWING_SHOW_HypocriteCollaborationLinearDrawingWindow = simulationConfig.isIntDrawingShowHypocriteCollaborationLinearDrawingWindow();
        Config.INT_DRAWING_SHOW_ResistanceLinearDrawingWindow = simulationConfig.isIntDrawingShowResistanceLinearDrawingWindow();
        Config.INT_DRAWING_SHOW_FluctuationLinearDrawingWindow = simulationConfig.isIntDrawingShowFluctuationLinearDrawingWindow();
        Config.INT_DRAWING_SHOW_DominanceLinearDrawingWindow = simulationConfig.isIntDrawingShowDominanceLinearDrawingWindow();
        Config.INT_DRAWING_SHOW_DagStatLinearDrawingWindow = simulationConfig.isIntDrawingShowDagraStatLinearDrawingWindow();


        Config.STATISTICS_IS_GENERATE = simulationConfig.isStatisticsIsGenerate();
        Config.STATISTICS_IS_SAVE_IMAGE = simulationConfig.isStatisticsIsSaveImage();
        Config.TRUST_MATRIX_IS_GENERATE = simulationConfig.isTrustMatrixIsGenerate();
        Config.TRUST_MATRIX_IS_ON = simulationConfig.isTrustMatrixIsOn();

    }

    private void reloadEnvironmentFromFile() throws FileNotFoundException {

        fileReader = new FileReader(Globals.SocietyDataFilePath);
        loadedSocietyFromJson = gson.fromJson(fileReader, Environment.class);

        if (loadedSocietyFromJson == null) {
            System.out.println(">> Simulator.reInit");
            System.out.println("> Error: environment not found.");
            return;
        }

        System.out.println("> Environment reloaded from file. simulationTimer");
    }


    public void simulate() throws Exception {

        MakeSound sound = new MakeSound();

        // playing start sound
        Thread.sleep(900);
        sound.playSound(ProjectPath.instance().alertStartFile());

        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        startTime = new Date().getTime();

        for (World world : worlds) {
            if (Globals.SIMULATION_TIMER > 0) {
                reloadEnvironmentFromFile();
            }
            Globals.reset();
            world.init(loadedSocietyFromJson);
            world.run();
            Globals.SIMULATION_TIMER++;
            if (Globals.SIMULATION_ROUND <= Globals.SIMULATION_TIMER) {
                break;
            }
            world.destroy();
            OutLog____.pl("s");
            sound.playSound(ProjectPath.instance().alertMidFile());

        }

        Globals.SIMULATION_TIMER--;
        if (Config.STATISTICS_IS_GENERATE && Config.STATISTICS_IS_SAVE_IMAGE && Config.INT_DRAWING_SHOW_ENABLED()) {
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

        endTime = new Date().getTime();

        timeReport();

        improvementAnalysis();

        sound.playSound(ProjectPath.instance().alertEndFile());
        Thread.sleep(200);
        sound.playSound(ProjectPath.instance().alertEndFile());
        Thread.sleep(200);
        sound.playSound(ProjectPath.instance().alertEndFile());
        Thread.sleep(200);

    }

    private void timeReport() {
        System.out.println("\n  > simulation duration: [" + new DecimalFormat().format((endTime - startTime) / 1000) + "] seconds");
    }


    //============================//============================//============================

    public void improvementAnalysis() {

        //============================//============================ 1
        World baseWorld = worlds[0];
        float coeff = Config.IMPROVEMENT_ANALYSIS_COEFFICIENT;

        int overallAgentInTarget = 0;
        int overallAgentInPitfall = 0;
        int overallTrustAccuracy = 0;
        int overallTrustSensitivity = 0;
        int overallTrustSpecificity = 0;
        int overallTrustToHonest = 0;
        int overallTrustToHypocrite = 0;
        int overallHonestCollab = 0;
        int overallHypocriteCollab = 0;
        int overallResistance = 0;
        int overallIgnorePos = 0;
        int overallIgnoreNeg = 0;

        int[] diffAgentInTarget = new int[worlds.length];
        int[] diffAgentInPitfall = new int[worlds.length];
        int[] diffTrustAccuracy = new int[worlds.length];
        int[] diffTrustSensitivity = new int[worlds.length];
        int[] diffTrustSpecificity = new int[worlds.length];
        int[] diffTrustToHonest = new int[worlds.length];
        int[] diffTrustToHypocrite = new int[worlds.length];
        int[] diffHonestCollab = new int[worlds.length];
        int[] diffHypocriteCollab = new int[worlds.length];
        int[] diffResistance = new int[worlds.length];
        int[] diffIgnorePos = new int[worlds.length];
        int[] diffIgnoreNeg = new int[worlds.length];


        Arrays.fill(diffAgentInTarget, 0);
        Arrays.fill(diffAgentInPitfall, 0);
        Arrays.fill(diffTrustAccuracy, 0);
        Arrays.fill(diffTrustSensitivity, 0);
        Arrays.fill(diffTrustSpecificity, 0);
        Arrays.fill(diffTrustToHonest, 0);
        Arrays.fill(diffTrustToHypocrite, 0);
        Arrays.fill(diffHonestCollab, 0);
        Arrays.fill(diffHypocriteCollab, 0);
        Arrays.fill(diffResistance, 0);
        Arrays.fill(diffIgnorePos, 0);
        Arrays.fill(diffIgnoreNeg, 0);


        //============================//============================ 2
        for (int worldIdx = 1, worldsLength = worlds.length; worldIdx < worldsLength; worldIdx++) {
            World thisWorld = worlds[worldIdx];
            for (int runIdx = 0, len = Config.WORLD_LIFE_TIME; runIdx < len; runIdx++) {
                float effect = 1 + (coeff * runIdx);

                WorldStatistics thisWorldStat = thisWorld.getWdStatistics()[runIdx];
                WorldStatistics baseWorldStat = baseWorld.getWdStatistics()[runIdx];

                diffAgentInTarget[worldIdx] += (int) (effect *
                        (thisWorldStat.getTimedAvgAgentTarget() - baseWorldStat.getTimedAvgAgentTarget())
                );

                diffAgentInPitfall[worldIdx] += (int) (-effect *
                        (thisWorldStat.getTimedAvgAgentInPitfall() - baseWorldStat.getTimedAvgAgentInPitfall())
                );

                diffTrustAccuracy[worldIdx] += (int) (effect *
                        (thisWorldStat.getAllTrustAccuracyI100() - baseWorldStat.getAllTrustAccuracyI100())
                );

                diffTrustSensitivity[worldIdx] += (int) (effect *
                        (thisWorldStat.getAllTrustSensitivityI100() - baseWorldStat.getAllTrustSensitivityI100())
                );

                diffTrustSpecificity[worldIdx] += (int) (effect *
                        (thisWorldStat.getAllTrustSpecificityI100() - baseWorldStat.getAllTrustSpecificityI100())
                );

                diffTrustToHonest[worldIdx] += (int) (effect *
                        (thisWorldStat.getTimeAvgTrustToHonest() - baseWorldStat.getTimeAvgTrustToHonest())
                );

                diffTrustToHypocrite[worldIdx] += (int) (-effect *
                        (thisWorldStat.getTimeAvgTrustToHypocrite() - baseWorldStat.getTimeAvgTrustToHypocrite())
                );

                diffHonestCollab[worldIdx] += (int) (effect *
                        (thisWorldStat.getStatisticsCollab().getAvgHonestTrustPerCollab100() - baseWorldStat.getStatisticsCollab().getAvgHonestTrustPerCollab100())
                );

                diffHypocriteCollab[worldIdx] += (int) (-effect *
                        (thisWorldStat.getStatisticsCollab().getAvgHypocriteTrustPerCollab100() - baseWorldStat.getStatisticsCollab().getAvgHypocriteTrustPerCollab100())
                );

                diffResistance[worldIdx] += (int) (effect *
                        (thisWorldStat.getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstAll() - baseWorldStat.getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstAll())
                );

                diffIgnorePos[worldIdx] += (int) (effect *
                        (thisWorldStat.getStatisticsHypo().getAvgHypoIgnoredPos() - baseWorldStat.getStatisticsHypo().getAvgHypoIgnoredPos())
                );

                diffIgnoreNeg[worldIdx] += (int) (effect *
                        (thisWorldStat.getStatisticsHypo().getAvgHypoIgnoredNeg() - baseWorldStat.getStatisticsHypo().getAvgHypoIgnoredNeg())
                );

            }
        }

        //============================//============================ 3

        System.out.println("\n==================================================");
        System.out.println("Improvements Analysis\n");


        overallAgentInTarget = printImprovementInfo("AgentInTarget", diffAgentInTarget);
        overallAgentInPitfall = printImprovementInfo("AgentInPitfall", diffAgentInPitfall);
        overallTrustAccuracy = printImprovementInfo("TrustAccuracy", diffTrustAccuracy);
        overallTrustSensitivity = printImprovementInfo("TrustSensitivity", diffTrustSensitivity);
        overallTrustSpecificity = printImprovementInfo("TrustSpecificity", diffTrustSpecificity);
        overallTrustToHonest = printImprovementInfo("TrustToHonest", diffTrustToHonest);
        overallTrustToHypocrite = printImprovementInfo("TrustToHypocrite", diffTrustToHypocrite);
        overallHonestCollab = printImprovementInfo("HonestCollab", diffHonestCollab);
        overallHypocriteCollab = printImprovementInfo("HypocriteCollab", diffHypocriteCollab);
        overallResistance = printImprovementInfo("Resistance", diffResistance);
        overallIgnorePos = printImprovementInfo("IgnorePos", diffIgnorePos);
        overallIgnoreNeg = printImprovementInfo("IgnoreNeg", diffIgnoreNeg);

        System.out.println("\n ------------------------------------");

        ArrayList<PopulationBunch> bunches = loadedSocietyProfileFromJson.getBunches();

        for (PopulationBunch bunch : bunches) {
            System.out.print(bunch.getBehavior().getHonestPercent() + ",");
            System.out.print(bunch.getBehavior().getAdversaryPercent() + ",");
            System.out.print(bunch.getBehavior().getMischiefPercent() + ",");
            System.out.print(bunch.getBehavior().getHypocriteBehavior().getHonestPercent() + ",");
            System.out.print(bunch.getBehavior().getHypocriteBehavior().getAdversaryPercent() + ",");
            System.out.print(bunch.getBehavior().getHypocriteBehavior().getMischiefPercent() + ",");
            System.out.print(bunch.getBunchCount() + "\n");
        }

        printImprovementInfoBrief(diffAgentInTarget);
        printImprovementInfoBrief(diffAgentInPitfall);
        printImprovementInfoBrief(diffTrustAccuracy);
        printImprovementInfoBrief(diffTrustSensitivity);
        printImprovementInfoBrief(diffTrustSpecificity);
        printImprovementInfoBrief(diffTrustToHonest);
        printImprovementInfoBrief(diffTrustToHypocrite);
        printImprovementInfoBrief(diffHonestCollab);
        printImprovementInfoBrief(diffHypocriteCollab);
        printImprovementInfoBrief(diffResistance);
        printImprovementInfoBrief(diffIgnorePos);
        printImprovementInfoBrief(diffIgnoreNeg);


        System.out.println("\n" +
                overallAgentInTarget + "," +
                overallAgentInPitfall + "," +
                overallTrustAccuracy + "," +
                overallTrustSensitivity + "," +
                overallTrustSpecificity + "," +
                overallTrustToHonest + "," +
                overallTrustToHypocrite + "," +
                overallHonestCollab + "," +
                overallHypocriteCollab + "," +
                overallResistance + "," +
                overallIgnorePos + "," +
                overallIgnoreNeg + "," +
                Globals.STATS_FILE_NAME + "," +
                ProjectPath.instance().getTarget()
        );


        //============================//============================ 4

    }

    private int printImprovementInfo(String title, int[] diff) {
        int overall = 0;
        System.out.println("\n" + title);
        for (int i = 0; i < worlds.length; i++) {
            overall += diff[i];
            System.out.print(diff[i] + " > ");
        }
        System.out.println("|>> [" + overall + "]");

        return overall;

    }

    private void printImprovementInfoBrief(int[] diff) {
        for (int i = 0; i < worlds.length; i++) {
            System.out.print((i > 0 ? "|" : "") + diff[i]);
        }
        System.out.print(",");
    }


}
