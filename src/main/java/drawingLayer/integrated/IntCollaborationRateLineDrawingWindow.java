package drawingLayer.integrated;

import drawingLayer.DrawingWindow;
import simulateLayer.SimulationConfig;
import simulateLayer.statistics.WorldStatistics;
import societyLayer.agentSubLayer.World;
import utils.Config;
import utils.Globals;
import utils.Point;

import java.awt.*;

public class IntCollaborationRateLineDrawingWindow extends DrawingWindow {

    private SimulationConfig configBunch;

    //============================//============================  panning params

    public IntCollaborationRateLineDrawingWindow(World worlds[], SimulationConfig configBunch) {
        super(worlds.length);
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[7];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated COLLAB [#Worlds: " + worlds.length + "]";
        setName("i_coll_stt");

        axisYScale = 1;
        _vs = 60;
        _hs = 8;
    }

    @Override
    public void resetParams() {
        super.resetParams();

        axisYScale = 1;
        _vs = 60;
        _hs = 8;
    }

    int loAxisX;

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        printStatsInfo(1, "# Collab (Honest)", worlds[simulationTimer].getWdStatistics()[worldTimer].getHonestCollaboration(), Globals.Color$.darkGreen);


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
                        drawCurve(200, dynamicHeight, Globals.Color$.arr[worldIdx], worldIdx, 20, -1);
                        g.drawString("Honest", 220, dynamicHeight);
                        //============================
                    }
                }
                //============================
                g.setColor(Globals.Color$.$configTitle);
                g.drawString("|>  " + worlds[worldIdx].getSimulationConfigInfo(), 500, dynamicHeight);
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


                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getHonestCollaboration());


                prevPoints[0].y = (int) (0.1 * _vs * statistics[0].getHonestCollaboration());
                prevPoints[0].x = loAxisX;


                for (int sttIdx = 1, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {

                    int number = statistics[sttIdx].getAvgHonestCollaboration();
                        if (showLineChartsFlag[0]) {
                            drawCurve(loAxisX, (int) (0.1 * _vs * number), Globals.Color$.arr[worldIdx], worldIdx, sttIdx);
                            if (prevPoints[0].y >= 0) {
                                drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, number);
                            }
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
