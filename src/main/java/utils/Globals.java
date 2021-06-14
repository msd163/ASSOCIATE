package utils;

import _type.TtBehaviorState;
import trustLayer.TrustManager;

import java.awt.*;
import java.util.Date;
import java.util.Random;

public class Globals {

    public static class Color$ {
        public static Color green = new Color(113, 253, 14);
        public static Color red = new Color(238, 38, 21);
        public static Color gray = new Color(161, 161, 161);
        public static Color magenta = new Color(252, 72, 248);
        public static Color yellow = new Color(247, 255, 68);

        public static Color lightGreen = new Color(187, 255, 141);
        public static Color lightRed = new Color(236, 168, 162);
        public static Color lightGray = new Color(206, 206, 206);
        public static Color lightMagenta = new Color(224, 186, 223);
        public static Color lightYellow = new Color(232, 223, 168);

        public static Color getLight(TtBehaviorState state) {
            switch (state) {
                case Honest:
                    return lightGreen;
                case Adversary:
                    return lightRed;
                case Mischief:
                    return lightGray;
                case IntelligentAdversary:
                    return lightMagenta;
                default:
                    return lightYellow;
            }
        }

        public static Color getNormal(TtBehaviorState state) {
            switch (state) {
                case Honest:
                    return green;
                case Adversary:
                    return red;
                case Mischief:
                    return gray;
                case IntelligentAdversary:
                    return magenta;
                default:
                    return yellow;
            }
        }
    }

    public static Random RANDOM = new Random(new Date().getTime());

    public static boolean PAUSE = false;

    public static TrustManager trustManager;

    public static int WORLD_TIMER;              // The timer of world which is increased by every run of world

    public static int EPISODE = 0;
    public static String STATS_FILE_NAME = ParsCalendar.getInstance().getShortDateTime()
            .replaceAll("[ ]", "-")
            .replaceAll("[:/]", "")
            + "_" + Config.EnvironmentDataFilePath.substring(Config.EnvironmentDataFilePath.lastIndexOf("/") + 1, Config.EnvironmentDataFilePath.lastIndexOf("."));

    public static StatsEnvGenerator statsEnvGenerator = new StatsEnvGenerator();
    public static StatsTrustGenerator statsTrustGenerator = new StatsTrustGenerator();
}
