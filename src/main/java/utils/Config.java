package utils;

import _type.TtDiagramThemeMode;
import _type.TtSimulationMode;

public class Config {

    //============================//============================
    public static final String SocietyConfigFilePath = ProjectPath.instance().simulationSocietyConfigFile();
    public static final String SocietyConfigFileName = SocietyConfigFilePath.substring(SocietyConfigFilePath.lastIndexOf("/"));

    public static final String TrustConfigFilePath = ProjectPath.instance().simulationTrustConfigFile();
    public static final String TrustConfigFileName = TrustConfigFilePath.substring(TrustConfigFilePath.lastIndexOf("/"));

    public static final String SocietyDataFilePath = ProjectPath.instance().societyData();
    public static final String SocietyDataFileName = SocietyDataFilePath.substring(SocietyDataFilePath.lastIndexOf("/"));

    public static final String SocietyDataSimProfileFilePath = ProjectPath.instance().societyDataSimPro();


    //============================ World
    public static final TtSimulationMode SIMULATION_MODE = TtSimulationMode.Consequence;

    public static int WORLD_LIFE_TIME = 313;                                 // Time of execution in each world
    public static int WORLD_SLEEP_MILLISECOND = 0;                          // Sleep time in each run of world
    public static int WORLD_SLEEP_MILLISECOND_IN_PAUSE = 500;              // Sleep time in each run of world when the world execution is PAUSED
    public static int WORLD_SLEEP_MILLISECOND_FOR_DRAWING = 2000;            // Sleep time of drawing thread
    public static int WORLD_SLEEP_MILLISECOND_FOR_DRAWING_IN_PAUSE = 200;   // Sleep time of drawing thread when it is PAUSED.


    public static float IMPROVEMENT_ANALYSIS_COEFFICIENT = 0.0001f;                            // used in state machine diagram

    public static int STATE_TILE_WIDTH = 30;                            // used in state machine diagram

    public static int STATISTICS_AVERAGE_TIME_WINDOW = 10;
    public static int STATISTICS_AVERAGE_TIME_WINDOW_FOR_RESISTANCE = 5;
    public static int STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION = 10;
    public static int STATISTICS_AVERAGE_TIME_WINDOW_FOR_ROC = 10;
    public static int STATISTICS_HYPOCRITE_DIAGNOSIS_THRESHOLD = 3;   // After a number of fluctuation (this threshold) in scores (saved in descending normalized list: [L'']), the agent labeled as Hypocrite
    public static int STATISTICS_HYPOCRITE_RESISTANCE_COUNT = 9;     // the resistance_number count that will be saved in statistics and traced

    public static double STATISTICS_SCALE_UP_Y_AXIS_NUMBER = 1.0; // This is for scaling up result. i.e. 1 is presented in scale of this value


    public static final int ROUTING_STAY_IN_TARGET_TIME /*      */ = 1;
    public static final int ROUTING_STAY_IN_PITFALL_TIME /*     */ = 1;


    //============================ OPTIMIZATION
    public static boolean OPTIMIZE_MEMORY /*                 */ = true;      // destroying old data (for finished worlds) such as TrustMatrix

    //============================ TURBO
    public static boolean TURBO_CERTIFIED_DAGRA_SINGLE_UPDATE_MULTIPLE_CLONE /* */ = true;    // For fast processing- processing DaGra network for the first time and copying it to other networks

    //============================ Drawing Windows

    public static final TtDiagramThemeMode THEME_MODE /*            */ = TtDiagramThemeMode.Light;

    public static final boolean DRAWING_WINDOWS_MAXIMIZING /*                      */ = true;
    public static final boolean DRAWING_WINDOWS_DEFAULT_PAINT_VISIBILITY /*        */ = false;

    //============================ Drawing Windows

    public static final boolean DRAWING_SHOW_stateMachineWindow /*                  */ = false;
    public static final boolean DRAWING_SHOW_travelStatsLinearDrawingWindow /*      */ = false;
    public static final boolean DRAWING_SHOW_travelHistoryBarDrawingWindow /*       */ = false;

