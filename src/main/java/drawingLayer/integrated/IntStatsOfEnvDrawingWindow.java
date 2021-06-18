package drawingLayer.integrated;

import drawingLayer.DrawingWindow;
import systemLayer.World;
import utils.Config;
import utils.Globals;
import utils.Point;
import utils.profiler.SimulationConfigBunch;
import utils.statistics.EpisodeStatistics;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class IntStatsOfEnvDrawingWindow extends DrawingWindow {

    private World worlds[];
    private SimulationConfigBunch configBunch;

    //============================//============================  panning params

    public IntStatsOfEnvDrawingWindow(World worlds[], SimulationConfigBunch configBunch) {
        super();
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[2];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, "Integrated Statistics of Environment")) {
            return;
        }

        int allAgentsInTarget = worlds[simulationTimer].getWdStatistics()[worldTimer].getAllAgentsInTarget();
        printStatsInfo(1, "Agents In Targets", allAgentsInTarget, "%" + 100 * (float) allAgentsInTarget / worlds[simulationTimer].getAgentsCount(), Color.GREEN);

        int allAgentsInPitfall = worlds[simulationTimer].getWdStatistics()[worldTimer].getAllAgentsInPitfall();
        printStatsInfo(2, "Agents In Pitfall", allAgentsInPitfall, "%" + 100 * (float) allAgentsInPitfall / worlds[simulationTimer].getAgentsCount(), Color.RED);

        //============================//============================ INFO
        for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
            World world = worlds[j];

            if (j > Globals.SIMULATION_TIMER || world == null) {
                break;
            }

            //============================
            int y = 40 * j + 220;
            g.setColor(Color.YELLOW);
            g.drawString("Sim " + j + " |", 80, y);
            //============================
            drawCurve(200, y, Color.GREEN, j, 10, 0);
            g.drawString("AgentsInTarget", 220, y);
            //============================
            drawCurve(500, y, Color.RED, j, 10, 0);
            g.drawString("AgentsInPitfall", 520, y);
            //============================
            g.setColor(Globals.Color$.lightGray);
            g.drawString("|>  " + configBunch.getByIndex(j).getInfo(), 800, y);
            //============================
        }

        //============================//============================//============================ Diagram drawing

        //============================ Translate
        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
        g.scale(scale, -scale);
        g.translate(100, -getHeight() / scale + 100);

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
            World world = worlds[j];

            if (j > Globals.SIMULATION_TIMER || world == null) {
                break;
            }

            axisX = j;
            axisY = 0;

            worldTimer = j < Globals.SIMULATION_TIMER ? Config.WORLD_LIFE_TIME : Globals.WORLD_TIMER;

            WorldStatistics[] statistics = world.getWdStatistics();
            for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                WorldStatistics stat = statistics[i];

                if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                    axisX += 8;
                    prevPoints[0].y = stat.getAllAgentsInTarget();
                    prevPoints[1].y = statistics[i].getAllAgentsInPitfall();
                    prevPoints[0].x = prevPoints[1].x = axisX;

                } else {

                    prevPoints[0].y = statistics[i - 1].getAllAgentsInTarget();
                    prevPoints[1].y = statistics[i - 1].getAllAgentsInPitfall();
                    prevPoints[0].x = prevPoints[1].x = axisX;
                    axisX += 8;
                }

                drawCurve(axisX, stat.getAllAgentsInTarget(), Color.GREEN, j, i);
                g.drawLine(prevPoints[0].x, prevPoints[0].y, axisX, stat.getAllAgentsInTarget());

                drawCurve(axisX, stat.getAllAgentsInPitfall(), Color.RED, j, i);
                g.drawLine(prevPoints[1].x, prevPoints[1].y, axisX, stat.getAllAgentsInPitfall());

            }
        }
        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);


        //============================//============================//============================ Episode Drawing

        g.translate(0, -1200);
        g.setColor(Color.ORANGE);
        g.drawLine(0, 0, getRealWith(), 0);


        for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
            World world = worlds[j];

            if (j > Globals.SIMULATION_TIMER || world == null) {
                break;
            }

            axisX = j;
            axisY = 0;

            worldTimer = j < Globals.SIMULATION_TIMER ? world.getEpStatistics().length : Globals.EPISODE - 1;

            EpisodeStatistics[] statistics = world.getEpStatistics();
            for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                EpisodeStatistics stat = statistics[i];

                if (stat.getToTime() == 0) {
                    break;
                }

                if (i > 0) {
                    prevPoints[0].x = prevPoints[1].x = axisX;
                    prevPoints[0].y = statistics[i - 1].getMidAgentsInTarget();
                    prevPoints[1].y = statistics[i - 1].getMidAgentsInPitfall();
                    axisX += 100;

                } else {
                    axisX += 100;
                    prevPoints[0].x = prevPoints[1].x = axisX;
                    prevPoints[0].y = stat.getMidAgentsInTarget();
                    prevPoints[1].y = stat.getMidAgentsInPitfall();
                }

                drawCurve(axisX, stat.getMidAgentsInTarget(), Color.GREEN, j, 20, i);
                g.drawLine(prevPoints[0].x, prevPoints[0].y, axisX, stat.getMidAgentsInTarget());

                drawCurve(axisX, stat.getMidAgentsInPitfall(), Color.RED, j, 20, i);
                g.drawLine(prevPoints[1].x, prevPoints[1].y, axisX, stat.getMidAgentsInPitfall());

            }
        }


    }
}
