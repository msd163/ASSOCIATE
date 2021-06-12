package utils;

import trustLayer.TrustManager;

import java.util.Date;
import java.util.Random;

public class Globals {
    public static Random RANDOM = new Random(new Date().getTime());

    public static boolean PAUSE = false;

    public static TrustManager trustManager;

    public static int WORLD_TIMER;              // The timer of world which is increased by every run of world

    public static int EPISODE = 0;
    public static String STATS_FILE_NAME = ParsCalendar.getInstance().getShortDateTime()
            .replaceAll("[ ]", "-")
            .replaceAll("[:/]", "")
            +"_" + Config.EnvironmentDataFilePath.substring(Config.EnvironmentDataFilePath.lastIndexOf("/") + 1, Config.EnvironmentDataFilePath.lastIndexOf("."));

    public static StatsEnvGenerator statsEnvGenerator = new StatsEnvGenerator();
    public static StatsTrustGenerator statsTrustGenerator = new StatsTrustGenerator();
}
