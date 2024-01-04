package SiM.monitor.integrated;

import SiM.monitor.DrawingWindow;
import SiM.profiler.config.TrustConfig;
import SiM.statistics.WorldStatistics;
import SiM.statistics.WorldStatisticsHypo;
import WSM.society.agent.World;
import core.utils.Config;
import core.utils.Globals;
import core.utils.Point;

import java.awt.*;

public class IntResistanceStatsLinearDrawingWindow extends DrawingWindow {

    private TrustConfig configBunch;

    //============================//============================  panning params


    public IntResistanceStatsLinearDrawingWindow(World worlds[], TrustConfig configBunch) {
        super(worlds.length);
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[7];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated Resistance [#Worlds: " + worlds.length + "]";
        setName("i_resis_stt");

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

        printStatsInfo(1, "# of Resist against Pos", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstPos(), Globals.Color$.color(0));
        printStatsInfo(2, "# of Resist against Neg", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstNeg(), Globals.Color$.color(1));
        printStatsInfo(3, "# of Resist against All", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstAll(), Globals.Color$.color(2));

        printStatsInfo(5, "# of Resist against Pos (Avg:" + Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_RESISTANCE + ")", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstPos(), Globals.Color$.color(0));
        printStatsInfo(6, "# of Resist against Neg (Avg:" + Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_RESISTANCE + ")", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstNeg(), Globals.Color$.color(1));
        printStatsInfo(7, "# of Resist against All (Avg:" + Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_RESISTANCE + ")", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstAll(), Globals.Color$.color(2));

        printStatsInfo(9, "# of Resist/Suspected ", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getIttHypoResistancePerSuspected(), Globals.Color$.color(3));


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
                        drawSymbolOnCurve(200, dynamicHeight, Globals.Color$.color(0), j, 20, -1);
                        g.drawString("Pos", 220, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(300, dynamicHeight, Globals.Color$.color(1), j, 20, -1);
                        g.drawString("Neg", 320, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(400, dynamicHeight, Globals.Color$.color(2), j, 20, -1);
                        g.drawString("All", 420, dynamicHeight);
                        //============================
                    }
                }
                //============================
                g.setColor(Globals.Color$.$configTitle);
                g.drawString("|>  " + worlds[j].getSimulationConfigInfo(), 550, dynamicHeight);
                //============================
            }
        }
        //============================//============================//============================ Diagram drawing

        reverseNormalizeCoordination();

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        //============================//============================//============================
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

                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstPos());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstNeg());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstAll());


                prevPoints[0].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstPos());
                prevPoints[1].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstNeg());
                prevPoints[2].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstAll());
                prevPoints[0].x
                        = prevPoints[1].x
                        = prevPoints[2].x
                        = loAxisX;


                for (int sttIdx = 1, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {

                    WorldStatisticsHypo stat = statistics[sttIdx].getStatisticsHypo();

                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getIttHypoResistanceOfAllNumberAgainstPos()), Globals.Color$.color(0), worldIdx, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getIttHypoResistanceOfAllNumberAgainstPos(), sttIdx,0);
                    }
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getIttHypoResistanceOfAllNumberAgainstNeg()), Globals.Color$.color(1), worldIdx, sttIdx);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getIttHypoResistanceOfAllNumberAgainstNeg(), sttIdx,1);
                    }
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getIttHypoResistanceOfAllNumberAgainstAll()), Globals.Color$.color(2), worldIdx, sttIdx);
                        drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getIttHypoResistanceOfAllNumberAgainstAll(), sttIdx,2);
                    }


                    prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstPos());
                    prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstNeg());
                    prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstAll());
                    prevPoints[0].x
                            = prevPoints[1].x
                            = prevPoints[2].x
                            = loAxisX;

                    loAxisX += _hs;

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }

        //============================//============================//============================
        if (showChartsFlag[1]) {
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

                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstPos());
                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstNeg());
                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstAll());


                prevPoints[0].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstPos());
                prevPoints[1].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstNeg());
                prevPoints[2].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstAll());
                prevPoints[0].x
                        = prevPoints[1].x
                        = prevPoints[2].x
                        = loAxisX;


                for (int sttIdx = 1, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {

                    WorldStatisticsHypo stat = statistics[sttIdx].getStatisticsHypo();

                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoResistanceOfAllNumberAgainstPos()), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getAvgHypoResistanceOfAllNumberAgainstPos(), sttIdx,3);
                    }
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoResistanceOfAllNumberAgainstNeg()), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getAvgHypoResistanceOfAllNumberAgainstNeg(), sttIdx,4);
                    }
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoResistanceOfAllNumberAgainstAll()), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getAvgHypoResistanceOfAllNumberAgainstAll(), sttIdx,5);
                    }


                    prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstPos());
                    prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstNeg());
                    prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstAll());
                    prevPoints[0].x
                            = prevPoints[1].x
                            = prevPoints[2].x
                            = loAxisX;

                    loAxisX += _hs;

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }

        //============================//============================//============================
   /*     if (showChartsFlag[2]) {
            g.translate(0, (int) (0.1 * _vs * (-maxAxisY[2] - maxAxisY[1]) - 50));
            loAxisX = 0;

            drawAxisX(2);
            drawAxisY(2);

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

                maxAxisY[2] = Math.max(maxAxisY[2], statistics[worldTimer - 1].getStatisticsHypo().getIttHypoResistancePerSuspected());


                prevPoints[0].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getIttHypoResistancePerSuspected());
                prevPoints[0].x = loAxisX;


                for (int i = 1, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {

                    WorldStatisticsHypo stat = statistics[i].getStatisticsHypo();

                    if (showLineChartsFlag[0]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getIttHypoResistancePerSuspected()), Globals.Color$.color(3), worldIdx, i);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getIttHypoResistancePerSuspected());
                    }


                    prevPoints[0].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getIttHypoResistancePerSuspected());
                    prevPoints[0].x = loAxisX;

                    loAxisX += _hs;

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }
*/
    }
}
