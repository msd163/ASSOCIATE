package drawingLayer;

import systemLayer.Agent;
import systemLayer.World;
import utils.Globals;

import java.awt.*;
import java.util.List;

public class AgentRecommendationDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public AgentRecommendationDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxTrustRecommendationCap();
        axisY = world.getAgentsCount() * 21;
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr,world.getDrawingTitle())) {
            return;
        }
        printStatsInfo(1, "Recommendation History Capacity and Current Size", Color.cyan);
        normalizeCoordination();

        //============================//============================//============================

        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        List<Agent> agents = world.getAgents();
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
