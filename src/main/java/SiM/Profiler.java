package SiM;

import com.google.gson.Gson;
import core.config.simulation.SimulationConfig;
import core.config.society.SocietyConfig;
import core.config.trust.TrustConfig;
import WSM.society.agent.World;
import WSM.society.environment.Environment;
import core.utils.Config;
import core.utils.Globals;
import core.utils.ProjectPath;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Profiler {


    public Profiler(Engine engine) {
        this.engine = engine;
    }

    private final Engine engine;

    protected Environment loadedSocietyFromJson;

    protected SocietyConfig loadedSocietyProfileFromJson;
    private TrustConfig trustConfig;
    private SimulationConfig simulationConfig;


    private FileReader fileReader;
    private final Gson gson = new Gson();


    public TrustConfig getTrustConfig() {
        return trustConfig;
    }

    protected void reloadEnvironmentFromFile() throws FileNotFoundException {

        fileReader = new FileReader(Globals.SocietyDataFilePath);
        loadedSocietyFromJson = gson.fromJson(fileReader, Environment.class);

        if (loadedSocietyFromJson == null) {
            System.out.println(">> Simulator.reInit");
            System.out.println("> Error: environment not found.");
            return;
        }

        System.out.println("> Environment reloaded from file. simulationTimer");
    }

    protected void init() throws Exception {
        //============================//============================ Loading Environment from file
        fileReader = new FileReader(Globals.SocietyDataFilePath);
        loadedSocietyFromJson = gson.fromJson(fileReader, Environment.class);

        if (loadedSocietyFromJson == null) {
            System.out.println(">> Simulator.init");
            System.out.println("> Error: environment not found.");
            return;
        }

        fileReader = new FileReader(Globals.SocietyDataSimProfileFilePath);
        loadedSocietyProfileFromJson = gson.fromJson(fileReader, SocietyConfig.class);

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
        engine.worlds = new World[Globals.SIMULATION_ROUND];

        for (int i = 0, worldsLength = engine.worlds.length; i < worldsLength; i++) {
            engine.worlds[i] = new World(i, engine, trustConfig.getNextConfig());
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
        Config.INT_DRAWING_SHOW_IntTrustRecogniseLinearDrawingWindow = simulationConfig.isIntDrawingShowIntTrustRecogniseLinearDrawingWindow();
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


}
