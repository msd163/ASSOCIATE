package drawingLayer;

import systemLayer.World;
import utils.Globals;
import utils.Point;
import utils.WorldStatistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DiagramDrawingWindow extends DrawingWindow {

    private World world;

    private int axisX = 0;

    //============================//============================  panning params

    public DiagramDrawingWindow(World world) {
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

        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
      /*  g.drawString("mouse: " + mousePosition.x + " , " + mousePosition.y, 100, 60);
        g.drawString("pn: " + pnOffset.x + " , " + pnOffset.y, 100, 100);
        g.drawString("sc: " + scale, 100, 140);
        g.drawString("scoff: " + scaleOffset.x + " , " + scaleOffset.y, 100, 180);*/

        g.translate(0, 200);
        g.drawString("World Time                : " + Globals.WORLD_TIMER, 100, 50);
        g.drawString("Episode                    : " + Globals.EPISODE, 100, 100);

        g.setColor(Color.WHITE);
        g.drawString("Agents In Targets ITT :   " + world.getStatistics()[Globals.WORLD_TIMER].getInTargetAgentsInThisTime(), 100, 200);
        g.setColor(Color.YELLOW);
        g.drawString("Success Travel ITT    :   " + world.getStatistics()[Globals.WORLD_TIMER].getSuccessTravelToGoToNeighbor(), 100, 250);
        g.setColor(Color.GREEN);
        g.drawString("Agents In Targets      :   " + world.getStatistics()[Globals.WORLD_TIMER].getAllInTargetAgents(), 100, 350);
        g.setColor(Color.RED);
        g.drawString("Agents In Pitfall     :   " + world.getStatistics()[Globals.WORLD_TIMER].getAgentsInPitfall(), 100, 400);
        g.setColor(Color.DARK_GRAY);
        g.drawString("Random Travel         :   " + world.getStatistics()[Globals.WORLD_TIMER].getRandomTravelToNeighbors(), 100, 450);

        g.translate(0, -200);

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
            g.fillOval(axisX, stat.getAllInTargetAgents(), 5, 5);
            //============================
            g.setColor(Color.WHITE);
            g.fillOval(axisX, stat.getInTargetAgentsInThisTime(), 5, 5);
            //============================
            g.setColor(Color.yellow);
            g.fillOval(axisX, stat.getSuccessTravelToGoToNeighbor(), 5, 5);
            //============================
            g.setColor(Color.RED);
            g.fillOval(axisX, stat.getAgentsInPitfall(), 5, 5);
            //============================
            g.setColor(Color.DARK_GRAY);
            g.fillOval(axisX, stat.getRandomTravelToNeighbors(), 5, 5);
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
