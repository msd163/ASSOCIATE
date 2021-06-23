package drawingLayer.trust;

import drawingLayer.DrawingWindow;
import systemLayer.World;
import utils.Globals;
import utils.Point;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class TrustStatsLinearDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public TrustStatsLinearDrawingWindow(World world) {
        super();
        this.world = world;
        this.prevPoints = new utils.Point[4];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr,"Trust To ... Info :: "+ world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }
        axisX = 0;
        axisY = 0;

        printStatsInfo(1, "All Trust To HONEST", world.getWdStatistics()[worldTimer].getAllTrustToHonest(), Color.GREEN);
        printStatsInfo(2, "All Trust To Adversary", world.getWdStatistics()[worldTimer].getAllTrustToAdversary(), Color.RED);
        printStatsInfo(3, "All Trust To Int.Adversary", world.getWdStatistics()[worldTimer].getAllTrustToHypocrite(), Color.MAGENTA);
        printStatsInfo(4, "All Trust To Mischief", world.getWdStatistics()[worldTimer].getAllTrustToMischief(), Color.WHITE);

        reverseNormalizeCoordination();

        //============================//============================//============================

        WorldStatistics[] statistics = world.getWdStatistics();

        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];

            if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                axisX += 8;
                prevPoints[0].y = stat.getAllTrustToMischief();
                prevPoints[1].y = stat.getAllTrustToHonest();
                prevPoints[2].y = stat.getAllTrustToHypocrite();
                prevPoints[3].y = stat.getAllTrustToAdversary();
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;

            } else {

                prevPoints[0].y = statistics[i - 1].getAllTrustToMischief();
                prevPoints[1].y = statistics[i - 1].getAllTrustToHonest();
                prevPoints[2].y = statistics[i - 1].getAllTrustToHypocrite();
                prevPoints[3].y = statistics[i - 1].getAllTrustToAdversary();
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x  = prevPoints[3].x = axisX;
                axisX += 8;
            }

            drawCurve(axisX, stat.getAllTrustToMischief(), Color.WHITE, 1, i);
            g.drawLine(prevPoints[0].x, prevPoints[0].y, axisX, stat.getAllTrustToMischief());

            drawCurve(axisX, stat.getAllTrustToHonest(), Color.GREEN, 2, i);
            g.drawLine(prevPoints[1].x, prevPoints[1].y, axisX, stat.getAllTrustToHonest());

            drawCurve(axisX, stat.getAllTrustToHypocrite(), Color.MAGENTA, 3, i);
            g.drawLine(prevPoints[2].x, prevPoints[2].y, axisX, stat.getAllTrustToHypocrite());

            drawCurve(axisX, stat.getAllTrustToAdversary(), Color.RED, 4, i);
            g.drawLine(prevPoints[3].x, prevPoints[3].y, axisX, stat.getAllTrustToAdversary());

        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

    }
}
