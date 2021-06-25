package drawingLayer.trust;

import drawingLayer.DrawingWindow;
import systemLayer.World;
import utils.Config;
import utils.Globals;
import utils.Point;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class TrustRecogniseLinearDrawingWindow extends DrawingWindow {

    public TrustRecogniseLinearDrawingWindow(World world) {
        super();
        this.world = world;
        this.prevPoints = new utils.Point[4];
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
        axisY = 0;

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

        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];

            if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                axisX += 8;
                prevPoints[0].y = _vs * stat.getIttFalsePositiveTrust();
                prevPoints[1].y = _vs * stat.getIttFalseNegativeTrust();
                prevPoints[2].y = _vs * stat.getIttTruePositiveTrust();
                prevPoints[3].y = _vs * stat.getIttTrueNegativeTrust();
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;

            } else {
                prevPoints[0].y = _vs * statistics[i - 1].getIttFalsePositiveTrust();
                prevPoints[1].y = _vs * statistics[i - 1].getIttFalseNegativeTrust();
                prevPoints[2].y = _vs * statistics[i - 1].getIttTruePositiveTrust();
                prevPoints[3].y = _vs * statistics[i - 1].getIttTrueNegativeTrust();
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;
                axisX += 8;
            }

            drawCurve(axisX, _vs * stat.getIttFalsePositiveTrust(), Color.PINK, 1, i);
            g.drawLine(prevPoints[0].x, prevPoints[0].y, axisX, _vs * stat.getIttFalsePositiveTrust());

            drawCurve(axisX, _vs * stat.getIttFalseNegativeTrust(), Color.RED, 2, i);
            g.drawLine(prevPoints[1].x, prevPoints[1].y, axisX, _vs * stat.getIttFalseNegativeTrust());

            drawCurve(axisX, _vs * stat.getIttTruePositiveTrust(), Color.YELLOW, 3, i);
            g.drawLine(prevPoints[2].x, prevPoints[2].y, axisX, _vs * stat.getIttTruePositiveTrust());

            drawCurve(axisX, _vs * stat.getIttTrueNegativeTrust(), Color.GREEN, 4, i);
            g.drawLine(prevPoints[3].x, prevPoints[3].y, axisX, _vs * stat.getIttTrueNegativeTrust());

        }
        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

        //============================//============================//============================ Average Chart

        g.translate(0, -600);
        axisX = 0;

        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];

            if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                axisX += 8;
                prevPoints[0].y = _vs * stat.getTimedAvgFalsePositive();
                prevPoints[1].y = _vs * stat.getTimedAvgFalseNegative();
                prevPoints[2].y = _vs * stat.getTimedAvgTruePositive();
                prevPoints[3].y = _vs * stat.getTimedAvgTrueNegative();
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;

            } else {
                prevPoints[0].y = _vs * statistics[i - 1].getTimedAvgFalsePositive();
                prevPoints[1].y = _vs * statistics[i - 1].getTimedAvgFalseNegative();
                prevPoints[2].y = _vs * statistics[i - 1].getTimedAvgTruePositive();
                prevPoints[3].y = _vs * statistics[i - 1].getTimedAvgTrueNegative();
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;
                axisX += 8;
            }

            drawCurve(axisX, _vs * stat.getTimedAvgFalsePositive(), Color.PINK, 1, i);
            g.drawLine(prevPoints[0].x, prevPoints[0].y, axisX, _vs * stat.getTimedAvgFalsePositive());

            drawCurve(axisX, _vs * stat.getTimedAvgFalseNegative(), Color.RED, 2, i);
            g.drawLine(prevPoints[1].x, prevPoints[1].y, axisX, _vs * stat.getTimedAvgFalseNegative());

            drawCurve(axisX, _vs * stat.getTimedAvgTruePositive(), Color.YELLOW, 3, i);
            g.drawLine(prevPoints[2].x, prevPoints[2].y, axisX, _vs * stat.getTimedAvgTruePositive());

            drawCurve(axisX, _vs * stat.getTimedAvgTrueNegative(), Color.GREEN, 4, i);
            g.drawLine(prevPoints[3].x, prevPoints[3].y, axisX, _vs * stat.getTimedAvgTrueNegative());

        }
        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

    }
}
