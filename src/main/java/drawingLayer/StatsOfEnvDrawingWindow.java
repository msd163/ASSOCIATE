package drawingLayer;

import systemLayer.World;
import utils.Globals;
import utils.WorldStatistics;

import java.awt.*;

public class StatsOfEnvDrawingWindow extends DrawingWindow {

    private World world;


    //============================//============================  panning params

    public StatsOfEnvDrawingWindow(World world) {
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
        axisY = 0;
        //============================//============================ Translate for panning and scaling

        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
      /*  g.drawString("mouse: " + mousePosition.x + " , " + mousePosition.y, 100, 60);
        g.drawString("pn: " + pnOffset.x + " , " + pnOffset.y, 100, 100);
        g.drawString("sc: " + scale, 100, 140);
        g.drawString("scoff: " + scaleOffset.x + " , " + scaleOffset.y, 100, 180);*/

        g.drawString("World Time                  : " + worldTimer, 100, 50);
        g.drawString("Episode                       : " + Globals.EPISODE, 100, 90);

        g.setColor(Color.WHITE);
        g.drawString("Agents In Targets ITT   :   " + world.getStatistics()[worldTimer].getIttAgentsInTarget(), 100, 160);
        g.setColor(Color.YELLOW);
        g.drawString("Success Travel ITT       :   " + world.getStatistics()[worldTimer].getIttSuccessTravelToNeighbor(), 100, 200);

        g.setColor(Color.GREEN);
        int allAgentsInTarget = world.getStatistics()[worldTimer].getAllAgentsInTarget();
        g.drawString("Agents In Targets         :   " + allAgentsInTarget + "        %" + 100 * (float) allAgentsInTarget / world.getAgentsCount(), 100, 240);

        g.setColor(Color.RED);
        int allAgentsInPitfall = world.getStatistics()[worldTimer].getAllAgentsInPitfall();
        g.drawString("Agents In Pitfall            :   " + allAgentsInPitfall + "        %" + 100 * (float) allAgentsInPitfall / world.getAgentsCount(), 100, 280);

        g.setColor(Color.PINK);
        int ittRandomTravelToNeighbors = world.getStatistics()[worldTimer].getIttRandomTravelToNeighbors();
        g.drawString("Random Travel              :   " + ittRandomTravelToNeighbors + "        %" + 100 * (float) ittRandomTravelToNeighbors / world.getAgentsCount(), 100, 320);


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


        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

        WorldStatistics[] statistics = world.getStatistics();
        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];
            axisX += 5;
            //============================ Bound Rectangle
            //g.drawRect(0, 0, world.getWidth(), world.getHeight());
            g.setColor(Color.GREEN);
            g.fillOval(axisX, stat.getAllAgentsInTarget(), 5, 5);
            //============================
            g.setColor(Color.WHITE);
            g.fillOval(axisX, stat.getIttAgentsInTarget(), 5, 5);
            //============================
            g.setColor(Color.yellow);
            g.fillOval(axisX, stat.getIttSuccessTravelToNeighbor(), 5, 5);
            //============================
            g.setColor(Color.RED);
            g.fillOval(axisX, stat.getAllAgentsInPitfall(), 5, 5);
            //============================
            g.setColor(Color.PINK);
            g.fillOval(axisX, stat.getIttRandomTravelToNeighbors(), 5, 5);
            //============================
        }

    }
}
