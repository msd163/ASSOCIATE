package drawingLayer;

import stateLayer.Environment;
import stateLayer.StateX;
import stateLayer.TransitionX;
import systemLayer.*;
import utils.Point;
import utils.RectangleX;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class StateMachineDrawingWindow extends DrawingWindow {

    private static final Random random = new Random();

    private Environment environment;
    private Color[] colors;

    private int _colorIndex = 0;

    //============================//============================//============================
    public StateMachineDrawingWindow(World world) {
        super();
        this.world = world;
        this.environment = world.getEnvironment();
        colors = new Color[environment.getStateCount() * 2];

        random.setSeed(new Date().getTime());

        for (int i = 0; i < colors.length; i++) {
            colors[i] = getRandColor();

        }

        pnOffset.x = 400;
        pnOffset.y = -200;
        scale = 0.03f;


    }

    //============================//============================//============================
    int index = 0;

    @Override
    public void paint(Graphics gr) {
        //============================//============================ Preparing
        g = (Graphics2D) gr;
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, getWidth(), getHeight());
        pauseNotice(g);

        g.setColor(Color.YELLOW);

        //============================ Title
        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        //g.drawString(world.toString(), 10, 10);

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

            if (trans.isDrawIsActive()) {
                g.setColor(colors[_colorIndex]);
                g.setStroke(new BasicStroke(10));
                trans.setDrawIsActive(false);
            } else {
                g.setColor(Color.DARK_GRAY);
                g.setStroke(new BasicStroke(2));
            }

            g.draw(new Arc2D.Float(trans.getDrawX(), trans.getDrawY(),                          // box upper left
                    trans.getDrawWidthAndHeight(), trans.getDrawWidthAndHeight(),               // box width and height
                    trans.getDrawAngStart(), trans.getDrawAngExtend(),                          // angle start, extent
                    Arc2D.OPEN));

            g.setStroke(new BasicStroke(6));
            g.setColor(colors[_colorIndex]);

            g.draw(new Arc2D.Float(trans.getDrawX(), trans.getDrawY(),                          // box upper left
                    trans.getDrawWidthAndHeight(), trans.getDrawWidthAndHeight(),               // box width and height
                    trans.getDrawAngStart(), trans.getDrawAngExtend() > 0 ? trans.getDrawSourceArrowSize() : -trans.getDrawSourceArrowSize(),                          // angle start, extent
                    Arc2D.OPEN));
//            g.drawRoundRect((int) trans.getDrawX(), (int) trans.getDrawY(), 20, 20, 4, 4);
            g.setStroke(new BasicStroke(1));

        }

        //============================//============================ Drawing states and their agents
        try {
            for (int stateIndex = 0, cnt = environment.getStateCount(); stateIndex < cnt; stateIndex++) {
                StateX stateX = environment.getState(stateIndex);

                if (stateX.isIsPitfall()) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.GREEN);
                }
                RectangleX rec = stateX.getBoundedRectangle();

                //-- Drawing state and its ID
                g.draw(new Rectangle.Float(rec.x, rec.y, rec.with, rec.height));
                g.drawString("(" + stateX.getId() + ")", rec.x, rec.y - 20);

                //-- Drawing agents of state
                if (!stateX.getAgents().isEmpty()) {
                    index = 0;
                    for (Agent agent : stateX.getAgents()) {
                        drawAgent(agent, g, index++);
                    }
                }
            }
        } catch (Exception e) {
        }

        //============================//============================ Creating Blue state for the state of agent that are traceable.
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

    Color honestBackColor;
    Color honestForeColor;
    Color honestBorderColor;

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

        agent.setLoc(loc_x, loc_y);

        honestBackColor = behavior.getHasHonestState() ? new Color(0, 255, 85) : new Color(255, 71, 71);
        honestForeColor = behavior.getHasHonestState() ? new Color(36, 151, 9) : new Color(255, 196, 166);
        honestBorderColor = behavior.getHasHonestState() ? new Color(29, 102, 0) : new Color(160, 0, 0);

        // Drawing links to watched agents
        if (agent.isSimConfigLinkToWatchedAgents()) {
            g.setColor(Color.GRAY);
            for (WatchedAgent wa : watchedAgents) {
                g.drawLine(agent.getLocX(), agent.getLocY(), wa.getAgent().getLocX(), wa.getAgent().getLocY());
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
        g.setFont(new Font("Tahoma", Font.PLAIN, 9));
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

}
