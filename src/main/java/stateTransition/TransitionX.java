package stateTransition;

import utils.Globals;

public class TransitionX {

    private StateX from;
    private StateX to;

    public TransitionX(StateX from, StateX to) {
        this.from = from;
        this.to = to;

        updatePath();
    }
    //============================//============================


    //============================//============================ DRAWING PARAMS

    private float drawX;                    // box upper left : X
    private float drawY;                    // box upper left : Y
    private float drawWidthAndHeight;       // box width and height
    private float drawAngStart;             // angle start
    private float drawAngExtend;            // angle extent

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

    private void updatePath() {

        int _arcCenX;
        int _arcCenY;

        int _sPointX = from.getLocation().getX();
        int _sPointY = from.getLocation().getY();

        int _tPointX = to.getLocation().getX();
        int _tPointY = to.getLocation().getY();

        // calc arc point
        if (_sPointX == _tPointX) {
            if (_sPointY == _tPointY) {
                _arcCenX = _tPointX;
                _arcCenY = _tPointY;
            } else {
                // moving edges from center of stages to borders : Y axis
                if (_sPointY > _tPointY) {
                    _sPointY -= Globals.STATE_WIDTH_IN_DRAWING;
                    _tPointY += Globals.STATE_WIDTH_IN_DRAWING;
                } else {
                    _sPointY += Globals.STATE_WIDTH_IN_DRAWING;
                    _tPointY -= Globals.STATE_WIDTH_IN_DRAWING;
                }

                _arcCenY = (_sPointY + _tPointY) / 2;
                _arcCenX = _sPointX + ((_sPointY > _tPointY ? -1 : 1) * 100);
            }
        } else {
            if (_sPointY == _tPointY) {
                // moving edges from center of stages to borders : Y axis
                if (_sPointX > _tPointX) {
                    _sPointX -= Globals.STATE_WIDTH_IN_DRAWING;
                    _tPointX += Globals.STATE_WIDTH_IN_DRAWING;
                } else {
                    _sPointX += Globals.STATE_WIDTH_IN_DRAWING;
                    _tPointX -= Globals.STATE_WIDTH_IN_DRAWING;
                }

                _arcCenX = (_sPointX + _tPointX) / 2;
                _arcCenY = _sPointY + ((_sPointX > _tPointX ? -1 : 1) * 100);
            } else {
                // moving edges from center of stages to borders : Y axis
                if (_sPointY > _tPointY) {
                    _sPointY -= Globals.STATE_WIDTH_IN_DRAWING;
                    _tPointY += Globals.STATE_WIDTH_IN_DRAWING;
                } else {
                    _sPointY += Globals.STATE_WIDTH_IN_DRAWING;
                    _tPointY -= Globals.STATE_WIDTH_IN_DRAWING;
                }
                // moving edges from center of stages to borders : Y axis
                if (_sPointX > _tPointX) {
                    _sPointX -= Globals.STATE_WIDTH_IN_DRAWING;
                    _tPointX += Globals.STATE_WIDTH_IN_DRAWING;
                } else {
                    _sPointX += Globals.STATE_WIDTH_IN_DRAWING;
                    _tPointX -= Globals.STATE_WIDTH_IN_DRAWING;
                }



                // The slop of main direct path between the source and the target
                float mf = (float) (_sPointY - _tPointY) / (_sPointX - _tPointX);
                // The slop of the line perpendicular to the main path
                float m = -1 / mf;

                // The middle of main path. middle line is on the perpendicular line
                int middleX = (_sPointX + _tPointX) / 2;
                int middleY = (_sPointY + _tPointY) / 2;

                // For perpendicular line:  Y = a * X + b , what is b?
                int b = (int) (middleY - (m * middleX));

                // Calculating arc point as the second point of perpendicular line
                _arcCenX = middleX + (_sPointX > _tPointX ? 1 : -1) * 50;
                _arcCenY = (int) (m * +_arcCenX + b);

            }
        }

        // Get radii of anchor and det point.
        float radSP = dist0(_sPointX, _sPointY, _arcCenX, _arcCenY);
        float radTP = dist0(_tPointX, _tPointY, _arcCenX, _arcCenY);

        // If either is zero there's nothing else to draw.
        if (radSP == 0 || radTP == 0) {
            return;
        }

        // Get the angles from center to points.
        drawAngStart = angle0(_sPointX, _sPointY, _arcCenX, _arcCenY);
        float angTP = angle0(_tPointX, _tPointY, _arcCenX, _arcCenY);  // (xb, yb) would work fine, too.
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

    public float getDrawAngStart() {
        return drawAngStart;
    }

    public float getDrawAngExtend() {
        return drawAngExtend;
    }
}
