package utils;

import _type.TtBehaviorState;
import _type.TtDiagramThemeMode;
import simulateLayer.statistics.StatsEnvGenerator;
import simulateLayer.statistics.StatsTrustGenerator;

import java.awt.*;
import java.util.Date;
import java.util.Random;

public class Globals {

    public static class ProfileBunchMax {
        public static int maxExperienceCap = 0;
        public static int maxExperienceItemCap = 0;
        public static int maxIndirectExperienceCap = 0;
        public static int maxIndirectExperienceItemCap = 0;
        public static int maxTrustRecommendationItemCap = 0;
        public static int maxTrustRecommendationCap = 0;
        public static int maxWatchListCap = 0;
        public static int maxWatchDepth = 0;
        public static int maxTravelHistoryCap = 0;
        public static int maxObservationCap = 0;
        public static int maxObservationItemCap = 0;
        public static int maxIndirectObservationCap = 0;
        public static int maxIndirectObservationItemCap = 0;
        public static int maxAgentTargetCount = 0;
        public static int maxWithInternetCount = 0;
    }

    public static class Color$ {
        public static Color white = new Color(255, 255, 255);
        public static Color black = new Color(0, 0, 0);

        public static Color lightGray2 = new Color(235, 235, 235);
        public static Color lightGray1 = new Color(220, 220, 220);
        public static Color lightGray = new Color(200, 200, 200);
        public static Color gray = new Color(190, 190, 190);
        public static Color darkGray = new Color(150, 150, 150);
        public static Color darkGray1 = new Color(100, 100, 100);
        public static Color darkGray2 = new Color(56, 56, 56);
        public static Color darkGray3 = new Color(33, 33, 33);

        public static Color lightGreen = new Color(187, 255, 141);
        public static Color green = new Color(113, 253, 14);
        public static Color darkGreen = new Color(0, 200, 0);
        public static Color darkGreen1 = new Color(0, 162, 0);
        public static Color darkGreen2 = new Color(5, 102, 5);
        public static Color darkGreen3 = new Color(2, 70, 2);


        public static Color lightRed1 = new Color(236, 168, 162);
        public static Color lightRed = new Color(255, 108, 108);
        public static Color red = new Color(238, 38, 21);
        public static Color darkRed = new Color(200, 29, 13);
        public static Color darkRed1 = new Color(170, 23, 10);
        public static Color darkRed2 = new Color(130, 16, 5);
        public static Color darkRed3 = new Color(100, 11, 2);

        public static Color lightBlue = new Color(132, 237, 255);
        public static Color blue = new Color(0, 111, 255);
        public static Color darkBlue = new Color(0, 60, 255);
        public static Color darkBlue1 = new Color(0, 60, 210);
        public static Color darkBlue2 = new Color(0, 46, 180);
        public static Color darkBlue3 = new Color(0, 33, 136);


        public static Color lightYellow = new Color(232, 223, 168);
        public static Color yellow = new Color(247, 255, 68);


        public static Color lightMagenta = new Color(224, 186, 223);
        public static Color magenta = new Color(252, 72, 252);
        public static Color darkMagenta = new Color(210, 52, 210);
        public static Color darkMagenta1 = new Color(175, 42, 175);
        public static Color darkMagenta2 = new Color(145, 7, 145);
        public static Color darkMagenta3 = new Color(110, 3, 110);


        public static Color lightOrange = new Color(255, 173, 0);
        public static Color orange = new Color(255, 127, 0);
        public static Color darkOrange = new Color(235, 100, 0);
        public static Color darkOrange1 = new Color(200, 80, 0);
        public static Color darkOrange2 = new Color(180, 60, 0);
        public static Color darkOrange3 = new Color(150, 30, 0);

        public static Color[] arr() {
            if (Config.THEME_MODE == TtDiagramThemeMode.Dark) {
                return new Color[]{green, white, gray, red, lightBlue, magenta, yellow, lightGreen, lightRed1, orange,
                        lightMagenta, darkGreen, darkGray, darkGreen, darkRed, darkMagenta, lightYellow, lightGray1, lightRed, lightOrange};
            } else {
                return new Color[]{
                        darkGreen1, darkGray1, darkRed1, darkBlue1, darkMagenta1, darkOrange1,
                        darkGreen3, darkGray3, darkRed3, darkBlue3, darkMagenta3, darkOrange3,
                        darkGreen, darkGray, darkRed, darkBlue, darkMagenta, darkOrange,
                        darkGreen2, darkGray2, darkRed2, darkBlue2, darkMagenta2, darkOrange2
                };
            }

        }

        ;

        public static Color getLight(TtBehaviorState state) {
            if (Config.THEME_MODE == TtDiagramThemeMode.Dark) {
                switch (state) {
                    case Honest:
                        return lightGreen;
                    case Adversary:
                        return lightRed1;
                    case Mischief:
                        return lightGray1;
                    case Hypocrite:
                        return lightMagenta;
                    default:
                        return lightYellow;
                }
            } else {
                switch (state) {
                    case Honest:
                        return green;
                    case Adversary:
                        return red;
                    case Mischief:
                        return gray;
                    case Hypocrite:
                        return magenta;
                    default:
                        return Color.orange;
                }
            }
        }

