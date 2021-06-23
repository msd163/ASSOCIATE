package drawingLayer;

import systemLayer.Agent;
import systemLayer.World;
import utils.Globals;

import java.awt.*;
import java.util.List;

public class AgentTrustDataDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public AgentTrustDataDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxExperienceCap();
        axisY = world.getAgentsCount() * 21;
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr,"Experience Data :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }

        normalizeCoordination();

        //============================//============================//============================

        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        List<Agent> agents = world.getAgents();
        for (int i = 0, jj = agents.size() - 1, agentsLength = agents.size(); i < agentsLength; i++, jj--) {
            Agent agent = agents.get(jj);

            //-- Drawing agent cap power rectangle
            g.setColor(Globals.Color$.getNormal(agent.getBehavior().getBehaviorState()));
            g.fillRect(-agent.getCapacity().getCapPower(), i * 21, agent.getCapacity().getCapPower(), 20);

            //-- Printing agent ID
            g.setColor(Color.BLACK);
            g.drawString(agent.getId() + "", -30, i * 21 + 15);

            //-- Drawing total number rectangle
            g.setColor(Color.GRAY);
            g.fillRect(5, i * 21, agent.getCapacity().getExperienceCap(), 20);

            //-- Drawing filled number rectangle
            g.setColor(Globals.Color$.getLight(agent.getBehavior().getBehaviorState()));
            g.fillRect(5, i * 21, agent.getTrust().getExperiences().size(), 20);

            //-- Printing number/total
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(agent.getTrust().getExperiences().size() + " / " + agent.getCapacity().getExperienceCap(),
                    agent.getTrust().getExperiences().size()+20, i * 21 + 15);

        }

    }
}
