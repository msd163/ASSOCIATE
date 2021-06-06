package drawingLayer;

import systemLayer.World;
import utils.Config;
import utils.Globals;
import utils.WorldStatistics;

import java.awt.*;

public class StatsOfEnvDrawingWindow extends DrawingWindow {

    private World world;

    private int axisX = 0;

    //============================//============================  panning params

    public StatsOfEnvDrawingWindow(World world) {
        super();
        this.world = world;
    }

    @Override
    public void paint(Graphics gr) {

        g = (Graphics2D) gr;
        g.setBackground(Color.BLACK);

        axisX = 0;

        g.clearRect(0, 0, getWidth(), getHeight());

        setBackground(Color.BLACK);
        g.setColor(Color.YELLOW);

        //============================//============================ Translate for panning and scaling

        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
      /*  g.drawString("mouse: " + mousePosition.x + " , " + mousePosition.y, 100, 60);
        g.drawString("pn: " + pnOffset.x + " , " + pnOffset.y, 100, 100);
        g.drawString("sc: " + scale, 100, 140);
        g.drawString("scoff: " + scaleOffset.x + " , " + scaleOffset.y, 100, 180);*/

        g.drawString("World Time                : " + Globals.WORLD_TIMER, 100, 50);
        g.drawString("Episode                    : " + Globals.EPISODE, 100, 90);

        if(Globals.WORLD_TIMER< Config.WORLD_LIFE_TIME) {
            g.setColor(Color.WHITE);
            g.drawString("Agents In Targets ITT :   " + world.getStatistics()[Globals.WORLD_TIMER].getIttAgentsInTarget(), 100, 160);
            g.setColor(Color.YELLOW);
            g.drawString("Success Travel ITT    :   " + world.getStatistics()[Globals.WORLD_TIMER].getIttSuccessTravelToNeighbor(), 100, 200);
            g.setColor(Color.GREEN);
            g.drawString("Agents In Targets      :   " + world.getStatistics()[Globals.WORLD_TIMER].getAllAgentsInTarget(), 100, 240);
            g.setColor(Color.RED);
            g.drawString("Agents In Pitfall     :   " + world.getStatistics()[Globals.WORLD_TIMER].getAllAgentsInPitfall(), 100, 280);
            g.setColor(Color.DARK_GRAY);
            g.drawString("Random Travel         :   " + world.getStatistics()[Globals.WORLD_TIMER].getIttRandomTravelToNeighbors(), 100, 320);
        }

        //============================//============================//============================

        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
        g.scale(scale, -scale);
        g.translate(100, -getHeight() / scale + 100);

        //============================ Translate
      /*  // g2.translate(SHIFT_X, SHIFT_Y);
        g.scale(1.0, -1.0);
        g.translate(0, -getHeight()+100);*/

        g.drawLine(0, 0, getWidth(), 0);

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
            g.setColor(Color.DARK_GRAY);
            g.fillOval(axisX, stat.getIttRandomTravelToNeighbors(), 5, 5);
            //============================
        }

    }
}
