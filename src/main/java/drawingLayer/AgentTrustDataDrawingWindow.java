package drawingLayer;

import systemLayer.Agent;
import systemLayer.World;
import utils.Globals;

import java.awt.*;
import java.util.List;

public class AgentTrustDataDrawingWindow extends DrawingWindow {

    private World world;

    //============================//============================  panning params

    public AgentTrustDataDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxTrustHistoryCap();
        axisY = world.getAgentsCount() * 21;
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr,world.getDrawingTitle())) {
            return;
        }
        printStatsInfo(1, "Trust Data Capacity and Current Size", Color.cyan);
        normalizeCoordination();

        //============================//============================//============================

        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        List<Agent> agents = world.getSortedAgentsByCapPower();
        for (int i = 0,jj= agents.size()-1, agentsLength = agents.size(); i < agentsLength; i++,jj--) {
            Agent agent = agents.get(jj);

            g.setColor(Globals.Color$.getNormal(agent.getBehavior().getBehaviorState()));

            g.fillRect(-agent.getCapacity().getCapPower(), i * 21, agent.getCapacity().getCapPower(), 20);
            g.setColor(Color.BLACK);
            g.drawString(agent.getId() + "", -30, i * 21 + 12);
            g.setColor(Color.GRAY);
            g.fillRect(5, i * 21, agent.getCapacity().getTrustHistoryCap(), 20);

            g.setColor(Globals.Color$.getLight(agent.getBehavior().getBehaviorState()));

            g.fillRect(5, i * 21, agent.getTrust().getHistorySize(), 20);

        }

    }
}
