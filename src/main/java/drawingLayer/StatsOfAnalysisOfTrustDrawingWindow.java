package drawingLayer;

import systemLayer.World;
import utils.Globals;
import utils.Point;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class StatsOfAnalysisOfTrustDrawingWindow extends DrawingWindow {

    private World world;

    //============================//============================  panning params

    public StatsOfAnalysisOfTrustDrawingWindow(World world) {
        super();
        this.world = world;
        this.prevPoints = new Point[3];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
    }


    @Override
    public void paint(Graphics gr) {
        if (!mainPaint(gr, world.getDrawingTitle())) {
            return;
        }
        axisX = 0;
        //============================//============================//============================

        printStatsInfo(1, "Accuracy (#of correct/#of all))", world.getWdStatistics()[worldTimer].getTrustAccuracy(), Color.GREEN);

        printStatsInfo(2, "Sensitivity (#of TP/#of all P)", world.getWdStatistics()[worldTimer].getTrustSensitivity(), Color.YELLOW);

        printStatsInfo(3, "Specificity (#of TN/#of all N)", world.getWdStatistics()[worldTimer].getTrustSpecificity(), Color.PINK);

        printStatsInfo(4, "[ X200 Scale ])", Color.WHITE);

        //============================//============================//============================

        reverseNormalizeCoordination();

        WorldStatistics[] statistics = world.getWdStatistics();

        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];

            if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                axisX += 8;
                prevPoints[0].y = stat.getTrustAccuracyI200();
                prevPoints[1].y = stat.getTrustSensitivityI200();
                prevPoints[2].y = stat.getTrustSpecificityI200();
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = axisX;

            } else {

                prevPoints[0].y = statistics[i - 1].getTrustAccuracyI200();
                prevPoints[1].y = statistics[i - 1].getTrustSensitivityI200();
                prevPoints[2].y = statistics[i - 1].getTrustSpecificityI200();
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = axisX;
                axisX += 8;
            }

            drawCurve(axisX, stat.getTrustAccuracyI200(), Color.GREEN, 1, i);
            if (prevPoints[0].y >= 0) {
                g.drawLine(prevPoints[0].x, prevPoints[0].y, axisX, stat.getTrustAccuracyI200());
            }
            drawCurve(axisX, stat.getTrustSensitivityI200(), Color.YELLOW, 2, i);
            if (prevPoints[1].y >= 0) {
                g.drawLine(prevPoints[1].x, prevPoints[1].y, axisX, stat.getTrustSensitivityI200());
            }
            drawCurve(axisX, stat.getTrustSpecificityI200(), Color.PINK, 3, i);
            if (prevPoints[2].y >= 0) {
                g.drawLine(prevPoints[2].x, prevPoints[2].y, axisX, stat.getTrustSpecificityI200());
            }
        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);
    }
}
