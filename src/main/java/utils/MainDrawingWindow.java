package utils;

import stateTransition.Environment;
import stateTransition.StateX;
import stateTransition.TransitionX;
import system.Agent;
import system.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainDrawingWindow extends JPanel implements MouseMotionListener, MouseWheelListener {

    private static final Random random = new Random();

    private World world;
    private Environment environment;
    private Color[] colors;

    //============================//============================  panning params
    private Point pnOffset = new Point(0, 0);
    private Point pnOffsetOld = new Point(0, 0);
    private Point pnStartPoint = new Point(0, 0);

    private float scale = 1f;

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
                        pnStartPoint.x = (int) (e.getPoint().getX());
                        pnStartPoint.y = (int) (e.getPoint().getY());
                        pnOffsetOld.x = pnOffset.x;
                        pnOffsetOld.y = pnOffset.y;
                    }
                });

        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
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

        g.translate(pnOffset.x, pnOffset.y);
        g.scale(scale, scale);
        //============================ Bound Rectangle
        //g.drawRect(0, 0, world.getWidth(), world.getHeight());


        //============================//============================ Drawing Transition
        _colorIndex = 0;
        for (TransitionX trans : environment.getTransitions()) {

            _colorIndex++;
            if (_colorIndex >= colors.length) {
                _colorIndex = 0;
            }

            g.setColor(colors[_colorIndex]);
            g.setStroke(new BasicStroke(2));

            g.draw(new Arc2D.Float(trans.getDrawX(), trans.getDrawY(),      // box upper left
                    trans.getDrawWidthAndHeight(), trans.getDrawWidthAndHeight(),                                   // box width and height
                    trans.getDrawAngStart(), trans.getDrawAngExtend(),                                 // angle start, extent
                    Arc2D.OPEN));

        }


        //============================//============================ Drawing states
        for (int x = 0; x < environment.getStateCount(); x++) {

            _stateX = environment.getState(x);
            _statePoint = _stateX.getLocation();
            _targets = _stateX.getTargets();

            drawStateX(g, _stateX);
        }


        //============================//============================ Drawing agents
      /*  for (Agent agent : world.getAgents()) {

            agent.draw((Graphics2D) g);

        }*/
    }

    //============================//============================//============================

    static void drawStateX(Graphics2D g, StateX stateX) {
        final int rad = stateX.getWidth() / 2;
        final int radEnd = 2 * rad;
        int x = stateX.getLocation().getX();
        int y = stateX.getLocation().getY();
        Color color = g.getColor();
//        g.setColor(Color.BLACK);
//        g.fill(new Rectangle.Float(x - rad, y - rad, 2 * rad, 2 * rad));
        g.setColor(Color.GREEN);
        g.draw(new Rectangle.Float(x - rad, y - rad, radEnd,  radEnd));
        g.drawString("(" + stateX.getId() + ")", x - rad, y - rad - 20);
        g.setColor(color);

        if (!stateX.getAgents().isEmpty()) {
            int index = 0;
            for (Agent agent : stateX.getAgents()) {
                agent.draw(g, index++);
            }


        }

    }

    //============================//============================//============================

    public Color getRandColor() {
        return new Color(
                random.nextInt(210) + 40,
                random.nextInt(210) + 40,
                random.nextInt(210) + 40
        );
    }

    //============================//============================//============================ Mouse events
    @Override
    public void mouseDragged(MouseEvent e) {
        pnOffset.x = pnOffsetOld.x + e.getPoint().x - pnStartPoint.x;
        pnOffset.y = pnOffsetOld.y + e.getPoint().y - pnStartPoint.y;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        float x = scale;

        if (x > 1) {
            x += -1 * e.getWheelRotation();
        } else {
            x += -0.1 * e.getWheelRotation();
        }
        if (x > 5) {
            x = 5;
        } else if (x < 0.2f) {
            x = 0.2f;
        }
        scale = x;
    }
}
