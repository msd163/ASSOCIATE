package SiM.monitor.integrated;

import SiM.monitor.DrawingWindow;
import SiM.statistics.WorldStatistics;
import WSM.World;
import core.utils.Config;
import core.utils.Globals;
import core.utils.Point;

import java.awt.*;

public class IntHonestCollaborationLineDrawingWindow extends DrawingWindow {


    //============================//============================  panning params

    public IntHonestCollaborationLineDrawingWindow(World worlds[]) {
        super(worlds.length);
        this.worlds = worlds;
        this.prevPoints = new Point[7];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated Honest Collaboration [#Worlds: " + worlds.length + "]";
        setName("i_coll_hon");

        //axisYScale = 1;
        _vs = 60;
        _hs = 8;
    }

    @Override
    public void resetParams() {
        super.resetParams();

        //axisYScale = 1;
        _vs = 60;
        _hs = 8;
    }


    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        printStatsInfo(1, "# Trust to Honest In Round (Avg:" + Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION + ")", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsCollab().getAvgTrustToHonestInRound(), Globals.Color$.$curve_1);
        printStatsInfo(2, "# Honest Collab (Avg:" + Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION + ")", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsCollab().getAvgHonestCollaboration(), Globals.Color$.$curve_2);
        printStatsInfo(3, "# Honest Collab In Round (Avg:" + Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION + ")", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsCollab().getAvgHonestCollaborationInRound(), Globals.Color$.$curve_3);

        printStatsInfo(5, "# Trust Per Collab In Round (Avg:" + Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION + ")", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsCollab().getAvgHonestTrustPerCollab100(), Globals.Color$.$curve_4);


        //============================//============================ INFO
        dynamicHeight += 20;

        if (isShowSimInfo) {
            for (int worldIdx = 0, worldsLength = worlds.length; worldIdx < worldsLength; worldIdx++) {

                World world = worlds[worldIdx];

                if (worldIdx > Globals.SIMULATION_TIMER || world == null) {
                    break;
                }

                //============================
                dynamicHeight += 40;
                g.setColor(Globals.Color$.$simTitle);
                g.drawString("Sim " + (worldIdx + 1) + " |", 80, dynamicHeight);
                //============================

                if (showWorldsFlag[worldIdx]) {
                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(200, dynamicHeight, Globals.Color$.$curve_1, worldIdx, 20, -1);
                        g.drawString("T2Hon", 220, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(350, dynamicHeight, Globals.Color$.$curve_2, worldIdx, 20, -1);
                        g.drawString("HonCl", 370, dynamicHeight);
                        //============================
                    }
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(500, dynamicHeight, Globals.Color$.$curve_3, worldIdx, 20, -1);
                        g.drawString("HonClRn", 520, dynamicHeight);
                        //============================
                    }

                    if (showLineChartsFlag[3]) {
                        drawSymbolOnCurve(650, dynamicHeight, Globals.Color$.$curve_4, worldIdx, 20, -1);
                        g.drawString("TtPerColl", 670, dynamicHeight);
                        //============================
                    }
                }
                //============================
                g.setColor(Globals.Color$.$configTitle);
                g.drawString("|>  " + worlds[worldIdx].getSimulationConfigInfo(), 800, dynamicHeight);
                //============================
            }
        }
        //============================//============================//============================ Diagram drawing

        reverseNormalizeCoordination();

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        //============================//============================//============================ Numbers
        axisYScale = Config.STATISTICS_SCALE_UP_Y_AXIS_NUMBER;

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


                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsCollab().getAvgTrustToHonestInRound());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsCollab().getAvgHonestCollaboration());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsCollab().getAvgHonestCollaborationInRound());


                prevPoints[0].y = (int) (0.1 * _vs * statistics[0].getStatisticsCollab().getAvgTrustToHonestInRound());
                prevPoints[1].y = (int) (0.1 * _vs * statistics[0].getStatisticsCollab().getAvgHonestCollaboration());
                prevPoints[2].y = (int) (0.1 * _vs * statistics[0].getStatisticsCollab().getAvgHonestCollaborationInRound());
                prevPoints[0].x
                        = prevPoints[1].x
                        = prevPoints[2].x
                        = loAxisX;


                for (int sttIdx = 1, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {

                    int number = statistics[sttIdx].getStatisticsCollab().getAvgTrustToHonestInRound();
                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * number), Globals.Color$.$curve_1, worldIdx, sttIdx);
                        if (prevPoints[0].y >= 0) {
                            drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, number,sttIdx, 0);
                        }
                    }
                    prevPoints[0].y = (int) (0.1 * _vs * number);

                    number = statistics[sttIdx].getStatisticsCollab().getAvgHonestCollaboration();
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * number), Globals.Color$.$curve_2, worldIdx, sttIdx);
                        if (prevPoints[1].y >= 0) {
                            drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, number,sttIdx, 1);
                        }
                    }
                    prevPoints[1].y = (int) (0.1 * _vs * number);

                    number = statistics[sttIdx].getStatisticsCollab().getAvgHonestCollaborationInRound();
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * number), Globals.Color$.$curve_3, worldIdx, sttIdx);
                        if (prevPoints[2].y >= 0) {
                            drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, number,sttIdx, 2);
                        }
                    }

                    prevPoints[2].y = (int) (0.1 * _vs * number);
                    prevPoints[0].x
                            = prevPoints[1].x
                            = prevPoints[2].x = loAxisX;

                    loAxisX += _hs;

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }


        //============================//============================//============================ Proportion: TrustToHonest/HonestCollaboration
        axisYScale = 0.01;

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


                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getStatisticsCollab().getAvgHonestTrustPerCollab100());


                prevPoints[3].y = (int) (0.1 * _vs * statistics[0].getStatisticsCollab().getAvgHonestTrustPerCollab100());
                prevPoints[3].x
                        = loAxisX;


                for (int sttIdx = 1, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {


                    int number = statistics[sttIdx].getStatisticsCollab().getAvgHonestTrustPerCollab100();
                    if (showLineChartsFlag[3]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * number), Globals.Color$.color(worldIdx), worldIdx, sttIdx);
                        if (prevPoints[3].y >= 0) {
                            drawLine(prevPoints[3].x, prevPoints[3].y, loAxisX, number,sttIdx, 3);
                        }
                    }
                    prevPoints[3].y = (int) (0.1 * _vs * number);
                    prevPoints[3].x = loAxisX;

                    loAxisX += _hs;

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }
    }
}