    public static final boolean DRAWING_SHOW_trustMatrixDrawingWindow /*            */ = false;
    public static final boolean DRAWING_SHOW_trustStatsLinearDrawingWindow/*        */ = false;
    public static final boolean DRAWING_SHOW_trustRecogniseLinearDrawingWindow /*   */ = false;
    public static final boolean DRAWING_SHOW_trustAnalysisLinearDrawingWindow/*     */ = false;

    public static final boolean DRAWING_SHOW_experienceBarDrawingWindow /*          */ = false;
    public static final boolean DRAWING_SHOW_indirectExperienceBarDrawingWindow /*  */ = false;

    public static final boolean DRAWING_SHOW_observationBarDrawingWindow /*         */ = false;
    public static final boolean DRAWING_SHOW_indirectObservationBarDrawingWindow /* */ = false;

    public static final boolean DRAWING_SHOW_recommendationBarDrawingWindow /*      */ = false;

    //============================ Integrated Drawing Windows in Simulation Rounds

    public static final boolean INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow /*           */ = false;
    public static final boolean INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow /*         */ = false;
    public static final boolean INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow /*            */ = false;
    public static final boolean INT_DRAWING_SHOW_IntResistancePerNumberLinearDrawingWindow /*   */ = false;
    public static final boolean INT_DRAWING_SHOW_RocPointDrawingWindow /*                       */ = false;
    public static final boolean INT_DRAWING_SHOW_HonestCollaborationLinearDrawingWindow /*      */ = false;
    public static final boolean INT_DRAWING_SHOW_HypocriteCollaborationLinearDrawingWindow /*   */ = false;
    public static final boolean INT_DRAWING_SHOW_ResistanceLinearDrawingWindow /*               */ = false;
    public static final boolean INT_DRAWING_SHOW_FluctuationLinearDrawingWindow /*              */ = false;

    //============================ Statistics
    public static final boolean STATISTICS_IS_GENERATE /*           */ = true;
    public static final boolean STATISTICS_IS_SAVE_IMAGE /*         */ = false;
    public static final boolean TRUST_MATRIX_IS_GENERATE /*         */ = true;
    public static final boolean TRUST_MATRIX_IS_ON /*               */ = true;

    public static  final int RUNTIME_THREAD_AGENT_COUNT = 8;


    public static final boolean DRAWING_SHOW_ENABLED =
            Config.DRAWING_SHOW_stateMachineWindow
                    || Config.DRAWING_SHOW_travelStatsLinearDrawingWindow
                    || Config.DRAWING_SHOW_travelHistoryBarDrawingWindow
                    || Config.DRAWING_SHOW_trustMatrixDrawingWindow
                    || Config.DRAWING_SHOW_trustStatsLinearDrawingWindow
                    || Config.DRAWING_SHOW_trustRecogniseLinearDrawingWindow
                    || Config.DRAWING_SHOW_trustAnalysisLinearDrawingWindow
                    || Config.DRAWING_SHOW_experienceBarDrawingWindow
                    || Config.DRAWING_SHOW_indirectExperienceBarDrawingWindow
                    || Config.DRAWING_SHOW_observationBarDrawingWindow
                    || Config.DRAWING_SHOW_indirectObservationBarDrawingWindow
                    || Config.DRAWING_SHOW_recommendationBarDrawingWindow;


    public static final boolean INT_DRAWING_SHOW_ENABLED =
            INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow
                    || INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow
                    || INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow
                    || INT_DRAWING_SHOW_IntResistancePerNumberLinearDrawingWindow
                    || INT_DRAWING_SHOW_RocPointDrawingWindow
                    || INT_DRAWING_SHOW_HonestCollaborationLinearDrawingWindow
                    || INT_DRAWING_SHOW_HypocriteCollaborationLinearDrawingWindow
                    || INT_DRAWING_SHOW_ResistanceLinearDrawingWindow
                    || INT_DRAWING_SHOW_FluctuationLinearDrawingWindow;


}
