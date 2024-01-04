package SiM.monitor.dag;

import _type.TtTrustMethodology;
import SiM.monitor.DrawingWindow;
import core.config.trust.TrustConfig;
import SiM.statistics.WorldStatistics;
import SiM.statistics.WorldStatisticsDagra;
import WSM.society.agent.World;
import core.utils.Config;
import core.utils.Globals;
import core.utils.Point;

import java.awt.*;

public class IntDagStatLinearDrawingWindow extends DrawingWindow {

    private TrustConfig configBunch;

    //============================//============================  panning params

    public IntDagStatLinearDrawingWindow(World worlds[], TrustConfig configBunch) {
        super(worlds.length);
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[9];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated Dagra Statistics Linear Chart [#Worlds: " + worlds.length + "]";
        setName("i_dgs_stt");
        axisYScale = 1;
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }


        printStatsInfo(1, "noContract (NC)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsDagra().getNoContractCount(), Globals.Color$.$curve_4);
        printStatsInfo(2, "expiredContract (EC)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsDagra().getExpiredContractCount(), Globals.Color$.$curve_9);
        printStatsInfo(3, "requestNew (RN)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsDagra().getRequestNewCount(), Globals.Color$.$curve_3);
        printStatsInfo(4, "requestSinging (RS)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsDagra().getRequestSingingCount(), Globals.Color$.$curve_6);
        printStatsInfo(5, "requestVerifying (RV)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsDagra().getRequestVerifyingCount(), Globals.Color$.$curve_2);
        printStatsInfo(6, "acceptNew (AN)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsDagra().getAcceptNewCount(), Globals.Color$.$curve_8);
        printStatsInfo(7, "acceptSigning (AS)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsDagra().getAcceptSigningCount(), Globals.Color$.$curve_5);
        printStatsInfo(8, "acceptVerifying (AV)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsDagra().getAcceptVerifyingCount(), Globals.Color$.$curve_7);
        printStatsInfo(9, "acceptAccept (AA)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsDagra().getAcceptAcceptCount(), Globals.Color$.$curve_1);


        //============================//============================ INFO
        dynamicHeight = 20 + dynamicHeight;
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

                if (showWorldsFlag[j]) {
                    if (showLineChartsFlag[0]) {
                        //============================
                        drawSymbolOnCurve(200, dynamicHeight, Globals.Color$.$curve_4, j, 20, -1);
                        g.drawString("NC", 220, dynamicHeight);
                    }
                    if (showLineChartsFlag[1]) {
                        //============================
                        drawSymbolOnCurve(300, dynamicHeight, Globals.Color$.$curve_9, j, 20, -1);
                        g.drawString("EC", 320, dynamicHeight);
                    }
                    if (showLineChartsFlag[2]) {
                        //============================
                        drawSymbolOnCurve(400, dynamicHeight, Globals.Color$.$curve_3, j, 20, -1);
                        g.drawString("RN", 420, dynamicHeight);
                    }
                    if (showLineChartsFlag[3]) {
                        //============================
                        drawSymbolOnCurve(500, dynamicHeight, Globals.Color$.$curve_6, j, 20, -1);
                        g.drawString("RS", 520, dynamicHeight);
                    }
                    if (showLineChartsFlag[4]) {
                        //============================
                        drawSymbolOnCurve(600, dynamicHeight, Globals.Color$.$curve_2, j, 20, -1);
                        g.drawString("RV", 620, dynamicHeight);
                    }
                    if (showLineChartsFlag[5]) {
                        //============================
                        drawSymbolOnCurve(700, dynamicHeight, Globals.Color$.$curve_8, j, 20, -1);
                        g.drawString("AN", 720, dynamicHeight);
                    }
                    if (showLineChartsFlag[6]) {
                        //============================
                        drawSymbolOnCurve(800, dynamicHeight, Globals.Color$.$curve_5, j, 20, -1);
                        g.drawString("AS", 820, dynamicHeight);
                    }
                    if (showLineChartsFlag[7]) {
                        //============================
                        drawSymbolOnCurve(900, dynamicHeight, Globals.Color$.$curve_7, j, 20, -1);
                        g.drawString("AV", 920, dynamicHeight);
                    }
                    if (showLineChartsFlag[8]) {
                        //============================
                        drawSymbolOnCurve(1000, dynamicHeight, Globals.Color$.$curve_1, j, 20, -1);
                        g.drawString("AA", 1020, dynamicHeight);
                    }
                }
                //============================
                g.setColor(Globals.Color$.$configTitle);
                g.drawString("|>  " + worlds[j].getSimulationConfigInfo(1), 1100, dynamicHeight);
                TtTrustMethodology ttMethod = worlds[j].getSimulationConfig().getTtMethod();
                if (ttMethod == TtTrustMethodology.TrustMode_ShortPath || ttMethod == TtTrustMethodology.TrustMode_RandomPath) {
                    dynamicHeight += 40;
                    g.drawString("    > " + worlds[j].getSimulationConfigInfo(2), 1100, dynamicHeight);
                    dynamicHeight += 40;
                    g.drawString("    > " + worlds[j].getSimulationConfigInfo(3), 1100, dynamicHeight);
                    dynamicHeight += 40;
                    g.drawString("    > " + worlds[j].getSimulationConfigInfo(4), 1100, dynamicHeight);
                }
                //============================
            }
        }

        //drawInfoSplitterLine();

        //============================//============================//============================ Diagram drawing

        reverseNormalizeCoordination();

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        if (showChartsFlag[0]) {
            prepareChartPosition(0);


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
                WorldStatisticsDagra statisticsDagra = statistics[worldTimer - 1].getStatisticsDagra();
                maxAxisY[0] = Math.max(maxAxisY[0], statisticsDagra.getNoContractCount());
                maxAxisY[0] = Math.max(maxAxisY[0], statisticsDagra.getExpiredContractCount());
                maxAxisY[0] = Math.max(maxAxisY[0], statisticsDagra.getRequestNewCount());
                maxAxisY[0] = Math.max(maxAxisY[0], statisticsDagra.getRequestSingingCount());
                maxAxisY[0] = Math.max(maxAxisY[0], statisticsDagra.getRequestVerifyingCount());
                maxAxisY[0] = Math.max(maxAxisY[0], statisticsDagra.getAcceptNewCount());
                maxAxisY[0] = Math.max(maxAxisY[0], statisticsDagra.getAcceptSigningCount());
                maxAxisY[0] = Math.max(maxAxisY[0], statisticsDagra.getAcceptVerifyingCount());
                maxAxisY[0] = Math.max(maxAxisY[0], statisticsDagra.getAcceptAcceptCount());

                for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {
                    WorldStatisticsDagra stat = statistics[sttIdx].getStatisticsDagra();

                    int curveIndex = -1;

                    if (sttIdx == 0) {
                        loAxisX += _hs;
                        prevPoints[0].y = (int) (0.1 * _vs * stat.getNoContractCount());
                        prevPoints[1].y = (int) (0.1 * _vs * stat.getExpiredContractCount());
                        prevPoints[2].y = (int) (0.1 * _vs * stat.getRequestNewCount());
                        prevPoints[3].y = (int) (0.1 * _vs * stat.getRequestSingingCount());
                        prevPoints[4].y = (int) (0.1 * _vs * stat.getRequestVerifyingCount());
                        prevPoints[5].y = (int) (0.1 * _vs * stat.getAcceptNewCount());
                        prevPoints[6].y = (int) (0.1 * _vs * stat.getAcceptSigningCount());
                        prevPoints[7].y = (int) (0.1 * _vs * stat.getAcceptVerifyingCount());
                        prevPoints[8].y = (int) (0.1 * _vs * stat.getAcceptAcceptCount());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x
                                = prevPoints[3].x = prevPoints[4].x = prevPoints[5].x
                                = prevPoints[6].x = prevPoints[7].x = prevPoints[8].x
                                = loAxisX;
                    } else {
                        prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getStatisticsDagra().getNoContractCount());
                        prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getStatisticsDagra().getExpiredContractCount());
                        prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getStatisticsDagra().getRequestNewCount());
                        prevPoints[3].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getStatisticsDagra().getRequestSingingCount());
                        prevPoints[4].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getStatisticsDagra().getRequestVerifyingCount());
                        prevPoints[5].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getStatisticsDagra().getAcceptNewCount());
                        prevPoints[6].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getStatisticsDagra().getAcceptSigningCount());
                        prevPoints[7].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getStatisticsDagra().getAcceptVerifyingCount());
                        prevPoints[8].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getStatisticsDagra().getAcceptAcceptCount());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x
                                = prevPoints[3].x = prevPoints[4].x = prevPoints[5].x
                                = prevPoints[6].x = prevPoints[7].x = prevPoints[8].x
                                = loAxisX;
                        loAxisX += _hs;
                    }

                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getNoContractCount()), Globals.Color$.$curve_4, j, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getNoContractCount(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getExpiredContractCount()), Globals.Color$.$curve_9, j, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getExpiredContractCount(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getRequestNewCount()), Globals.Color$.$curve_3, j, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getRequestNewCount(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[3]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getRequestSingingCount()), Globals.Color$.$curve_6, j, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getRequestSingingCount(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[4]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getRequestVerifyingCount()), Globals.Color$.$curve_2, j, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getRequestVerifyingCount(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[5]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAcceptNewCount()), Globals.Color$.$curve_8, j, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getAcceptNewCount(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[6]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAcceptSigningCount()), Globals.Color$.$curve_5, j, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getAcceptSigningCount(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[7]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAcceptVerifyingCount()), Globals.Color$.$curve_7, j, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getAcceptVerifyingCount(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[8]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAcceptAcceptCount()), Globals.Color$.$curve_1, j, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getAcceptAcceptCount(), sttIdx, ++curveIndex);
                    }

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }

            }
        }
    }
}
