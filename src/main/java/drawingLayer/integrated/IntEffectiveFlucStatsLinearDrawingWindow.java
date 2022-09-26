package drawingLayer.integrated;

import drawingLayer.DrawingWindow;
import simulateLayer.SimulationConfig;
import simulateLayer.statistics.WorldStatistics;
import societyLayer.agentSubLayer.World;
import utils.Config;
import utils.Globals;
import utils.Point;

import java.awt.*;

public class IntEffectiveFlucStatsLinearDrawingWindow extends DrawingWindow {

    private SimulationConfig configBunch;

    //============================//============================  panning params


    public IntEffectiveFlucStatsLinearDrawingWindow(World worlds[], SimulationConfig configBunch) {
        super(worlds.length);
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[7];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated Effective FluctuationParams [#Worlds: " + worlds.length + "]";
        setName("i_eff_fluc_stt");

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

        for (int i = 1; i < 7; i++) {
            printStatsInfo(i + 1, "# of Eff Fluc (" + i + ")", worlds[simulationTimer].getWdStatistics()[worldTimer].getAvgEffectiveFluctuationResistanceNumber()[i], Globals.Color$.arr()[i]);
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
                g.setColor(Globals.Color$.$simTitle);
                g.drawString("Sim " + (j + 1) + " |", 80, dynamicHeight);
                //============================

                if (showWorldsFlag[j]) {
                    if (showLineChartsFlag[0]) {
                        drawCurve(200, dynamicHeight, Globals.Color$.arr()[0], j, 20, -1);
                        g.drawString("DATA", 220, dynamicHeight);
                        //============================
                    }
                }
                //============================
                g.setColor(Globals.Color$.$configTitle);
                g.drawString("|>  " + worlds[j].getSimulationConfigInfo(), 700, dynamicHeight);
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

                for (int i = 0; i < 7; i++) {
                    maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getAvgEffectiveFluctuationResistanceNumber()[i]);
                }

                for (int idx = 0; idx < 7; idx++) {
                    prevPoints[idx].y = (int) (0.1 * _vs * statistics[0].getAvgEffectiveFluctuationResistanceNumber()[idx]);
                    prevPoints[idx].x = loAxisX;
                }

                for (int i = 1, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {

                    int[] number = statistics[i].getAvgEffectiveFluctuationResistanceNumber();
                    for (int idx = 1; idx < 7; idx++) {
                        if (showLineChartsFlag[idx]) {
                            drawCurve(loAxisX, (int) (0.1 * _vs * number[idx]), Globals.Color$.arr()[idx], j, i);
                            if (prevPoints[idx].y >= 0) {
                                drawLine(prevPoints[idx].x, prevPoints[idx].y, loAxisX, number[idx]);
                            }
                        }
                    }

                    for (int idx = 0; idx < 7; idx++) {
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
