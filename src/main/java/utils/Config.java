package utils;

import _type.TtHonestStrategy;

public class Config {

    public static int SIMULATION_WORLD_COUNT = 1;

    //============================ World
    public static int WORLD_RUN_TIME = 100; // time of execution in each world


    public static final int WORLD_MIN_HEIGHT = 1500;        // Min width of height of world grand
    public static final int WORLD_MAX_HEIGHT = 1500;        // Max width of height of world grand

    public static final int WORLD_MIN_WIDTH = 2500;
    public static final int WORLD_MAX_WIDTH = 2500;

    public static final int WORLD_MIN_AGENT = 100;         //Min number of agents (nodes) in the world
    public static final int WORLD_MAX_AGENT = 100;         //Max number of agents (nodes) in the world

    public static final int WORLD_SERVICES_COUNT = 1;

    public static final float WORLD_MAX_VELOCITY_RATIO_X = 0.05f;  // [0-1] percent of WORLD_MAX_HEIGHT
    public static final float WORLD_MAX_VELOCITY_RATIO_Y = 0.05f;  // [0-1] percent of WORLD_MAX_WIDTH

    public static final TtHonestStrategy TRUST_HONEST_STRATEGY = TtHonestStrategy.Discrete;
    public static final int TRUST_HONEST_DISCRETE_PERCENT = 50;   // if TtHonestStrategy.Discrete selected, this defines the percentage of honest agents

    // TRUST LEVEL
    public static final boolean TRUST_LEVEL_RECORD_ACTIVITIES_OF_ITSELF = true;
    public static final boolean TRUST_LEVEL_RECORD_ACTIVITIES_OF_OTHERS = true;
    public static final boolean TRUST_LEVEL_RECORD_EXPERIENCES_OF_OTHERS = true;

    public static final boolean DO_REQUEST_SERVICE_BY_DISHONEST_AGENT = false;      // Whether a dishonest agent can request a service?


    public static final boolean DRAWING_SHOW_POWERFUL_AGENTS_RADIUS = true;
    public static final int DRAWING_POWERFUL_AGENTS_THRESHOLD = 50 ;                // [1,100]

}
