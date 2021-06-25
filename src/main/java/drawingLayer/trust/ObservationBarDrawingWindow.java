package drawingLayer.trust;

import drawingLayer.DrawingWindow;
import systemLayer.Agent;
import systemLayer.World;
import trustLayer.data.TrustObservation;
import utils.Globals;

import java.awt.*;
import java.util.List;

public class ObservationBarDrawingWindow extends DrawingWindow {


    //============================//============================  panning params

    public ObservationBarDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxObservationCap();
        axisY = world.getAgentsCount() * 21;
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, "Observation Data :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }

        normalizeCoordination();

        //============================//============================//============================

        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        List<Agent> agents = world.getAgents();
        for (int i = 0, jj = agents.size() - 1; jj > -1; jj--, i++) {
            Agent agent = agents.get(jj);

            drawBar(agent,
                    agent.getBehavior().getBehaviorState(),
                    i,
                    agent.getCapacity().getObservationCap(),
                    agent.getCapacity().getObservationItemCap(),
                    agent.getTrust().getObservationRewardsCount(),
                    agent.getTrust().getObservations()
            );

        }

    }
}
