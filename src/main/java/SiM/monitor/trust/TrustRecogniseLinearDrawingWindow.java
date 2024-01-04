package SiM.monitor.trust;

import SiM.monitor.DrawingWindow;
import WSM.society.agent.World;
import core.utils.Config;
import core.utils.Globals;
import core.utils.Point;
import SiM.statistics.WorldStatistics;

import java.awt.*;

public class TrustRecogniseLinearDrawingWindow extends DrawingWindow {

    public TrustRecogniseLinearDrawingWindow(World world) {
        super();
        this.world = world;
        this.prevPoints = new Point[4];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Trust Recognition (TP|TN|FP|FN) Statistics Linear Chart";
        setName("tut_rcg");
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle + " :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }
        axisX = 0;

        printStatsInfo(1, "False Positive", world.getWdStatistics()[worldTimer].getIttFalsePositiveTrust(), Color.PINK);
        printStatsInfo(2, "False Negative", world.getWdStatistics()[worldTimer].getIttFalseNegativeTrust(), Color.RED);
        printStatsInfo(3, "True Positive", world.getWdStatistics()[worldTimer].getIttTruePositiveTrust(), Color.YELLOW);
        printStatsInfo(4, "True Negative", world.getWdStatistics()[worldTimer].getIttTrueNegativeTrust(), Color.GREEN);

        printStatsInfo(6, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") False Positive", world.getWdStatistics()[worldTimer].getTimedAvgFalsePositive(), Color.PINK);
        printStatsInfo(7, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") False Negative", world.getWdStatistics()[worldTimer].getTimedAvgFalseNegative(), Color.RED);
        printStatsInfo(8, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") True Positive", world.getWdStatistics()[worldTimer].getTimedAvgTruePositive(), Color.YELLOW);
        printStatsInfo(9, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") True Negative", world.getWdStatistics()[worldTimer].getTimedAvgTrueNegative(), Color.GREEN);

        reverseNormalizeCoordination();
        //============================//============================//============================

        WorldStatistics[] statistics = world.getWdStatistics();

        for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < Globals.WORLD_TIMER && sttIdx < statisticsLength; sttIdx++) {
            WorldStatistics stat = statistics[sttIdx];

            if (sttIdx == 0 || stat.getEpisode() != statistics[sttIdx - 1].getEpisode()) {
                axisX += 8;
                prevPoints[0].y = (int) (0.1 * _vs * stat.getIttFalsePositiveTrust());
                prevPoints[1].y = (int) (0.1 * _vs * stat.getIttFalseNegativeTrust());
                prevPoints[2].y = (int) (0.1 * _vs * stat.getIttTruePositiveTrust());
                prevPoints[3].y = (int) (0.1 * _vs * stat.getIttTrueNegativeTrust());
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;

            } else {
                prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getIttFalsePositiveTrust());
                prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getIttFalseNegativeTrust());
                prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getIttTruePositiveTrust());
                prevPoints[3].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getIttTrueNegativeTrust());
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;
                axisX += 8;
            }

            drawSymbolOnCurve(axisX, (int) (0.1 * _vs * stat.getIttFalsePositiveTrust()), Color.PINK, 1, sttIdx);
            drawLine(prevPoints[0].x, prevPoints[0].y, axisX, stat.getIttFalsePositiveTrust(), sttIdx,0);

            drawSymbolOnCurve(axisX, (int) (0.1 * _vs * stat.getIttFalseNegativeTrust()), Color.RED, 2, sttIdx);
            drawLine(prevPoints[1].x, prevPoints[1].y, axisX, stat.getIttFalseNegativeTrust(), sttIdx,1);

            drawSymbolOnCurve(axisX, (int) (0.1 * _vs * stat.getIttTruePositiveTrust()), Color.YELLOW, 3, sttIdx);
            drawLine(prevPoints[2].x, prevPoints[2].y, axisX, stat.getIttTruePositiveTrust(), sttIdx,2);

            drawSymbolOnCurve(axisX, (int) (0.1 * _vs * stat.getIttTrueNegativeTrust()), Color.GREEN, 4, sttIdx);
            drawLine(prevPoints[3].x, prevPoints[3].y, axisX, stat.getIttTrueNegativeTrust(), sttIdx,3);

        }
        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

        //============================//============================//============================ Average Chart

        g.translate(0, -600);
        axisX = 0;

        for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < Globals.WORLD_TIMER && sttIdx < statisticsLength; sttIdx++) {
            WorldStatistics stat = statistics[sttIdx];

            if (sttIdx == 0 || stat.getEpisode() != statistics[sttIdx - 1].getEpisode()) {
                axisX += 8;
                prevPoints[0].y = (int) (0.1 * _vs * stat.getTimedAvgFalsePositive());
                prevPoints[1].y = (int) (0.1 * _vs * stat.getTimedAvgFalseNegative());
                prevPoints[2].y = (int) (0.1 * _vs * stat.getTimedAvgTruePositive());
                prevPoints[3].y = (int) (0.1 * _vs * stat.getTimedAvgTrueNegative());
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;

            } else {
                prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimedAvgFalsePositive());
                prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimedAvgFalseNegative());
                prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimedAvgTruePositive());
                prevPoints[3].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimedAvgTrueNegative());
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;
                axisX += 8;
            }

            drawSymbolOnCurve(axisX, (int) (0.1 * _vs * stat.getTimedAvgFalsePositive()), Color.PINK, 1, sttIdx);
            drawLine(prevPoints[0].x, prevPoints[0].y, axisX, stat.getTimedAvgFalsePositive(), sttIdx,4);

            drawSymbolOnCurve(axisX, (int) (0.1 * _vs * stat.getTimedAvgFalseNegative()), Color.RED, 2, sttIdx);
            drawLine(prevPoints[1].x, prevPoints[1].y, axisX, stat.getTimedAvgFalseNegative(), sttIdx,5);

            drawSymbolOnCurve(axisX, (int) (0.1 * _vs * stat.getTimedAvgTruePositive()), Color.YELLOW, 3, sttIdx);
            drawLine(prevPoints[2].x, prevPoints[2].y, axisX, stat.getTimedAvgTruePositive(), sttIdx,6);

            drawSymbolOnCurve(axisX, (int) (0.1 * _vs * stat.getTimedAvgTrueNegative()), Color.GREEN, 4, sttIdx);
            drawLine(prevPoints[3].x, prevPoints[3].y, axisX, stat.getTimedAvgTrueNegative(), sttIdx,7);

        }
        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

    }
}
