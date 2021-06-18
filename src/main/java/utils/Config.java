package utils;

public class Config {

    //============================//============================
    public static final String SimulatingFilePath = ProjectPath.instance().simulationProfileFile();
    public static final String SimulatingFileName = SimulatingFilePath.substring(SimulatingFilePath.lastIndexOf("/"));

    public static final String SimulationConfigFilePath = ProjectPath.instance().simulationConfigFile();
    public static final String SimulationConfigFileName = SimulationConfigFilePath.substring(SimulationConfigFilePath.lastIndexOf("/"));

    public static final String EnvironmentDataFilePath = ProjectPath.instance().environmentData();
    public static final String EnvironmentDataFileName = EnvironmentDataFilePath.substring(EnvironmentDataFilePath.lastIndexOf("/"));


    //============================ World
    public static int WORLD_LIFE_TIME = 3000;          // Time of execution in each world
    public static int WORLD_SLEEP_MILLISECOND = 1;     // Sleep time in each run of world

    public final static int EPISODE_TIMOUT = 200;

    public static int STATE_TILE_WIDTH = 40;

    //============================ Drawing Windows

    public static final boolean DRAWING_SHOW_STATE_MACHINE /*               */ = false;
    public static final boolean DRAWING_SHOW_STAT_OF_ENV /*                 */ = false;
    public static final boolean DRAWING_SHOW_STATS_OF_TRUST/*               */ = false;
    public static final boolean DRAWING_SHOW_TRUST_MATRIX /*                */ = false;
    public static final boolean DRAWING_SHOW_STATS_OF_PO_NE /*              */ = false;
    public static final boolean DRAWING_SHOW_ANALYSIS_OF_TRUST_PARAM/*      */ = false;
    public static final boolean DRAWING_SHOW_AGENT_TRAVEL_INFO /*           */ = false;
    public static final boolean DRAWING_SHOW_AGENT_TRUST_DATA /*            */ = false;
    public static final boolean DRAWING_SHOW_AGENT_RECOMMENDATION_DATA /*   */ = false;
    public static final boolean DRAWING_SHOW_AGENT_OBSERVATION_DATA /*      */ = false;

    //============================ Integrated Drawing Windows in Simulation Rounds

    public static final boolean INT_DRAWING_SHOW_STAT_OF_ENV /*          */ = true;

    //============================ Statistics
    public static final boolean STATISTICS_IS_GENERATE /*           */ = true;
    public static final boolean TRUST_MATRIX_IS_GENERATE /*         */ = true;

}
