package utils;

import _type.TtSimulationMode;

public class Config {

    public static TtSimulationMode SIMULATION_MODE = TtSimulationMode.SimulateMode;

    public static final String SimulatingFile = ProjectPath.instance().simulationData(1);
    public static final String FullEnvironmentDataFile = ProjectPath.instance().envData();


    //============================ World
    public static int WORLD_LIFE_TIME = 120;                        // time of execution in each world
    public static int WORLD_SLEEP_MILLISECOND = 10;                 // time of execution in each world

    public final static int EPISODE_TIMOUT= 200;

    public static int STATE_TILE_WIDTH = 40;

    //============================ Dra`wing Windows

    public static final boolean DRAWING_SHOW_MAIN_WINDOW        = true;
    public static final boolean DRAWING_SHOW_ENV_STAT_WINDOW    = true;
    public static final boolean DRAWING_SHOW_TRUST_STAT_WINDOW  = true;
    public static final boolean DRAWING_SHOW_TRUST_MAT_WINDOW   = true;
    public static final boolean DRAWING_SHOW_TRUST_PoNe_WINDOW  = true;
    public static final boolean DRAWING_SHOW_TRUST_Param_WINDOW = true;

    //============================ Statistics
    public static final boolean STATISTICS_IS_GENERATE = false;
    public static final boolean TRUST_MATRIX_IS_GENERATE = false;

}
