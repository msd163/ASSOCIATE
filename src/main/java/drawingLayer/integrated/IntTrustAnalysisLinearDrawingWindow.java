package drawingLayer.integrated;

import _type.TtSimulationMode;
import drawingLayer.DrawingWindow;
import simulateLayer.SimulationConfig;
import systemLayer.World;
import utils.Config;
import utils.Globals;
import utils.Point;
import utils.statistics.EpisodeStatistics;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class IntTrustAnalysisLinearDrawingWindow extends DrawingWindow {

    private World worlds[];
    private SimulationConfig configBunch;

    //============================//============================  panning params

    public IntTrustAnalysisLinearDrawingWindow(World worlds[], SimulationConfig configBunch) {
        super();
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[3];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
    }

    int loAxisX;

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, "Integrated Trust Analysis Params", null)) {
            return;
        }

        printStatsInfo(1, "Accuracy (#of correct/#of all))", worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustAccuracy(), Color.GREEN);

        printStatsInfo(2, "Sensitivity (#of TP/#of all P)", worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustSensitivity(), Color.YELLOW);

        printStatsInfo(3, "Specificity (#of TN/#of all N)", worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustSpecificity(), Color.PINK);


        //============================//============================ INFO
        for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
            World world = worlds[j];

            if (j > Globals.SIMULATION_TIMER || world == null) {
                break;
            }

            //============================
            int y = 40 * j + 220;
            g.setColor(Color.white);
            g.drawString("Sim " + j + " |", 80, y);
            //============================
            drawCurve(200, y, Color.GREEN, j, 20, -1);
            g.drawString("Accuracy", 220, y);
            //============================
            drawCurve(500, y, Color.YELLOW, j, 20, -1);
            g.drawString("Sensitivity", 520, y);
            //============================
            drawCurve(800, y, Color.PINK, j, 20, -1);
            g.drawString("Specificity", 820, y);
            //============================
            g.setColor(Globals.Color$.lightGray);
            g.drawString("|>  " + configBunch.getByIndex(j).getInfo(), 1100, y);
            //============================
        }

        //============================//============================//============================ Diagram drawing

        //============================ Translate
        reverseNormalizeCoordination();

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
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
                    loAxisX += 8;
                    prevPoints[0].y = stat.getTrustAccuracyI200();
                    prevPoints[1].y = stat.getTrustSensitivityI200();
                    prevPoints[2].y = stat.getTrustSpecificityI200();
                    prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;

                } else {

                    prevPoints[0].y = statistics[i - 1].getTrustAccuracyI200();
                    prevPoints[1].y = statistics[i - 1].getTrustSensitivityI200();
                    prevPoints[2].y = statistics[i - 1].getTrustSpecificityI200();
                    prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                    loAxisX += 8;
                }

                drawCurve(loAxisX, stat.getTrustAccuracyI200(), Color.GREEN, j, i);
                if (prevPoints[0].y >= 0) {
                    g.drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getTrustAccuracyI200());
                }
                drawCurve(loAxisX, stat.getTrustSensitivityI200(), Color.YELLOW, j, i);
                if (prevPoints[1].y >= 0) {
                    g.drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getTrustSensitivityI200());
                }
                drawCurve(loAxisX, stat.getTrustSpecificityI200(), Color.PINK, j, i);
                if (prevPoints[2].y >= 0) {
                    g.drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getTrustSpecificityI200());
                }

                if (axisX < loAxisX) {
                    axisX = loAxisX;
                }
            }
        }
        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

        //============================//============================//============================ Average Chart

        g.translate(0, -600);
        loAxisX = 0;

        for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
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
                    loAxisX += 8;
                    prevPoints[0].y = stat.getAllTrustAccuracyI200();
                    prevPoints[1].y = stat.getAllTrustSensitivityI200();
                    prevPoints[2].y = stat.getAllTrustSpecificityI200();
                    prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;

                } else {

                    prevPoints[0].y = statistics[i - 1].getAllTrustAccuracyI200();
                    prevPoints[1].y = statistics[i - 1].getAllTrustSensitivityI200();
                    prevPoints[2].y = statistics[i - 1].getAllTrustSpecificityI200();
                    prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                    loAxisX += 8;
                }

                drawCurve(loAxisX, stat.getAllTrustAccuracyI200(), Color.GREEN, j, i);
                if (prevPoints[0].y >= 0) {
                    g.drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getAllTrustAccuracyI200());
                }
                drawCurve(loAxisX, stat.getAllTrustSensitivityI200(), Color.YELLOW, j, i);
                if (prevPoints[1].y >= 0) {
                    g.drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getAllTrustSensitivityI200());
                }
                drawCurve(loAxisX, stat.getAllTrustSpecificityI200(), Color.PINK, j, i);
                if (prevPoints[2].y >= 0) {
                    g.drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getAllTrustSpecificityI200());
                }

                if (axisX < loAxisX) {
                    axisX = loAxisX;
                }
            }
        }
        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);


        //============================//============================//============================ Episode Drawing
        if (Config.SIMULATION_MODE == TtSimulationMode.Episodic) {
            g.translate(0, -1000);
            g.setColor(Color.pink);
            g.drawLine(0, 0, getRealWith(), 0);


            for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
                World world = worlds[j];

                if (j > Globals.SIMULATION_TIMER || world == null) {
                    break;
                }

                loAxisX = j;
                axisY = 0;

                worldTimer = j < Globals.SIMULATION_TIMER ? world.getEpStatistics().length : Globals.EPISODE - 1;

                EpisodeStatistics[] statistics = world.getEpStatistics();
                for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                    EpisodeStatistics stat = statistics[i];

                    if (stat.getToTime() == 0) {
                        break;
                    }

                    if (i > 0) {
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                        prevPoints[0].y = statistics[i - 1].getTrustAccuracyI200();
                        prevPoints[1].y = statistics[i - 1].getTrustSensitivityI200();
                        prevPoints[2].y = statistics[i - 1].getTrustSpecificityI200();
                        loAxisX += 100;

                    } else {
                        loAxisX += 100;
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                        prevPoints[0].y = stat.getTrustAccuracyI200();
                        prevPoints[1].y = stat.getTrustSensitivityI200();
                        prevPoints[2].y = stat.getTrustSpecificityI200();
                    }

                    drawCurve(loAxisX, stat.getTrustAccuracyI200(), Color.GREEN, j, 20, i);
                    g.drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getTrustAccuracyI200());

                    drawCurve(loAxisX, stat.getTrustSensitivityI200(), Color.YELLOW, j, 20, i);
                    g.drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getTrustSensitivityI200());

                    drawCurve(loAxisX, stat.getTrustSpecificityI200(), Color.PINK, j, 20, i);
                    g.drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getTrustSpecificityI200());

                }
            }
        }

    }
}
