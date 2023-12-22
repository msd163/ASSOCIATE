package drawingLayer;

import _type.TtBehaviorState;
import _type.TtDiagramThemeMode;
import societyLayer.agentSubLayer.Agent;
import societyLayer.agentSubLayer.World;
import trustLayer.data.TrustData;
import trustLayer.data.TrustDataArray;
import utils.Config;
import utils.Globals;
import utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Arrays;

public class DrawingWindow extends JPanel implements MouseMotionListener, MouseWheelListener {

    //============================//============================  panning params
    protected Point scaleOffset = new Point(0, 0);
    protected Point pnOffset = new Point(0, 0);
    protected Point pnOffsetOld = new Point(0, 0);
    protected Point pnStartPoint = new Point(0, 0);
    protected Point mousePosition = new Point(0, 0);

    protected int lineThickness = 1;
    protected int axisNumberFontSize = 35;

    protected int loAxisX;      // a temp variable for saving previous X value in drawing curves
    protected int axisX = 0;
    protected int axisY = 0;
    protected int dynamicHeight = 0;
    protected int widthOfInfo = 0;
    protected int maxAxisY[];       // For holding maximum Y value of each chart
    protected int minAxisY[];       // For holding minimum Y value of each chart

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
    int tempPrevFromX[];
    int tempPrevFromY[];

    protected Point prevPoints[];      //-- Previously visited point

    protected int translateInTitle_Y = 0;
    private int translateInNormCoord_X = 200;
    protected int translateInNormCoord_Y = 300;
    private int translateInRevCoord_X = 100;
    private float translateInRevCoord_Y = 100;

    private boolean showMousePlus = true;
    private boolean isShowDrawingTitle = true;
    private boolean isShowStatsInfo = true;
    private boolean isPaintTheChart = true;
    private boolean isAntialiasText = true;
    private boolean isAntialiasCurve = true;

    protected boolean isShowSimInfo = true;
    protected boolean isShowAgentId = true;
    protected boolean isShowBarChartCapInfo = true;

    protected double axisYScale = Config.STATISTICS_SCALE_UP_Y_AXIS_NUMBER;

    protected int verticalGridLineScaleInAxisX = 1;
    protected int symbolIncreaseSizeOnCurves = 0;

    protected int curveSmoothStep = 1;


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

        tempPrevFromX = new int[9];
        Arrays.fill(tempPrevFromX, -999);

        tempPrevFromY = new int[9];
        Arrays.fill(tempPrevFromY, -999);

        showWorldsFlag = new boolean[worldCount];
        Arrays.fill(showWorldsFlag, true);

        maxAxisY = new int[3];
        Arrays.fill(maxAxisY, 0);

        minAxisY = new int[3];
        Arrays.fill(minAxisY, 0);

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
                        case 112:        // left  F1
                            if (_vs > 25) {
                                _vs -= 25;
                                // pnOffset.y += getRealHeight(0)/2;
                            }
                            break;
                        case 113:        // top   F2
                            _vs += 25;
                            //pnOffset.y += getRealHeight(0)/2;
                            break;
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

                        //============================   Changing Line Thickness of chart lines
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


                        //============================   Changing font size of axis X and axis Y
                        case 36:        // HOME
                            axisNumberFontSize++;
                            break;

                        case 35:        // END
                            if (axisNumberFontSize > 1) {
                                axisNumberFontSize--;
                            }
                            break;

                        //============================  Symbol Increment On The Curve
                        case 127:        // DELETE keyCode
                            symbolIncreaseSizeOnCurves--;

                            break;

