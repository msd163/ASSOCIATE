package SiM.monitor.integrated;

import SiM.monitor.DrawingWindow;
import core.enums.TtSimulationMode;
import SiM.profiler.config.TrustConfig;
import SiM.statistics.EpisodeStatistics;
import SiM.statistics.WorldStatistics;
import WSM.World;
import core.utils.Config;
import core.utils.Globals;
import core.utils.Point;

import java.awt.*;

public class IntTrustAnalysisLinearDrawingWindow extends DrawingWindow {

    private TrustConfig configBunch;

    //============================//============================  panning params

    public IntTrustAnalysisLinearDrawingWindow(World worlds[], TrustConfig configBunch) {
        super(worlds.length);
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[3];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated Trust Analysis Params [#Worlds: " + worlds.length + "]";
        setName("i_tut_anl");
        axisYScale = 0.01;
        _vs = 100;
    }


    private boolean isCurveStarted[] = {false, false, false};

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
                        drawSymbolOnCurve(200, dynamicHeight, Globals.Color$.$curve_1, j, 20, -1);
                        g.drawString("Accuracy", 220, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(500, dynamicHeight, Globals.Color$.$curve_2, j, 20, -1);
                        g.drawString("Sensitivity", 520, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(800, dynamicHeight, Globals.Color$.$curve_3, j, 20, -1);
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

                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getTrustAccuracyI100());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getTrustSensitivityI100());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getTrustSpecificityI100());

                for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {
                    WorldStatistics stat = statistics[sttIdx];

                    if (sttIdx == 0 || stat.getEpisode() != statistics[sttIdx - 1].getEpisode()) {
                        loAxisX += _hs;
                        prevPoints[0].y = (int) (0.1 * _vs * stat.getTrustAccuracyI100());
                        prevPoints[1].y = (int) (0.1 * _vs * stat.getTrustSensitivityI100());
                        prevPoints[2].y = (int) (0.1 * _vs * stat.getTrustSpecificityI100());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;

                    } else {

                        prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTrustAccuracyI100());
                        prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTrustSensitivityI100());
                        prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTrustSpecificityI100());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                        loAxisX += _hs;
                    }

                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTrustAccuracyI100()), Globals.Color$.$curve_1, j, sttIdx);
                        if (prevPoints[0].y >= 0) {
                            drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getTrustAccuracyI100(), sttIdx, 0);
                        }
                    }
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTrustSensitivityI100()), Globals.Color$.$curve_2, j, sttIdx);
                        if (prevPoints[1].y >= 0) {
                            drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getTrustSensitivityI100(), sttIdx, 1);
                        }
                    }
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTrustSpecificityI100()), Globals.Color$.$curve_3, j, sttIdx);
                        if (prevPoints[2].y >= 0) {
                            drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getTrustSpecificityI100(), sttIdx, 2);
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

                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getAllTrustAccuracyI100());
                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getAllTrustSensitivityI100());
                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getAllTrustSpecificityI100());

                isCurveStarted[0]
                        = isCurveStarted[1]
                        = isCurveStarted[2]
                        = false;

                prevPoints[0].y
                        = prevPoints[1].y
                        = prevPoints[2].y
                        = -1;

                for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {
                    WorldStatistics stat = statistics[sttIdx];

                    if (!isCurveStarted[0] && stat.getAllTrustAccuracyI100() >= 0) {
                        isCurveStarted[0] = true;
                        prevPoints[0].y = (int) (0.1 * _vs * stat.getAllTrustAccuracyI100());
                    } else if (sttIdx > 0) {
                        prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getAllTrustAccuracyI100());
                    }

                    if (!isCurveStarted[1] && stat.getAllTrustSensitivityI100() >= 0) {
                        isCurveStarted[1] = true;
                        prevPoints[1].y = (int) (0.1 * _vs * stat.getAllTrustSensitivityI100());
                    } else if (sttIdx > 0) {
                        prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getAllTrustSensitivityI100());
                    }

                    if (!isCurveStarted[2] && stat.getAllTrustSpecificityI100() >= 0) {
                        isCurveStarted[2] = true;
                        prevPoints[2].y = (int) (0.1 * _vs * stat.getAllTrustSpecificityI100());
                    } else if (sttIdx > 0) {
                        prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getAllTrustSpecificityI100());
                    }

                    prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                    loAxisX += _hs;

                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAllTrustAccuracyI100()), Globals.Color$.$curve_1, j, sttIdx);
                        if (stat.getAllTrustAccuracyI100() >= 0) {
                            drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getAllTrustAccuracyI100(), sttIdx, 3);
                        }
                    }
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAllTrustSensitivityI100()), Globals.Color$.$curve_2, j, sttIdx);
                        if (stat.getAllTrustSensitivityI100() >= 0) {
                            drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getAllTrustSensitivityI100(), sttIdx, 4);
                        }
                    }
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAllTrustSpecificityI100()), Globals.Color$.$curve_3, j, sttIdx);
                        if (stat.getAllTrustSpecificityI100() >= 0) {
                            drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getAllTrustSpecificityI100(), sttIdx, 5);
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
                    for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {
                        EpisodeStatistics stat = statistics[sttIdx];

                        if (stat.getToTime() == 0) {
                            break;
                        }

                        if (sttIdx > 0) {
                            prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                            prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTrustAccuracyI200());
                            prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTrustSensitivityI200());
                            prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTrustSpecificityI200());
                            loAxisX += 100;

                        } else {
                            loAxisX += 100;
                            prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                            prevPoints[0].y = (int) (0.1 * _vs * stat.getTrustAccuracyI200());
                            prevPoints[1].y = (int) (0.1 * _vs * stat.getTrustSensitivityI200());
                            prevPoints[2].y = (int) (0.1 * _vs * stat.getTrustSpecificityI200());
                        }

                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTrustAccuracyI200()), Globals.Color$.$curve_1, j, 20, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getTrustAccuracyI200(), sttIdx, 6);

                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTrustSensitivityI200()), Globals.Color$.$curve_2, j, 20, sttIdx);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getTrustSensitivityI200(), sttIdx, 7);

                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTrustSpecificityI200()), Globals.Color$.$curve_3, j, 20, sttIdx);
                        drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getTrustSpecificityI200(), sttIdx, 8);

                    }
                }
            }
        }
    }
}
