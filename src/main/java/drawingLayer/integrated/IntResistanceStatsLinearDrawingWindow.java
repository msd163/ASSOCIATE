package drawingLayer.integrated;

import drawingLayer.DrawingWindow;
import simulateLayer.SimulationConfig;
import simulateLayer.statistics.WorldStatistics;
import simulateLayer.statistics.WorldStatisticsHypo;
import societyLayer.agentSubLayer.World;
import utils.Config;
import utils.Globals;
import utils.Point;

import java.awt.*;

public class IntResistanceStatsLinearDrawingWindow extends DrawingWindow {

    private SimulationConfig configBunch;

    //============================//============================  panning params


    public IntResistanceStatsLinearDrawingWindow(World worlds[], SimulationConfig configBunch) {
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
                        drawCurve(200, dynamicHeight, Globals.Color$.color(0), j, 20, -1);
                        g.drawString("Pos", 220, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[1]) {
                        drawCurve(300, dynamicHeight, Globals.Color$.color(1), j, 20, -1);
                        g.drawString("Neg", 320, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[2]) {
                        drawCurve(400, dynamicHeight, Globals.Color$.color(2), j, 20, -1);
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


                for (int i = 1, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {

                    WorldStatisticsHypo stat = statistics[i].getStatisticsHypo();

                    if (showLineChartsFlag[0]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getIttHypoResistanceOfAllNumberAgainstPos()), Globals.Color$.color(0), worldIdx, i);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getIttHypoResistanceOfAllNumberAgainstPos());
                    }
                    if (showLineChartsFlag[1]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getIttHypoResistanceOfAllNumberAgainstNeg()), Globals.Color$.color(1), worldIdx, i);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getIttHypoResistanceOfAllNumberAgainstNeg());
                    }
                    if (showLineChartsFlag[2]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getIttHypoResistanceOfAllNumberAgainstAll()), Globals.Color$.color(2), worldIdx, i);
                        drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getIttHypoResistanceOfAllNumberAgainstAll());
                    }


                    prevPoints[0].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstPos());
                    prevPoints[1].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstNeg());
                    prevPoints[2].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getIttHypoResistanceOfAllNumberAgainstAll());
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


                for (int i = 1, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {

                    WorldStatisticsHypo stat = statistics[i].getStatisticsHypo();

                    if (showLineChartsFlag[0]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoResistanceOfAllNumberAgainstPos()), Globals.Color$.color(0), worldIdx, i);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getAvgHypoResistanceOfAllNumberAgainstPos());
                    }
                    if (showLineChartsFlag[1]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoResistanceOfAllNumberAgainstNeg()), Globals.Color$.color(1), worldIdx, i);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getAvgHypoResistanceOfAllNumberAgainstNeg());
                    }
                    if (showLineChartsFlag[2]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoResistanceOfAllNumberAgainstAll()), Globals.Color$.color(2), worldIdx, i);
                        drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getAvgHypoResistanceOfAllNumberAgainstAll());
                    }


                    prevPoints[0].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstPos());
                    prevPoints[1].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstNeg());
                    prevPoints[2].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstAll());
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
        if (showChartsFlag[2]) {
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

    }
}
