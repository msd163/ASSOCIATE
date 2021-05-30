package utils;

import stateLayer.MissionGenerator;
import systemLayer.profiler.CapacityProfiler;
import trustLayer.TrustManager;

import java.util.Date;
import java.util.Random;

public class Globals {
    public static Random RANDOM = new Random(new Date().getTime());

    public static TrustManager trustManager = new TrustManager();
    public static CapacityProfiler profiler = new CapacityProfiler();
    public static MissionGenerator commander = new MissionGenerator();

    public static int WORLD_TIMER;                                       // The timer of world which is increased by every run of world

    public static int STATE_TILE_WIDTH = 40;

    public static StatGenerator statGenerator = new StatGenerator();
}
