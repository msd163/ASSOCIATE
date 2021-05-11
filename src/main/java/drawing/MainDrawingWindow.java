package drawing;

import stateTransition.Environment;
import stateTransition.StateX;
import stateTransition.TransitionX;
import system.Agent;
import system.World;
import utils.RectangleX;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.util.Date;
import java.util.Random;

public class MainDrawingWindow extends JPanel implements MouseMotionListener, MouseWheelListener {

    private static final Random random = new Random();

    private World world;
    private Environment environment;
    private Color[] colors;

    //============================//============================  panning params
    private utils.Point pnOffset = new utils.Point(0, 0);
    private utils.Point pnOffsetOld = new utils.Point(0, 0);
    private utils.Point pnStartPoint = new utils.Point(0, 0);

    private float scale = 1f;
    private int _colorIndex = 0;

    Graphics2D g;

    //============================//============================//============================
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

        //============================//============================ Translate for panning and scaling

        g.translate(pnOffset.x, pnOffset.y);
        g.scale(scale, scale);

        //============================//============================ Drawing Transition
        _colorIndex = 0;
        for (TransitionX trans : environment.getTransitions()) {

            _colorIndex++;
            if (_colorIndex >= colors.length) {
                _colorIndex = 0;
            }

            g.setColor(colors[_colorIndex]);
            if (trans.isDrawIsActive()) {
                g.setStroke(new BasicStroke(10));
                trans.setDrawIsActive(false);
            } else {
                g.setStroke(new BasicStroke(2));
            }

            g.draw(new Arc2D.Float(trans.getDrawX(), trans.getDrawY(),                          // box upper left
                    trans.getDrawWidthAndHeight(), trans.getDrawWidthAndHeight(),               // box width and height
                    trans.getDrawAngStart(), trans.getDrawAngExtend(),                          // angle start, extent
                    Arc2D.OPEN));

            g.setStroke(new BasicStroke(6));

            g.draw(new Arc2D.Float(trans.getDrawX(), trans.getDrawY(),                          // box upper left
                    trans.getDrawWidthAndHeight(), trans.getDrawWidthAndHeight(),               // box width and height
                    trans.getDrawAngStart(), trans.getDrawAngExtend() > 0 ? trans.getDrawSourceArrowSize() : -trans.getDrawSourceArrowSize(),                          // angle start, extent
                    Arc2D.OPEN));
//            g.drawRoundRect((int) trans.getDrawX(), (int) trans.getDrawY(), 20, 20, 4, 4);
            g.setStroke(new BasicStroke(1));

        }

        //============================//============================ Drawing states
        for (int stateIndex = 0, cnt = environment.getStateCount(); stateIndex < cnt; stateIndex++) {
            StateX stateX = environment.getState(stateIndex);
            RectangleX rec = stateX.getBoundedRectangle();
            Color color = g.getColor();

           // g.setFont(new Font("TimesRoman", Font.PLAIN, 15));

           /* g.setColor(Color.RED);
            g.drawString(
                    "TL " + rec.topLeft().x + "," + rec.topLeft().y,
                    rec.topLeft().x - 10,
                    rec.topLeft().y - 10);

            g.setColor(Color.YELLOW);
            g.drawString(
                    "TR " + rec.topRight().x + "," + rec.topRight().y,
                    rec.topRight().x - 10,
                    rec.topRight().y - 10
            );


            g.setColor(Color.cyan);
            g.drawString(
                    "BR " + rec.bottomRight().x + "," + rec.bottomRight().y,
                    rec.bottomRight().x ,
                    rec.bottomRight().y + 20
            );


            g.setColor(Color.orange);
            g.drawString(
                    "BL " + rec.bottomLeft().x + "," + rec.bottomLeft().y,
                    rec.bottomLeft().x ,
                    rec.bottomLeft().y + 30
            );*/

            g.setColor(Color.GREEN);
            g.draw(new Rectangle.Float(rec.x, rec.y, rec.with, rec.height));
            g.drawString("(" + stateX.getId() + ")", rec.x, rec.y - 20);
            g.setColor(color);

            if (!stateX.getAgents().isEmpty()) {
                int index = 0;
                for (Agent agent : stateX.getAgents()) {
                    agent.draw(g, index++);
                }
            }
        }

        //============================//============================ Drawing agents
        for (Agent agent : world.getAgents()) {

            if(agent.isSimConfigShowTargetState()){
                RectangleX rec = agent.getTargetState().getBoundedRectangle();
                g.setColor(Color.RED);
                g.draw(new Rectangle.Float(rec.x, rec.y, rec.with, rec.height));
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
            x += -0.5 * e.getWheelRotation();
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
