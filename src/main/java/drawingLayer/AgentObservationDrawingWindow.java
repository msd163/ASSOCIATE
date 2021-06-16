package drawingLayer;

import systemLayer.Agent;
import systemLayer.World;
import utils.Globals;

import java.awt.*;
import java.util.List;

public class AgentObservationDrawingWindow extends DrawingWindow {

    private World world;


    //============================//============================  panning params

    public AgentObservationDrawingWindow(World world) {
        super();
        this.world = world;
    }

    private int worldTimer;

    @Override
    public void paint(Graphics gr) {

        worldTimer = Globals.WORLD_TIMER - 1;

        if (worldTimer < 0) {
            return;
        }

        g = (Graphics2D) gr;
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, getWidth(), getHeight());
        pauseNotice(g);

        axisX = 0;

        g.setColor(Color.YELLOW);

        //============================//============================ Translate for panning and scaling

        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));

        g.drawString("World Time                : " + worldTimer, 100, 50);
        g.drawString("Episode                    : " + Globals.EPISODE, 100, 90);
        g.drawString("Observation Capacity and Current Size", 100, 150);

        //============================//============================//============================
        //============================ Draw mouse plus
        Point mousePoint = getMousePosition();
        if (mousePoint != null) {
            g.setColor(Color.WHITE);
            //-- (TOP-DOWN) Drawing vertical line for mouse pointer
            g.drawLine(mousePoint.x, 0, mousePoint.x, getHeight());
            //-- (LEFT-RIGHT) Drawing horizontal line for mouse pointer
            g.drawLine(0, mousePoint.y, getWidth(), mousePoint.y);
        }

        //============================ Translate
        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
        g.scale(scale, scale);
        g.translate(200, 300);

        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        List<Agent> agents = world.getSortedAgentsByCapPower();
        for (int i = 0, jj = agents.size() - 1; jj > -1; jj--, i++) {
            Agent agent = agents.get(jj);

            g.setColor(Globals.Color$.getNormal(agent.getBehavior().getBehaviorState()));

            g.fillRect(-agent.getCapacity().getCapPower(), i * 21, agent.getCapacity().getCapPower(), 20);
            g.setColor(Color.BLACK);
            g.drawString(agent.getId() + "", -30, i * 21 + 12);
            g.setColor(Color.GRAY);
            g.fillRect(5, i * 21, agent.getCapacity().getObservationCap(), 20);

            //g.setColor(Globals.Color$.getLight(agent.getBehavior().getBehaviorState()));

            int obsSize = agent.getTrust().getObservations().size();
           // g.fillRect(5, i * 21, obsSize, 20);

            if (obsSize > 0) {
                int[] obsTarPit = agent.getTrust().getObservationInTargetAndPitfallCount();
                g.setColor(Globals.Color$.lightGreen);
                g.fillRect(5, i * 21 + 2, obsTarPit[0], 16);
                g.setColor(Globals.Color$.lightRed);
                g.fillRect(5 + obsTarPit[0], i * 21 + 2, obsTarPit[1], 16);
            }

        }

    }
}
