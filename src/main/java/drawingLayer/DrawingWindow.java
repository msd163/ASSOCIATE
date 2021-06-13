package drawingLayer;

import utils.Globals;
import utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DrawingWindow extends JPanel implements MouseMotionListener, MouseWheelListener {

    //============================//============================  panning params
    protected utils.Point scaleOffset = new utils.Point(0, 0);
    protected utils.Point pnOffset = new utils.Point(0, 0);
    protected utils.Point pnOffsetOld = new utils.Point(0, 0);
    protected utils.Point pnStartPoint = new utils.Point(0, 0);
    protected utils.Point mousePosition = new utils.Point(0, 0);

    protected int axisX = 0;
    protected int axisY = 0;

    protected float scale = 1f;

    public DrawingWindow() {
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
                            resetParams();
                        }

                        if (e.isControlDown()) {
                            Globals.PAUSE = !Globals.PAUSE;
                        }
                    }
                });

        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);

    }

    protected Graphics2D g;

    @Override
    public void paint(Graphics gr) {

        g = (Graphics2D) gr;


       /* g = (Graphics2D) gr;
        g.setBackground(Color.BLACK);

        g.clearRect(0, 0, getWidth(), getHeight());

        setBackground(Color.BLACK);
        g.setColor(Color.YELLOW);

        //============================//============================ Translate for panning and scaling

        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));

        g.drawString("Drawing Window ",100,100);


        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
        g.scale(scale, -scale);
        g.translate(0, -getHeight() + 100);
        //============================ Translate
      *//*  // g2.translate(SHIFT_X, SHIFT_Y);
        g.scale(1.0, -1.0);
        g.translate(0, -getHeight()+100);*//*

        g.drawRect(0, 0, 100, 100);*/
    }

    //============================//============================
    public void resetParams() {
        pnOffset = new utils.Point(0, 0);
        pnOffsetOld = new utils.Point(0, 0);
        pnStartPoint = new utils.Point(0, 0);
        pnOffsetOld.x = pnOffset.x;
        pnOffsetOld.y = pnOffset.y;
        scaleOffset = new Point(0, 0);
        scale = 1f;
    }

    public int getRealWith() {
        return axisX + 400;
    }

    public int getRealHeight() {
        return axisY > 0 ? axisY + 1500 : getHeight();//axisY;
    }
    //============================//============================

    protected void pauseNotice(Graphics2D g) {
        if (Globals.PAUSE) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 80));
            g.setColor(Color.GREEN);
            g.drawString("PAUSED ", 400, 100);
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
        mousePosition.y = (int) ((e.getY() - pnOffset.y - scaleOffset.y) / -scale) + (int) (getHeight() / scale) - 100;
        mousePosition.x = (int) ((e.getX() - pnOffset.x - scaleOffset.x) / scale) - 100;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        float sc = scale;

        if (sc > 1) {
            sc += -0.5 * e.getWheelRotation();
        } else if (sc < 0.1) {
            sc += -0.01 * e.getWheelRotation();
        } else if (sc < 1) {
            sc += -0.05 * e.getWheelRotation();
        } else {
            if (e.getWheelRotation() < 0) {
                sc += -0.5 * e.getWheelRotation();
            } else {
                sc += -0.05 * e.getWheelRotation();
            }
        }
        if (sc > 10) {
            sc = 10;
        } else if (sc < 0.009f) {
            sc = 0.01f;
        }


        if (sc > scale) {
            pnOffset.y -= (e.getY() * (sc - scale));
        } else if (sc < scale) {
            pnOffset.y += (int) (e.getY() * (scale - sc));
        }
        scale = sc;
    }

}
