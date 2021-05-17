package stateLayer;

public class TransitionX {

    private StateX from;
    private StateX to;

    public TransitionX(StateX from, StateX to) {
        this.from = from;
        this.to = to;
    }
    //============================//============================


    //============================//============================ DRAWING PARAMS

    private float drawX;                    // box upper left : X
    private float drawY;                    // box upper left : Y
    private float drawWidthAndHeight;       // box width and height
    private float drawAngStart;             // angle start
    private float drawAngExtend;            // angle extent
    private boolean drawIsActive;           // true: a transmission is done from this transition now.
    // Return the distance from any point to the arc center.
    float dist0(float x, float y, float x0, float y0) {
        return (float) Math.sqrt(sqr(x - x0) + sqr(y - y0));
    }

    // Return the square of a float.
    float sqr(float x) {
        return x * x;
    }

    // Return polar angle of any point relative to arc center.
    float angle0(float x, float y, float x0, float y0) {
        return (float) Math.toDegrees(Math.atan2(y0 - y, x - x0));
    }

    // Find the angular difference between a and b, -180 <= diff < 180.
    float angleDiff(float a, float b) {
        float d = b - a;
        while (d >= 180f) {
            d -= 360f;
        }
        while (d < -180f) {
            d += 360f;
        }
        return d;
    }

    public void updatePath() {

        int _arcCenX;
        int _arcCenY;

        int fromX = from.getLocation().getX();
        int fromY = from.getLocation().getY();

        int toX = to.getLocation().getX();
        int toY = to.getLocation().getY();

        // calc arc point
        if (fromX == toX) {
            if (fromY == toY) {
                _arcCenX = toX;
                _arcCenY = toY;
            } else {
                // moving edges from center of stages to borders : Y axis
                if (fromY > toY) {
                    fromY -= from.getWidth() / 2;
                    toY += to.getWidth() / 2;
                } else {
                    fromY += from.getWidth() / 2;
                    toY -= to.getWidth() / 2;
                }

                _arcCenY = (fromY + toY) / 2;
                _arcCenX = fromX + ((fromY > toY ? -1 : 1) * 100);
            }
        } else {
            if (fromY == toY) {
                // moving edges from center of stages to borders : Y axis
                if (fromX > toX) {
                    fromX -= from.getWidth() / 2;
                    toX += to.getWidth() / 2;
                } else {
                    fromX += from.getWidth() / 2;
                    toX -= to.getWidth() / 2;
                }

                _arcCenX = (fromX + toX) / 2;
                _arcCenY = fromY + ((fromX > toX ? -1 : 1) * 100);
            } else {
                // moving edges from center of stages to borders : Y axis
                if (fromY > toY) {
                    fromY -= from.getWidth() / 2;
                    toY += to.getWidth() / 2;
                } else {
                    fromY += from.getWidth() / 2;
                    toY -= to.getWidth() / 2;
                }
                // moving edges from center of stages to borders : Y axis
                if (fromX > toX) {
                    fromX -= from.getWidth() / 2;
                    toX += to.getWidth() / 2;
                } else {
                    fromX += from.getWidth() / 2;
                    toX -= to.getWidth() / 2;
                }


                // The slop of main direct path between the source and the target
                float mf = (float) (fromY - toY) / (fromX - toX);
                // The slop of the line perpendicular to the main path
                float m = -1 / mf;

                // The middle of main path. middle line is on the perpendicular line
                int middleX = (fromX + toX) / 2;
                int middleY = (fromY + toY) / 2;

                // For perpendicular line:  Y = a * X + b , what is b?
                int b = (int) (middleY - (m * middleX));

                // Calculating arc point as the second point of perpendicular line
                _arcCenX = middleX + (fromX > toX ? 1 : -1) * 50;
                _arcCenY = (int) (m * +_arcCenX + b);

            }
        }

        // Get radii of anchor and det point.
        float radSP = dist0(fromX, fromY, _arcCenX, _arcCenY);
        float radTP = dist0(toX, toY, _arcCenX, _arcCenY);

        // If either is zero there's nothing else to draw.
        if (radSP == 0 || radTP == 0) {
            return;
        }

        // Get the angles from center to points.
        drawAngStart = angle0(fromX, fromY, _arcCenX, _arcCenY);
        float angTP = angle0(toX, toY, _arcCenX, _arcCenY);  // (xb, yb) would work fine, too.
        drawAngExtend = angleDiff(drawAngStart, angTP);

        drawX = _arcCenX - radSP;
        drawY = _arcCenY - radSP;
        drawWidthAndHeight = 2 * radSP;

    }

    //============================//============================


    public StateX getFrom() {
        return from;
    }

    public void setFrom(StateX from) {
        this.from = from;
    }

    public StateX getTo() {
        return to;
    }

    public void setTo(StateX to) {
        this.to = to;
    }

    public float getDrawX() {
        return drawX;
    }

    public float getDrawY() {
        return drawY;
    }

    public float getDrawWidthAndHeight() {
        return drawWidthAndHeight;
    }

    public float getDrawSourceArrowSize() {
        return 1000/drawWidthAndHeight ;
    }

    public float getDrawAngStart() {
        return drawAngStart;
    }

    public float getDrawAngExtend() {
        return drawAngExtend;
    }

    public boolean isDrawIsActive() {
        return drawIsActive;
    }

    public void setDrawIsActive(boolean drawIsActive) {
        this.drawIsActive = drawIsActive;
    }
}
