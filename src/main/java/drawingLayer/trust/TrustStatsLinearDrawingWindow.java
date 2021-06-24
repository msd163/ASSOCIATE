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

    private int getAverage(int value, int count) {
        return count == 0 ? value : value / count;
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, "Trust To ... Info :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }
        axisX = 0;
        axisY = 0;

        printStatsInfo(1, "All Trust To HONEST", world.getWdStatistics()[worldTimer].getAllTrustToHonest(), Color.GREEN);
        printStatsInfo(2, "All Trust To Adversary", world.getWdStatistics()[worldTimer].getAllTrustToAdversary(), Color.RED);
        printStatsInfo(3, "All Trust To Int.Adversary", world.getWdStatistics()[worldTimer].getAllTrustToHypocrite(), Color.MAGENTA);
        printStatsInfo(4, "All Trust To Mischief", world.getWdStatistics()[worldTimer].getAllTrustToMischief(), Color.WHITE);

        printStatsInfo(6, "Trust To HONEST", world.getWdStatistics()[worldTimer].getIttTrustToHonest(), Color.GREEN);
        printStatsInfo(7, "Trust To Adversary", world.getWdStatistics()[worldTimer].getIttTrustToAdversary(), Color.RED);
        printStatsInfo(8, "Trust To Int.Adversary", world.getWdStatistics()[worldTimer].getIttTrustToHypocrite(), Color.MAGENTA);
        printStatsInfo(9, "Trust To Mischief", world.getWdStatistics()[worldTimer].getIttTrustToMischief(), Color.WHITE);

        printStatsInfo(11, "Avg Trust To HONEST", getAverage(world.getWdStatistics()[worldTimer].getAllTrustToHonest(), worldTimer), Color.GREEN);
        printStatsInfo(12, "Avg Trust To Adversary", getAverage(world.getWdStatistics()[worldTimer].getAllTrustToAdversary(), worldTimer), Color.RED);
        printStatsInfo(13, "Avg Trust To Int.Adversary", getAverage(world.getWdStatistics()[worldTimer].getAllTrustToHypocrite(), worldTimer), Color.MAGENTA);
        printStatsInfo(14, "Avg Trust To Mischief", getAverage(world.getWdStatistics()[worldTimer].getAllTrustToMischief(), worldTimer), Color.WHITE);

        reverseNormalizeCoordination();

        //============================//============================//============================

        WorldStatistics[] statistics = world.getWdStatistics();

        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];

            if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                axisX += 8;
                prevPoints[0].y = stat.getIttTrustToMischief();
                prevPoints[1].y = stat.getIttTrustToHonest();
                prevPoints[2].y = stat.getIttTrustToHypocrite();
                prevPoints[3].y = stat.getIttTrustToAdversary();
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;

            } else {
                prevPoints[0].y = statistics[i - 1].getIttTrustToMischief();
                prevPoints[1].y = statistics[i - 1].getIttTrustToHonest();
                prevPoints[2].y = statistics[i - 1].getIttTrustToHypocrite();
                prevPoints[3].y = statistics[i - 1].getIttTrustToAdversary();
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;
                axisX += 8;
            }

            drawCurve(axisX, stat.getIttTrustToMischief(), Color.WHITE, 1, i);
            g.drawLine(prevPoints[0].x, prevPoints[0].y, axisX, stat.getIttTrustToMischief());

            drawCurve(axisX, stat.getIttTrustToHonest(), Color.GREEN, 2, i);
            g.drawLine(prevPoints[1].x, prevPoints[1].y, axisX, stat.getIttTrustToHonest());

            drawCurve(axisX, stat.getIttTrustToHypocrite(), Color.MAGENTA, 3, i);
            g.drawLine(prevPoints[2].x, prevPoints[2].y, axisX, stat.getIttTrustToHypocrite());

            drawCurve(axisX, stat.getIttTrustToAdversary(), Color.RED, 4, i);
            g.drawLine(prevPoints[3].x, prevPoints[3].y, axisX, stat.getIttTrustToAdversary());

        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

        //============================//============================//============================
        g.translate(0, -600);
        axisX = 0;
        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];

            if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                axisX += 8;
                prevPoints[0].y = getAverage(stat.getAllTrustToMischief(), i);
                prevPoints[1].y = getAverage(stat.getAllTrustToHonest(), i);
                prevPoints[2].y = getAverage(stat.getAllTrustToHypocrite(), i);
                prevPoints[3].y = getAverage(stat.getAllTrustToAdversary(), i);
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;

            } else {
                prevPoints[0].y = getAverage(statistics[i - 1].getAllTrustToMischief(), i - 1);
                prevPoints[1].y = getAverage(statistics[i - 1].getAllTrustToHonest(), i - 1);
                prevPoints[2].y = getAverage(statistics[i - 1].getAllTrustToHypocrite(), i - 1);
                prevPoints[3].y = getAverage(statistics[i - 1].getAllTrustToAdversary(), i - 1);
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;
                axisX += 8;
            }

            drawCurve(axisX, getAverage(stat.getAllTrustToMischief(), i), Color.WHITE, 1, i);
            g.drawLine(prevPoints[0].x, prevPoints[0].y, axisX, getAverage(stat.getAllTrustToMischief(), i));

            drawCurve(axisX, getAverage(stat.getAllTrustToHonest(), i), Color.GREEN, 2, i);
            g.drawLine(prevPoints[1].x, prevPoints[1].y, axisX, getAverage(stat.getAllTrustToHonest(), i));

            drawCurve(axisX, getAverage(stat.getAllTrustToHypocrite(), i), Color.MAGENTA, 3, i);
            g.drawLine(prevPoints[2].x, prevPoints[2].y, axisX, getAverage(stat.getAllTrustToHypocrite(), i));

            drawCurve(axisX, getAverage(stat.getAllTrustToAdversary(), i), Color.RED, 4, i);
            g.drawLine(prevPoints[3].x, prevPoints[3].y, axisX, getAverage(stat.getAllTrustToAdversary(), i));

        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);


    }
}
