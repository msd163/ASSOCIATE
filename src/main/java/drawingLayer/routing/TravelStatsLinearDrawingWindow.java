package drawingLayer.routing;

import drawingLayer.DrawingWindow;
import systemLayer.World;
import utils.Config;
import utils.Point;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class TravelStatsLinearDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public TravelStatsLinearDrawingWindow(World world) {
        super();
        this.world = world;
        this.prevPoints = new utils.Point[6];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Travel Statistics Linear Chart";
        setName("tvl_stt");
    }

    int loAxisX = 0;

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr,headerTitle+ " :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }
        axisX = 0;
        axisY = 0;
        //============================//============================ Translate for panning and scaling

        printStatsInfo(1, "Agents In Targets ITT", world.getWdStatistics()[worldTimer].getIttAgentsInTarget(), Color.GREEN);
        printStatsInfo(2, "Agents In Pitfall ITT", world.getWdStatistics()[worldTimer].getIttAgentsInPitfall(), Color.RED);
        printStatsInfo(3, "Success Travel with Help ITT", world.getWdStatistics()[worldTimer].getIttSuccessTravelToNeighbor(), Color.WHITE);
        printStatsInfo(4, "Random Travel", world.getWdStatistics()[worldTimer].getIttRandomTravelToNeighbors(), Color.MAGENTA);
        printStatsInfo(6, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") In Targets", world.getWdStatistics()[worldTimer].getTimedAvgAgentTarget(), Color.YELLOW);
        printStatsInfo(7, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") In Pitfall", world.getWdStatistics()[worldTimer].getTimedAvgAgentInPitfall(), Color.PINK);

        reverseNormalizeCoordination();

        //============================//============================//============================

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        loAxisX = 0;
        axisY = 0;

        WorldStatistics[] statistics = world.getWdStatistics();
        for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];

            if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                loAxisX += 8;
                prevPoints[0].y = stat.getIttAgentsInTarget();
                prevPoints[1].y = stat.getIttAgentsInPitfall();
                prevPoints[4].y = stat.getIttSuccessTravelToNeighbor();
                prevPoints[5].y = stat.getIttRandomTravelToNeighbors();
                prevPoints[0].x = prevPoints[1].x = prevPoints[4].x = prevPoints[5].x = loAxisX;

            } else {

                prevPoints[0].y = statistics[i - 1].getIttAgentsInTarget();
                prevPoints[1].y = statistics[i - 1].getIttAgentsInPitfall();
                prevPoints[4].y = statistics[i - 1].getIttSuccessTravelToNeighbor();
                prevPoints[5].y = statistics[i - 1].getIttRandomTravelToNeighbors();
                prevPoints[0].x = prevPoints[1].x = prevPoints[4].x = prevPoints[5].x = loAxisX;
                loAxisX += 8;
            }

            drawCurve(loAxisX, stat.getIttAgentsInTarget(), Color.GREEN, 0, i);
            g.drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getIttAgentsInTarget());

            drawCurve(loAxisX, stat.getIttAgentsInPitfall(), Color.RED, 1, i);
            g.drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getIttAgentsInPitfall());

            drawCurve(loAxisX, stat.getIttSuccessTravelToNeighbor(), Color.WHITE, 4, i);
            g.drawLine(prevPoints[4].x, prevPoints[4].y, loAxisX, stat.getIttSuccessTravelToNeighbor());

            drawCurve(loAxisX, stat.getIttRandomTravelToNeighbors(), Color.MAGENTA, 5, i);
            g.drawLine(prevPoints[5].x, prevPoints[5].y, loAxisX, stat.getIttRandomTravelToNeighbors());

            if (axisX < loAxisX) {
                axisX = loAxisX;
            }
        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);


        g.translate(0, -600);
        loAxisX = 0;


        for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];

            if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                loAxisX += 8;
                prevPoints[2].y = stat.getTimedAvgAgentTarget();
                prevPoints[3].y = stat.getTimedAvgAgentInPitfall();
                prevPoints[2].x = prevPoints[3].x = loAxisX;

            } else {

                prevPoints[2].y = statistics[i - 1].getTimedAvgAgentTarget();
                prevPoints[3].y = statistics[i - 1].getTimedAvgAgentInPitfall();
                prevPoints[2].x = prevPoints[3].x = loAxisX;
                loAxisX += 8;
            }

            drawCurve(loAxisX, stat.getTimedAvgAgentTarget(), Color.YELLOW, 2, i);
            g.drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getTimedAvgAgentTarget());

            drawCurve(loAxisX, stat.getTimedAvgAgentInPitfall(), Color.pink, 3, i);
            g.drawLine(prevPoints[3].x, prevPoints[3].y, loAxisX, stat.getTimedAvgAgentInPitfall());

            if (axisX < loAxisX) {
                axisX = loAxisX;
            }
        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.PINK);
        g.drawLine(0, 0, getRealWith(), 0);

    }
}
