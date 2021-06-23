package drawingLayer.trust;

import _type.TtBehaviorState;
import drawingLayer.DrawingWindow;
import systemLayer.Agent;
import systemLayer.World;
import trustLayer.data.TrustIndirectExperience;
import trustLayer.data.TrustIndirectObservation;
import utils.Globals;

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

    List<TrustIndirectExperience> indirectExperiences;
    int indirectExperienceCap;
    Agent agent;
    TtBehaviorState behaviorState;

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

            indirectExperiences = agent.getTrust().getIndirectExperiences();
            indirectExperienceCap = agent.getCapacity().getIndirectExperienceCap();
            behaviorState = agent.getBehavior().getBehaviorState();

            //-- Drawing agent cap power rectangle
            g.setColor(Globals.Color$.getNormal(behaviorState));
            g.fillRect(-agent.getCapacity().getCapPower(), i * 21, agent.getCapacity().getCapPower(), 20);

            //-- Printing agent ID
            g.setColor(Color.BLACK);
            g.drawString(agent.getId() + "", -30, i * 21 + 15);

            //-- Drawing total number rectangle
            g.setColor(Color.GRAY);
            g.fillRect(5, i * 21, indirectExperienceCap, 20);

            //-- Drawing filled number rectangle
            g.setColor(Globals.Color$.lightGray);
            g.fillRect(5, i * 21, indirectExperiences.size(), 20);

            //-- Printing number/total
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(indirectExperiences.size() + " / " + indirectExperienceCap,
                    indirectExperiences.size() + 20, i * 21 + 15);

            int obsSize = agent.getTrust().getIndirectExperiences().size();

            if (obsSize > 0) {
                //-- Drawing positive and negative reward bars
                int[] obsTarPit = agent.getTrust().getIndirectExperienceRewardsCount();
                g.setColor(Globals.Color$.lightGreen);
                g.fillRect(5, i * 21, obsTarPit[0], 20);
                g.setColor(Globals.Color$.lightRed);
                g.fillRect(5 + obsTarPit[0], i * 21, obsTarPit[1], 20);

                //-- Drawing percentage of items size: itemSize/itemCap
                List<TrustIndirectExperience> indirectExperiences = agent.getTrust().getIndirectExperiences();
                for (int j = 0, indirectExperiencesSize = indirectExperiences.size(); j < indirectExperiencesSize; j++) {
                    TrustIndirectExperience io = indirectExperiences.get(j);
                    g.setColor(io.getAbstractReward() > 0 ? Globals.Color$.green : Globals.Color$.red);
                    g.drawLine(5 + j, i * 21, 5 + j, i * 21 + io.getItems().size() / io.getItemCap());
                }
            }


        }
    }
}
