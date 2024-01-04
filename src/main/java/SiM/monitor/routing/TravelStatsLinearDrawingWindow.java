package SiM.monitor.routing;

import SiM.monitor.DrawingWindow;
import SiM.statistics.WorldStatistics;
import WSM.society.agent.World;
import core.utils.Config;
import core.utils.Point;

import java.awt.*;

public class TravelStatsLinearDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public TravelStatsLinearDrawingWindow(World world) {
        super();
        this.world = world;
        this.prevPoints = new Point[6];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Travel Statistics Linear Chart";
        setName("tvl_stt");
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle + " :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }
        axisX = 0;
        //============================//============================ Translate for panning and scaling

        printStatsInfo(1, "Agents In Targets ITT", world.getWdStatistics()[worldTimer].getIttAgentsInTarget(), Color.GREEN);
        printStatsInfo(2, "Agents In Pitfall ITT", world.getWdStatistics()[worldTimer].getIttAgentsInPitfall(), Color.RED);
        printStatsInfo(3, "Success Travel with Help ITT", world.getWdStatistics()[worldTimer].getIttSuccessTravelToNeighbor(), Color.WHITE);
        printStatsInfo(4, "Random Travel", world.getWdStatistics()[worldTimer].getIttRandomTravelToNeighbors(), Color.MAGENTA);
        printStatsInfo(6, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") In Targets", world.getWdStatistics()[worldTimer].getTimedAvgAgentTarget(), Color.YELLOW);
        printStatsInfo(7, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") In Pitfall", world.getWdStatistics()[worldTimer].getTimedAvgAgentInPitfall(), Color.PINK);

        if (showChartsFlag[0]) {
            prepareChartPosition(0);

            reverseNormalizeCoordination();

            //============================//============================//============================

            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));


            WorldStatistics[] statistics = world.getWdStatistics();
            for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {
                WorldStatistics stat = statistics[sttIdx];

                if (sttIdx == 0 || stat.getEpisode() != statistics[sttIdx - 1].getEpisode()) {
                    loAxisX += _hs;
                    prevPoints[0].y = (int) (0.1 * _vs * stat.getIttAgentsInTarget());
                    prevPoints[1].y = (int) (0.1 * _vs * stat.getIttAgentsInPitfall());
                    prevPoints[4].y = (int) (0.1 * _vs * stat.getIttSuccessTravelToNeighbor());
                    prevPoints[5].y = (int) (0.1 * _vs * stat.getIttRandomTravelToNeighbors());
                    prevPoints[0].x = prevPoints[1].x = prevPoints[4].x = prevPoints[5].x = loAxisX;

                } else {

                    prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getIttAgentsInTarget());
                    prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getIttAgentsInPitfall());
                    prevPoints[4].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getIttSuccessTravelToNeighbor());
                    prevPoints[5].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getIttRandomTravelToNeighbors());
                    prevPoints[0].x = prevPoints[1].x = prevPoints[4].x = prevPoints[5].x = loAxisX;
                    loAxisX += _hs;
                }

                drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getIttAgentsInTarget()), Color.GREEN, 0, sttIdx);
                drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getIttAgentsInTarget(), sttIdx, 0);

                drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getIttAgentsInPitfall()), Color.RED, 1, sttIdx);
                drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getIttAgentsInPitfall(), sttIdx, 1);

                drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getIttSuccessTravelToNeighbor()), Color.WHITE, 4, sttIdx);
                drawLine(prevPoints[4].x, prevPoints[4].y, loAxisX, stat.getIttSuccessTravelToNeighbor(), sttIdx, 4);

                drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getIttRandomTravelToNeighbors()), Color.MAGENTA, 5, sttIdx);
                drawLine(prevPoints[5].x, prevPoints[5].y, loAxisX, stat.getIttRandomTravelToNeighbors(), sttIdx, 5);

                if (axisX < loAxisX) {
                    axisX = loAxisX;
                }
            }

            //============================//============================ Draw X-axis line
            g.setColor(Color.YELLOW);
            g.drawLine(0, 0, getRealWith(), 0);


            g.translate(0, -600);
            loAxisX = 0;


            for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {
                WorldStatistics stat = statistics[sttIdx];

                if (sttIdx == 0 || stat.getEpisode() != statistics[sttIdx - 1].getEpisode()) {
                    loAxisX += _hs;
                    prevPoints[2].y = (int) (0.1 * _vs * stat.getTimedAvgAgentTarget());
                    prevPoints[3].y = (int) (0.1 * _vs * stat.getTimedAvgAgentInPitfall());
                    prevPoints[2].x = prevPoints[3].x = loAxisX;

                } else {

                    prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimedAvgAgentTarget());
                    prevPoints[3].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimedAvgAgentInPitfall());
                    prevPoints[2].x = prevPoints[3].x = loAxisX;
                    loAxisX += _hs;
                }

                drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTimedAvgAgentTarget()), Color.YELLOW, 2, sttIdx);
                drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getTimedAvgAgentTarget(), sttIdx, 2);

                drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTimedAvgAgentInPitfall()), Color.pink, 3, sttIdx);
                drawLine(prevPoints[3].x, prevPoints[3].y, loAxisX, stat.getTimedAvgAgentInPitfall(), sttIdx, 3);

                if (axisX < loAxisX) {
                    axisX = loAxisX;
                }
            }

            //============================//============================ Draw X-axis line
            g.setColor(Color.PINK);
            g.drawLine(0, 0, getRealWith(), 0);

        }
    }
}
