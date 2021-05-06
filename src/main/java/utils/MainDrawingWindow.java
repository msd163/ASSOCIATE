package utils;

import stateTransition.Environment;
import stateTransition.StateX;
import stateTransition.TransitionX;
import system.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainDrawingWindow extends JPanel implements MouseMotionListener {

    private static final Random random = new Random();

    private World world;
    private Environment environment;
    private Color[] colors;

    private java.awt.Point offset = new java.awt.Point(0, 0);
    private java.awt.Point startPoint;

    public MainDrawingWindow(World world) {
        this.world = world;
        this.environment = world.getEnvironment();
        colors = new Color[environment.getStateCount() * 2];

        random.setSeed(new Date().getTime());

        for (int i = 0; i < colors.length; i++) {
            colors[i] = getRandColor();
        }

        //============================//============================
        addMouseListener(
                 new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {
                        startPoint = e.getPoint();
                        startPoint.x -= offset.x;
                        startPoint.y -= offset.y;
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        startPoint = null;
                    }


        });

        this.addMouseMotionListener(this);

    }

    //============================//============================//============================

    private int _colorIndex = 0;
    private StateX _stateX;
    private Point _statePoint;
    private ArrayList<StateX> _targets;

    Graphics2D g;

    @Override
    public void paint(Graphics gr) {
        //============================//============================ Preparing
        g = (Graphics2D) gr;


        g.setBackground(Color.BLACK);

        g.clearRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.YELLOW);

        //============================ Title
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g.drawString(world.toString(), 40, 40);

        //============================ Translate

        g.translate(offset.x, offset.y);

        //============================ Bound Rectangle
        //g.drawRect(0, 0, world.getWidth(), world.getHeight());


        //============================//============================ Drawing states
        _colorIndex = 0;
        for (int x = 0; x < environment.getStateCount(); x++) {

            _stateX = environment.getState(x);
            _statePoint = _stateX.getLocation();
            _targets = _stateX.getTargets();

            drawStateX(g, _stateX);
        }


        for (TransitionX trans : environment.getTransitions()) {

            _colorIndex++;
            if (_colorIndex >= colors.length) {
                _colorIndex = 0;
            }

            g.setColor(colors[_colorIndex]);
            g.setStroke(new BasicStroke(5));

            g.draw(new Arc2D.Float(trans.getDrawX(), trans.getDrawY(),      // box upper left
                    trans.getDrawWidthAndHeight(), trans.getDrawWidthAndHeight(),                                   // box width and height
                    trans.getDrawAngStart(), trans.getDrawAngExtend(),                                 // angle start, extent
                    Arc2D.OPEN));

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

    //============================//============================//============================

    public Color getRandColor() {
        return new Color(
                random.nextInt(210) + 40,
                random.nextInt(210) + 40,
                random.nextInt(210) + 40
        );
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        int x = p.x - startPoint.x;
        int y = p.y - startPoint.x;
        offset = new java.awt.Point(x, y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
