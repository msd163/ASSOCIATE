package drawingLayer.integrated;

import drawingLayer.DrawingWindow;
import simulateLayer.config.trust.TrustConfig;
import simulateLayer.statistics.WorldStatistics;
import societyLayer.agentSubLayer.World;
import utils.Config;
import utils.Globals;
import utils.Point;

import java.awt.*;

public class IntResistancePerNumberStatsLinearDrawingWindow extends DrawingWindow {

    private TrustConfig configBunch;

    //============================//============================  panning params


    public IntResistancePerNumberStatsLinearDrawingWindow(World worlds[], TrustConfig configBunch) {
        super(worlds.length);
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated Resistance Per Number [#Worlds: " + worlds.length + "]";
        setName("i_resis_per_num_stt");

//        axisYScale = 1;
        _vs = 280;
    }

    @Override
    public void resetParams() {
        super.resetParams();

        //axisYScale = 1;
        _vs = 280;
        _hs = 8;
    }


    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        for (int i = 0; i < Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT; i++) {
            printStatsInfo(i + 1, "# of Resist Per Number All [" + i + "] (Avg: " + Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_RESISTANCE + ")", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoResistanceByNumberAgainstAll()[i], Globals.Color$.color(i));
        }

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
                g.setColor(Globals.Color$.color(j));
                g.drawString("Sim " + (j + 1) + " |", 80, dynamicHeight);
                //============================

                if (showWorldsFlag[j]) {
                    if (showLineChartsFlag[0]) {
                        drawCurve(200, dynamicHeight, Globals.Color$.color(0), j, 20, -1);
                        g.drawString("DATA", 220, dynamicHeight);
                        //============================
                    }
                }
                //============================
                g.setColor(Globals.Color$.$configTitle);
                g.drawString("|>  " + worlds[j].getSimulationConfigInfo(), 350, dynamicHeight);
                //============================
            }
        }
        //============================//============================//============================ Diagram drawing

        reverseNormalizeCoordination();

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        if (showChartsFlag[0]) {
            prepareChartPosition(0);


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

                for (int i = 0; i < Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT; i++) {
                    maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsHypo().getAvgHypoResistanceByNumberAgainstAll()[i]);
                }

                for (int idx = 0; idx < Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT; idx++) {
                    prevPoints[idx].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getAvgHypoResistanceByNumberAgainstAll()[idx]);
                    prevPoints[idx].x = loAxisX;
                }

                for (int i = 1, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {

                    int[] number = statistics[i].getStatisticsHypo().getAvgHypoResistanceByNumberAgainstAll();
                    for (int idx = 0; idx < Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT; idx++) {
                        if (showLineChartsFlag[idx]) {
                            drawCurve(loAxisX, (int) (0.1 * _vs * number[idx]), Globals.Color$.color(idx), worldIdx, i);
                            if (prevPoints[idx].y >= 0) {
                                drawLine(prevPoints[idx].x, prevPoints[idx].y, loAxisX, number[idx]);
                            }
                        }
                    }

                    for (int idx = 0; idx < Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT; idx++) {
                        prevPoints[idx].y = (int) (0.1 * _vs * number[idx]);
                        prevPoints[idx].x = loAxisX;
                    }

                    loAxisX += _hs;

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }

    }
}
