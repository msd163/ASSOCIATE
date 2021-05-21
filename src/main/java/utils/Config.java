package utils;

import _type.TtBehaviorStrategy;
import _type.TtSimulationMode;
import _type.TtTrustReplaceHistoryMethod;

public class Config {

    public static TtSimulationMode SIMULATION_MODE = TtSimulationMode.FullEnvironment;

    public static final String SimulatingFile = ProjectPath.instance().simulationData(1);
    public static final String PureEnvironmentDataFile = ProjectPath.instance().pureEnvData(1);
    public static final String FullEnvironmentDataFile = ProjectPath.instance().fullEnvData(5);


    //============================ World
    public static int WORLD_LIFE_TIME = 1000;                        // time of execution in each world
    public static int WORLD_SLEEP_MILLISECOND = 100;                 // time of execution in each world

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
    public static final boolean DRAWING_SHOW_DIAGRAM_WINDOW = false;

    public static final boolean DRAWING_SHOW_POWERFUL_AGENTS_RADIUS = true;
    public static final int DRAWING_POWERFUL_AGENTS_THRESHOLD = 95;                // [1,100]


}
