package drawingLayer.integrated;

import _type.TtSimulationMode;
import drawingLayer.DrawingWindow;
import simulateLayer.SimulationConfig;
import systemLayer.World;
import utils.Config;
import utils.Globals;
import utils.Point;
import utils.statistics.EpisodeStatistics;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class IntTravelStatsLinearDrawingWindow extends DrawingWindow {

    private World worlds[];
    private SimulationConfig configBunch;

    //============================//============================  panning params

    public IntTravelStatsLinearDrawingWindow(World worlds[], SimulationConfig configBunch) {
        super();
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[2];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
    }

    int loAxisX = 0;

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, "Integrated Statistics of Environment", null)) {
            return;
        }

        int ittAgentsInTarget = worlds[simulationTimer].getWdStatistics()[worldTimer].getIttAgentsInTarget();
        printStatsInfo(1, "Agents In Targets", ittAgentsInTarget, "%" + 100 * (float) ittAgentsInTarget / worlds[simulationTimer].getAgentsCount(), Color.GREEN);

        int ittAgentsInPitfall = worlds[simulationTimer].getWdStatistics()[worldTimer].getIttAgentsInPitfall();
        printStatsInfo(2, "Agents In Pitfall", ittAgentsInPitfall, "%" + 100 * (float) ittAgentsInPitfall / worlds[simulationTimer].getAgentsCount(), Color.RED);


        printStatsInfo(4, "Avg Agents In Targets", worlds[simulationTimer].getWdStatistics()[worldTimer].getAllAgentsInTarget() / worldTimer, Color.GREEN);
        printStatsInfo(5, "Avg Agents In Pitfall", worlds[simulationTimer].getWdStatistics()[worldTimer].getAllAgentsInPitfall() / worldTimer, Color.RED);


        //============================//============================ INFO
        for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
            World world = worlds[j];

            if (j > Globals.SIMULATION_TIMER || world == null) {
                break;
            }

            //============================
            int y = 40 * j + 340;
            g.setColor(Color.YELLOW);
            g.drawString("Sim " + j + " |", 80, y);
            //============================
            drawCurve(200, y, Color.GREEN, j, 20, -1);
            g.drawString("AgentsInTarget", 220, y);
            //============================
            drawCurve(500, y, Color.RED, j, 20, -1);
            g.drawString("AgentsInPitfall", 520, y);
            //============================
            g.setColor(Globals.Color$.lightGray);
            g.drawString("|>  " + configBunch.getByIndex(j).getInfo(), 800, y);
            //============================
        }

        //============================//============================//============================ Diagram drawing

        //============================ Translate
        reverseNormalizeCoordination();

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
            World world = worlds[j];

            if (j > Globals.SIMULATION_TIMER || world == null) {
                break;
            }

            loAxisX = j;
            axisY = 0;

            worldTimer = j < Globals.SIMULATION_TIMER ? Config.WORLD_LIFE_TIME : Globals.WORLD_TIMER;

            WorldStatistics[] statistics = world.getWdStatistics();
            for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                WorldStatistics stat = statistics[i];

                if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                    loAxisX += 8;
                    prevPoints[0].y = stat.getIttAgentsInTarget();
                    prevPoints[1].y = statistics[i].getIttAgentsInPitfall();
                    prevPoints[0].x = prevPoints[1].x = loAxisX;

                } else {

                    prevPoints[0].y = statistics[i - 1].getIttAgentsInTarget();
                    prevPoints[1].y = statistics[i - 1].getIttAgentsInPitfall();
                    prevPoints[0].x = prevPoints[1].x = loAxisX;
                    loAxisX += 8;
                }

                drawCurve(loAxisX, stat.getIttAgentsInTarget(), Color.GREEN, j, i);
                g.drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getIttAgentsInTarget());

                drawCurve(loAxisX, stat.getIttAgentsInPitfall(), Color.RED, j, i);
                g.drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getIttAgentsInPitfall());

                if (axisX < loAxisX) {
                    axisX = loAxisX;
                }
            }

        }
        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

        //============================//============================//============================ Timed Average Chart

        g.translate(0, -700);
        loAxisX = 0;

        for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
            World world = worlds[j];

            if (j > Globals.SIMULATION_TIMER || world == null) {
                break;
            }

            loAxisX = j;
            axisY = 0;

            worldTimer = j < Globals.SIMULATION_TIMER ? Config.WORLD_LIFE_TIME : Globals.WORLD_TIMER;

            WorldStatistics[] statistics = world.getWdStatistics();
            for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                WorldStatistics stat = statistics[i];

                if (i == 0 || stat.getEpisode() != statistics[i - 1].getEpisode()) {
                    loAxisX += 8;
                    prevPoints[0].y = stat.getTimedAvgAgentTarget();
                    prevPoints[1].y = stat.getTimedAvgAgentInPitfall();
                    prevPoints[0].x = prevPoints[1].x = loAxisX;

                } else {

                    prevPoints[0].y = statistics[i - 1].getTimedAvgAgentTarget();
                    prevPoints[1].y = statistics[i - 1].getTimedAvgAgentInPitfall();
                    prevPoints[0].x = prevPoints[1].x = loAxisX;
                    loAxisX += 8;
                }

                drawCurve(loAxisX, stat.getTimedAvgAgentTarget(), Color.GREEN, j, i);
                g.drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getTimedAvgAgentTarget());

                drawCurve(loAxisX, stat.getTimedAvgAgentInPitfall(), Color.RED, j, i);
                g.drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getTimedAvgAgentInPitfall());

                if (axisX < loAxisX) {
                    axisX = loAxisX;
                }
            }
        }
        //============================//============================ Draw X-axis line
        g.setColor(Color.CYAN);
        g.drawLine(0, 0, getRealWith(), 0);


        //============================//============================//============================ Episode Drawing
        if (Config.SIMULATION_MODE == TtSimulationMode.Episodic) {

            g.translate(0, -1200);
            g.setColor(Color.ORANGE);
            g.drawLine(0, 0, getRealWith(), 0);


            for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
                World world = worlds[j];

                if (j > Globals.SIMULATION_TIMER || world == null) {
                    break;
                }

                loAxisX = j;
                axisY = 0;

                worldTimer = j < Globals.SIMULATION_TIMER ? world.getEpStatistics().length : Globals.EPISODE - 1;

                EpisodeStatistics[] statistics = world.getEpStatistics();
                for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                    EpisodeStatistics stat = statistics[i];

                    if (stat.getToTime() == 0) {
                        break;
                    }

                    if (i > 0) {
                        prevPoints[0].x = prevPoints[1].x = loAxisX;
                        prevPoints[0].y = statistics[i - 1].getMidAgentsInTarget();
                        prevPoints[1].y = statistics[i - 1].getMidAgentsInPitfall();
                        loAxisX += 100;

                    } else {
                        loAxisX += 100;
                        prevPoints[0].x = prevPoints[1].x = loAxisX;
                        prevPoints[0].y = stat.getMidAgentsInTarget();
                        prevPoints[1].y = stat.getMidAgentsInPitfall();
                    }

                    drawCurve(loAxisX, stat.getMidAgentsInTarget(), Color.GREEN, j, 20, i);
                    g.drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getMidAgentsInTarget());

                    drawCurve(loAxisX, stat.getMidAgentsInPitfall(), Color.RED, j, 20, i);
                    g.drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getMidAgentsInPitfall());

                }
            }
        }
    }
}
