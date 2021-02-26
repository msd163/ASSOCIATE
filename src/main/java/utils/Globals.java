package utils;

import java.util.Date;
import java.util.Random;

public class Globals {

    public  static Random RANDOM = new Random(new Date().getTime());
    public static int WORLD_TIMER;                                       // The timer of world which is increased by every run of world
}
