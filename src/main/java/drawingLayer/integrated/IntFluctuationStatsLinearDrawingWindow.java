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

public class IntFluctuationStatsLinearDrawingWindow extends DrawingWindow {

    private SimulationConfig configBunch;

    //============================//============================  panning params


    public IntFluctuationStatsLinearDrawingWindow(World worlds[], SimulationConfig configBunch) {
        super(worlds.length);
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[7];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated Fluctuation [#Worlds: " + worlds.length + "]";
        setName("i_flucc_stt");

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

    int loAxisX;

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        printStatsInfo(1, "# of Fluctuation", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoFluct(), Globals.Color$.arr()[0]);
        printStatsInfo(2, "# of Diagnosis", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoSuspectDiagnosis(), Globals.Color$.arr()[1]);
        printStatsInfo(3, "# of Ignored Pos", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoIgnoredPos(), Globals.Color$.arr()[2]);
        printStatsInfo(4, "# of Ignored Neg", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoIgnoredNeg(), Globals.Color$.arr()[3]);
        printStatsInfo(5, "# of Ignored Pos TP", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoIgnoredPosTruePositive(), Globals.Color$.arr()[4]);
        printStatsInfo(6, "# of Ignored Neg TP", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoIgnoredNegTruePositive(), Globals.Color$.arr()[5]);
        printStatsInfo(8, "(Avg Ignored Pos) / (Avg Fluct)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgIgnoredPosProportionToFluct1000(), Globals.Color$.arr()[6]);
        printStatsInfo(9, "(Avg Ignored Neg) / (Avg Fluct)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgIgnoredNegProportionToFluct1000(), Globals.Color$.arr()[7]);


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
                g.setColor(Globals.Color$.yellow);
                g.drawString("Sim " + (j + 1) + " |", 80, dynamicHeight);
                //============================

                if (showWorldsFlag[j]) {
                    if (showLineChartsFlag[0]) {
                        drawCurve(200, dynamicHeight, Globals.Color$.arr()[0], j, 20, -1);
                        g.drawString("Flc", 220, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[1]) {
                        drawCurve(320, dynamicHeight, Globals.Color$.arr()[1], j, 20, -1);
                        g.drawString("Dgn", 340, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[2]) {
                        drawCurve(440, dynamicHeight, Globals.Color$.arr()[2], j, 20, -1);
                        g.drawString("IgPs", 460, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[3]) {
                        drawCurve(560, dynamicHeight, Globals.Color$.arr()[3], j, 20, -1);
                        g.drawString("IgNg", 580, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[4]) {
                        drawCurve(680, dynamicHeight, Globals.Color$.arr()[4], j, 20, -1);
                        g.drawString("IgPsTP", 700, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[5]) {
                        drawCurve(800, dynamicHeight, Globals.Color$.arr()[5], j, 20, -1);
                        g.drawString("IgPsNg", 820, dynamicHeight);
                        //============================
                    }
                }
                //============================
                g.setColor(Globals.Color$.$configTitle);
                g.drawString("|>  " + worlds[j].getSimulationConfigInfo(), 940, dynamicHeight);
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

                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsHypo().getAvgHypoFluct());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsHypo().getAvgHypoSuspectDiagnosis());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsHypo().getAvgHypoIgnoredPos());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsHypo().getAvgHypoIgnoredNeg());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsHypo().getAvgHypoIgnoredPosTruePositive());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsHypo().getAvgHypoIgnoredNegTruePositive());


                prevPoints[0].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getAvgHypoFluct());
                prevPoints[1].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getAvgHypoSuspectDiagnosis());
                prevPoints[2].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getAvgHypoIgnoredPos());
                prevPoints[3].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getAvgHypoIgnoredNeg());
                prevPoints[4].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getAvgHypoIgnoredPosTruePositive());
                prevPoints[5].y = (int) (0.1 * _vs * statistics[0].getStatisticsHypo().getAvgHypoIgnoredNegTruePositive());
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = prevPoints[4].x = prevPoints[5].x = loAxisX;


                for (int i = 1, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {

                    WorldStatisticsHypo stat = statistics[i].getStatisticsHypo();

                    if (showLineChartsFlag[0]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoFluct()), Globals.Color$.arr()[0], worldIdx, i);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getAvgHypoFluct());
                    }
                    if (showLineChartsFlag[1]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoSuspectDiagnosis()), Globals.Color$.arr()[1], worldIdx, i);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getAvgHypoSuspectDiagnosis());
                    }
                    if (showLineChartsFlag[2]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoIgnoredPos()), Globals.Color$.arr()[2], worldIdx, i);
                        drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getAvgHypoIgnoredPos());
                    }

                    if (showLineChartsFlag[3]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoIgnoredNeg()), Globals.Color$.arr()[3], worldIdx, i);
                        drawLine(prevPoints[3].x, prevPoints[3].y, loAxisX, stat.getAvgHypoIgnoredNeg());
                    }

                    if (showLineChartsFlag[4]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoIgnoredPosTruePositive()), Globals.Color$.arr()[4], worldIdx, i);
                        drawLine(prevPoints[4].x, prevPoints[4].y, loAxisX, stat.getAvgHypoIgnoredPosTruePositive());
                    }

                    if (showLineChartsFlag[5]) {
                        drawCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoIgnoredNegTruePositive()), Globals.Color$.arr()[5], worldIdx, i);
                        drawLine(prevPoints[5].x, prevPoints[5].y, loAxisX, stat.getAvgHypoIgnoredNegTruePositive());
                    }


                    prevPoints[0].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getAvgHypoFluct());
                    prevPoints[1].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getAvgHypoSuspectDiagnosis());
                    prevPoints[2].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getAvgHypoIgnoredPos());
                    prevPoints[3].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getAvgHypoIgnoredNeg());
                    prevPoints[4].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getAvgHypoIgnoredPosTruePositive());
                    prevPoints[5].y = (int) (0.1 * _vs * statistics[i].getStatisticsHypo().getAvgHypoIgnoredNegTruePositive());
                    prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = prevPoints[4].x = prevPoints[5].x = loAxisX;

                    loAxisX += _hs;

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }

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
                int propPos = statistics[worldTimer - 1].getStatisticsHypo().getAvgIgnoredPosProportionToFluct1000();
                int propNeg = statistics[worldTimer - 1].getStatisticsHypo().getAvgIgnoredNegProportionToFluct1000();

                maxAxisY[1] = Math.max(maxAxisY[1], propPos);
                maxAxisY[1] = Math.max(maxAxisY[1], propNeg);

                propPos = statistics[0].getStatisticsHypo().getAvgIgnoredPosProportionToFluct1000();
                propNeg = statistics[0].getStatisticsHypo().getAvgIgnoredNegProportionToFluct1000();

                prevPoints[0].y = (int) (0.1 * _vs * propPos);
                prevPoints[1].y = (int) (0.1 * _vs * propNeg);
                prevPoints[0].x = prevPoints[1].x = loAxisX;


                for (int i = 1, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {

                    WorldStatisticsHypo stat = statistics[i].getStatisticsHypo();


                    if (showLineChartsFlag[0]) {
                        propPos = stat.getAvgIgnoredPosProportionToFluct1000();

                        drawCurve(loAxisX, (int) (0.1 * _vs * propPos), Globals.Color$.arr()[6], worldIdx, i);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, propPos);
                    }
                    if (showLineChartsFlag[1]) {
                        propNeg = stat.getAvgIgnoredNegProportionToFluct1000();
                        drawCurve(loAxisX, (int) (0.1 * _vs * propNeg), Globals.Color$.arr()[7], worldIdx, i);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, propNeg);
                    }

                    prevPoints[0].y = (int) (0.1 * _vs * propPos);
                    prevPoints[1].y = (int) (0.1 * _vs * propNeg);
                    prevPoints[0].x = prevPoints[1].x = loAxisX;


                    loAxisX += _hs;

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }

    }
}
