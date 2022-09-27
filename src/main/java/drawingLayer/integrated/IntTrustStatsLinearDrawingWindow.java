package drawingLayer.integrated;

import drawingLayer.DrawingWindow;
import simulateLayer.SimulationConfig;
import simulateLayer.statistics.WorldStatistics;
import societyLayer.agentSubLayer.World;
import utils.Config;
import utils.Globals;
import utils.Point;
import simulateLayer.statistics.WorldStatistics;

import java.awt.*;

public class IntTrustStatsLinearDrawingWindow extends DrawingWindow {

    private SimulationConfig configBunch;

    //============================//============================  panning params

    public IntTrustStatsLinearDrawingWindow(World worlds[], SimulationConfig configBunch) {
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
        auc = new int[worlds.length];

    }

    int loAxisX;

    int[] auc;

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        printStatsInfo(1, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Mischief", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToMischief(), Globals.Color$.$curve_3);
        printStatsInfo(2, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Honest", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToHonest(), Globals.Color$.$curve_1);
        printStatsInfo(3, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Hypocrite", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToHypocrite(), Globals.Color$.$curve_3);
        printStatsInfo(4, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Adversary", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToAdversary(), Globals.Color$.$curve_2);
        printStatsInfo(5, "CollRate ", worlds[simulationTimer].getWdStatistics()[worldTimer - 1].getHonestCollaborationRate(), Globals.Color$.$curve_5);

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
                    if (worldTimer == Config.WORLD_LIFE_TIME - 1) {
                        auc[j] = 0;
                        for (int sttIdx = 1, statisticsLength = world.getWdStatistics().length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {
                            auc[j] += world.getWdStatistics()[sttIdx].getHonestCollaborationRate();
                        }
                    }
                    if (showLineChartsFlag[0]) {
                        drawCurve(200, dynamicHeight, Globals.Color$.$curve_4, j, 20, -1);
                        g.drawString("Mischief", 220, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[1]) {
                        drawCurve(450, dynamicHeight, Globals.Color$.$curve_1, j, 20, -1);
                        g.drawString("Honest", 470, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[2]) {
                        drawCurve(700, dynamicHeight, Globals.Color$.$curve_3, j, 20, -1);
                        g.drawString("Hypocrite", 720, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[3]) {
                        drawCurve(950, dynamicHeight, Globals.Color$.$curve_2, j, 20, -1);
                        g.drawString("Adversary", 970, dynamicHeight);
                    }
                    if (showLineChartsFlag[4]) {
                        drawCurve(1050, dynamicHeight, Globals.Color$.$curve_2, j, 20, -1);
                        g.drawString("AUC: " + auc[j], 1070, dynamicHeight);
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
            g.translate(0, (int) (0.1 * _vs * -maxAxisY[0] - 50));
            drawAxisX(0);
            drawAxisY(0);

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

                for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                    WorldStatistics stat = statistics[i];

                    if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                        loAxisX += _hs;
                        prevPoints[0].y = (int) (0.1 * _vs * stat.getTimeAvgTrustToMischief());
                        prevPoints[1].y = (int) (0.1 * _vs * stat.getTimeAvgTrustToHonest());
                        prevPoints[2].y = (int) (0.1 * _vs * stat.getTimeAvgTrustToHypocrite());
                        prevPoints[3].y = (int) (0.1 * _vs * stat.getTimeAvgTrustToAdversary());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = loAxisX;

                    } else {

                        prevPoints[0].y = (int) (0.1 * _vs * statistics[i - 1].getTimeAvgTrustToMischief());
                        prevPoints[1].y = (int) (0.1 * _vs * statistics[i - 1].getTimeAvgTrustToHonest());
                        prevPoints[2].y = (int) (0.1 * _vs * statistics[i - 1].getTimeAvgTrustToHypocrite());
                        prevPoints[3].y = (int) (0.1 * _vs * statistics[i - 1].getTimeAvgTrustToAdversary());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = loAxisX;
                        loAxisX += _hs;
                    }

                    if (showLineChartsFlag[0]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getTimeAvgTrustToMischief()), Globals.Color$.$curve_4, j, i);
                        if (prevPoints[0].y >= 0) {
                            drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getTimeAvgTrustToMischief());
                        }
                    }

                    if (showLineChartsFlag[1]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getTimeAvgTrustToHonest()), Globals.Color$.$curve_1, j, i);
                        if (prevPoints[1].y >= 0) {
                            drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getTimeAvgTrustToHonest());
                        }
                    }

                    if (showLineChartsFlag[2]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getTimeAvgTrustToHypocrite()), Globals.Color$.$curve_3, j, i);
                        if (prevPoints[2].y >= 0) {
                            drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getTimeAvgTrustToHypocrite());
                        }
                    }

                    if (showLineChartsFlag[3]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getTimeAvgTrustToAdversary()), Globals.Color$.$curve_2, j, i);
                        if (prevPoints[2].y >= 0) {
                            drawLine(prevPoints[3].x, prevPoints[3].y, loAxisX, stat.getTimeAvgTrustToAdversary());
                        }
                    }

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }

        //============================//============================//============================

        if (showChartsFlag[1]) {
            g.translate(0, (int) (0.1 * _vs * (-maxAxisY[1] - maxAxisY[0]) - 50));
            loAxisX = 0;
            drawAxisX(1);
            drawAxisY(1);


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

                WorldStatistics[] statistics = world.getWdStatistics();


                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getHonestCollaborationRate());


                prevPoints[1].y = (int) (0.1 * _vs * statistics[1].getHonestCollaborationRate());
                prevPoints[1].x = loAxisX;


                for (int sttIdx = 1, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {

                    int number = statistics[sttIdx].getHonestCollaborationRate();
                    if (showLineChartsFlag[1]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * number), Globals.Color$.arr()[worldIdx], worldIdx, sttIdx);
                        if (prevPoints[1].y >= 0) {
                            drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, number);
                        }
                    }

                    prevPoints[1].y = (int) (0.1 * _vs * number);
                    prevPoints[1].x = loAxisX;

                    loAxisX += _hs;

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }


    }
}
