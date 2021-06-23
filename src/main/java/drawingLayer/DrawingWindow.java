package drawingLayer;

import systemLayer.World;
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

    protected World world;
    protected int worldTimer;
    protected int simulationTimer;

    protected utils.Point prevPoints[];      //-- Previously visited point

    public int getWorldId() {
        return world == null ? -1 : world.getId();
    }

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


    public void printDrawingTitle(String title) {
        g.setColor(Color.cyan);
        g.setFont(new Font("TimesRoman", Font.BOLD, 30));
        g.drawString(title, 100, 50);
        g.drawLine(50, 70, 2000, 70);

        g.translate(0, 80);
    }

    public void printStatsInfo(int index, String title, Color color) {
        g.setColor(color);
        g.drawString(title, 100, index * 50);
    }

    public void printStatsInfo(int index, String title, int value, Color color) {
        g.setColor(color);
        g.drawString(title, 100, index * 50);
        g.drawString(": " + value, 600, index * 50);
    }

    public void printStatsInfo(int index, String title, float value, Color color) {
        g.setColor(color);
        g.drawString(title, 100, index * 50);
        g.drawString(": " + value, 600, index * 50);
    }

    public void printStatsInfo(int index, String title, int value1, String value2, Color color) {
        g.setColor(color);
        g.drawString(title, 100, index * 50);
        g.drawString(": " + value1, 600, index * 50);
        g.drawString(": " + value2, 800, index * 50);
    }

    public boolean mainPaint(Graphics gr) {
        return mainPaint(gr, getName());
    }

    public boolean mainPaint(Graphics gr, String title) {

        worldTimer = Globals.WORLD_TIMER - 1;
        simulationTimer = Globals.SIMULATION_TIMER;

        if (worldTimer < 0) {
            return false;
        }

        g = (Graphics2D) gr;
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, getWidth(), getHeight());
        pauseNotice(g);

        printDrawingTitle(title);


        g.setColor(Color.YELLOW);

        //============================//============================ Translate for panning and scaling

        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));

        if (world != null) {
            g.drawString("World Id", 1100, 50);
            g.drawString(": " + world.getId(), 1300, 50);
        } else {
            g.drawString("Simulation Time", 1100, 50);
            g.drawString(": " + simulationTimer, 1300, 50);
        }
        g.drawString("World Time", 1100, 90);
        g.drawString(": " + worldTimer, 1300, 90);
        g.drawString("Episode", 1100, 130);
        g.drawString(": " + Globals.EPISODE, 1300, 130);


        java.awt.Point mousePoint = getMousePosition();
        if (mousePoint != null) {
            g.setColor(Color.WHITE);
            //-- (TOP-DOWN) Drawing vertical line for mouse pointer
            g.drawLine(mousePoint.x, 0, mousePoint.x, getHeight());
            //-- (LEFT-RIGHT) Drawing horizontal line for mouse pointer
            g.drawLine(0, mousePoint.y, getWidth(), mousePoint.y);
        }
        return true;
    }

    //============================//============================
    public void normalizeCoordination() {
        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
        g.scale(scale, scale);
        g.translate(200, 300);
    }

    public void reverseNormalizeCoordination() {
        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
        g.scale(scale, -scale);
        g.translate(100, -getHeight() / scale + 100);
    }

    //============================//============================

    public void drawCurve(int x, int y, Color color, int index, int xIndex) {
        drawCurve(x, y, color, index, 10, xIndex);
    }

    public void drawCurve(int x, int y, Color color, int index, int size, int xIndex) {
        if (xIndex != -1 && (index + xIndex) % 10 != 0) {
            size = 4;
        }
        int sizeHf = size / 2;
        switch (index) {
            case 0:
                g.setColor(color);
                g.fillOval(x - sizeHf, y - sizeHf, size, size);
                break;
            case 1:
                g.setColor(color);
                g.drawLine(x - sizeHf, y, x + sizeHf, y);
                g.drawLine(x, y - sizeHf, x, y + sizeHf);
                break;
            case 2:
                g.setColor(color);
                g.fillRect(x - sizeHf, y - sizeHf, size, size);
                break;
            case 3:
                g.setColor(color);
                g.drawLine(x - sizeHf, y - sizeHf, x + sizeHf, y + sizeHf);
                g.drawLine(x - sizeHf, y + sizeHf, x + sizeHf, y - sizeHf);
                break;
            case 4:
                g.setColor(color);
                g.drawRect(x - sizeHf, y - sizeHf, size, size);
                break;
            case 5:
                g.setColor(color);
                g.drawOval(x - sizeHf, y - sizeHf, size, size);
                break;
            case 6:
                g.setColor(color);
                g.drawLine(x - sizeHf, y, x + sizeHf, y);
                break;
            default:
                g.setColor(color);
                g.drawLine(x, y, x, y + size * 3);
        }
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
        if (sc > 20) {
            sc = 20;
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
