package drawingLayer.trust;

import _type.TtBehaviorState;
import drawingLayer.DrawingWindow;
import systemLayer.Agent;
import systemLayer.World;
import trustLayer.data.TrustIndirectExperience;

import java.awt.*;
import java.util.List;

public class IndirectExperienceBarDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public IndirectExperienceBarDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxExperienceCap();
        axisY = world.getAgentsCount() * 21;
    }

    Agent agent;

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, "Indirect Experience Data :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }

        normalizeCoordination();

        //============================//============================//============================

        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        List<Agent> agents = world.getAgents();
        for (int i = 0, jj = agents.size() - 1, agentsLength = agents.size(); i < agentsLength; i++, jj--) {
            agent = agents.get(jj);

            drawBar(agent,
                    agent.getBehavior().getBehaviorState(),
                    i,
                    agent.getCapacity().getIndirectExperienceCap(),
                    agent.getCapacity().getIndirectExperienceItemCap(),
                    agent.getTrust().getIndirectExperienceRewardsCount(),
                    agent.getTrust().getIndirectExperiences()
            );

        }
    }
}
