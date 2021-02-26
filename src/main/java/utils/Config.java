package utils;

import _type.TtBehaviorStrategy;
import _type.TtMovementMode;
import _type.TtTrustReplaceHistoryMethod;

public class Config {

    public static int SIMULATION_WORLD_COUNT = 1;

    //============================ MAP
    public static final int MAP_TILE_SIZE = 10;            // Size of each tile in the map
    public static final TtMovementMode MOVEMENT_MODE = TtMovementMode.FreeMovement;
    //============================ World
    public static int WORLD_LIFE_TIME = 1000;                 // time of execution in each world
    public static int WORLD_SLEEP_MILLISECOND = 100;                 // time of execution in each world


    public static final int WORLD_MIN_HEIGHT = 1500;        // Min width of height of world grand
    public static final int WORLD_MAX_HEIGHT = 1500;        // Max width of height of world grand

    public static final int WORLD_MIN_WIDTH = 2500;
    public static final int WORLD_MAX_WIDTH = 2500;

    public static final int WORLD_MIN_AGENT = 200;         //Min number of agents (nodes) in the world
    public static final int WORLD_MAX_AGENT = 200;         //Max number of agents (nodes) in the world

    public static final int WORLD_SERVICES_COUNT = 1;

    public static final float WORLD_MAX_VELOCITY_RATIO_X = 0.05f;  // [0-1] percent of WORLD_MAX_HEIGHT
    public static final float WORLD_MAX_VELOCITY_RATIO_Y = 0.05f;  // [0-1] percent of WORLD_MAX_WIDTH

    public static final TtBehaviorStrategy TRUST_BEHAVIOR_STRATEGY = TtBehaviorStrategy.Discrete;
    public static final int TRUST_HONEST_DISCRETE_PERCENT = 50;   // if TtHonestStrategy.Discrete selected, this defines the percentage of honest agents

    //============================  TRUST LEVEL
    public static final boolean TRUST_LEVEL_RECORD_ACTIVITIES_OF_ITSELF = true;
    public static final boolean TRUST_LEVEL_RECORD_ACTIVITIES_OF_OTHERS = true;
    public static final boolean TRUST_LEVEL_RECORD_EXPERIENCES_OF_OTHERS = true;

    public static final TtTrustReplaceHistoryMethod TRUST_REPLACE_HISTORY_METHOD = TtTrustReplaceHistoryMethod.Sequential_Circular;

    public static final boolean DO_REQUEST_SERVICE_BY_DISHONEST_AGENT = false;      // Whether a dishonest agent can request a service?

    //============================ Drawing Windows

    public static final boolean DRAWING_SHOW_MAIN_WINDOW = true;
    public static final boolean DRAWING_SHOW_DIAGRAM_WINDOW = true;

    public static final boolean DRAWING_SHOW_POWERFUL_AGENTS_RADIUS = true;
    public static final int DRAWING_POWERFUL_AGENTS_THRESHOLD = 50;                // [1,100]


}
