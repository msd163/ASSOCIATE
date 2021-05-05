package utils;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

class TestCanvas extends JComponent {

    float x0 = 190f, y0 = 150f;   // Arc center. Subscript 0 used for center throughout.
    float xa = 200f, ya = 150f;   // Arc anchor point.  Subscript a for anchor.
    float xd = 150f, yd =  50f;   // Point determining arc angle. Subscript d for determiner.

    // Return the distance from any point to the arc center.
    float dist0(float x, float y) {
        return (float)Math.sqrt(sqr(x - x0) + sqr(y - y0));
    }

    // Return polar angle of any point relative to arc center.
    float angle0(float x, float y) {
        return (float)Math.toDegrees(Math.atan2(y0 - y, x - x0));
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;

        // Can always draw the center point.
        dot(g, x0, y0);

        // Get radii of anchor and det point.
        float ra = dist0(xa, ya);
        float rd = dist0(xd, yd);

        // If either is zero there's nothing else to draw.
        if (ra == 0 || rd == 0) { return; }

        // Get the angles from center to points.
        float aa = angle0(xa, ya);
        float ad = angle0(xd, yd);  // (xb, yb) would work fine, too.

        // Draw the arc and other dots.
        g.draw(new Arc2D.Float(x0 - ra, y0 - ra, // box upper left
                2 * ra, 2 * ra,                  // box width and height
                aa, angleDiff(aa, ad),           // angle start, extent
                Arc2D.OPEN));
        dot(g, xa, ya);

        // Use similar triangles to get the second dot location.
        float xb = x0 + (xd - x0) * ra / rd;
        float yb = y0 + (yd - y0) * ra / rd;
        dot(g, xb, yb);
    }

    // Some helper functions.

    // Draw a small dot with the current color.
    static void dot(Graphics2D g, float x, float y) {
        final int rad = 2;
        g.fill(new Ellipse2D.Float(x - rad, y - rad, 2 * rad, 2 * rad));
    }

    // Return the square of a float.
    static float sqr(float x) { return x * x; }

    // Find the angular difference between a and b, -180 <= diff < 180.
    static float angleDiff(float a, float b) {
        float d = b - a;
        while (d >= 180f) { d -= 360f; }
        while (d < -180f) { d += 360f; }
        return d;
    }

    // Construct a test canvas with mouse handling.
    TestCanvas() {
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    // Listener changes arc parameters with click and drag.
    MouseInputAdapter mouseListener = new MouseInputAdapter() {
        boolean mouseDown = false; // Is left mouse button down?

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                mouseDown = true;
                xa = xd = e.getX();
                ya = yd = e.getY();
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                mouseDown = false;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (mouseDown) {
                xd = e.getX();
                yd = e.getY();
                repaint();
            }
        }
    };
}

public class Test extends JFrame {

    public Test() {
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(new TestCanvas());
    }

    public static void main(String[] args) {
        // Swing code must run in the UI thread, so
        // must invoke setVisible rather than just calling it.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Test().setVisible(true);
            }
        });
    }
}