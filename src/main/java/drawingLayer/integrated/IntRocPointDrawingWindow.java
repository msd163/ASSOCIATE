package drawingLayer.integrated;

import drawingLayer.DrawingWindow;
import simulateLayer.config.trust.TrustConfig;
import simulateLayer.statistics.WorldStatistics;
import societyLayer.agentSubLayer.World;
import utils.Config;
import utils.Globals;
import utils.Point;

import java.awt.*;

public class IntRocPointDrawingWindow extends DrawingWindow {

    private TrustConfig configBunch;

    //============================//============================  panning params

    public IntRocPointDrawingWindow(World worlds[], TrustConfig configBunch) {
        super(worlds.length);
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[7];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated ROC [#Worlds: " + worlds.length + "]";
        setName("i_roc_stt");

        //axisYScale = 1;
        _vs = 60;
        _hs = 8;
    }

    @Override
    public void resetParams() {
        super.resetParams();

        //axisYScale = 1;
        _vs = 60;
        _hs = 8;
    }


    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        printStatsInfo(1, "# ROC (TPR)", worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustTpRate100I(), Globals.Color$.yellow);
        printStatsInfo(2, "# ROC (TNR)", worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustTnRate100I(), Globals.Color$.darkGreen);
        printStatsInfo(3, "# ROC (FPR)", worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustFpRate100I(), Globals.Color$.red);
        printStatsInfo(4, "# ROC (FNR)", worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustFnRate100I(), Globals.Color$.orange);


        //============================//============================ INFO
        dynamicHeight += 20;
        if (isShowSimInfo) {
            for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {

                World world = worlds[j];

                if (j > Globals.SIMULATION_TIMER || world == null) {
                    break;
                }

                //============================
                dynamicHeight += 40;
                g.setColor(Globals.Color$.$simTitle);
                g.drawString("Sim " + (j + 1) + " |", 80, dynamicHeight);
                //============================

                if (showWorldsFlag[j]) {
                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(200, dynamicHeight, Globals.Color$.color(0), j, 20, -1);
                        g.drawString("DATA", 220, dynamicHeight);
                        //============================
                    }
                }
                //============================
                g.setColor(Globals.Color$.$configTitle);
                g.drawString("|>  " + worlds[j].getSimulationConfigInfo(), 500, dynamicHeight);
                //============================
            }
        }
        //============================//============================//============================ Diagram drawing

        reverseNormalizeCoordination();

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        if (showChartsFlag[0]) {
            prepareChartPosition(0);

            drawDiameter(0);


            for (int worldIdx = 0, worldsLength = worlds.length; worldIdx < worldsLength; worldIdx++) {
                if (!showWorldsFlag[worldIdx]) {
                    continue;
                }

                World world = worlds[worldIdx];

                if (worldIdx > Globals.SIMULATION_TIMER || world == null) {
                    break;
                }

                loAxisX = worldIdx;

                worldTimer = worldIdx < Globals.SIMULATION_TIMER ? Config.WORLD_LIFE_TIME : Globals.WORLD_TIMER;

                if (worldTimer < 3) {
                    continue;
                }

                WorldStatistics[] statistics = world.getWdStatistics();

                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getAllTrustAccuracyI100());

                if (showLineChartsFlag[0]) {

                    if (worldTimer > Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_ROC) {

                        float fpr = 0;
                        float tpr = 0;

                        for (int i = worldTimer - 1; i > worldTimer - Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_ROC; i--) {
                            fpr += statistics[i].getTrustFnRate();
                            tpr += statistics[i].getTrustTnRate();
                        }
                        fpr /= (0.01 * Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_ROC);
                        tpr /= (0.01 * Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_ROC);

                        drawSymbolOnCurve((int) fpr, (int) tpr, Globals.Color$.color(worldIdx), worldIdx, -11);
                        /*if (prevPoints[0].y >= 0) {
                            drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, acc);
                        }*/
                    }
                }
                loAxisX += _hs;

                if (axisX < loAxisX) {
                    axisX = loAxisX;
                }

            }
        }


/*        if (showChartsFlag[1]) {
            g.translate(0, (int) (0.1 * _vs * (-maxAxisY[1] - maxAxisY[0]) - 50));
            loAxisX = 0;

            drawAxisX(0);
            drawAxisY(0);
            drawDiameter();


            for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
                if (!showWorldsFlag[j]) {
                    continue;
                }

                World world = worlds[j];

                if (j > Globals.SIMULATION_TIMER || world == null) {
                    break;
                }

                loAxisX = j;

                worldTimer = j < Globals.SIMULATION_TIMER ? Config.WORLD_LIFE_TIME : Globals.WORLD_TIMER;

                WorldStatistics[] statistics = world.getWdStatistics();


                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getAllTrustAccuracyI100());


                prevPoints[1].y = (int) (0.1 * _vs * statistics[1].getTrustTpRate100I());
                prevPoints[1].x = (int) (_hs * statistics[1].getTrustTnRate100I());


                for (int i = 1, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {

                    int tp = (int) (0.1 * _vs * statistics[i].getTrustTpRate100I());
                    int fp = (int) ( _hs * statistics[i].getTrustTnRate100I());

                    if (showLineChartsFlag[1]) {
                        drawCurve(fp, tp, Globals.Color$.darkGreen, j, 5);
                        *//*if (prevPoints[1].y >= 0) {
                            drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, acc);
                        }*//*
                    }


                    prevPoints[1].y = tp;
                    prevPoints[1].x = fp;


                    loAxisX += _hs;

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }*/
    }
}
