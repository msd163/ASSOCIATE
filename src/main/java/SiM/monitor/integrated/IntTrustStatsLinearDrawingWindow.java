package SiM.monitor.integrated;

import SiM.monitor.DrawingWindow;
import SiM.profiler.config.TrustConfig;
import SiM.statistics.WorldStatistics;
import WSM.society.agent.World;
import core.utils.Config;
import core.utils.Globals;
import core.utils.Point;

import java.awt.*;

public class IntTrustStatsLinearDrawingWindow extends DrawingWindow {

    private TrustConfig configBunch;

    //============================//============================  panning params

    public IntTrustStatsLinearDrawingWindow(World worlds[], TrustConfig configBunch) {
        super(worlds.length);
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[4];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated Trust Statistics Params [#Worlds: " + worlds.length + "]";
        setName("i_tut_stt");

//        axisYScale = 1;
        _vs = 100;

    }


    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        printStatsInfo(1, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Mischief", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToMischief(), Globals.Color$.$curve_4);
        printStatsInfo(2, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Honest", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToHonest(), Globals.Color$.$curve_1);
        printStatsInfo(3, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Hypocrite", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToHypocrite(), Globals.Color$.$curve_3);
        printStatsInfo(4, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Adversary", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToAdversary(), Globals.Color$.$curve_2);

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
                        drawSymbolOnCurve(200, dynamicHeight, Globals.Color$.$curve_4, j, 20, -1);
                        g.drawString("Mischief", 220, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(450, dynamicHeight, Globals.Color$.$curve_1, j, 20, -1);
                        g.drawString("Honest", 470, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(700, dynamicHeight, Globals.Color$.$curve_3, j, 20, -1);
                        g.drawString("Hypocrite", 720, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[3]) {
                        drawSymbolOnCurve(950, dynamicHeight, Globals.Color$.$curve_2, j, 20, -1);
                        g.drawString("Adversary", 970, dynamicHeight);
                    }
                }
                //============================
                g.setColor(Globals.Color$.$configTitle);
                g.drawString("|>  " + worlds[j].getSimulationConfigInfo(), 1300, dynamicHeight);
                //============================
            }
        }
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

                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getTimeAvgTrustToMischief());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getTimeAvgTrustToHonest());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getTimeAvgTrustToHypocrite());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getTimeAvgTrustToAdversary());

                for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {
                    WorldStatistics stat = statistics[sttIdx];

                    if (sttIdx == 0 || stat.getEpisode() != statistics[sttIdx - 1].getEpisode()) {
                        loAxisX += _hs;
                        prevPoints[0].y = (int) (0.1 * _vs * stat.getTimeAvgTrustToMischief());
                        prevPoints[1].y = (int) (0.1 * _vs * stat.getTimeAvgTrustToHonest());
                        prevPoints[2].y = (int) (0.1 * _vs * stat.getTimeAvgTrustToHypocrite());
                        prevPoints[3].y = (int) (0.1 * _vs * stat.getTimeAvgTrustToAdversary());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = loAxisX;

                    } else {

                        prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimeAvgTrustToMischief());
                        prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimeAvgTrustToHonest());
                        prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimeAvgTrustToHypocrite());
                        prevPoints[3].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimeAvgTrustToAdversary());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = loAxisX;
                        loAxisX += _hs;
                    }

                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTimeAvgTrustToMischief()), Globals.Color$.$curve_4, j, sttIdx);
                        if (prevPoints[0].y >= 0) {
                            drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getTimeAvgTrustToMischief(),sttIdx,0);
                        }
                    }

                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTimeAvgTrustToHonest()), Globals.Color$.$curve_1, j, sttIdx);
                        if (prevPoints[1].y >= 0) {
                            drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getTimeAvgTrustToHonest(),sttIdx,1);
                        }
                    }

                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTimeAvgTrustToHypocrite()), Globals.Color$.$curve_3, j, sttIdx);
                        if (prevPoints[2].y >= 0) {
                            drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getTimeAvgTrustToHypocrite(),sttIdx,2);
                        }
                    }

                    if (showLineChartsFlag[3]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTimeAvgTrustToAdversary()), Globals.Color$.$curve_2, j, sttIdx);
                        if (prevPoints[2].y >= 0) {
                            drawLine(prevPoints[3].x, prevPoints[3].y, loAxisX, stat.getTimeAvgTrustToAdversary(),sttIdx,3);
                        }
                    }

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }

        //============================//============================//============================

    }
}
