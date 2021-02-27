package utils;

import profiler.CapacityProfiler;
import java.util.Date;
import java.util.Random;

public class Globals {
    public  static Random RANDOM = new Random(new Date().getTime());
    public static int WORLD_TIME;
    public static CapacityProfiler profiler = new CapacityProfiler();

}
