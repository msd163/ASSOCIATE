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
                prevPoints[0].y = (int) (stat.getTrustAccuracy() * 200);
                prevPoints[1].y = (int) (stat.getTrustSensitivity() * 200);
                prevPoints[2].y = (int) (stat.getTrustSpecificity() * 200);
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = axisX;

            } else {

                prevPoints[0].y = (int) (statistics[i - 1].getTrustAccuracy() * 200);
                prevPoints[1].y = (int) (statistics[i - 1].getTrustSensitivity() * 200);
                prevPoints[2].y = (int) (statistics[i - 1].getTrustSpecificity() * 200);
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = axisX;
                axisX += 8;
            }

            drawCurve(axisX, (int) (stat.getTrustAccuracy() * 200), Color.GREEN, 1, i);
            g.drawLine(prevPoints[0].x, prevPoints[0].y, axisX, (int) (stat.getTrustAccuracy() * 200));

            drawCurve(axisX, (int) (stat.getTrustSensitivity() * 200), Color.YELLOW, 2, i);
            g.drawLine(prevPoints[1].x, prevPoints[1].y, axisX, (int) (stat.getTrustSensitivity() * 200));

            drawCurve(axisX, (int) (stat.getTrustSpecificity() * 200), Color.PINK, 3, i);
            g.drawLine(prevPoints[2].x, prevPoints[2].y, axisX, (int) (stat.getTrustSpecificity() * 200));

        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);
    }
}
