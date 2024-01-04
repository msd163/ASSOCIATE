package SiM.monitor.trust;

import SiM.monitor.DrawingWindow;
import WSM.society.agent.Agent;
import WSM.society.agent.World;

import java.awt.*;
import java.util.List;

public class IndirectExperienceBarDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public IndirectExperienceBarDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxExperienceCap();
        axisY = world.getAgentsCount() * 21;
        headerTitle = "Indirect Trust Experience Bar Chart";
        setName("tut_ixp");
    }

    Agent agent;

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle + " :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }

        normalizeCoordination();

        //============================//============================//============================

        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        List<Agent> agents = world.getAgents();
        for (int i = 0, jj = agents.size() - 1, agentsLength = agents.size(); i < agentsLength; i++, jj--) {
            agent = agents.get(jj);

            if (agent == null
                    || agent.getBehavior() == null || agent.getBehavior().getBehaviorState() == null
                    || agent.getCapacity() == null || agent.getTrust() == null
            ) {
                continue;
            }
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
