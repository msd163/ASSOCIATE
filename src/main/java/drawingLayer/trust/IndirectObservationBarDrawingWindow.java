package drawingLayer.trust;

import drawingLayer.DrawingWindow;
import systemLayer.Agent;
import systemLayer.World;
import utils.Globals;

import java.awt.*;
import java.util.List;

public class IndirectObservationBarDrawingWindow extends DrawingWindow {


    //============================//============================  panning params

    public IndirectObservationBarDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxObservationCap();
        axisY = world.getAgentsCount() * 21;
    }


    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, "Indirect Observation Data :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }

        normalizeCoordination();

        //============================//============================//============================

        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        List<Agent> agents = world.getAgents();
        for (int i = 0, jj = agents.size() - 1; jj > -1; jj--, i++) {
            Agent agent = agents.get(jj);

            g.setColor(Globals.Color$.getNormal(agent.getBehavior().getBehaviorState()));

            g.fillRect(-agent.getCapacity().getCapPower(), i * 21, agent.getCapacity().getCapPower(), 20);
            g.setColor(Color.BLACK);
            g.drawString(agent.getId() + "", -30, i * 21 + 12);
            g.setColor(Color.GRAY);
            g.fillRect(5, i * 21, agent.getCapacity().getIndirectObservationCap(), 20);

            int obsSize = agent.getTrust().getIndirectObservations().size();

            if (obsSize > 0) {
                int[] obsTarPit = agent.getTrust().getIndirectObservationRewardsCount();
                g.setColor(Globals.Color$.lightGreen);
                g.fillRect(5, i * 21 + 2, obsTarPit[0], 16);
                g.setColor(Globals.Color$.lightRed);
                g.fillRect(5 + obsTarPit[0], i * 21 + 2, obsTarPit[1], 16);
            }
        }

    }
}
