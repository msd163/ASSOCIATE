package utils;

import _type.TtTrustMethodology;

public class Config {


    public static final TtTrustMethodology TRUST_METHODOLOGY = TtTrustMethodology.BasicTrust_OnlyByItsHistory;

    public static final String SimulatingFile = ProjectPath.instance().simulationProfileData(1);
    public static final String FullEnvironmentDataFile = ProjectPath.instance().environmentData(2);


    //============================ World
    public static int WORLD_LIFE_TIME           = 1000;         // Time of execution in each world
    public static int WORLD_SLEEP_MILLISECOND   = 10;           // Sleep time in each run of world

    public final static int EPISODE_TIMOUT      = 200;

    public static int STATE_TILE_WIDTH          = 40;

    //============================ Dra`wing Windows

    public static final boolean DRAWING_SHOW_STATE_MACHINE          = false;
    public static final boolean DRAWING_SHOW_STAT_OF_ENV            = true;
    public static final boolean DRAWING_SHOW_STATS_OF_TRUST         = true;
    public static final boolean DRAWING_SHOW_TRUST_MATRIX           = true;
    public static final boolean DRAWING_SHOW_STATS_OF_PO_NE         = true;
    public static final boolean DRAWING_SHOW_STATS_OF_TRUST_PARAM   = true;

    //============================ Statistics
    public static final boolean STATISTICS_IS_GENERATE      = true;
    public static final boolean TRUST_MATRIX_IS_GENERATE    = true;

}
