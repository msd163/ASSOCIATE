package drawingLayer;

import systemLayer.World;
import utils.Globals;
import utils.WorldStatistics;

import java.awt.*;

public class StatsOfFalsePoNeDrawingWindow extends DrawingWindow {

    private World world;

    //============================//============================  panning params

    public StatsOfFalsePoNeDrawingWindow(World world) {
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

        g.setColor(Color.PINK);
        g.drawString("False Positive            :   " + world.getStatistics()[worldTimer].getIttFalsePositiveTrust(), 100, 150);
        g.setColor(Color.RED);
        g.drawString("False Negative            :   " + world.getStatistics()[worldTimer].getIttFalseNegativeTrust(), 100, 190);
        g.setColor(Color.YELLOW);
        g.drawString("True Positive              :   " + world.getStatistics()[worldTimer].getIttTruePositiveTrust(), 100, 230);
        g.setColor(Color.GREEN);
        g.drawString("True Negative              :   " + world.getStatistics()[worldTimer].getIttTrueNegativeTrust(), 100, 270);


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
