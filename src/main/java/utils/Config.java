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

    public static int WORLD_LIFE_TIME = 4000;       // Time of execution in each world
    public static int WORLD_SLEEP_MILLISECOND = 0;      // Sleep time in each run of world

    public final static int EPISODE_TIMOUT = 50;

    public static int STATE_TILE_WIDTH = 30;

    public static int STATISTICS_AVERAGE_TIME_WINDOW = 50;


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

    public static final boolean DRAWING_SHOW_experienceBarDrawingWindow /*          */ = true;
    public static final boolean DRAWING_SHOW_indirectExperienceBarDrawingWindow /*  */ = true;

    public static final boolean DRAWING_SHOW_observationBarDrawingWindow /*         */ = true;
    public static final boolean DRAWING_SHOW_indirectObservationBarDrawingWindow /* */ = true;

    public static final boolean DRAWING_SHOW_recommendationBarDrawingWindow /*      */ = true;

    //============================ Integrated Drawing Windows in Simulation Rounds

    public static final boolean INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow /*           */ = false;
    public static final boolean INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow /*         */ = false;
    public static final boolean INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow /*            */ = false;

    //============================ Statistics
    public static final boolean STATISTICS_IS_GENERATE /*           */ = true;
    public static final boolean TRUST_MATRIX_IS_GENERATE /*         */ = true;
    public static final boolean TRUST_MATRIX_IS_ON /*               */ = true;

}
