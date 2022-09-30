package utils;

import _type.TtDiagramThemeMode;
import _type.TtSimulationMode;

public class Config {

    //============================//============================
    public static final String SocietyConfigFilePath = ProjectPath.instance().simulationSocietyConfigFile();
    public static final String SocietyConfigFileName = SocietyConfigFilePath.substring(SocietyConfigFilePath.lastIndexOf("/"));

    public static final String TrustConfigFilePath = ProjectPath.instance().simulationTrustConfigFile();
    public static final String TrustConfigFileName = TrustConfigFilePath.substring(TrustConfigFilePath.lastIndexOf("/"));

    public static final String EnvironmentDataFilePath = ProjectPath.instance().societyData();
    public static final String EnvironmentDataFileName = EnvironmentDataFilePath.substring(EnvironmentDataFilePath.lastIndexOf("/"));


    //============================ World
    public static final TtSimulationMode SIMULATION_MODE = TtSimulationMode.Consequence;

    public static int WORLD_LIFE_TIME = 255;                                 // Time of execution in each world
    public static int WORLD_SLEEP_MILLISECOND = 0;                          // Sleep time in each run of world
    public static int WORLD_SLEEP_MILLISECOND_IN_PAUSE = 200;              // Sleep time in each run of world when the world execution is PAUSED
    public static int WORLD_SLEEP_MILLISECOND_FOR_DRAWING = 500;            // Sleep time of drawing thread
    public static int WORLD_SLEEP_MILLISECOND_FOR_DRAWING_IN_PAUSE = 0;   // Sleep time of drawing thread when it is PAUSED.



    public static int STATE_TILE_WIDTH = 30;                            // used in state machine diagram

    public static int STATISTICS_AVERAGE_TIME_WINDOW = 10;
    public static int STATISTICS_AVERAGE_TIME_WINDOW_FOR_RESISTANCE = 5;
    public static int STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION = 10;
    public static int STATISTICS_AVERAGE_TIME_WINDOW_FOR_ROC = 10;
    public static int STATISTICS_HYPOCRITE_DIAGNOSIS_THRESHOLD = 3;   // After a number of fluctuation (this threshold) in scores (saved in descending normalized list: [L'']), the agent labeled as Hypocrite
    public static int STATISTICS_HYPOCRITE_RESISTANCE_COUNT = 9;     // the resistance_number count that will be saved in statistics and traced

    public static double STATISTICS_SCALE_UP_Y_AXIS_NUMBER = 4.0; // This is for scaling up result. i.e. 1 is presented in scale of this value


    public static final int ROUTING_STAY_IN_TARGET_TIME /*      */ = 1;
    public static final int ROUTING_STAY_IN_PITFALL_TIME /*     */ = 1;


    //============================ OPTIMIZATION
    public static boolean OPTIMIZE_MEMORY /*                 */ = true;      // destroying old data (for finished worlds) such as TrustMatrix

    //============================ TURBO
    public static boolean TURBO_CERTIFIED_DAGRA_SINGLE_UPDATE_MULTIPLE_CLONE /* */ = true;    // For fast processing- processing DaGra network for the first time and copying it to other networks

    //============================ Drawing Windows

    public static final TtDiagramThemeMode THEME_MODE /*            */ = TtDiagramThemeMode.Light;

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

    public static final boolean INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow /*           */ = true;
    public static final boolean INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow /*         */ = true;
    public static final boolean INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow /*            */ = true;
    public static final boolean INT_DRAWING_SHOW_IntResistancePerNumberLinearDrawingWindow /*   */ = true;
    public static final boolean INT_DRAWING_SHOW_RocPointDrawingWindow /*                       */ = false;
    public static final boolean INT_DRAWING_SHOW_HonestCollaborationLinearDrawingWindow /*      */ = true;
    public static final boolean INT_DRAWING_SHOW_HypocriteCollaborationLinearDrawingWindow /*   */ = true;
    public static final boolean INT_DRAWING_SHOW_ResistanceLinearDrawingWindow /*               */ = true;
    public static final boolean INT_DRAWING_SHOW_FluctuationLinearDrawingWindow /*              */ = true;

    //============================ Statistics
    public static final boolean STATISTICS_IS_GENERATE /*           */ = true;
    public static final boolean STATISTICS_IS_SAVE_IMAGE /*         */ = false;
    public static final boolean TRUST_MATRIX_IS_GENERATE /*         */ = true;
    public static final boolean TRUST_MATRIX_IS_ON /*               */ = true;

}
