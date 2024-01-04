package SiM.monitor.integrated;

import SiM.monitor.DrawingWindow;
import SiM.statistics.WorldStatistics;
import WSM.World;
import core.utils.Config;
import core.utils.Globals;
import core.utils.Point;

import java.awt.*;

public class IntDominanceCollaborationLineDrawingWindow extends DrawingWindow {


    //============================//============================  panning params

    public IntDominanceCollaborationLineDrawingWindow(World worlds[]) {
        super(worlds.length);
        this.worlds = worlds;
        this.prevPoints = new Point[7];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated Dominance [#Worlds: " + worlds.length + "]";
        setName("i_dmn_stt");

        axisYScale = 0.01;
        _vs = 60;
        _hs = 8;
    }

    @Override
    public void resetParams() {
        super.resetParams();

        axisYScale = 0.01;
        _vs = 60;
        _hs = 8;
    }


    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        printStatsInfo(3, "# Dominance In Round (Avg:" + Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_DOMINANCE + ")", worlds[simulationTimer].getWdStatistics()[worldTimer].getStatisticsCollab().getAvgDominanceInRound100(), Globals.Color$.$curve_1);


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
                        g.drawString("Domin", 220, dynamicHeight);
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
        //axisYScale = 1;//Config.STATISTICS_SCALE_UP_Y_AXIS_NUMBER;

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


                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getStatisticsCollab().getAvgDominanceInRound100());
                minAxisY[0] = Math.min(minAxisY[0], statistics[worldTimer - 1].getStatisticsCollab().getAvgDominanceInRound100());


                prevPoints[0].y = (int) (0.1 * _vs * statistics[0].getStatisticsCollab().getAvgDominanceInRound100());
                prevPoints[0].x = loAxisX;

                for (int sttIdx = 1, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {

                    int number = statistics[sttIdx].getStatisticsCollab().getAvgDominanceInRound100();
                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * number), Globals.Color$.$curve_1, worldIdx, sttIdx);

                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, number, sttIdx, 0);

                    }
                    prevPoints[0].y = (int) (0.1 * _vs * number);
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
