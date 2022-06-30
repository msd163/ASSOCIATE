package drawingLayer.trust;

import drawingLayer.DrawingWindow;
import societyLayer.agentSubLayer.Agent;
import societyLayer.agentSubLayer.World;

import java.awt.*;
import java.util.List;

public class IndirectObservationBarDrawingWindow extends DrawingWindow {


    //============================//============================  panning params

    public IndirectObservationBarDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxObservationCap();
        axisY = world.getAgentsCount() * 21;
        headerTitle = "Indirect Trust Observation Bar Chart";
        setName("tut_ibs");
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle + " :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }

        normalizeCoordination();

        //============================//============================//============================

        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        List<Agent> agents = world.getAgents();
        for (int i = 0, jj = agents.size() - 1; jj > -1; jj--, i++) {
            Agent agent = agents.get(jj);
            if(agent!=null && agent.getBehavior()!=null && agent.getCapacity()!=null && agent.getTrust()!=null) {
                drawBar(agent,
                        agent.getBehavior().getBehaviorState(),
                        i,
                        agent.getCapacity().getIndirectObservationCap(),
                        agent.getCapacity().getIndirectObservationItemCap(),
                        agent.getTrust().getIndirectObservationRewardsCount(),
                        agent.getTrust().getIndirectObservations()
                );
            }
        }

    }
}