        public static Color getNormal(TtBehaviorState state) {
            if (Config.THEME_MODE == TtDiagramThemeMode.Dark) {
                switch (state) {
                    case Honest:
                        return green;
                    case Adversary:
                        return red;
                    case Mischief:
                        return gray;
                    case Hypocrite:
                        return magenta;
                    default:
                        return yellow;
                }
            } else {
                switch (state) {
                    case Honest:
                        return darkGreen;
                    case Adversary:
                        return red;
                    case Mischief:
                        return darkGray;
                    case Hypocrite:
                        return magenta;
                    default:
                        return orange;
                }
            }
        }


        public static Color getDark(TtBehaviorState state) {
            if (Config.THEME_MODE == TtDiagramThemeMode.Dark) {
                switch (state) {
                    case Honest:
                        return green;
                    case Adversary:
                        return red;
                    case Mischief:
                        return gray;
                    case Hypocrite:
                        return magenta;
                    default:
                        return yellow;
                }
            } else {
                switch (state) {
                    case Honest:
                        return darkGreen2;
                    case Adversary:
                        return darkRed2;
                    case Mischief:
                        return darkGray2;
                    case Hypocrite:
                        return darkMagenta;
                    default:
                        return black;
                }
            }
        }

        public static Color $pause = Config.THEME_MODE == TtDiagramThemeMode.Dark ? green : darkGreen1;
        public static Color $background = Config.THEME_MODE == TtDiagramThemeMode.Dark ? black : white;
        public static Color $mainTitle = Config.THEME_MODE == TtDiagramThemeMode.Dark ? yellow : darkGray2;
        public static Color $subTitle = gray;
        public static Color $simTitle = Config.THEME_MODE == TtDiagramThemeMode.Dark ? white : black;
        public static Color $configTitle = Config.THEME_MODE == TtDiagramThemeMode.Dark ? lightGray1 : darkGray2;
        public static Color $axis = Config.THEME_MODE == TtDiagramThemeMode.Dark ? yellow : black;
        public static Color $axisSplit = Config.THEME_MODE == TtDiagramThemeMode.Dark ? gray : darkGray1;
        public static Color $axisSplit2 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? darkGray2 : lightGray1;
        public static Color $mousePlus = Config.THEME_MODE == TtDiagramThemeMode.Dark ? white : Color.blue;
        public static Color $drawingTitle = Config.THEME_MODE == TtDiagramThemeMode.Dark ? Color.cyan : darkRed;

        public static Color $curve_1 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? green : darkGreen2;
        public static Color $curve_2 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? red : darkRed;
        public static Color $curve_3 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? magenta : darkMagenta;
        public static Color $curve_4 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? white : Color.blue;
        public static Color $curve_5 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? gray : Color.darkGray;
        public static Color $curve_6 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? lightOrange : darkOrange1;

        public static Color $trustHonest = Config.THEME_MODE == TtDiagramThemeMode.Dark ? green : darkGreen1;
        public static Color $trustAdversary = red;
        public static Color $trustMischief = Config.THEME_MODE == TtDiagramThemeMode.Dark ? Color.white : gray;
    }

    public static Random RANDOM = new Random(new Date().getTime());

    public static boolean PAUSE = false;


    //============================//============================//============================
    public static int DAGRA_SIGN_NEXT_ID = 0;               // an static variable for counting CertSign ID
    public static int DAGRA_VERIFY_NEXT_ID = 0;             // an static variable for counting CertSign ID
    public static int DAGRA_CONTRACT_NEXT_ID = 0;           // an static variable for counting CertContract ID
    public static int DAGRA_NEXT_ID = 0;                    // an static variable for counting DaGra ID
    public static int DAGRA_REQUEST_STAGE__REQUESTED_COUNT_IN_CURRENT_PERIOD = 0;
    //============================//============================//============================
    public static int WORLD_TIMER;              // The timer of world which is increased by every run of world

    public static int SIMULATION_ROUND = 1;     // This value filled by environment profile as input parameter. number of simulation. starts from one.
    public static int SIMULATION_TIMER = 0;     // Index of simulation. starts from zero

    public static int EPISODE = 0;


    //============================//============================//============================


    public static String STATS_FILE_NAME = ParsCalendar.getInstance().getShortDateTime()
            .replaceAll("[ ]", "-")
            .replaceAll("[:/]", "")
            + "_" + Config.EnvironmentDataFilePath.substring(Config.EnvironmentDataFilePath.lastIndexOf("/") + 1, Config.EnvironmentDataFilePath.lastIndexOf("."));

    public static StatsEnvGenerator statsEnvGenerator = new StatsEnvGenerator();
    public static StatsTrustGenerator statsTrustGenerator = new StatsTrustGenerator();
    //public static TrustManager trustManager;


    public static void reset() {
        RANDOM = new Random(new Date().getTime());
        EPISODE = 0;
        //-- Initializing the timer of the world.
        //-- Setting -1 for registering first history of travel time to -1;
        //-- it used in initVar() of agent
        WORLD_TIMER = -1;
        PAUSE = false;

    }
}
