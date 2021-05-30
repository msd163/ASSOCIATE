package drawingLayer;

import stateLayer.Environment;
import stateLayer.StateX;
import stateLayer.TransitionX;
import systemLayer.*;
import utils.Config;
import utils.Point;
import utils.RectangleX;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainDrawingWindow extends JPanel implements MouseMotionListener, MouseWheelListener {

    private static final Random random = new Random();

    private World world;
    private Environment environment;
    private Color[] colors;

    //============================//============================  panning params
    private utils.Point scaleOffset = new utils.Point(0, 0);
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

                        // For resetting screen by double click
                        if (e.getClickCount() == 2) {
                            pnOffset = new utils.Point(0, 0);
                            pnOffsetOld = new utils.Point(0, 0);
                            pnStartPoint = new utils.Point(0, 0);
                            pnOffsetOld.x = pnOffset.x;
                            pnOffsetOld.y = pnOffset.y;
                            scale = 1f;
                        }
                    }
                });

        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    //============================//============================//============================
    Color tempColor;

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

        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
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

           // tempColor = g.getColor();

            if (stateX.isIsPitfall()) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }
            RectangleX rec = stateX.getBoundedRectangle();

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

            g.draw(new Rectangle.Float(rec.x, rec.y, rec.with, rec.height));
            g.drawString("(" + stateX.getId() + ")", rec.x, rec.y - 20);
           // g.setColor(tempColor);

            if (!stateX.getAgents().isEmpty()) {
                int index = 0;
                for (Agent agent : stateX.getAgents()) {
                    drawAgent(agent, g, index++);
                }
            }
        }

        //============================//============================ Drawing agents
        for (Agent agent : world.getAgents()) {

            if (agent.isSimConfigShowTargetState()) {
                RectangleX rec = agent.getCurrentTarget().getBoundedRectangle();
                g.setColor(Color.BLUE);
                g.draw(new Rectangle.Float(rec.x, rec.y, rec.with, rec.height));
            }

        }
    }

    //============================//============================//============================ Drawing Agent


    BasicStroke stroke3 = new BasicStroke(3);
    BasicStroke stroke2 = new BasicStroke(2);
    BasicStroke stroke1 = new BasicStroke(1);
    Font font = new Font("Tahoma", Font.PLAIN, 9);
    Color honestBackColor;
    Color honestForeColor;
    Color honestBorderColor;

    private boolean isCapCandid = false;

    public void drawAgent(Agent agent, Graphics2D g, int index) {

        StateX state = agent.getState();
        AgentBehavior behavior = agent.getBehavior();
        AgentCapacity capacity = agent.getCapacity();
        List<WatchedAgent> watchedAgents = agent.getWatchedAgents();

        int loc_x;
        int loc_y;

        Point tileIndex = state.getTileLocation(index);

        loc_x = tileIndex.x;
        loc_y = tileIndex.y;


        honestBackColor = behavior.getIsHonest() ? new Color(0, 255, 85) : new Color(255, 71, 71);
        honestForeColor = behavior.getIsHonest() ? new Color(36, 151, 9) : new Color(255, 196, 166);
        honestBorderColor = behavior.getIsHonest() ? new Color(29, 102, 0) : new Color(160, 0, 0);
        isCapCandid = Config.DRAWING_SHOW_POWERFUL_AGENTS_RADIUS && capacity.getCapPower() > Config.DRAWING_POWERFUL_AGENTS_THRESHOLD;
        // Drawing watch radius
        if (isCapCandid || agent.isSimConfigShowWatchRadius()) {
            g.setColor(isCapCandid ? (behavior.getIsHonest() ? Color.GREEN : Color.RED) : agent.isSimConfigTraceable() ? Color.CYAN : Color.lightGray);
            g.drawOval(
                    loc_x - capacity.getWatchDepth(),
                    loc_y - capacity.getWatchDepth(),
                    capacity.getWatchDepth() * 2,
                    capacity.getWatchDepth() * 2
            );
        }

        // Drawing links to watched agents
        if (agent.isSimConfigLinkToWatchedAgents()) {
            g.setColor(Color.GRAY);
            for (WatchedAgent wa : watchedAgents) {
                g.drawLine(loc_x, loc_y, wa.getAgent().getLoc_x(), wa.getAgent().getLoc_y());
            }
        }

/*        if (agent.isSimConfigShowRequestedService() && agent.getRequestedServices().size() > 0) {
            Service service = agent.getRequestedServices().get(agent.getRequestedServices().size() - 1);
            if (service != null) {
                g.setStroke(stroke3);
                if (service.getDoer() != null) {
                    g.setColor(service.getDoer().getBehavior().getIsHonest() ? Color.GREEN : Color.RED);
                    g.drawLine(loc_x, loc_y, service.getDoer().getLoc_x(), service.getDoer().getLoc_y());
                } else {
                    g.setColor(Color.GREEN);
                    g.drawLine(loc_x, loc_y, loc_x + 40, loc_y + 40);
                }
                g.setStroke(stroke1);
            }
        }*/

/*        if (simConfigShowTargetState) {
            RectangleX rec = targetState.getBoundedRectangle();
            g.setColor(Color.RED);
            g.draw(new Rectangle.Float(rec.x, rec.y, rec.with, rec.height));
        }*/

        // Set color of node with honest strategy
        g.setColor(honestBackColor);

        // Draw node according to it's capacity
        int agentBound = capacity.getCapPower() / 10;
        g.fillOval(loc_x - agentBound, loc_y - agentBound, agentBound * 2, agentBound * 2);

        if (agent.isInTargetState()) {
            // If agent is in target state draw a circle around it.
            g.setColor(honestBorderColor);
            g.setStroke(stroke2);
            g.drawArc(loc_x - agentBound, loc_y - agentBound, agentBound * 2, agentBound * 2, 0, 270);
        }

        // Drawing id of the node
        g.setColor(honestForeColor);
        g.setFont(font);
        g.drawString(agent.getId() + "", loc_x - 5, loc_y + 5 /*+ capacity.getCapPower() + 10*/);

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

        float sc = scale;

        if (sc > 1) {
            sc += -0.5 * e.getWheelRotation();
        } else if (sc < 1) {
            sc += -0.05 * e.getWheelRotation();
        } else {
            if (e.getWheelRotation() < 0) {
                sc += -0.5 * e.getWheelRotation();
            } else {
                sc += -0.05 * e.getWheelRotation();
            }
        }
        if (sc > 5) {
            sc = 5;
        } else if (sc < 0.09f) {
            sc = 0.05f;
        }

        if (sc > scale) {
            scaleOffset.y = -(int) ((e.getY()+ pnOffset.y) * (sc - scale));
//            pnOffset.x -= (e.getX() * (sc - scale));
        } else if (sc < scale) {
            scaleOffset.y = (int) (e.getY() * (scale - sc));
//            pnOffset.x += (e.getX() * (scale - sc));
        }

        scale = sc;
    }
}
