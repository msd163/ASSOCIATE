package SiM.monitor.trust;

import SiM.monitor.DrawingWindow;
import WSM.society.agent.Agent;
import WSM.World;

import java.awt.*;
import java.util.List;

public class RecommendationBarDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public RecommendationBarDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxTrustRecommendationCap();
        axisY = world.getAgentsCount() * 21;
        headerTitle = "Trust Recommendation Bar Chart";
        setName("tut_rcm");
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
        for (int jj = agents.size() - 1, i = 0; jj > -1; i++, jj--) {
            Agent agent = agents.get(jj);

            if (agent == null
                    || agent.getBehavior() == null || agent.getBehavior().getBehaviorState() == null
                    || agent.getCapacity() == null || agent.getTrust() == null
            ) {
                continue;
            }

            drawBar(agent,
                    agent.getBehavior().getBehaviorState(),
                    i,
                    agent.getCapacity().getTrustRecommendationCap(),
                    agent.getCapacity().getTrustRecommendationItemCap(),
                    agent.getTrust().getRecommendationRewardsCount(),
                    agent.getTrust().getRecommendations()
            );
        }

    }
}
