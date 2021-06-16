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

    private int worldTimer;

    @Override
    public void paint(Graphics gr) {

        worldTimer = Globals.WORLD_TIMER - 1;

        if (worldTimer < 0) {
            return;
        }

        g = (Graphics2D) gr;
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, getWidth(), getHeight());
        pauseNotice(g);

        axisX = 0;

        g.setColor(Color.YELLOW);

        //============================//============================ Translate for panning and scaling

        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));

        g.drawString("World Time                : " + worldTimer, 100, 50);
        g.drawString("Episode                    : " + Globals.EPISODE, 100, 90);

        g.setColor(Color.GREEN);
        g.drawString("All Trust To HONEST                  :   " + world.getStatistics()[worldTimer].getAllTrustToHonest(), 100, 150);
        g.setColor(Color.RED);
        g.drawString("All Trust To Adversary             :   " + world.getStatistics()[worldTimer].getAllTrustToAdversary(), 100, 190);
        g.setColor(Color.MAGENTA);
        g.drawString("All Trust To Int.Adversary       :   " + world.getStatistics()[worldTimer].getAllTrustToIntelligentAdversary(), 100, 230);
        g.setColor(Color.WHITE);
        g.drawString("Trust To Mischief                     :   " + world.getStatistics()[worldTimer].getAllTrustToMischief(), 100, 270);


        //============================//============================//============================
        //============================ Draw mouse plus
        Point mousePoint = getMousePosition();
        if (mousePoint != null) {
            g.setColor(Color.WHITE);
            //-- (TOP-DOWN) Drawing vertical line for mouse pointer
            g.drawLine(mousePoint.x, 0, mousePoint.x, getHeight());
            //-- (LEFT-RIGHT) Drawing horizontal line for mouse pointer
            g.drawLine(0, mousePoint.y, getWidth(), mousePoint.y);
        }

        //============================ Translate
        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
        g.scale(scale, -scale);
        g.translate(100, -getHeight() / scale + 100);


        WorldStatistics[] statistics = world.getStatistics();

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
