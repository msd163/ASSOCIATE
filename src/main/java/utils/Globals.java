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
        public static Color black = new Color(0, 0, 0);
        public static Color white = new Color(255, 255, 255);
        public static Color green = new Color(113, 253, 14);
        public static Color red = new Color(238, 38, 21);
        public static Color gray = new Color(190, 190, 190);
        public static Color magenta = new Color(252, 72, 248);
        public static Color yellow = new Color(247, 255, 68);

        public static Color lightGreen = new Color(187, 255, 141);
        public static Color lightRed = new Color(236, 168, 162);
        public static Color lightGray = new Color(220, 220, 220);
        public static Color lightMagenta = new Color(224, 186, 223);
        public static Color lightYellow = new Color(232, 223, 168);

        public static Color darkRed = new Color(170, 24, 10);
        public static Color darkRed2 = new Color(135, 16, 5);
        public static Color darkGreen = new Color(0, 158, 0);
        public static Color darkGreen2 = new Color(5, 102, 5);
        public static Color darkGray = new Color(56, 56, 56);
        public static Color darkGray2 = new Color(33, 33, 33);
        public static Color darkMagenta = new Color(175, 24, 171);
        public static Color darkMagenta2 = new Color(105, 6, 102);

        public static Color orange = new Color(255, 127, 30);
        public static Color lightOrange = new Color(255, 173, 108);

        public static Color getLight(TtBehaviorState state) {
           if(Config.THEME_MODE==TtDiagramThemeMode.Dark){
               switch (state) {
                   case Honest:
                       return lightGreen;
                   case Adversary:
                       return lightRed;
                   case Mischief:
                       return lightGray;
                   case Hypocrite:
                       return lightMagenta;
                   default:
                       return lightYellow;
               }
           }else{
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
            if(Config.THEME_MODE==TtDiagramThemeMode.Dark){
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
            }else{
                switch (state) {
                    case Honest:
                        return darkGreen;
                    case Adversary:
                        return darkRed;
                    case Mischief:
                        return darkGray;
                    case Hypocrite:
                        return darkMagenta;
                    default:
                        return orange;
                }
            }
        }

        public static Color $pause = Config.THEME_MODE == TtDiagramThemeMode.Dark ? green : darkGreen;
        public static Color $background = Config.THEME_MODE == TtDiagramThemeMode.Dark ? black : white;
        public static Color $mainTitle = Config.THEME_MODE == TtDiagramThemeMode.Dark ? yellow : darkGray;
        public static Color $subTitle = gray;
        public static Color $simTitle = Config.THEME_MODE == TtDiagramThemeMode.Dark ? white : black;
        public static Color $configTitle = Config.THEME_MODE == TtDiagramThemeMode.Dark ? lightGray : darkGray;
        public static Color $axis = Config.THEME_MODE == TtDiagramThemeMode.Dark ? yellow : black;
        public static Color $axisSplit = Config.THEME_MODE == TtDiagramThemeMode.Dark ? gray : gray;
        public static Color $axisSplit2 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? darkGray : lightGray;
        public static Color $mousePlus = Config.THEME_MODE == TtDiagramThemeMode.Dark ? white : Color.blue;
        public static Color $drawingTitle = Config.THEME_MODE == TtDiagramThemeMode.Dark ? Color.cyan : darkRed;
        public static Color $curve_1 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? green : darkGreen2;
        public static Color $curve_2 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? red : darkRed;
        public static Color $curve_3 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? magenta : darkMagenta;
        public static Color $curve_4 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? white : Color.blue;
        public static Color $curve_5 = Config.THEME_MODE == TtDiagramThemeMode.Dark ? white : Color.blue;

        public static Color $trustHonest = Config.THEME_MODE == TtDiagramThemeMode.Dark ? green : darkGreen2;
        public static Color $trustAdversary = red;
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
