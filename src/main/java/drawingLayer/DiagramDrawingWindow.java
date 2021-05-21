package drawingLayer;

import systemLayer.World;
import systemLayer.WorldHistory;
import utils.Globals;
import utils.WorldStatistics;

import java.awt.*;

public class DiagramDrawingWindow extends Canvas {

    public final static int SHIFT_X = 0;
    public final static int SHIFT_Y = 0;
    private World world;

    private int axisX = 0;

    public DiagramDrawingWindow(World world) {
        this.world = world;
    }

    Graphics2D g2;

    @Override
    public void update(Graphics g) {

        g2 = (Graphics2D) g;

        axisX = 0;

        g2.clearRect(0, 0, getWidth(), getHeight());

        setBackground(Color.BLACK);
        g2.setColor(Color.YELLOW);

        //============================ Translate
        // g2.translate(SHIFT_X, SHIFT_Y);
        g2.scale(1.0, -1.0);
        g2.translate(0, -getHeight()+100);

        g2.drawLine(0,0,getWidth(),0);

        WorldStatistics[] statistics = world.getStatistics();
        for (int i = 0, statisticsLength = statistics.length; i< Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];
            axisX += 5;
            //============================ Bound Rectangle
            //g.drawRect(0, 0, world.getWidth(), world.getHeight());
            g2.setColor(Color.WHITE);
            g2.fillOval(axisX, 2* stat.getAllInTargetAgents(), 5, 5);
            //============================
            g2.setColor(Color.GREEN);
            g2.fillOval(axisX, 2* stat.getInTargetAgentsInThisTime(), 5, 5);
            //============================
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillOval(axisX, 2* stat.getSuccessTravelToGoToNeighbor(), 5, 5);
            //============================
            g2.setColor(Color.RED);
            g2.fillOval(axisX, 2* stat.getRandomTravelToNeighbors(), 5, 5);
            //============================
/*
            g2.setColor(Color.WHITE);
            g2.translate(0, 700);
            g2.drawLine(0, 0, getWidth(), 0);

            g2.setColor(Color.GREEN);
            g2.fillOval(axisX, (int) (history.getHonestServiceRatio() * 1000), 5, 5);

            g2.setColor(Color.RED);
            g2.fillOval(axisX, (int) (history.getDishonestServiceRatio() * 1000), 5, 5);

            g2.translate(0, -700);*/
        }

    }


}
