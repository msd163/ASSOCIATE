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

    public static int WORLD_LIFE_TIME = 4000;          // Time of execution in each world
    public static int WORLD_SLEEP_MILLISECOND = 1;      // Sleep time in each run of world

    public final static int EPISODE_TIMOUT = 200;

    public static int STATE_TILE_WIDTH = 30;

    public static int STATISTICS_AVERAGE_TIME_WINDOW = 50;


    public static final int ROUTING_STAY_IN_TARGET_TIME = 1;
    public static final int ROUTING_STAY_IN_PITFALL_TIME = 1;

    //============================ Trust
    public static int TRUST_CERTIFIED_HONEST_PERCENTAGE_THRESHOLD = 70;

    public static int DAGRA_REQUEST_STAGE__ALLOWED_REQUEST_PERIOD = 100;                 // The new certification request is sent in this period times
    public static int DAGRA_REQUEST_STAGE__NUMBER_OF_REQUEST_IN_EACH_PERIOD = 2;       // The new certification request is sent in this period times

    public static int DAGRA_REQUEST_STAGE__CERTIFICATIONS_TO_BE_SIGNED = 2;
    public static int DAGRA_REQUEST_STAGE__CERTIFICATIONS_TO_BE_VERIFIED = 1;
    public static int DAGRA_ACCEPT_STAGE__EXPIRE_TIME_THRESHOLD = 1000;
    public static int DAGRA_ACCEPT_STAGE__NEEDED_SIGNS = 1;
    public static int DAGRA_ACCEPT_STAGE__NEEDED_VERIFICATIONS = 1;

    // public static boolean TRUST_RECOMMENDATION_SEND_NEGATIVE = true;
    //  public static float TRUST_LEVEL_VALUE_IGNORE_THRESHOLD = 0.001f;    // A threshold value that identify trust levels which are less than it will be ignored in trust calculations
    //  public static int TRUST_MAXIMUM_CONSIDERED_ROUTING_HELP_IN_TRUST_MECHANISM = 6;
    //  public static boolean TRUST_VALIDATING_ROUTING_HELPER_ACCORDING_OBSERVATIONS = false;

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

    //============================ Statistics
    public static final boolean STATISTICS_IS_GENERATE /*           */ = true;
    public static final boolean TRUST_MATRIX_IS_GENERATE /*         */ = true;

}
