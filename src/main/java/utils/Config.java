package utils;

import _type.TtSimulationMode;

public class Config {

    //============================//============================
    public static final String SimulatingFilePath = ProjectPath.instance().simulationProfileFile();
    public static final String SimulatingFileName = SimulatingFilePath.substring(SimulatingFilePath.lastIndexOf("/"));

    public static final String SimulationConfigFilePath = ProjectPath.instance().simulationConfigFile();
    public static final String SimulationConfigFileName = SimulationConfigFilePath.substring(SimulationConfigFilePath.lastIndexOf("/"));

    public static final String EnvironmentDataFilePath = ProjectPath.instance().environmentData();
    public static final String EnvironmentDataFileName = EnvironmentDataFilePath.substring(EnvironmentDataFilePath.lastIndexOf("/"));


    //============================ World
    public static final TtSimulationMode SIMULATION_MODE = TtSimulationMode.Consequence;

    public static int WORLD_LIFE_TIME = 222;          // Time of execution in each world
    public static int WORLD_SLEEP_MILLISECOND = 1;      // Sleep time in each run of world

    public final static int EPISODE_TIMOUT = 200;

    public static int STATE_TILE_WIDTH = 30;

    public static int STATISTICS_AVERAGE_TIME_WINDOW = 50;


    public static final int ROUTING_STAY_IN_TARGET_TIME = 1;
    public static final int ROUTING_STAY_IN_PITFALL_TIME = 1;

    //============================ Trust
    public static int TRUST_CERTIFIED_HONEST_PERCENTAGE_THRESHOLD = 30;
    public static int TRUST_CERTIFIED_DAGRA_NUMBER_OF_SIGN_TRY = 20;
    public static int TRUST_CERTIFIED_DAGRA_NUMBER_OF_VERIFICATION_TRY = 20;

    //============================ Drawing Windows

    public static final boolean DRAWING_SHOW_stateMachineWindow /*                  */ = false;
    public static final boolean DRAWING_SHOW_travelStatsLinearDrawingWindow /*      */ = false;
    public static final boolean DRAWING_SHOW_travelHistoryBarDrawingWindow /*       */ = false;

    public static final boolean DRAWING_SHOW_trustMatrixDrawingWindow /*            */ = true;
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

    //============================ Statistics
    public static final boolean STATISTICS_IS_GENERATE /*           */ = false;
    public static final boolean TRUST_MATRIX_IS_GENERATE /*         */ = false;

}
