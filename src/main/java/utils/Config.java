package utils;

import _type.TtTrustMethodology;

public class Config {

    public static final TtTrustMethodology TRUST_METHODOLOGY = TtTrustMethodology.BasicTrust_OnlyByItsHistory;

    public static final String SimulatingFilePath = ProjectPath.instance().simulationProfileData(1);
    public static final String SimulatingFileName = SimulatingFilePath.substring(SimulatingFilePath.lastIndexOf("/"));

    public static final String EnvironmentDataFilePath = ProjectPath.instance().environmentData();
    public static final String EnvironmentDataFileName = EnvironmentDataFilePath.substring(EnvironmentDataFilePath.lastIndexOf("/"));


    //============================ World
    public static int WORLD_LIFE_TIME = 10000;         // Time of execution in each world
    public static int WORLD_SLEEP_MILLISECOND = 1;           // Sleep time in each run of world

    public final static int EPISODE_TIMOUT = 200;

    public static int STATE_TILE_WIDTH = 40;

    //============================ Dra`wing Windows

    public static final boolean DRAWING_SHOW_STATE_MACHINE              = false;
    public static final boolean DRAWING_SHOW_STAT_OF_ENV                = false;
    public static final boolean DRAWING_SHOW_STATS_OF_TRUST             = false;
    public static final boolean DRAWING_SHOW_TRUST_MATRIX               = false;
    public static final boolean DRAWING_SHOW_STATS_OF_PO_NE             = false;
    public static final boolean DRAWING_SHOW_ANALYSIS_OF_TRUST_PARAM    = false;
    public static final boolean DRAWING_SHOW_AGENT_TRAVEL_INFO          = true;
    public static final boolean DRAWING_SHOW_AGENT_TRUST_DATA          = true;

    //============================ Statistics
    public static final boolean STATISTICS_IS_GENERATE = true;
    public static final boolean TRUST_MATRIX_IS_GENERATE = true;

}