                        case 155:        // INSERT keyCode
                            symbolIncreaseSizeOnCurves++;

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
                    } else {
                        switch (keyCode) {
                            case (int) 'H':
                                Globals.HIDE_ALL_DRAWING = true;
                                break;
                            case (int) 'S':
                                Globals.HIDE_ALL_DRAWING = false;
                                break;

                            case 37:        // left
                                if (curveSmoothStep > 1) {
                                    curveSmoothStep--;
                                }
                                break;
                            case 39:        // right
                                curveSmoothStep++;
                                break;
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
                        switch (keyCode) {
                            case (int) 'Q':
                                isShowDrawingTitle = !isShowDrawingTitle;
                                break;
                            case (int) 'W':
                                isShowStatsInfo = !isShowStatsInfo;
                                break;
                            case (int) 'E':
                                isShowSimInfo = !isShowSimInfo;
                                break;
                            case (int) 'R':
                                isShowAgentId = !isShowAgentId;
                                break;
                            case (int) 'T':
                                isShowBarChartCapInfo = !isShowBarChartCapInfo;

                                break;
                            case (int) 'A':
                                pnOffset.y = -getRealUpHeight(0);
                                pnOffset.x = 0;

                                break;
                            case (int) 'H':
                                isPaintTheChart = false;
                                break;
                            case (int) 'S':
                                isPaintTheChart = true;
                                break;

                            // antialias
                            case (int) 'Z':
                                isAntialiasText = !isAntialiasText;
                                break;

                            case (int) 'X':
                                isAntialiasCurve = !isAntialiasCurve;
                                break;
                        }

                        //============================//============================ PAUSE SETTING
                        switch (keyCode) {
                            case 116:
                                if (Config.WORLD_SLEEP_MILLISECOND > 200) {
                                    Config.WORLD_SLEEP_MILLISECOND -= 200;
                                } else {
                                    Config.WORLD_SLEEP_MILLISECOND = 0;
                                }
                                break;
                            case 117:
                                Config.WORLD_SLEEP_MILLISECOND += 200;
                                break;

                            case 118:
                                if (Config.WORLD_SLEEP_MILLISECOND_IN_PAUSE > 200) {
                                    Config.WORLD_SLEEP_MILLISECOND_IN_PAUSE -= 200;
                                } else {
                                    Config.WORLD_SLEEP_MILLISECOND_IN_PAUSE = 0;
                                }
                                break;
                            case 119:
                                Config.WORLD_SLEEP_MILLISECOND_IN_PAUSE += 200;
                                break;

                            case 120:
                                if (Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING > 200) {
                                    Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING -= 200;
                                } else {
                                    Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING = 0;
                                }
                                break;
                            case 121:
                                Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING += 200;
                                break;

                            case 122:
                                if (Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING_IN_PAUSE > 200) {
                                    Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING_IN_PAUSE -= 200;
                                } else {
                                    Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING_IN_PAUSE = 0;
                                }
                                break;
                            case 123:
                                Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING_IN_PAUSE += 200;
                                break;

                        }

                        //============================//============================ X-AXIS SCALE
                        switch (keyCode) {
                            case 37:        // left
                                if (verticalGridLineScaleInAxisX > 1) {
                                    verticalGridLineScaleInAxisX--;
                                }
                                break;

                            case 39:        // right
                                verticalGridLineScaleInAxisX++;
                        }


                    }
                }
                //- Pausing and UnPausing
                if (keyCode == (int) ' ') {
                    Globals.PAUSE = !e.isControlDown();
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

        // antialiasing for curves
        if (isAntialiasCurve) {
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

        }

        if (isAntialiasText) {
            // antialiasing for text
            g.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        g.setBackground(Globals.HIDE_ALL_DRAWING ? Globals.Color$.darkGray1 : isPaintTheChart ? Globals.Color$.$background : Globals.Color$.darkGreen3);
        g.clearRect(0, 0, getWidth(), getHeight());
        pauseAndFinishNotice(g);

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
                g.drawString(": " + simulationTimer + "/" + (worlds.length - 1), 1300, 50);
            }

            g.setColor(Globals.Color$.$subTitle2);

            g.drawString("World Time", 1100, 90);
            g.drawString(": " + worldTimer, 1300, 90);
            g.drawString("Episode", 1100, 130);
            g.drawString(": " + Globals.EPISODE, 1300, 130);
            g.drawString("Average", 1100, 170);
            g.drawString(": " + Config.STATISTICS_AVERAGE_TIME_WINDOW, 1300, 170);

            g.setColor(Globals.Color$.$subTitle);

            g.drawString("Vertical   : X " + _vs, 1500, 50);
            g.drawString("Horizontal: X " + _hs, 1500, 90);
            g.drawString("Zoom: X " + scale, 1500, 130);
            g.drawString("LineThick: " + lineThickness, 1500, 170);


            g.drawString("SLEEP : " + Config.WORLD_SLEEP_MILLISECOND, 1800, 50);
            g.drawString("SLEEP (PAUSE) : " + Config.WORLD_SLEEP_MILLISECOND_IN_PAUSE, 1800, 90);
            g.drawString("SLEEP DRAWING : " + Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING, 1800, 130);
            g.drawString("SLEEP DRAWING (PAUSE) : " + Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING_IN_PAUSE, 1800, 170);


        }
        return isPaintTheChart && !Globals.HIDE_ALL_DRAWING;
    }

    protected void drawAxisX(int index) {
        drawAxisX(index, true);
    }

    protected void drawAxisX(int index, boolean isDrawGridLine) {
        int realWith = getRealWith();
        int realUpHeight = getRealUpHeight(index);
        int realDownHeight = getRealDownHeight(index);

        g.setColor(Globals.Color$.$axis);
        g.drawLine(0, 0, realWith, 0);
        g.setFont(new Font("TimesRoman", Font.PLAIN, axisNumberFontSize));
        int verticalGridCount = 1;

        for (int i = 0, x = 0; i < realWith; i += _hs, x++) {
            g.setColor(Globals.Color$.$axis);
            if (i > 0) {
                if (i % (10 * _hs) == 0) {
                    if (verticalGridCount % verticalGridLineScaleInAxisX == 0) {
//                        if (_hs > 20 || (_hs > 10 && i % (10 * _hs) == 0) || (_hs > 5 && i % (20 * _hs) == 0) || (_hs > 1 && i % (40 * _hs) == 0) || i % (80 * _hs) == 0) {
                        g.scale(1, -1);
                        g.drawString(x + "", i - (axisNumberFontSize / 2), (int) (1.2 * axisNumberFontSize));
                        g.scale(1, -1);
//                        }
                        g.drawLine(i, -5, i, 5);
                        if (isDrawGridLine) {
                            g.setColor(Globals.Color$.$axisSplit);
                            g.drawLine(i, -realDownHeight, i, realUpHeight);
                        }
                    }
                    verticalGridCount++;
                } else if (i % (5 * _hs) == 0) {
                    g.drawLine(i, -3, i, 1);
                    if (isDrawGridLine) {
                        if (verticalGridCount % verticalGridLineScaleInAxisX == 0) {
                            g.setColor(Globals.Color$.$axisSplit2);
                            g.drawLine(i, -realDownHeight, i, realUpHeight);
                        }
                    }
                    verticalGridCount++;
                } else if (_hs > 1) {
                    g.drawLine(i, 0, i, 1);
                }
            }
        }
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat();

    protected void drawDiameter(int index) {
        int realHeight = getRealUpHeight(index);
        int realWith = (maxAxisY[0] + 10) * _hs;
        g.drawLine(0, 0, realWith, realHeight);

    }

    protected int getRealUpHeight(int diagramIndex) {
        return maxAxisY[diagramIndex] == 0 ? 0 : (int) ((maxAxisY[diagramIndex] + 100) * 0.1 * _vs);
    }

    protected int getRealDownHeight(int diagramIndex) {
        return minAxisY[diagramIndex] == 0 ? 0 : (int) ((-minAxisY[diagramIndex] + 100) * 0.1 * _vs);
    }

    /**
     * This function is used in begin of drawing chart
     *
     * @param chartIndex indicated the index of chart in the one drawing windows
     */
    protected void prepareChartPosition(int chartIndex) {
        g.translate(0, -getRealUpHeight(chartIndex) - 100);
        loAxisX = 0;

        drawAxisX(chartIndex);
        drawAxisY(chartIndex);
    }

    /**
     * This function is used in begin of drawing chart
     *
     * @param chartIndex indicated the index of chart in the one drawing windows
     */
    protected void prepareChartPosition(int chartIndex, boolean isDrawAxisX, boolean isDrawAxisXGrid, boolean isDrawAxisY, boolean isDrawAxisYGrid) {
        g.translate(0, -getRealUpHeight(chartIndex) - 100);
        loAxisX = 0;

        if (isDrawAxisX) {
            drawAxisX(chartIndex, isDrawAxisXGrid);
        }
        if (isDrawAxisY) {
            drawAxisY(chartIndex, isDrawAxisYGrid);
        }
    }

    private void drawAxisY(int minLength, int maxLength, int bigSplitter, int littleSplitter, int labelStep, boolean isDrawAxisYGrid) {
        g.setColor(Globals.Color$.$axis);
        g.drawLine(0, minLength, 0, maxLength);
        g.setFont(new Font("TimesRoman", Font.PLAIN, axisNumberFontSize));
        for (int i = 0, z = 0; i < maxLength; i += _vs, z += 10) {
            g.setColor(Globals.Color$.$axis);

            if (i % bigSplitter == 0) {
                g.drawLine(-8, i, 9, i);
            } else if (i % littleSplitter == 0) {
                g.drawLine(-5, i, 5, i);
            }
            if (i % labelStep == 0) {
                g.scale(1, -1);
                double yValue = z * axisYScale;

                int xPosOfString =
                        (yValue >= 10000 ? -5 * axisNumberFontSize
                                : yValue >= 1000 ? (int) (-3.1 * axisNumberFontSize)
                                : yValue >= 100 ? (int) (-2.7 * axisNumberFontSize)
                                : yValue >= 10 ? (int) (-2.0 * axisNumberFontSize)
                                : yValue >= 1 ? (int) (-1.3 * axisNumberFontSize)
                                : (int) (-1.9 * axisNumberFontSize)
                        );

                g.drawString(yValue > 1 ? ((int) (yValue) + "") : decimalFormat.format(yValue),
                        xPosOfString,
                        -i + (axisNumberFontSize / 7));
                g.scale(1, -1);
            }
        }


        for (int i = -_vs, z = 10; i > minLength; i -= _vs, z += 10) {
            g.setColor(Globals.Color$.$axis);

            if (i % bigSplitter == 0) {
                g.drawLine(-8, i, 9, i);
            } else if (i % littleSplitter == 0) {
                g.drawLine(-5, i, 5, i);
            }
            if (i % labelStep == 0) {
                g.scale(1, -1);
                double yValue = z * axisYScale;

                int xPosOfString =
                        (yValue >= 10000 ? (int) (-3.5 * axisNumberFontSize)
                                : yValue >= 1000 ? (int) (-2.9 * axisNumberFontSize)
                                : yValue >= 100 ? (int) (-2.7 * axisNumberFontSize)
                                : yValue >= 10 ? (int) (-2.2 * axisNumberFontSize)
                                : yValue >= 1 ? (int) (-1.8 * axisNumberFontSize)
                                : (int) (-2.2 * axisNumberFontSize)
                        );

                g.drawString(yValue > 1 ? ((int) (-yValue) + "") : (decimalFormat.format(-yValue)),
                        xPosOfString,
                        -i + (axisNumberFontSize / 7));
                g.scale(1, -1);
            }
        }
    }

    protected void drawAxisY(int index) {
        drawAxisY(-getRealDownHeight(index), getRealUpHeight(index), 100, 10, 50, false);
    }

    protected void drawAxisY(int index, boolean isDrawAxisYGrid) {
        drawAxisY(-getRealDownHeight(index), getRealUpHeight(index), 100, 10, 50, isDrawAxisYGrid);
    }
    //============================//============================//============================

    protected void drawBar(Agent agent, TtBehaviorState behaviorState, int i, int dataCap, int dataItemCap, int[] rewardCountArray, TrustDataArray data) {

        int dataSize = data.getFilledSize();
        int yIndex = i * (21 + _vs - 1);
        //-- Drawing agent cap power rectangle
        g.setColor(Globals.Color$.getNormal(behaviorState));
        g.fillRect(-agent.getCapacity().getCapPower(), yIndex, agent.getCapacity().getCapPower(), 20);

        //-- Printing agent ID
        if (isShowAgentId) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 7));
            g.setColor(Color.BLACK);
            g.drawString("A" + agent.getId(), -50, yIndex + 15);
        }
        //-- Drawing total number rectangle
        g.setColor(Config.THEME_MODE == TtDiagramThemeMode.Dark ? Globals.Color$.darkGray1 : Globals.Color$.lightGray1);
        g.fillRect(5, yIndex, dataCap * _hs, 20);

        //-- Drawing filled number rectangle
        g.setColor(Config.THEME_MODE == TtDiagramThemeMode.Dark ? Globals.Color$.gray : Globals.Color$.lightGray);
        g.fillRect(5, yIndex, dataSize * _hs, 20);

        //-- Printing data_number/data_cap
        if (isShowBarChartCapInfo) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 9));

            g.setColor(Config.THEME_MODE == TtDiagramThemeMode.Dark ? Globals.Color$.lightGray2 : Globals.Color$.gray);
            g.drawString(dataSize + " / " + dataCap,
                    dataCap * _hs + 20, yIndex + 15);
            //-- Printing dataItemCap
            g.drawString("I.C: " + dataItemCap,
                    dataCap * _hs + 80, yIndex + 15);
        }
        if (dataSize > 0) {
            //-- Drawing positive and negative reward bars
            g.setColor(Config.THEME_MODE == TtDiagramThemeMode.Dark ? Globals.Color$.lightGreen : Globals.Color$.darkGreen);
            g.fillRect(5, yIndex, rewardCountArray[0] * _hs, 20);
            g.setColor(Config.THEME_MODE == TtDiagramThemeMode.Dark ? Globals.Color$.lightRed1 : Globals.Color$.lightRed);
            g.fillRect(5 + rewardCountArray[0] * _hs, yIndex, rewardCountArray[1] * _hs, 20);

            //-- Drawing percentage of items size: itemSize/itemCap
            for (int j = 0, dSize = data.size(); j < dSize; j++) {
                TrustData io = data.get(j);
                if (io == null) {
                    break;
                }
                if (io.getItemCap() > 0) {
                    if (io.isIsUpdated()) {
                        g.setColor(io.getAbstractReward() > 0 ? Globals.Color$.darkGreen2 : Globals.Color$.darkRed);
                        io.setIsUpdated(false);
                    } else {
                        g.setColor(io.getAbstractReward() > 0 ? Globals.Color$.darkGreen1 : Globals.Color$.red);
                    }
                    int d = 1 + _hs;
                    g.fillRect(6 + j * _hs, yIndex + 3, d > 5 ? d - 4 : d > 3 ? d - 2 : 1, 16 * io.getFilledSize() / io.getItemCap());
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

    public void drawSymbolOnCurve(int x, int y, Color color, int index, int xIndex) {
        drawSymbolOnCurve(x, y, color, index, 25, xIndex);
    }

    public void drawSymbolOnCurve(int x, int y, Color color, int index, int size, int xIndex) {


        g.setColor(color);

        int sizeHf;
        if (xIndex == -11) {
            size = (_hs);
            size = Math.min(size, 10);
            sizeHf = size / 2;
        } else {
            if (xIndex != -1 && (5 * index + xIndex) % (300 / _hs) != 0) {
                return;
            }
            switch (index) {
                case 0:
                case 2:

                    size = Math.max(size + symbolIncreaseSizeOnCurves, 4);
                    sizeHf = size / 2;
                    break;
                case 1:
                case 3:
                case 4:
                case 6:
                default:
                    size = Math.max(size + symbolIncreaseSizeOnCurves, 4);
                    sizeHf = size / 2;

                    g.setStroke(lineThicknessObj_x);

                    break;

            }
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
                g.drawOval(x - sizeHf, y - sizeHf, size, size);
                g.setStroke(lineThicknessObj_1);
        }
    }

    BasicStroke lineThicknessObj_1 = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
    protected BasicStroke lineThicknessObj_x = new BasicStroke(lineThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    public void drawLine(int fromX, int fromY, int toX, int toY, int roundIndex, int curveIndex) {
        if (tempPrevFromX[curveIndex] == -999 || roundIndex < 2) {
            g.setStroke(lineThicknessObj_x);
            g.drawLine(fromX, fromY, toX, (int) (0.1 * _vs * toY));
            tempPrevFromX[curveIndex] = toX;
            tempPrevFromY[curveIndex] = (int) (0.1 * _vs * toY);
        } else if (roundIndex % curveSmoothStep == 0) {
            g.setStroke(lineThicknessObj_x);
            g.drawLine(tempPrevFromX[curveIndex], tempPrevFromY[curveIndex], toX, (int) (0.1 * _vs * toY));
            tempPrevFromX[curveIndex] = toX;
            tempPrevFromY[curveIndex] = (int) (0.1 * _vs * toY);
        }
        g.setStroke(lineThicknessObj_1);

    }

    public void drawDottedLine(int fromX, int fromY, int toX, int toY, int gap) {

        int diffX = toX - fromX;
        int diffY = toY - fromY;

        float gapX, gapY;
        float splitCount;

        if (diffX < diffY) {
            gapX = gap;
            splitCount = (float) diffX / (gap);
            gapY = diffY / (splitCount == 0 ? 1 : splitCount);
        } else {
            gapY = gap;
            splitCount = (float) diffY / (gap);
            gapX = diffX / (splitCount == 0 ? 1 : splitCount);
        }


        int currentX = fromX, currentY = fromY;

        for (int cnt = 0; cnt < splitCount * 2; cnt++) {
            if (currentX > toX || currentY > toY) {
                currentX = toX;
                currentY = toY;
            }

            g.drawOval(currentX, currentY, 1, 1);

            currentX += gapX;
            currentY += gapY;
        }
    }

    //============================//============================
    public void resetParams() {
        pnOffset = new Point(0, 0);
        pnOffsetOld = new Point(0, 0);
        pnStartPoint = new Point(0, 0);
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

    @Deprecated
    public int getRealUpHeight() {
        return (axisY > 0 || g.getTransform() == null) ? axisY + 1500 : (int) g.getTransform().getTranslateY() + 200;//axisY;

    }

    //============================//============================
    protected int getAverage(int value, int count) {
        return count == 0 ? value * 2 : (2 * value) / (count);
    }

    protected void pauseAndFinishNotice(Graphics2D g) {
        if (Globals.PAUSE) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 80));
            g.setColor(Globals.Color$.$pause);
            g.drawString("PAUSED ", 600, 200);
        }

        if (Globals.FINISHED) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            g.setColor(Globals.Color$.$finished);
            g.drawString("FINISHED ", 1200, 80);
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
//        mousePosition.y = (int) ((e.getY() - pnOffset.y - scaleOffset.y) / -scale) + (int) (getHeight() / scale) - 100;
//        mousePosition.x = (int) ((e.getX() - pnOffset.x - scaleOffset.x) / scale) - 100;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        float sc = scale;

        // zoom control
        if (e.isControlDown()) {

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
        } else if (e.isShiftDown()) {
            if (e.isAltDown()) {
                pnOffset.x -= e.getWheelRotation() * 200;
            } else {
                pnOffset.x -= e.getWheelRotation() * 10;
            }
        } else {
            if (e.isAltDown()) {
                pnOffset.y -= e.getWheelRotation() * 200;
            } else {
                pnOffset.y -= e.getWheelRotation() * 10;
            }
        }
    }

}
