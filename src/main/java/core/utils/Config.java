package core.utils;

import _type.TtDiagramThemeMode;
import _type.TtSimulationMode;

public class Config {

    //============================

    public static int WORLD_LIFE_TIME;                                 // Time of execution in each world

    public static int RUNTIME_THREAD_COUNT;

    public static TtDiagramThemeMode THEME_MODE;

    //============================ World

    public static int WORLD_SLEEP_MILLISECOND;                          // Sleep time in each run of world
    public static int WORLD_SLEEP_MILLISECOND_IN_PAUSE;              // Sleep time in each run of world when the world execution is PAUSED
    public static int WORLD_SLEEP_MILLISECOND_FOR_DRAWING;            // Sleep time of drawing thread
    public static int WORLD_SLEEP_MILLISECOND_FOR_DRAWING_IN_PAUSE;   // Sleep time of drawing thread when it is PAUSED.


    public static float IMPROVEMENT_ANALYSIS_COEFFICIENT;                            // used in state machine diagram

    public static int STATISTICS_AVERAGE_TIME_WINDOW;
    public static int STATISTICS_AVERAGE_TIME_WINDOW_FOR_RESISTANCE;
    public static int STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION;
    public static int STATISTICS_AVERAGE_TIME_WINDOW_FOR_DOMINANCE;
    public static int STATISTICS_AVERAGE_TIME_WINDOW_FOR_ROC;

    public static int STATISTICS_HYPOCRITE_DIAGNOSIS_THRESHOLD;               // After a number of fluctuation (this threshold) in scores (saved in descending normalized list: [L'']), the agent labeled as Hypocrite
    public static int STATISTICS_HYPOCRITE_RESISTANCE_COUNT;                    // the resistance_number count that will be saved in statistics and traced

    public static double STATISTICS_SCALE_UP_Y_AXIS_NUMBER;                   // This is for scaling up result. i.e. 1 is presented in scale of this value


    public static int ROUTING_STAY_IN_TARGET_TIME;
    public static int ROUTING_STAY_IN_PITFALL_TIME;


    //============================ OPTIMIZATION
    public static boolean OPTIMIZE_MEMORY;      // destroying old data (for finished worlds) such as TrustMatrix

    //============================ TURBO
    public static boolean TURBO_CERTIFIED_DAGRA_SINGLE_UPDATE_MULTIPLE_CLONE;   // For fast processing. processing DaGra network for the first time and copying it to other networks

    //============================ Drawing Windows

    public static boolean DRAWING_WINDOWS_MAXIMIZING;
    public static boolean DRAWING_WINDOWS_DEFAULT_PAINT_VISIBILITY;

    //============================ Drawing Windows

    public static boolean DRAWING_SHOW_stateMachineWindow;
    public static boolean DRAWING_SHOW_travelStatsLinearDrawingWindow;
    public static boolean DRAWING_SHOW_travelHistoryBarDrawingWindow;

    public static boolean DRAWING_SHOW_trustMatrixDrawingWindow;
    public static boolean DRAWING_SHOW_trustStatsLinearDrawingWindow;
    public static boolean DRAWING_SHOW_trustRecogniseLinearDrawingWindow;
    public static boolean DRAWING_SHOW_trustAnalysisLinearDrawingWindow;

    public static boolean DRAWING_SHOW_experienceBarDrawingWindow;
    public static boolean DRAWING_SHOW_indirectExperienceBarDrawingWindow;

    public static boolean DRAWING_SHOW_observationBarDrawingWindow;
    public static boolean DRAWING_SHOW_indirectObservationBarDrawingWindow;

    public static boolean DRAWING_SHOW_recommendationBarDrawingWindow;

    public static boolean DRAWING_SHOW_dagGraphDrawingWindow;

    //============================ Integrated Drawing Windows in Simulation Rounds

    public static boolean INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow;
    public static boolean INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow;
    public static boolean INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow;
    public static boolean INT_DRAWING_SHOW_IntTrustRecogniseLinearDrawingWindow;
    public static boolean INT_DRAWING_SHOW_IntResistancePerNumberLinearDrawingWindow;
    public static boolean INT_DRAWING_SHOW_RocPointDrawingWindow;
    public static boolean INT_DRAWING_SHOW_HonestCollaborationLinearDrawingWindow;
    public static boolean INT_DRAWING_SHOW_DishonestCollaborationLinearDrawingWindow;
    public static boolean INT_DRAWING_SHOW_HypocriteCollaborationLinearDrawingWindow;
    public static boolean INT_DRAWING_SHOW_ResistanceLinearDrawingWindow;
    public static boolean INT_DRAWING_SHOW_FluctuationLinearDrawingWindow;
    public static boolean INT_DRAWING_SHOW_DominanceLinearDrawingWindow;
    public static boolean INT_DRAWING_SHOW_DagStatLinearDrawingWindow;

    //============================ Statistics
    public static boolean STATISTICS_IS_GENERATE;
    public static boolean STATISTICS_IS_SAVE_IMAGE;
    public static boolean TRUST_MATRIX_IS_GENERATE;
    public static boolean TRUST_MATRIX_IS_ON;


    public static boolean DRAWING_SHOW_ENABLED() {
        return
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
                        || Config.DRAWING_SHOW_recommendationBarDrawingWindow
                        || Config.DRAWING_SHOW_dagGraphDrawingWindow
                ;
    }

    public static boolean INT_DRAWING_SHOW_ENABLED() {
        return
                INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow
                        || INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow
                        || INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow
                        || INT_DRAWING_SHOW_IntTrustRecogniseLinearDrawingWindow
                        || INT_DRAWING_SHOW_IntResistancePerNumberLinearDrawingWindow
                        || INT_DRAWING_SHOW_RocPointDrawingWindow
                        || INT_DRAWING_SHOW_HonestCollaborationLinearDrawingWindow
                        || INT_DRAWING_SHOW_DishonestCollaborationLinearDrawingWindow
                        || INT_DRAWING_SHOW_HypocriteCollaborationLinearDrawingWindow
                        || INT_DRAWING_SHOW_ResistanceLinearDrawingWindow
                        || INT_DRAWING_SHOW_FluctuationLinearDrawingWindow
                        || INT_DRAWING_SHOW_DominanceLinearDrawingWindow
                        || INT_DRAWING_SHOW_DagStatLinearDrawingWindow
                ;

    }


    public static TtSimulationMode SIMULATION_MODE = TtSimulationMode.Consequence;

    public static int STATE_TILE_WIDTH = 30;                            // used in state machine diagram


}
