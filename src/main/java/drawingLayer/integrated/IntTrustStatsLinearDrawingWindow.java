package drawingLayer.integrated;

import drawingLayer.DrawingWindow;
import simulateLayer.SimulationConfig;
import systemLayer.World;
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
    }

    int loAxisX;

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        printStatsInfo(1, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Mischief", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToMischief(), Color.WHITE);
        printStatsInfo(2, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Honest", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToHonest(), Color.GREEN);
        printStatsInfo(3, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Hypocrite", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToHypocrite(), Color.MAGENTA);
        printStatsInfo(4, "Avg (" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Trust to Adversary", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimeAvgTrustToAdversary(), Color.RED);

        //============================//============================ INFO
        for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {

            World world = worlds[j];

            if (j > Globals.SIMULATION_TIMER || world == null) {
                break;
            }

            //============================
            int y = 40 * j + 250;
            g.setColor(Color.white);
            g.drawString("Sim " + (j + 1) + " |", 80, y);
            //============================

            if (showWorldsFlag[j]) {
                if (showLineChartsFlag[0]) {
                    drawCurve(200, y, Color.WHITE, j, 20, -1);
                    g.drawString("Mischief", 220, y);
                    //============================
                }
                if (showLineChartsFlag[1]) {
                    drawCurve(450, y, Color.GREEN, j, 20, -1);
                    g.drawString("Honest", 470, y);
                    //============================
                }
                if (showLineChartsFlag[2]) {
                    drawCurve(700, y, Color.MAGENTA, j, 20, -1);
                    g.drawString("Hypocrite", 720, y);
                    //============================
                }
                if (showLineChartsFlag[3]) {
                    drawCurve(950, y, Color.RED, j, 20, -1);
                    g.drawString("Adversary", 970, y);
                }
            }
            //============================
            g.setColor(Globals.Color$.lightGray);
            g.drawString("|>  " + worlds[j].getSimulationConfigInfo(), 1100, y);
            //============================
        }

        //============================//============================//============================ Diagram drawing

        reverseNormalizeCoordination();

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        if (showChartsFlag[0]) {
            for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
                if (!showWorldsFlag[j]) {
                    continue;
                }

                World world = worlds[j];

                if (j > Globals.SIMULATION_TIMER || world == null) {
                    break;
                }

                loAxisX = j;
                axisY = 0;

                worldTimer = j < Globals.SIMULATION_TIMER ? Config.WORLD_LIFE_TIME : Globals.WORLD_TIMER;

                WorldStatistics[] statistics = world.getWdStatistics();
                for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                    WorldStatistics stat = statistics[i];

                    if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                        loAxisX += _hs;
                        prevPoints[0].y = _vs * stat.getTimeAvgTrustToMischief();
                        prevPoints[1].y = _vs * stat.getTimeAvgTrustToHonest();
                        prevPoints[2].y = _vs * stat.getTimeAvgTrustToHypocrite();
                        prevPoints[3].y = _vs * stat.getTimeAvgTrustToAdversary();
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = loAxisX;

                    } else {

                        prevPoints[0].y = _vs * statistics[i - 1].getTimeAvgTrustToMischief();
                        prevPoints[1].y = _vs * statistics[i - 1].getTimeAvgTrustToHonest();
                        prevPoints[2].y = _vs * statistics[i - 1].getTimeAvgTrustToHypocrite();
                        prevPoints[3].y = _vs * statistics[i - 1].getTimeAvgTrustToAdversary();
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = loAxisX;
                        loAxisX += _hs;
                    }

                    if (showLineChartsFlag[0]) {
                        drawCurve(loAxisX, _vs * stat.getTimeAvgTrustToMischief(), Color.WHITE, j, i);
                        if (prevPoints[0].y >= 0) {
                            g.drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, _vs * stat.getTimeAvgTrustToMischief());
                        }
                    }

                    if (showLineChartsFlag[1]) {
                        drawCurve(loAxisX, _vs * stat.getTimeAvgTrustToHonest(), Color.GREEN, j, i);
                        if (prevPoints[1].y >= 0) {
                            g.drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, _vs * stat.getTimeAvgTrustToHonest());
                        }
                    }

                    if (showLineChartsFlag[2]) {
                        drawCurve(loAxisX, _vs * stat.getTimeAvgTrustToHypocrite(), Color.MAGENTA, j, i);
                        if (prevPoints[2].y >= 0) {
                            g.drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, _vs * stat.getTimeAvgTrustToHypocrite());
                        }
                    }

                    if (showLineChartsFlag[3]) {
                        drawCurve(loAxisX, _vs * stat.getTimeAvgTrustToAdversary(), Color.RED, j, i);
                        if (prevPoints[2].y >= 0) {
                            g.drawLine(prevPoints[3].x, prevPoints[3].y, loAxisX, _vs * stat.getTimeAvgTrustToAdversary());
                        }
                    }

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
            //============================//============================ Draw X-axis line
            g.setColor(Color.YELLOW);
            g.drawLine(0, 0, getRealWith(), 0);

        }
    }
}
