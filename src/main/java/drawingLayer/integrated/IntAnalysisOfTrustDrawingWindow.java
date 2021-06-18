package drawingLayer.integrated;

import drawingLayer.DrawingWindow;
import systemLayer.World;
import utils.Config;
import utils.Globals;
import utils.profiler.SimulationConfigBunch;
import utils.statistics.WorldStatistics;

import java.awt.*;

public class IntAnalysisOfTrustDrawingWindow extends DrawingWindow {

    private World worlds[];
    private SimulationConfigBunch configBunch;

    //============================//============================  panning params

    public IntAnalysisOfTrustDrawingWindow(World worlds[], SimulationConfigBunch configBunch) {
        super();
        this.worlds = worlds;
        this.configBunch = configBunch;
    }

    private int worldTimer;
    private int simulationTimer;

    @Override
    public void paint(Graphics gr) {

        worldTimer = Globals.WORLD_TIMER - 1;
        simulationTimer = Globals.SIMULATION_TIMER;

        if (worldTimer < 0) {
            return;
        }

        g = (Graphics2D) gr;
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, getWidth(), getHeight());
        pauseNotice(g);

        g.setColor(Color.YELLOW);

        //============================//============================ Translate for panning and scaling

        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));

        g.drawString("Simulation Time                  : " + simulationTimer, 100, 50);
        g.drawString("World Time                         : " + worldTimer, 100, 90);
        g.drawString("Episode                               : " + Globals.EPISODE, 100, 130);

        g.setColor(Color.GREEN);
        g.drawString("Accuracy (#of correct/#of all))               :   " + worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustAccuracy(), 800, 50);
        g.setColor(Color.YELLOW);
        g.drawString("Sensitivity (#of TP/#of all P)                  :   " + worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustSensitivity(), 800, 90);
        g.setColor(Color.PINK);
        g.drawString("Specificity (#of TN/#of all N)                  :   " + worlds[simulationTimer].getWdStatistics()[worldTimer].getTrustSpecificity(), 800, 130);

        g.setColor(Color.WHITE);
        g.drawString("[ X200 Scale ]", 800, 180);

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
            drawCurve( 200, y, Color.GREEN, j, 10);
            g.drawString("AgentsInTarget", 220, y);
            //============================
            drawCurve( 500, y, Color.RED, j, 10);
            g.drawString("AgentsInPitfall", 520, y);
            //============================
            g.setColor(Globals.Color$.lightGray);
            g.drawString("|>  " + configBunch.getByIndex(j).getInfo(), 800, y);
            //============================

        }

        //============================//============================//============================ Diagram drawing

        //============================ Draw mouse plus
        Point mousePoint = getMousePosition();
        if (mousePoint != null) {
            g.setColor(Color.WHITE);
            //-- (TOP-DOWN) Drawing vertical line for mouse pointer
            g.drawLine(mousePoint.x, 0, mousePoint.x, getHeight());
            //-- (LEFT-RIGHT) Drawing horizontal line for mouse pointer
            g.drawLine(0, mousePoint.y, getWidth(), mousePoint.y);
        }

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

            axisX = j * 4;
            axisY = 0;

            worldTimer = j < Globals.SIMULATION_TIMER ? Config.WORLD_LIFE_TIME : Globals.WORLD_TIMER;

            WorldStatistics[] statistics = world.getWdStatistics();
            for (int i = 0, statisticsLength = statistics.length; i < worldTimer && i < statisticsLength; i++) {
                WorldStatistics stat = statistics[i];
                axisX += 20;

                drawCurve( axisX, stat.getAllAgentsInTarget(), Color.GREEN, j, i);

                drawCurve( axisX, stat.getAllAgentsInPitfall(), Color.RED, j, i);

            }
        }
        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

        //============================//============================


    }
}
