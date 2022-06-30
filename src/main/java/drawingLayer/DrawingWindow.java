package drawingLayer;

import _type.TtBehaviorState;
import societyLayer.agentSubLayer.Agent;
import societyLayer.agentSubLayer.World;
import trustLayer.data.TrustData;
import utils.Globals;
import utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class DrawingWindow extends JPanel implements MouseMotionListener, MouseWheelListener {

    //============================//============================  panning params
    protected utils.Point scaleOffset = new utils.Point(0, 0);
    protected utils.Point pnOffset = new utils.Point(0, 0);
    protected utils.Point pnOffsetOld = new utils.Point(0, 0);
    protected utils.Point pnStartPoint = new utils.Point(0, 0);
    protected utils.Point mousePosition = new utils.Point(0, 0);

    protected int lineThickness = 1;
    protected int axisNumberFontSize = 35;

    protected int axisX = 0;
    protected int axisY = 0;
    protected int dynamicHeight = 0;
    protected int widthOfInfo = 0;
    protected int maxAxisY[];       // For holding maximum Y value of each chart

    protected int _hs = 8; // For adding space to charts horizontally
    protected int _vs = 10; // For adding space to charts vertically

    protected float scale = 1f;

    protected World world;
    protected World worlds[];

    protected int worldTimer;
    protected int simulationTimer;
    protected boolean showWorldsFlag[];
    protected boolean showLineChartsFlag[];
    protected boolean showChartsFlag[];

    protected utils.Point prevPoints[];      //-- Previously visited point

    protected int translateInTitle_Y = 0;
    private int translateInNormCoord_X = 200;
    protected int translateInNormCoord_Y = 300;
    private int translateInRevCoord_X = 100;
    private float translateInRevCoord_Y = 100;

    private boolean showMousePlus = true;
    private boolean isShowDrawingTitle = true;
    private boolean isShowStatsInfo = true;
    protected boolean isShowSimInfo = true;

    protected double axisYScale = 1.0;

    public int getWorldId() {
        return world == null ? -1 : world.getId();
    }

    protected String headerTitle = "Drawing Windows";

    public String getHeaderTitle() {
        if (world != null) {
            return "W " + world.getId() + " | " + headerTitle + " [ " + world.getSimulationConfig().getTtMethod() + "] ";
        }
        return headerTitle;
    }

    public DrawingWindow() {
        this(1);
    }

    public DrawingWindow(int worldCount) {

        showLineChartsFlag = new boolean[9];
        Arrays.fill(showLineChartsFlag, true);

        showChartsFlag = new boolean[9];
        Arrays.fill(showChartsFlag, true);

        showWorldsFlag = new boolean[worldCount];
        Arrays.fill(showWorldsFlag, true);

        maxAxisY = new int[worldCount + 1];
        Arrays.fill(maxAxisY, 0);

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

        setEnabled(true);
        setRequestFocusEnabled(true);
        setFocusable(true);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (e.isShiftDown()) {
                    //System.out.println(keyCode);
                    switch (keyCode) {
                        case 37:        // left
                            if (_hs > 1) {
                                _hs--;
                            }
                            break;
                        case 38:        // top
                            _vs += 1;
                            break;
                        case 39:        // right
                            _hs++;
                            break;
                        case 40:        // bottom
                            if (_vs > 1) {
                                _vs -= 1;
                            }
                            break;
                        case 90:        // z
                            _hs = 8;
                            _vs = 10;
                            break;

                        //  Changing Line Thickness of chart lines
                        case 33:        // page_up
                            lineThickness++;
                            lineThicknessObj_x = new BasicStroke(lineThickness);
                            break;

                        case 34:        // page_down
                            if (lineThickness > 1) {
                                lineThickness--;
                                lineThicknessObj_x = new BasicStroke(lineThickness);
                            }
                            break;


                        //  Changing font size of axis X and axis Y
                        case 36:        // HOME
                            axisNumberFontSize++;
                            break;

                        case 35:        // END
                            if (axisNumberFontSize > 1) {
                                axisNumberFontSize--;
                            }
                            break;
                    }
                    //-- for showing or hiding Simulation charts
                    if (keyCode == 48) {
                        Arrays.fill(showWorldsFlag, true);
                    } else if (keyCode >= 49 && keyCode <= 57) {
                        int index = keyCode - 49;
                        if (index < showWorldsFlag.length) {
                            showWorldsFlag[index] = !showWorldsFlag[index];
                        }
                    }
                }
                //-- For showing or hiding line charts in each chart
                if (e.isAltDown()) {
                    if (keyCode == 48) {
                        Arrays.fill(showLineChartsFlag, true);
                    } else if (keyCode >= 49 && keyCode <= 57) {
                        int index = keyCode - 49;
                        if (index < showLineChartsFlag.length) {
                            showLineChartsFlag[index] = !showLineChartsFlag[index];
                        }
                    }
                }
                //-- For showing or hiding line charts in each chart
                if (e.isControlDown()) {
                    if (keyCode == 48) {
                        Arrays.fill(showChartsFlag, true);
                    } else if (keyCode >= 49 && keyCode <= 57) {
                        int index = keyCode - 49;
                        if (index < showChartsFlag.length) {
                            showChartsFlag[index] = !showChartsFlag[index];
                        }
                    } else {
                        if (keyCode == (int) 'q' || keyCode == (int) 'Q') {
                            isShowDrawingTitle = !isShowDrawingTitle;
                        } else if (keyCode == (int) 'w' || keyCode == (int) 'W') {
                            isShowStatsInfo = !isShowStatsInfo;
                        } else if (keyCode == (int) 'e' || keyCode == (int) 'E') {
                            isShowSimInfo = !isShowSimInfo;
                        }
                    }
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }

    protected Graphics2D g;

    public void printDrawingTitle(String title, String subTitle) {

        if (!isShowDrawingTitle) {
            return;
        }

        java.awt.Point mousePoint = getMousePosition();
        if (showMousePlus && mousePoint != null) {
            g.setColor(Globals.Color$.$mousePlus);
            //-- (TOP-DOWN) Drawing vertical line for mouse pointer
            g.drawLine(mousePoint.x, 0, mousePoint.x, getHeight());
            //-- (LEFT-RIGHT) Drawing horizontal line for mouse pointer
            g.drawLine(0, mousePoint.y, getWidth(), mousePoint.y);
        }

        g.setColor(Globals.Color$.$drawingTitle);
        g.setFont(new Font("TimesRoman", Font.BOLD, 30));
        g.drawString(title, 100, 50);
        if (subTitle != null) {
            g.setColor(Color.ORANGE);
            g.setFont(new Font("TimesRoman", Font.BOLD, 25));
            g.drawString(subTitle, 100, 90);
            g.drawLine(50, 110, 2000, 110);
            // g.translate(0, 120);
            translateInTitle_Y = 120;
            g.translate(0, translateInTitle_Y);
        } else {
            g.drawLine(50, 70, 2000, 70);
            translateInTitle_Y = 80;
            g.translate(0, translateInTitle_Y);
        }
    }

    public void printStatsInfo(int index, String title, Color color) {

        if (!isShowStatsInfo) {
            return;
        }

        g.setColor(color);
        g.drawString(title, 100, index * 50);
        dynamicHeight = Math.max(index * 50, dynamicHeight);
    }

    public void printStatsInfo(int index, String title, int value, Color color) {
        if (!isShowStatsInfo) {
            return;
        }
        g.setColor(color);
        g.drawString(title, 100, index * 50);
        g.drawString(": " + value, 600, index * 50);
        dynamicHeight = Math.max(index * 50, dynamicHeight);
    }

    public void printStatsInfo(int index, String title, float value, Color color) {
        if (!isShowStatsInfo) {
            return;
        }
        g.setColor(color);
        g.drawString(title, 100, index * 50);
        g.drawString(": " + value, 600, index * 50);
        dynamicHeight = Math.max(index * 50, dynamicHeight);
    }

    public void printStatsInfo(int index, String title, int value1, String value2, Color color) {
        if (!isShowStatsInfo) {
            return;
        }
        g.setColor(color);
        g.drawString(title, 100, index * 50);
        g.drawString(": " + value1, 600, index * 50);
        g.drawString(": " + value2, 800, index * 50);
        dynamicHeight = Math.max(index * 50, dynamicHeight);
    }

    public void drawInfoSplitterLine() {
        g.drawLine(0, dynamicHeight, 2000, dynamicHeight);
    }

    public void drawBottomSlitterLine() {
        g.setColor(Color.red);
        g.drawLine(0, -50, 2100, -50);
    }

    public boolean mainPaint(Graphics gr) {
        return mainPaint(gr, getName(), null);
    }

    public boolean mainPaint(Graphics gr, String title, String subTitle) {

        dynamicHeight = 0;

        worldTimer = Globals.WORLD_TIMER - 1;
        simulationTimer = Globals.SIMULATION_TIMER;

        if (worldTimer < 0) {
            return false;
        }

        g = (Graphics2D) gr;
        g.setBackground(Globals.Color$.$background);
        g.clearRect(0, 0, getWidth(), getHeight());
        pauseNotice(g);

        printDrawingTitle(title, subTitle);

        g.setColor(Globals.Color$.$mainTitle);

        //============================//============================ Translate for panning and scaling
        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));

        if (isShowDrawingTitle) {

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

            g.setColor(Globals.Color$.$subTitle);

            g.drawString("Vertical   : X " + _vs, 1500, 50);
            g.drawString("Horizontal: X " + _hs, 1500, 90);
            g.drawString("Zoom: X " + scale, 1500, 130);
            g.drawString("LineThick: " + lineThickness, 1500, 170);
        }
        return true;
    }

    protected void drawAxisX(int index) {
        int realWith = getRealWith();
        g.setColor(Globals.Color$.$axis);
        g.drawLine(0, 0, realWith, 0);
        g.setFont(new Font("TimesRoman", Font.PLAIN, axisNumberFontSize));
        for (int i = 0, x = 0; i < realWith; i += _hs, x++) {
            g.setColor(Globals.Color$.$axis);
            if (i > 0) {
                if (i % (10 * _hs) == 0) {
                    if (_hs > 10 || (_hs > 5 && i % (20 * _hs) == 0) || (_hs > 1 && i % (40 * _hs) == 0) || i % (80 * _hs) == 0) {
                        g.scale(1, -1);
                        g.drawString(x + "", i - (axisNumberFontSize / 2), axisNumberFontSize);
                        g.scale(1, -1);
                    }
                    g.drawLine(i, -5, i, 5);
                    g.setColor(Globals.Color$.$axisSplit);
                    g.drawLine(i, 5, i, (int) (maxAxisY[index] * 0.1 * _vs));
                } else if (i % (5 * _hs) == 0) {
                    g.drawLine(i, -3, i, 1);
                    g.setColor(Globals.Color$.$axisSplit2);
                    g.drawLine(i, 5, i, (int) (maxAxisY[index] * 0.1 * _vs));
                } else if (_hs > 1) {
                    g.drawLine(i, 0, i, 1);
                }
            }
        }
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat();

    protected void drawAxisY(int index) {
        int realWith = (int) ((maxAxisY[index] + 10) * 0.1 * _vs);
        g.setColor(Globals.Color$.$axis);
        g.drawLine(0, 0, 0, realWith);
        g.setFont(new Font("TimesRoman", Font.PLAIN, axisNumberFontSize));
        for (int i = 0, x = 0,z=0; i < realWith; i += _vs, x++,z+=10) {
            g.setColor(Globals.Color$.$axis);
            if (x > 0) {
                if (i % 5 == 0) {
                    if (_vs > 60
                            || (_vs > 40 && x % 2 == 0)
                            || (_vs > 15 && _vs <= 40 && x % (5) == 0)
                            || (_vs > 5 && _vs <= 15 && x % (10) == 0)
                            || (_vs > 2 && _vs <= 5 && x % (20) == 0)
                            || (_vs > 0 && _vs <= 2 && x % (50) == 0)
                    ) {
                        g.scale(1, -1);
                        g.drawString((z * axisYScale) > 1 ? ((int) (z * axisYScale) + "") : (decimalFormat.format(z * axisYScale)),
                                (z * axisYScale >= 10000 ? -3 * axisNumberFontSize : z * axisYScale >= 1000 ? (int) (-2.5 * axisNumberFontSize) : -2 * axisNumberFontSize), -i + (axisNumberFontSize / 7));
                        g.scale(1, -1);
                        g.drawLine(-8, i, 5, i);
                        g.setColor(Globals.Color$.$axisSplit);
                        g.drawLine(5, i, getRealWith(), i);
                    } else {
                        g.drawLine(-4, i, 5, i);
                    }
                } else if (_vs > 1 && i % (5 * _vs) == 0) {
                    g.drawLine(-3, i, 1, i);
                    g.setColor(Globals.Color$.$axisSplit2);
                    g.drawLine(5, i, getRealWith(), i);
                } else if (_vs > 3) {
                    g.drawLine(0, i, 1, i);
                }
            }
        }
    }
    //============================//============================//============================

    protected void drawBar(Agent agent, TtBehaviorState behaviorState, int i, int dataCap, int dataItemCap, int[] rewardCountArray, List<?> data) {

        int dataSize = data.size();

        //-- Drawing agent cap power rectangle
        g.setColor(Globals.Color$.getNormal(behaviorState));
        g.fillRect(-agent.getCapacity().getCapPower(), i * 21, agent.getCapacity().getCapPower(), 20);

        //-- Printing agent ID
        g.setColor(Color.BLACK);
        g.drawString(agent.getId() + "", -30, i * 21 + 15);

        //-- Drawing total number rectangle
        g.setColor(Color.GRAY);
        g.fillRect(5, i * 21, dataCap, 20);


        //-- Drawing filled number rectangle
        g.setColor(Globals.Color$.lightGray);
        g.fillRect(5, i * 21, dataSize, 20);

        //-- Printing data_number/data_cap
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(dataSize + " / " + dataCap,
                dataSize + 20, i * 21 + 15);
        //-- Printing dataItemCap
        g.drawString("I.C: " + dataItemCap,
                dataSize + 150, i * 21 + 15);

        if (dataSize > 0) {
            //-- Drawing positive and negative reward bars
            g.setColor(Globals.Color$.lightGreen);
            g.fillRect(5, i * 21, rewardCountArray[0], 20);
            g.setColor(Globals.Color$.lightRed);
            g.fillRect(5 + rewardCountArray[0], i * 21, rewardCountArray[1], 20);

            //-- Drawing percentage of items size: itemSize/itemCap
            for (int j = 0, dSize = data.size(); j < dSize; j++) {
                TrustData io = (TrustData) data.get(j);
                if (io.getItemCap() > 0) {
                    if (io.isIsUpdated()) {
                        g.setColor(io.getAbstractReward() > 0 ? Globals.Color$.darkGreen2 : Globals.Color$.darkRed);
                        io.setIsUpdated(false);
                    } else {
                        g.setColor(io.getAbstractReward() > 0 ? Globals.Color$.darkGreen : Globals.Color$.red);
                    }
                    g.drawLine(5 + j, i * 21 + 3, 5 + j, i * 21 + 3 + 16 * io.getItems().size() / io.getItemCap());
                }
            }
        }
    }

    //============================//============================
    public void normalizeCoordination() {
        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
        g.scale(scale, scale);
        g.translate(translateInNormCoord_X, translateInNormCoord_Y);
//        g.translate(200, 300);
    }

    public void reverseNormalizeCoordination() {
        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y + dynamicHeight);
        g.translate(translateInRevCoord_X, translateInRevCoord_Y);
        g.scale(scale, -scale);

        //translateInRevCoord_Y = -getHeight() / scale + 100;
        //g.translate(translateInRevCoord_X, translateInRevCoord_Y);
        //g.translate(100, -getHeight() / scale + 100);
    }

    //============================//============================

    public void drawCurve(int x, int y, Color color, int index, int xIndex) {
        drawCurve(x, y, color, index, 25, xIndex);
    }

    public void drawCurve(int x, int y, Color color, int index, int size, int xIndex) {


        g.setColor(color);

        int sizeHf = 0;
        switch (index) {
            case 0:
            case 2:
                if (xIndex != -1 && (index + xIndex) % (150 / _hs) != 0) {
                    size = (_hs);
                    size = Math.min(size, 4);
                }
                sizeHf = size / 2;
                break;
            case 1:
            case 3:
            case 4:
            case 6:
            default:
                if (xIndex != -1 && (index + xIndex) % (150 / _hs) != 0) {
                    size = (_hs);
                    size = Math.min(size, 4);
                    sizeHf = size / 2;
                    if (lineThickness > 1) {
                        sizeHf += 1;
                    }
                } else {
                    sizeHf = size / 2;
                }
                g.setStroke(lineThicknessObj_x);

                break;

        }
        switch (index) {
            case 0:
                g.fillOval(x - sizeHf, y - sizeHf, size, size);
                break;
            case 1:
                g.drawLine(x - sizeHf, y, x + sizeHf, y);
                g.drawLine(x, y - sizeHf, x, y + sizeHf);
                g.setStroke(lineThicknessObj_1);

                break;
            case 2:
                g.fillRect(x - sizeHf, y - sizeHf, size, size);
                break;
            case 3:
                g.drawLine(x - sizeHf, y - sizeHf, x + sizeHf, y + sizeHf);
                g.drawLine(x - sizeHf, y + sizeHf, x + sizeHf, y - sizeHf);
                g.setStroke(lineThicknessObj_1);
                break;
            case 4:
                g.drawRect(x - sizeHf, y - sizeHf, size, size);
                g.setStroke(lineThicknessObj_1);
                break;
            case 5:
                g.drawOval(x - sizeHf, y - sizeHf, size, size);
                break;
            case 6:

                g.drawLine(x - sizeHf, y, x + sizeHf, y);
                g.setStroke(lineThicknessObj_1);
                break;
            default:
                g.drawLine(x, y, x, y + size * 3);
                g.setStroke(lineThicknessObj_1);
        }
    }

    BasicStroke lineThicknessObj_1 = new BasicStroke(1);
    BasicStroke lineThicknessObj_x = new BasicStroke(lineThickness);

    public void drawLine(int fromX, int fromY, int toX, int toY) {
        g.setStroke(lineThicknessObj_x);
        g.drawLine(fromX, fromY, toX, (int) (0.1 * _vs * toY));
        g.setStroke(lineThicknessObj_1);

    }

    //============================//============================
    public void resetParams() {
        pnOffset = new utils.Point(0, 0);
        pnOffsetOld = new utils.Point(0, 0);
        pnStartPoint = new utils.Point(0, 0);
        pnOffsetOld.x = pnOffset.x;
        pnOffsetOld.y = pnOffset.y;
        scaleOffset = new Point(0, 0);
        _hs = 8;
        _vs = 10;
        scale = 1f;


        showLineChartsFlag = new boolean[9];
        Arrays.fill(showLineChartsFlag, true);

        showChartsFlag = new boolean[9];
        Arrays.fill(showChartsFlag, true);

        Arrays.fill(showWorldsFlag, true);

    }

    public void setShowMousePlus(boolean showMousePlus) {
        this.showMousePlus = showMousePlus;
    }

    public int getRealWith() {
        return axisX + 400;
    }

    public int getRealHeight() {
        return axisY > 0 ? axisY + 1500 : (int) g.getTransform().getTranslateY() + 200;//axisY;

    }

    //============================//============================
    protected int getAverage(int value, int count) {
        return count == 0 ? value * 2 : (2 * value) / (count);
    }

    protected void pauseNotice(Graphics2D g) {
        if (Globals.PAUSE) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 80));
            g.setColor(Globals.Color$.$pause);
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
