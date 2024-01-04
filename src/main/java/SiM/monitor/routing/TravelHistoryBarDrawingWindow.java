package SiM.monitor.routing;

import SiM.monitor.DrawingWindow;
import _type.TtDiagramThemeMode;
import WSM.society.agent.Agent;
import WSM.society.agent.World;
import core.utils.Config;
import core.utils.Globals;

import java.awt.*;
import java.util.List;

public class TravelHistoryBarDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public TravelHistoryBarDrawingWindow(World world) {
        super();
        this.world = world;
        axisX = world.getEnvironment().getProMax().getMaxTravelHistoryCap();
        axisY = world.getAgentsCount() * 21;
        headerTitle = "Travel History Bar Chart";
        setName("tvl_his");
        _hs = 1;
        _vs = 1;
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle + " :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }

        normalizeCoordination();

        //============================//============================//============================

        g.setFont(new Font("TimesRoman", Font.PLAIN, 9));
        List<Agent> agents = world.getAgents();
        for (int i = 0, jj = agents.size() - 1, agentsLength = agents.size(); i < agentsLength; jj--, i++) {
            Agent agent = agents.get(jj);
            int yIndex = i * (21 + _vs - 1);

            if (agent == null || agent.getTravelHistories() == null) {
                continue;
            }

            int size = agent.getTravelHistories().size();

            g.setColor(Globals.Color$.getNormal(agent.getBehavior().getBehaviorState()));

            g.fillRect(-agent.getCapacity().getCapPower(), yIndex, agent.getCapacity().getCapPower(), 20);
            g.setColor(Color.BLACK);
            g.drawString("A" + agent.getId(), -60, yIndex + 12);
            g.setColor(Config.THEME_MODE == TtDiagramThemeMode.Dark ? Globals.Color$.darkGray1 : Globals.Color$.gray);
            g.fillRect(5, yIndex, agent.getCapacity().getTravelHistoryCap() * _hs, 20);

            g.setColor(Globals.Color$.getNormal(agent.getBehavior().getBehaviorState()));

            g.fillRect(5, yIndex, size * _hs, 20);

            //-- Printing data_number/data_cap
            if (isShowBarChartCapInfo) {
                g.setColor(Globals.Color$.lightGray1);
                g.drawString(size + " / " + agent.getCapacity().getTravelHistoryCap(),
                        agent.getCapacity().getTravelHistoryCap() * _hs+ 8, yIndex + 15);
//                g.setColor(Color.LIGHT_GRAY);
//                g.drawString("cTarget: " + (agent.getCurrentTargetStateIndex()) + "/" + agent.getTargetCounts(), size * _hs
//                        + 150, yIndex + 15);
            }
        }
    }
}
