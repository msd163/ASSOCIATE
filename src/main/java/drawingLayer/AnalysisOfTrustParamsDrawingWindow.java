package drawingLayer;

import systemLayer.World;
import utils.Globals;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class AnalysisOfTrustParamsDrawingWindow extends DrawingWindow {

    private World world;

    //============================//============================  panning params

    public AnalysisOfTrustParamsDrawingWindow(World world) {
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

        g.setColor(Color.YELLOW);

        axisX = 0;

        //============================//============================ Translate for panning and scaling

        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));

        g.drawString("World Time                : " + (worldTimer), 100, 50);
        g.drawString("Episode                    : " + Globals.EPISODE, 100, 90);


        g.setColor(Color.GREEN);
        g.drawString("Accuracy (#of correct/#of all))               :   " + world.getStatistics()[worldTimer].getTrustAccuracy(), 100, 150);
        g.setColor(Color.YELLOW);
        g.drawString("Sensitivity (#of TP/#of all P)                  :   " + world.getStatistics()[worldTimer].getTrustSensitivity(), 100, 190);
        g.setColor(Color.PINK);
        g.drawString("Specificity (#of TN/#of all N)                  :   " + world.getStatistics()[worldTimer].getTrustSpecificity(), 100, 230);

        g.setColor(Color.WHITE);
        g.drawString("[ X200 Scale ]" , 100, 300);


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

            g.setColor(Color.GREEN);
            g.fillOval(axisX, (int) (stat.getTrustAccuracy() * 200), 5, 5);

            g.setColor(Color.YELLOW);
            g.fillOval(axisX, (int) (stat.getTrustSensitivity() * 200), 5, 5);

            g.setColor(Color.PINK);
            g.fillOval(axisX, (int) (stat.getTrustSpecificity() * 200), 5, 5);

        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);
    }
}
