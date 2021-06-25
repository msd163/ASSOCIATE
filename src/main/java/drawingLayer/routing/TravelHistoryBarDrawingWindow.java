package drawingLayer.routing;

import drawingLayer.DrawingWindow;
import systemLayer.Agent;
import systemLayer.World;
import utils.Globals;

import java.awt.*;
import java.util.List;

public class TravelHistoryBarDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public TravelHistoryBarDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxTravelHistoryCap();
        axisY = world.getAgentsCount() * 21;
    }


    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr,"Travel History Data :: "+world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }

        normalizeCoordination();

        //============================//============================//============================

        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        List<Agent> agents = world.getAgents();
        for (int i = 0, jj = agents.size() - 1, agentsLength = agents.size(); i < agentsLength; jj--, i++) {
            Agent agent = agents.get(jj);
            int size = agent.getTravelHistories().size();

            g.setColor(Globals.Color$.getNormal(agent.getBehavior().getBehaviorState()));

            g.fillRect(-agent.getCapacity().getCapPower(), i * 21, agent.getCapacity().getCapPower(), 20);
            g.setColor(Color.BLACK);
            g.drawString(agent.getId() + "", -30, i * 21 + 12);
            g.setColor(Color.GRAY);
            g.fillRect(5, i * 21, agent.getCapacity().getTravelHistoryCap(), 20);

            g.setColor(Globals.Color$.getLight(agent.getBehavior().getBehaviorState()));

            g.fillRect(5, i * 21, size, 20);

            //-- Printing data_number/data_cap
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(size + " / " + agent.getCapacity().getTravelHistoryCap(),
                    size + 20, i * 21 + 15);
        }
    }
}
