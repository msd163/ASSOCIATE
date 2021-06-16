package drawingLayer;

import systemLayer.Agent;
import systemLayer.World;
import utils.Globals;

import java.awt.*;
import java.util.List;

public class AgentRecommendationDrawingWindow extends DrawingWindow {

    private World world;


    //============================//============================  panning params

    public AgentRecommendationDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxTrustRecommendationCap();
        axisY = world.getAgentsCount() * 21;
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


        //============================//============================ Translate for panning and scaling

        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));

        g.setColor(Color.GREEN);
        g.drawString("Simulation Timer            : " + Globals.SIMULATION_TIMER, 100, 50);
        g.drawString("Recommendation History Capacity and Current Size", 500, 50);


        g.setColor(Color.YELLOW);
        g.drawString("World Time                : " + worldTimer, 100, 100);
        g.drawString("Episode                    : " + Globals.EPISODE, 100, 140);

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
        for (int jj = agents.size() - 1, i = 0; jj > -1; i++, jj--) {
            Agent agent = agents.get(jj);

            g.setColor(Globals.Color$.getNormal(agent.getBehavior().getBehaviorState()));

            g.fillRect(-agent.getCapacity().getCapPower(), i * 21, agent.getCapacity().getCapPower(), 20);
            g.setColor(Color.BLACK);
            g.drawString(agent.getId() + "", -30, i * 21 + 12);
            g.setColor(Color.GRAY);
            g.fillRect(5, i * 21, agent.getCapacity().getTrustRecommendationCap(), 20);

            g.setColor(Globals.Color$.getLight(agent.getBehavior().getBehaviorState()));

            g.fillRect(5, i * 21, agent.getTrust().getRecommendations().size(), 20);

        }

    }
}
