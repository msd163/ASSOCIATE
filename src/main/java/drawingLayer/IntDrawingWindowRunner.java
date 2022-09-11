package drawingLayer;

import drawingLayer.integrated.*;
import simulateLayer.SimulationConfig;
import societyLayer.agentSubLayer.World;
import utils.Config;

import javax.swing.*;
import java.awt.*;

public class IntDrawingWindowRunner extends Thread {


    World[] worlds;
    private SimulationConfig simulationConfig;

    private IntTravelStatsLinearDrawingWindow intTravelStatsLinearDrawingWindow;
    private IntTrustAnalysisLinearDrawingWindow intTrustAnalysisLinearDrawingWindow;
    private IntTrustStatsLinearDrawingWindow intTrustStatsLinearDrawingWindow;
    private IntEffectiveFlucStatsLinearDrawingWindow intEffectiveFlucStatsLinearDrawingWindow;
    private IntRocPointDrawingWindow intRocPointDrawingWindow;
    private IntCollaborationRateLineDrawingWindow intCollaborationRateLineDrawingWindow;


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
            if (Config.INT_DRAWING_SHOW_IntEffectiveFlucLinearDrawingWindow) {
                intEffectiveFlucStatsLinearDrawingWindow.repaint();
            }
            if (Config.INT_DRAWING_SHOW_RocPointDrawingWindow) {
                intRocPointDrawingWindow.repaint();
            }
            if (Config.INT_DRAWING_SHOW_CollaborationLinearDrawingWindow) {
                intCollaborationRateLineDrawingWindow.repaint();
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

        if (Config.INT_DRAWING_SHOW_IntEffectiveFlucLinearDrawingWindow) {
            intEffectiveFlucStatsLinearDrawingWindow = new IntEffectiveFlucStatsLinearDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intEffectiveFlucStatsLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_RocPointDrawingWindow) {
            intRocPointDrawingWindow = new IntRocPointDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intRocPointDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_CollaborationLinearDrawingWindow) {
            intCollaborationRateLineDrawingWindow = new IntCollaborationRateLineDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intCollaborationRateLineDrawingWindow, widthHalf, heightHalf);
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

    public IntEffectiveFlucStatsLinearDrawingWindow getIntEffectiveFlucStatsLinearDrawingWindow() {
        return intEffectiveFlucStatsLinearDrawingWindow;
    }

    public IntRocPointDrawingWindow getIntRocPointDrawingWindow() {
        return intRocPointDrawingWindow;
    }
}
