package drawingLayer.integrated;

import _type.TtSimulationMode;
import drawingLayer.DrawingWindow;
import simulateLayer.SimulationConfig;
import systemLayer.World;
import utils.Config;
import utils.Globals;
import utils.Point;
import simulateLayer.statistics.EpisodeStatistics;
import simulateLayer.statistics.WorldStatistics;

import java.awt.*;

public class IntTrustAnalysisLinearDrawingWindow extends DrawingWindow {

    private SimulationConfig configBunch;

    //============================//============================  panning params

    public IntTrustAnalysisLinearDrawingWindow(World worlds[], SimulationConfig configBunch) {
        super(worlds.length);
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[3];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated Trust Analysis Params [#Worlds: " + worlds.length + "]";
        setName("i_tut_anl");
    }

    int loAxisX;

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        printStatsInfo(1, "Accuracy (#of correct/#of all))", worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustAccuracy(), Globals.Color$.$curve_1);

        printStatsInfo(2, "Sensitivity (#of TP/#of all P)", worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustSensitivity(), Globals.Color$.$curve_2);

        printStatsInfo(3, "Specificity (#of TN/#of all N)", worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustSpecificity(), Globals.Color$.$curve_3);


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
                        drawCurve(200, dynamicHeight, Globals.Color$.$curve_1, j, 20, -1);
                        g.drawString("Accuracy", 220, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[1]) {
                        drawCurve(500, dynamicHeight, Globals.Color$.$curve_2, j, 20, -1);
                        g.drawString("Sensitivity", 520, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[2]) {
                        drawCurve(800, dynamicHeight, Globals.Color$.$curve_3, j, 20, -1);
                        g.drawString("Specificity", 820, dynamicHeight);
                        //============================
                    }
                }
                g.setColor(Globals.Color$.$configTitle);
                g.drawString("|>  " + worlds[j].getSimulationConfigInfo(), 1100, dynamicHeight);
                //============================
            }
        }

        //============================//============================//============================ Diagram drawing

        reverseNormalizeCoordination();

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        if (showChartsFlag[0]) {
            g.translate(0, _vs * -maxAxisY[0] - 50);
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

                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getTrustAccuracyI100());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getTrustSensitivityI100());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getTrustSpecificityI100());

                for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                    WorldStatistics stat = statistics[i];

                    if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                        loAxisX += _hs;
                        prevPoints[0].y = _vs * stat.getTrustAccuracyI100();
                        prevPoints[1].y = _vs * stat.getTrustSensitivityI100();
                        prevPoints[2].y = _vs * stat.getTrustSpecificityI100();
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;

                    } else {

                        prevPoints[0].y = _vs * statistics[i - 1].getTrustAccuracyI100();
                        prevPoints[1].y = _vs * statistics[i - 1].getTrustSensitivityI100();
                        prevPoints[2].y = _vs * statistics[i - 1].getTrustSpecificityI100();
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                        loAxisX += _hs;
                    }

                    if (showLineChartsFlag[0]) {
                        drawCurve(loAxisX, _vs * stat.getTrustAccuracyI100(), Globals.Color$.$curve_1, j, i);
                        if (prevPoints[0].y >= 0) {
                            g.drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, _vs * stat.getTrustAccuracyI100());
                        }
                    }
                    if (showLineChartsFlag[1]) {
                        drawCurve(loAxisX, _vs * stat.getTrustSensitivityI100(), Globals.Color$.$curve_2, j, i);
                        if (prevPoints[1].y >= 0) {
                            g.drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, _vs * stat.getTrustSensitivityI100());
                        }
                    }
                    if (showLineChartsFlag[2]) {
                        drawCurve(loAxisX, _vs * stat.getTrustSpecificityI100(), Globals.Color$.$curve_3, j, i);
                        if (prevPoints[2].y >= 0) {
                            g.drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, _vs * stat.getTrustSpecificityI100());
                        }
                    }

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }
        //============================//============================//============================ Average Chart

        if (showChartsFlag[1] && maxAxisY.length > 1) {
            g.translate(0, _vs * (-maxAxisY[1] - maxAxisY[0]) - 50);
            loAxisX = 0;

            drawAxisX(1);
            drawAxisY(1);

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
                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getAllTrustSensitivityI100());
                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getAllTrustSpecificityI100());

                for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                    WorldStatistics stat = statistics[i];

                    if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                        loAxisX += _hs;
                        prevPoints[0].y = _vs * stat.getAllTrustAccuracyI100();
                        prevPoints[1].y = _vs * stat.getAllTrustSensitivityI100();
                        prevPoints[2].y = _vs * stat.getAllTrustSpecificityI100();
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;

                    } else {

                        prevPoints[0].y = _vs * statistics[i - 1].getAllTrustAccuracyI100();
                        prevPoints[1].y = _vs * statistics[i - 1].getAllTrustSensitivityI100();
                        prevPoints[2].y = _vs * statistics[i - 1].getAllTrustSpecificityI100();
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                        loAxisX += _hs;
                    }

                    if (showLineChartsFlag[0]) {
                        drawCurve(loAxisX, _vs * stat.getAllTrustAccuracyI100(), Globals.Color$.$curve_1, j, i);
                        if (prevPoints[0].y >= 0) {
                            g.drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, _vs * stat.getAllTrustAccuracyI100());
                        }
                    }
                    if (showLineChartsFlag[1]) {
                        drawCurve(loAxisX, _vs * stat.getAllTrustSensitivityI100(), Globals.Color$.$curve_2, j, i);
                        if (prevPoints[1].y >= 0) {
                            g.drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, _vs * stat.getAllTrustSensitivityI100());
                        }
                    }
                    if (showLineChartsFlag[2]) {
                        drawCurve(loAxisX, _vs * stat.getAllTrustSpecificityI100(), Globals.Color$.$curve_3, j, i);
                        if (prevPoints[2].y >= 0) {
                            g.drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, _vs * stat.getAllTrustSpecificityI100());
                        }
                    }

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }

        //============================//============================//============================ Episode Drawing
        if (Config.SIMULATION_MODE == TtSimulationMode.Episodic) {
            if (showChartsFlag[2]) {

                g.translate(0, -1000);
                g.setColor(Globals.Color$.$curve_3);
                g.drawLine(0, 0, getRealWith(), 0);

                for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
                    if (!showWorldsFlag[j]) {
                        continue;
                    }

                    World world = worlds[j];

                    if (j > Globals.SIMULATION_TIMER || world == null) {
                        break;
                    }

                    loAxisX = j;

                    worldTimer = j < Globals.SIMULATION_TIMER ? world.getEpStatistics().length : Globals.EPISODE - 1;

                    EpisodeStatistics[] statistics = world.getEpStatistics();
                    for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                        EpisodeStatistics stat = statistics[i];

                        if (stat.getToTime() == 0) {
                            break;
                        }

                        if (i > 0) {
                            prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                            prevPoints[0].y = _vs * statistics[i - 1].getTrustAccuracyI200();
                            prevPoints[1].y = _vs * statistics[i - 1].getTrustSensitivityI200();
                            prevPoints[2].y = _vs * statistics[i - 1].getTrustSpecificityI200();
                            loAxisX += 100;

                        } else {
                            loAxisX += 100;
                            prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                            prevPoints[0].y = _vs * stat.getTrustAccuracyI200();
                            prevPoints[1].y = _vs * stat.getTrustSensitivityI200();
                            prevPoints[2].y = _vs * stat.getTrustSpecificityI200();
                        }

                        drawCurve(loAxisX, _vs * stat.getTrustAccuracyI200(), Globals.Color$.$curve_1, j, 20, i);
                        g.drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, _vs * stat.getTrustAccuracyI200());

                        drawCurve(loAxisX, _vs * stat.getTrustSensitivityI200(), Globals.Color$.$curve_2, j, 20, i);
                        g.drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, _vs * stat.getTrustSensitivityI200());

                        drawCurve(loAxisX, _vs * stat.getTrustSpecificityI200(), Globals.Color$.$curve_3, j, 20, i);
                        g.drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, _vs * stat.getTrustSpecificityI200());

                    }
                }
            }
        }
    }
}
