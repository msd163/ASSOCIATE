package drawingLayer.trust;

import drawingLayer.DrawingWindow;
import societyLayer.agentSubLayer.World;
import utils.Globals;
import utils.Point;
import simulateLayer.statistics.WorldStatistics;

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
        headerTitle = "Trust Statistics Linear Chart";
        setName("tut_stt");
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle + " :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }
        axisX = 0;

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
                prevPoints[0].y = (int)(0.1 * _vs * stat.getIttTrustToMischief());
                prevPoints[1].y = (int)(0.1 * _vs * stat.getIttTrustToHonest());
                prevPoints[2].y = (int)(0.1 * _vs * stat.getIttTrustToHypocrite());
                prevPoints[3].y = (int)(0.1 * _vs * stat.getIttTrustToAdversary());
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;

            } else {
                prevPoints[0].y = (int)(0.1 * _vs * statistics[i - 1].getIttTrustToMischief());
                prevPoints[1].y = (int)(0.1 * _vs * statistics[i - 1].getIttTrustToHonest());
                prevPoints[2].y = (int)(0.1 * _vs * statistics[i - 1].getIttTrustToHypocrite());
                prevPoints[3].y = (int)(0.1 * _vs * statistics[i - 1].getIttTrustToAdversary());
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;
                axisX += 8;
            }

            drawCurve(axisX, (int)(0.1 * _vs * stat.getIttTrustToMischief()), Color.WHITE, 1, i);
            drawLine(prevPoints[0].x, prevPoints[0].y, axisX, stat.getIttTrustToMischief());

            drawCurve(axisX, (int)(0.1 * _vs * stat.getIttTrustToHonest()), Color.GREEN, 2, i);
            drawLine(prevPoints[1].x, prevPoints[1].y, axisX, stat.getIttTrustToHonest());

            drawCurve(axisX, (int)(0.1 * _vs * stat.getIttTrustToHypocrite()), Color.MAGENTA, 3, i);
            drawLine(prevPoints[2].x, prevPoints[2].y, axisX, stat.getIttTrustToHypocrite());

            drawCurve(axisX, (int)(0.1 * _vs * stat.getIttTrustToAdversary()), Color.RED, 4, i);
            drawLine(prevPoints[3].x, prevPoints[3].y, axisX, stat.getIttTrustToAdversary());

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
                prevPoints[0].y = (int)(0.1 * _vs * stat.getTimeAvgTrustToMischief());
                prevPoints[1].y = (int)(0.1 * _vs * stat.getTimeAvgTrustToHonest());
                prevPoints[2].y = (int)(0.1 * _vs * stat.getTimeAvgTrustToHypocrite());
                prevPoints[3].y = (int)(0.1 * _vs * stat.getTimeAvgTrustToAdversary());
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;

            } else {
                prevPoints[0].y = (int)(0.1 * _vs * statistics[i - 1].getTimeAvgTrustToMischief());
                prevPoints[1].y = (int)(0.1 * _vs * statistics[i - 1].getTimeAvgTrustToHonest());
                prevPoints[2].y = (int)(0.1 * _vs * statistics[i - 1].getTimeAvgTrustToHypocrite());
                prevPoints[3].y = (int)(0.1 * _vs * statistics[i - 1].getTimeAvgTrustToAdversary());
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = axisX;
                axisX += 8;
            }

            drawCurve(axisX, (int)(0.1 * _vs * stat.getTimeAvgTrustToMischief()), Color.WHITE, 1, i);
            drawLine(prevPoints[0].x, prevPoints[0].y, axisX, stat.getTimeAvgTrustToMischief());

            drawCurve(axisX, (int)(0.1 * _vs * stat.getTimeAvgTrustToHonest()), Color.GREEN, 2, i);
            drawLine(prevPoints[1].x, prevPoints[1].y, axisX, stat.getTimeAvgTrustToHonest());

            drawCurve(axisX, (int)(0.1 * _vs * stat.getTimeAvgTrustToHypocrite()), Color.MAGENTA, 3, i);
            drawLine(prevPoints[2].x, prevPoints[2].y, axisX, stat.getTimeAvgTrustToHypocrite());

            drawCurve(axisX, (int)(0.1 * _vs * stat.getTimeAvgTrustToAdversary()), Color.RED, 4, i);
            drawLine(prevPoints[3].x, prevPoints[3].y, axisX, stat.getTimeAvgTrustToAdversary());

        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.cyan);
        g.drawLine(0, 0, getRealWith(), 0);


    }
}
