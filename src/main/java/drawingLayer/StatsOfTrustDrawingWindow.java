package drawingLayer;

import systemLayer.World;
import utils.Globals;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class StatsOfTrustDrawingWindow extends DrawingWindow {

    private World world;

    //============================//============================  panning params

    public StatsOfTrustDrawingWindow(World world) {
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

        g.setColor(Color.GREEN);
        g.drawString("All Trust To HONEST                  :   " + world.getWdStatistics()[worldTimer].getAllTrustToHonest(), 100, 150);
        g.setColor(Color.RED);
        g.drawString("All Trust To Adversary             :   " + world.getWdStatistics()[worldTimer].getAllTrustToAdversary(), 100, 190);
        g.setColor(Color.MAGENTA);
        g.drawString("All Trust To Int.Adversary       :   " + world.getWdStatistics()[worldTimer].getAllTrustToIntelligentAdversary(), 100, 230);
        g.setColor(Color.WHITE);
        g.drawString("Trust To Mischief                     :   " + world.getWdStatistics()[worldTimer].getAllTrustToMischief(), 100, 270);


        reverseNormalizeCoordination();

        //============================//============================//============================

        WorldStatistics[] statistics = world.getWdStatistics();

        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];
            axisX += 5;

            //============================ Bound Rectangle
            //g.drawRect(0, 0, world.getWidth(), world.getHeight());
            g.setColor(Color.WHITE);
            g.fillOval(axisX, stat.getAllTrustToMischief(), 5, 5);
            ///
            g.setColor(Color.GREEN);
            g.fillOval(axisX, stat.getAllTrustToHonest(), 5, 5);
            //============================
            g.setColor(Color.MAGENTA);
            g.fillOval(axisX, stat.getAllTrustToIntelligentAdversary(), 5, 5);
            ///
            g.setColor(Color.RED);
            g.fillOval(axisX, stat.getAllTrustToAdversary(), 5, 5);
        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

    }
}
