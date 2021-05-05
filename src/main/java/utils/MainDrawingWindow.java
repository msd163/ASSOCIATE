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

    @Override
    public void update(Graphics gr) {

        Graphics2D g = (Graphics2D) gr;

        g.clearRect(0, 0, getWidth(), getHeight());

        setBackground(Color.BLACK);
        g.setColor(Color.YELLOW);

        //============================ Title
        g.setFont(new Font("TimesRoman", Font.PLAIN, 38));
        g.drawString(world.toString(), 40, 40);

        //============================ Translate

        g.translate(SHIFT_X, SHIFT_Y);

        //============================ Bound Rectangle
        g.drawRect(0, 0, world.getWidth(), world.getHeight());

        //============================

        int colorIndex = 0;
        for (int x = 0; x < environment.getStateCount(); x++) {

            StateX start = environment.getState(x);
            Point point = start.getLocation();
            ArrayList<StateX> targets = start.getTargets();

            drawState(g, start);
            float x0, y0;   // Arc center. Subscript 0 used for center throughout.
            float xa, ya;   // Arc anchor point.  Subscript a for anchor.
            float xd, yd;   // Point determining arc angle. Subscript d for determiner.

            xa = point.getX();
            ya = point.getY();

            for (StateX st : targets) {

                colorIndex++;
                if (colorIndex >= colors.length) {
                    colorIndex = 0;
                }

                xd = st.getLocation().getX();
                yd = st.getLocation().getY();

                if (xa == xd) {
                    if (ya == yd) {
                        x0 = xd;
                        y0 = yd;
                    } else {
                        y0 = (ya + yd) / 2;
                        x0 = xa + ((ya > yd ? -1 : 1) * 100);
                    }
                } else {
                    if (ya == yd) {
                        x0 = (xa + xd) / 2;
                        y0 = ya + ((xa > xd ? -1 : 1) * 100);
                    } else {
                        x0 = xa;
                        y0 = yd;
                    }
                }

                drawTransitionCurveBase(g, x0, y0);


              /*  int w = st.getLocation().getX() - point.getX();
                int h = st.getLocation().getY() - point.getY();
*/
                // Get radii of anchor and det point.
                float ra = dist0(xa, ya, x0, y0);
                float rd = dist0(xd, yd, x0, y0);

                // If either is zero there's nothing else to draw.
                if (ra == 0 || rd == 0) {
                    continue;
                }

                // Get the angles from center to points.
                float aa = angle0(xa, ya, x0, y0);
                float ad = angle0(xd, yd, x0, y0);  // (xb, yb) would work fine, too.


                g.setColor(colors[colorIndex]);
                g.draw(new Arc2D.Float(x0 - ra, y0 - ra, // box upper left
                        2 * ra, 2 * ra,                  // box width and height
                        aa, angleDiff(aa, ad),           // angle start, extent
                        Arc2D.OPEN));

          /*      g.drawArc(
                        point.getX(),
                        point.getY(),
                        w,
                        h,
                        0,
                        180
                );*/
            }
        }

      /*  for (Agent agent : world.getAgents()) {

            agent.draw((Graphics2D) g);

        }*/
    }

    //============================//============================//============================

    // Return the square of a float.
    static float sqr(float x) {
        return x * x;
    }

    static void drawState(Graphics2D g, StateX stateX) {
        final int rad = 9;
        int x = stateX.getLocation().getX();
        int y = stateX.getLocation().getY();
        Color color = g.getColor();
        g.setColor(Color.GREEN);
        g.fill(new Rectangle.Float(x - rad, y - rad, 2 * rad, 2 * rad));
        g.drawString("(" + stateX.getId() + ")", x, y + (3 * rad));
        g.setColor(color);
    }

    static void drawTransitionCurveBase(Graphics2D g, float x, float y) {
        final int rad = 9;
        Color color = g.getColor();
        g.setColor(Color.GRAY);
        g.fill(new Ellipse2D.Float(x - rad, y - rad, 2 * rad, 2 * rad));
        g.setColor(color);
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
