package drawingLayer.routing;

import drawingLayer.DrawingWindow;
import systemLayer.World;
import utils.Globals;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class TravelStatsLinearDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public TravelStatsLinearDrawingWindow(World world) {
        super();
        this.world = world;
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr,"Environment Data :: "+ world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }
        axisX = 0;
        axisY = 0;
        //============================//============================ Translate for panning and scaling

        printStatsInfo(1, "Agents In Targets ITT", world.getWdStatistics()[worldTimer].getIttAgentsInTarget(), Color.WHITE);
        printStatsInfo(2, "Success Travel ITT", world.getWdStatistics()[worldTimer].getIttSuccessTravelToNeighbor(), Color.YELLOW);
        printStatsInfo(4, "Agents In Targets", world.getWdStatistics()[worldTimer].getAllAgentsInTarget(), Color.GREEN);
        printStatsInfo(5, "Agents In Pitfall", world.getWdStatistics()[worldTimer].getAllAgentsInPitfall(), Color.RED);
        printStatsInfo(6, "Random Travel", world.getWdStatistics()[worldTimer].getIttRandomTravelToNeighbors(), Color.PINK);

            reverseNormalizeCoordination();

        //============================//============================//============================

        WorldStatistics[] statistics = world.getWdStatistics();
        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];
            axisX += 5;
            //============================ Bound Rectangle
            //g.drawRect(0, 0, world.getWidth(), world.getHeight());
            g.setColor(Color.GREEN);
            g.fillOval(axisX, stat.getAllAgentsInTarget(), 5, 5);
            //============================
            g.setColor(Color.WHITE);
            g.fillOval(axisX, stat.getIttAgentsInTarget(), 5, 5);
            //============================
            g.setColor(Color.yellow);
            g.fillOval(axisX, stat.getIttSuccessTravelToNeighbor(), 5, 5);
            //============================
            g.setColor(Color.RED);
            g.fillOval(axisX, stat.getAllAgentsInPitfall(), 5, 5);
            //============================
            g.setColor(Color.PINK);
            g.fillOval(axisX, stat.getIttRandomTravelToNeighbors(), 5, 5);
            //============================
        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

    }
}
