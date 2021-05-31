package drawingLayer;

import systemLayer.World;
import utils.Globals;
import utils.Point;
import utils.WorldStatistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DiagramDrawingWindow extends JPanel implements MouseMotionListener, MouseWheelListener {

    private World world;

    private int axisX = 0;

    //============================//============================  panning params
    private utils.Point scaleOffset = new utils.Point(0, 0);
    private utils.Point pnOffset = new utils.Point(0, 0);
    private utils.Point pnOffsetOld = new utils.Point(0, 0);
    private utils.Point pnStartPoint = new utils.Point(0, 0);
    private utils.Point mousePosition = new utils.Point(0, 0);

    private float scale = 1f;


    public DiagramDrawingWindow(World world) {
        this.world = world;

        //============================//============================
        addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {
                        pnStartPoint.x = (int) (e.getPoint().getX());
                        pnStartPoint.y = (int) (e.getPoint().getY());
                        pnOffsetOld.x = pnOffset.x;
                        pnOffsetOld.y = pnOffset.y;

                        // For resetting screen by double click
                        if (e.getClickCount() == 2) {
                            pnOffset = new utils.Point(0, 0);
                            pnOffsetOld = new utils.Point(0, 0);
                            pnStartPoint = new utils.Point(0, 0);
                            pnOffsetOld.x = pnOffset.x;
                            pnOffsetOld.y = pnOffset.y;
                            scaleOffset = new Point(0, 0);
                            scale = 1f;
                        }
                    }
                });

        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    Graphics2D g;

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
        g.drawString("mouse: " + mousePosition.x + " , " + mousePosition.y, 100, 60);
        g.drawString("pn: " + pnOffset.x + " , " + pnOffset.y, 100, 100);
        g.drawString("sc: " + scale, 100, 140);
        g.drawString("scoff: " + scaleOffset.x + " , " + scaleOffset.y, 100, 180);


        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
        g.scale(scale, -scale);
        g.translate(0, -getHeight() + 100);
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
            g.setColor(Color.WHITE);
            g.fillOval(axisX, stat.getAllInTargetAgents(), 5, 5);
            //============================
            g.setColor(Color.GREEN);
            g.fillOval(axisX, stat.getInTargetAgentsInThisTime(), 5, 5);
            //============================
            g.setColor(Color.DARK_GRAY);
            g.fillOval(axisX, stat.getSuccessTravelToGoToNeighbor(), 5, 5);
            //============================
            g.setColor(Color.RED);
            g.fillOval(axisX, stat.getAgentsInPitfall(), 5, 5);
            //============================
            g.setColor(Color.yellow);
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

    //============================//============================//============================ Mouse events

    @Override
    public void mouseDragged(MouseEvent e) {
        pnOffset.x = pnOffsetOld.x + e.getPoint().x - pnStartPoint.x;
        pnOffset.y = pnOffsetOld.y + e.getPoint().y - pnStartPoint.y;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        mousePosition.x = e.getX();
        mousePosition.y = (int) ((e.getY() - pnOffset.y - scaleOffset.y) / -scale) + getHeight() - 100;
        mousePosition.x = (int) ((e.getX() - pnOffset.x - scaleOffset.x) / scale);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        float sc = scale;

        if (sc > 1) {
            sc += -0.5 * e.getWheelRotation();
        } else if (sc < 1) {
            sc += -0.05 * e.getWheelRotation();
        } else {
            if (e.getWheelRotation() < 0) {
                sc += -0.5 * e.getWheelRotation();
            } else {
                sc += -0.05 * e.getWheelRotation();
            }
        }
        if (sc > 5) {
            sc = 5;
        } else if (sc < 0.09f) {
            sc = 0.05f;
        }


        if (sc > scale) {
//            scaleOffset.y = -(int) ((mousePosition.y - pnOffset.y - scaleOffset.y) * (sc - scale));
          //  pnOffset.y = -(int) ((mousePosition.y) * (sc - scale));
            pnOffset.y -= (e.getY() * (sc - scale));
        } else if (sc < scale) {
            pnOffset.y += (int) (e.getY() * (scale - sc));
//            pnOffset.x += (e.getX() * (scale - sc));
        }
        scale = sc;
    }

}
