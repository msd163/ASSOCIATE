package SiM.monitor.integrated;

import SiM.monitor.DrawingWindow;
import core.config.trust.TrustConfig;
import SiM.statistics.WorldStatistics;
import SiM.statistics.WorldStatisticsHypo;
import WSM.society.agent.World;
import core.utils.Config;
import core.utils.Globals;
import core.utils.Point;

import java.awt.*;

public class IntFluctuationStatsLinearDrawingWindow extends DrawingWindow {

    private TrustConfig configBunch;

    //============================//============================  panning params


    public IntFluctuationStatsLinearDrawingWindow(World worlds[], TrustConfig configBunch) {
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


    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        printStatsInfo(1, "# of Fluctuation", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoFluct(), Globals.Color$.color(0));
        printStatsInfo(2, "# of Diagnosis", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoSuspectDiagnosis(), Globals.Color$.color(1));
        printStatsInfo(3, "# of Ignored Pos", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoIgnoredPos(), Globals.Color$.color(2));
        printStatsInfo(4, "# of Ignored Neg", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoIgnoredNeg(), Globals.Color$.color(3));
        printStatsInfo(5, "# of Ignored Pos TP", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoIgnoredPosTruePositive(), Globals.Color$.color(4));
        printStatsInfo(6, "# of Ignored Neg TP", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgHypoIgnoredNegTruePositive(), Globals.Color$.color(5));
        printStatsInfo(8, "(Avg Ignored Pos) / (Avg Fluct)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgIgnoredPosProportionToFluct1000(), Globals.Color$.color(6));
        printStatsInfo(9, "(Avg Ignored Neg) / (Avg Fluct)", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsHypo().getAvgIgnoredNegProportionToFluct1000(), Globals.Color$.color(7));


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
                        drawSymbolOnCurve(200, dynamicHeight, Globals.Color$.color(0), j, 20, -1);
                        g.drawString("Flc", 220, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(320, dynamicHeight, Globals.Color$.color(1), j, 20, -1);
                        g.drawString("Dgn", 340, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(440, dynamicHeight, Globals.Color$.color(2), j, 20, -1);
                        g.drawString("IgPs", 460, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[3]) {
                        drawSymbolOnCurve(560, dynamicHeight, Globals.Color$.color(3), j, 20, -1);
                        g.drawString("IgNg", 580, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[4]) {
                        drawSymbolOnCurve(680, dynamicHeight, Globals.Color$.color(4), j, 20, -1);
                        g.drawString("IgPsTP", 700, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[5]) {
                        drawSymbolOnCurve(800, dynamicHeight, Globals.Color$.color(5), j, 20, -1);
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


                for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {

                    int curveIndex = -1;
                    WorldStatisticsHypo stat = statistics[sttIdx].getStatisticsHypo();

                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoFluct()), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getAvgHypoFluct(), sttIdx,++curveIndex);
                    }
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoSuspectDiagnosis()), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getAvgHypoSuspectDiagnosis(), sttIdx, ++curveIndex);
                    }
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoIgnoredPos()), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getAvgHypoIgnoredPos(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[3]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoIgnoredNeg()), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        drawLine(prevPoints[3].x, prevPoints[3].y, loAxisX, stat.getAvgHypoIgnoredNeg(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[4]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoIgnoredPosTruePositive()), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        drawLine(prevPoints[4].x, prevPoints[4].y, loAxisX, stat.getAvgHypoIgnoredPosTruePositive(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[5]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getAvgHypoIgnoredNegTruePositive()), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        drawLine(prevPoints[5].x, prevPoints[5].y, loAxisX, stat.getAvgHypoIgnoredNegTruePositive(), sttIdx, ++curveIndex);
                    }


                    prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getAvgHypoFluct());
                    prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getAvgHypoSuspectDiagnosis());
                    prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getAvgHypoIgnoredPos());
                    prevPoints[3].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getAvgHypoIgnoredNeg());
                    prevPoints[4].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getAvgHypoIgnoredPosTruePositive());
                    prevPoints[5].y = (int) (0.1 * _vs * statistics[sttIdx].getStatisticsHypo().getAvgHypoIgnoredNegTruePositive());
                    prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = prevPoints[3].x = prevPoints[4].x = prevPoints[5].x = loAxisX;

                    loAxisX += _hs;

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }

        if (showChartsFlag[1]) {
            prepareChartPosition(1);

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


                for (int sttIdx = 1, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {

                    WorldStatisticsHypo stat = statistics[sttIdx].getStatisticsHypo();
                    int curveIndex = 5;


                    if (showLineChartsFlag[0]) {
                        propPos = stat.getAvgIgnoredPosProportionToFluct1000();

                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * propPos), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, propPos, sttIdx, ++curveIndex);
                    }
                    if (showLineChartsFlag[1]) {
                        propNeg = stat.getAvgIgnoredNegProportionToFluct1000();
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * propNeg), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, propNeg, sttIdx, ++curveIndex);
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
