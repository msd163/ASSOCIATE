package utils;

import stateTransition.Environment;
import stateTransition.StateX;
import system.World;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainDrawingWindow extends Canvas {

    public final static int SHIFT_X = 100;
    public final static int SHIFT_Y = 200;
    private static final Random random = new Random();

    private World world;
    private Environment environment;
    private Color[] colors;

    public MainDrawingWindow(World world) {
        this.world = world;
        this.environment = world.getEnvironment();
        colors = new Color[environment.getStateCount() * 2];

        random.setSeed(new Date().getTime());

        for (int i = 0; i < colors.length; i++) {
            colors[i] = getRandColor();
        }
    }

    //============================//============================//============================

    private int _colorIndex = 0;
    private StateX _stateX;
    private Point _statePoint;
    private ArrayList<StateX> _targets;
    private float _arcCenX, _arcCenY;   // Transition Arc center. Subscript 0 used for center throughout.
    private float _sPointX, _sPointY;   // SourcePoint
    private float _tPointX, _tPointY;   // TargetPoint

    @Override
    public void update(Graphics gr) {

        //============================//============================ Preparing
        Graphics2D g = (Graphics2D) gr;

        g.clearRect(0, 0, getWidth(), getHeight());

        setBackground(Color.BLACK);
        g.setColor(Color.YELLOW);

        //============================ Title
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g.drawString(world.toString(), 40, 40);

        //============================ Translate

        g.translate(SHIFT_X, SHIFT_Y);

        //============================ Bound Rectangle
        g.drawRect(0, 0, world.getWidth(), world.getHeight());


        //============================//============================ Drawing states
        _colorIndex = 0;
        for (int x = 0; x < environment.getStateCount(); x++) {

            _stateX = environment.getState(x);
            _statePoint = _stateX.getLocation();
            _targets = _stateX.getTargets();

            drawStateX(g, _stateX);

            _sPointX = _statePoint.getX();
            _sPointY = _statePoint.getY();

            for (StateX st : _targets) {

                _colorIndex++;
                if (_colorIndex >= colors.length) {
                    _colorIndex = 0;
                }

                _tPointX = st.getLocation().getX();
                _tPointY = st.getLocation().getY();

                // calc arc point
                if (_sPointX == _tPointX) {
                    if (_sPointY == _tPointY) {
                        _arcCenX = _tPointX;
                        _arcCenY = _tPointY;
                    } else {
                        _arcCenY = (_sPointY + _tPointY) / 2;
                        _arcCenX = _sPointX + ((_sPointY > _tPointY ? -1 : 1) * 100);
                    }
                } else {
                    if (_sPointY == _tPointY) {
                        _arcCenX = (_sPointX + _tPointX) / 2;
                        _arcCenY = _sPointY + ((_sPointX > _tPointX ? -1 : 1) * 100);
                    } else {
                        _arcCenX = _sPointX;
                        _arcCenY = _tPointY;
                    }
                }

                drawArcCenterPont(g, _arcCenX, _arcCenY);

                // Get radii of anchor and det point.
                float radSP = dist0(_sPointX, _sPointY, _arcCenX, _arcCenY);
                float radTP = dist0(_tPointX, _tPointY, _arcCenX, _arcCenY);

                // If either is zero there's nothing else to draw.
                if (radSP == 0 || radTP == 0) {
                    continue;
                }

                // Get the angles from center to points.
                float angSP = angle0(_sPointX, _sPointY, _arcCenX, _arcCenY);
                float angTP = angle0(_tPointX, _tPointY, _arcCenX, _arcCenY);  // (xb, yb) would work fine, too.

                g.setColor(colors[_colorIndex]);
                g.draw(new Arc2D.Float(_arcCenX - radSP, _arcCenY - radSP, // box upper left
                        2 * radSP, 2 * radSP,                  // box width and height
                        angSP, angleDiff(angSP, angTP),           // angle start, extent
                        Arc2D.OPEN));

            }
        }

        //============================//============================ Drawing agents
      /*  for (Agent agent : world.getAgents()) {

            agent.draw((Graphics2D) g);

        }*/
    }

    //============================//============================//============================

    static void drawStateX(Graphics2D g, StateX stateX) {
        final int rad = 9;
        int x = stateX.getLocation().getX();
        int y = stateX.getLocation().getY();
        Color color = g.getColor();
        g.setColor(Color.GREEN);
        g.fill(new Rectangle.Float(x - rad, y - rad, 2 * rad, 2 * rad));
        g.drawString("(" + stateX.getId() + ")", x, y + (4 * rad));
        g.setColor(color);
    }

    static void drawArcCenterPont(Graphics2D g, float x, float y) {
        final int rad = 9;
        Color color = g.getColor();
        g.setColor(Color.GRAY);
        g.fill(new Ellipse2D.Float(x - rad, y - rad, 2 * rad, 2 * rad));
        g.setColor(color);
    }


    //============================//============================//============================

    // Return the square of a float.
    static float sqr(float x) {
        return x * x;
    }

    // Return the distance from any point to the arc center.
    float dist0(float x, float y, float x0, float y0) {
        return (float) Math.sqrt(sqr(x - x0) + sqr(y - y0));
    }

    // Return polar angle of any point relative to arc center.
    float angle0(float x, float y, float x0, float y0) {
        return (float) Math.toDegrees(Math.atan2(y0 - y, x - x0));
    }

    // Find the angular difference between a and b, -180 <= diff < 180.
    static float angleDiff(float a, float b) {
        float d = b - a;
        while (d >= 180f) {
            d -= 360f;
        }
        while (d < -180f) {
            d += 360f;
        }
        return d;
    }

    public Color getRandColor() {
        return new Color(
                random.nextInt(220) + 20,
                random.nextInt(220) + 20,
                random.nextInt(220) + 20
        );
    }

}
