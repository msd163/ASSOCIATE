package drawingLayer;

import systemLayer.World;
import utils.Globals;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class StatsOfFalsePoNeDrawingWindow extends DrawingWindow {

    private World world;

    //============================//============================  panning params

    public StatsOfFalsePoNeDrawingWindow(World world) {
        super();
        this.world = world;
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, world.getDrawingTitle())) {
            return;
        }
        axisX = 0;
        axisY = 0;

        g.setColor(Color.PINK);
        g.drawString("False Positive            :   " + world.getWdStatistics()[worldTimer].getIttFalsePositiveTrust(), 100, 150);
        g.setColor(Color.RED);
        g.drawString("False Negative            :   " + world.getWdStatistics()[worldTimer].getIttFalseNegativeTrust(), 100, 190);
        g.setColor(Color.YELLOW);
        g.drawString("True Positive              :   " + world.getWdStatistics()[worldTimer].getIttTruePositiveTrust(), 100, 230);
        g.setColor(Color.GREEN);
        g.drawString("True Negative              :   " + world.getWdStatistics()[worldTimer].getIttTrueNegativeTrust(), 100, 270);

        reverseNormalizeCoordination();
        //============================//============================//============================

        WorldStatistics[] statistics = world.getWdStatistics();

        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];
            axisX += 5;

            //============================ Bound Rectangle
            //g.drawRect(0, 0, world.getWidth(), world.getHeight());
            g.setColor(Color.PINK);
            g.fillOval(axisX, stat.getIttFalsePositiveTrust(), 5, 5);
            ///
            g.setColor(Color.RED);
            g.fillOval(axisX, stat.getIttFalseNegativeTrust(), 5, 5);
            //
            g.setColor(Color.YELLOW);
            g.fillOval(axisX, stat.getIttTruePositiveTrust(), 5, 5);
            ///
            g.setColor(Color.GREEN);
            g.fillOval(axisX, stat.getIttTrueNegativeTrust(), 5, 5);

        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

    }
}
