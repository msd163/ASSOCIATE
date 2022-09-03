package drawingLayer;

import drawingLayer.integrated.IntTravelStatsLinearDrawingWindow;
import drawingLayer.integrated.IntTrustAnalysisLinearDrawingWindow;
import drawingLayer.integrated.IntTrustStatsLinearDrawingWindow;
import simulateLayer.SimulationConfig;
import societyLayer.agentSubLayer.World;
import trustLayer.TrustMatrix;
import utils.Config;

import javax.swing.*;
import java.awt.*;

public class IntDrawingWindowRunner extends Thread {


    World[] worlds;
    private SimulationConfig simulationConfig;

    private IntTravelStatsLinearDrawingWindow intTravelStatsLinearDrawingWindow;
    private IntTrustAnalysisLinearDrawingWindow intTrustAnalysisLinearDrawingWindow;
    private IntTrustStatsLinearDrawingWindow intTrustStatsLinearDrawingWindow;


    public IntDrawingWindowRunner(World[] worlds, SimulationConfig simulationConfig) {
        this.worlds = worlds;
        this.simulationConfig = simulationConfig;
    }

    @Override
    public void run() {
        while (true) {

            if (Config.INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow) {
                intTravelStatsLinearDrawingWindow.repaint();
            }
            if (Config.INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow) {
                intTrustAnalysisLinearDrawingWindow.repaint();
            }
            if (Config.INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow) {
                intTrustStatsLinearDrawingWindow.repaint();
            }

           /* try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }


    //============================//============================//============================


    private void initDrawingWindow(DrawingWindow drawingWindow, int widthHalf, int heightHalf) {
        drawingWindow.setDoubleBuffered(true);
        JFrame statsFrame = new JFrame();

        statsFrame.getContentPane().add(drawingWindow);
        statsFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
        statsFrame.setVisible(true);
        statsFrame.setLocation(0, heightHalf);
        statsFrame.setTitle(drawingWindow.getHeaderTitle());
    }

    public void initDrawingWindows() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthHalf = (int) screenSize.getWidth() / 2;
        int heightHalf = (int) screenSize.getHeight() / 2;

        //============================ Initializing Diagram Drawing Windows
        if (Config.INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow) {
            intTravelStatsLinearDrawingWindow = new IntTravelStatsLinearDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intTravelStatsLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow) {
            intTrustAnalysisLinearDrawingWindow = new IntTrustAnalysisLinearDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intTrustAnalysisLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow) {
            intTrustStatsLinearDrawingWindow = new IntTrustStatsLinearDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intTrustStatsLinearDrawingWindow, widthHalf, heightHalf);
        }

    }


    //============================//============================//============================


    public IntTravelStatsLinearDrawingWindow getIntTravelStatsLinearDrawingWindow() {
        return intTravelStatsLinearDrawingWindow;
    }

    public IntTrustAnalysisLinearDrawingWindow getIntTrustAnalysisLinearDrawingWindow() {
        return intTrustAnalysisLinearDrawingWindow;
    }

    public IntTrustStatsLinearDrawingWindow getIntTrustStatsLinearDrawingWindow() {
        return intTrustStatsLinearDrawingWindow;
    }
}
