package utils;

import java.awt.*;

public class RectConflict {

    public static boolean hasConflicts(Rectangle r1, Rectangle r2) {
        return hasConflicts(r1, r2, 0);
    }

    public static boolean hasConflicts(Rectangle r1, Rectangle r2, int margin) {


        // To check if either rectangle is actually a line
        // For example :  l1 ={-1,0}  r1={1,1}  l2={0,-1}
        // r2={0,1}

        if (r1.x == r1.width || r1.y == r2.height || r2.x == r2.width
                || r2.y == r2.height) {
            // the line cannot have positive overlap
            return false;
        }

        // If one rectangle is on left side of other
        if (r1.x >= r2.width || r2.x >= r1.width)
            return false;

        // If one rectangle is above other
        if (r1.y <= r2.height || r2.y <= r1.height)
            return false;

        return true;

    }
}
