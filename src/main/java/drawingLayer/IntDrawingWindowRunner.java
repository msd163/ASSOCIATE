package drawingLayer;

import drawingLayer.integrated.*;
import simulateLayer.SimulationConfig;
import societyLayer.agentSubLayer.World;
import utils.Config;
import utils.Globals;

import javax.swing.*;
import java.awt.*;

public class IntDrawingWindowRunner extends Thread {


    World[] worlds;
    private SimulationConfig simulationConfig;

    private IntTravelStatsLinearDrawingWindow intTravelStatsLinearDrawingWindow;
    private IntTrustAnalysisLinearDrawingWindow intTrustAnalysisLinearDrawingWindow;
    private IntTrustStatsLinearDrawingWindow intTrustStatsLinearDrawingWindow;
    private IntResistancePerNumberStatsLinearDrawingWindow intResistancePerNumberStatsLinearDrawingWindow;
    private IntRocPointDrawingWindow intRocPointDrawingWindow;
    private IntHonestCollaborationLineDrawingWindow intHonestCollaborationLineDrawingWindow;
    private IntHypocriteCollaborationLineDrawingWindow intHypocriteCollaborationLineDrawingWindow;
    private IntResistanceStatsLinearDrawingWindow intResistanceStatsLinearDrawingWindow;
    private IntFluctuationStatsLinearDrawingWindow intFluctuationStatsLinearDrawingWindow;


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
            if (Config.INT_DRAWING_SHOW_IntResistancePerNumberLinearDrawingWindow) {
                intResistancePerNumberStatsLinearDrawingWindow.repaint();
            }
            if (Config.INT_DRAWING_SHOW_RocPointDrawingWindow) {
                intRocPointDrawingWindow.repaint();
            }
            if (Config.INT_DRAWING_SHOW_HonestCollaborationLinearDrawingWindow) {
                intHonestCollaborationLineDrawingWindow.repaint();
            }
            if (Config.INT_DRAWING_SHOW_HypocriteCollaborationLinearDrawingWindow) {
                intHypocriteCollaborationLineDrawingWindow.repaint();
            }
            if (Config.INT_DRAWING_SHOW_ResistanceLinearDrawingWindow) {
                intResistanceStatsLinearDrawingWindow.repaint();
            }
            if (Config.INT_DRAWING_SHOW_FluctuationLinearDrawingWindow) {
                intFluctuationStatsLinearDrawingWindow.repaint();
            }

            if (Globals.PAUSE) {
                if (Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING_IN_PAUSE > 0) {
                    try {
                        Thread.sleep(Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING_IN_PAUSE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING > 0) {
                    try {
                        Thread.sleep(Config.WORLD_SLEEP_MILLISECOND_FOR_DRAWING);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
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

        if (Config.INT_DRAWING_SHOW_IntResistancePerNumberLinearDrawingWindow) {
            intResistancePerNumberStatsLinearDrawingWindow = new IntResistancePerNumberStatsLinearDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intResistancePerNumberStatsLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_RocPointDrawingWindow) {
            intRocPointDrawingWindow = new IntRocPointDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intRocPointDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_HonestCollaborationLinearDrawingWindow) {
            intHonestCollaborationLineDrawingWindow = new IntHonestCollaborationLineDrawingWindow(worlds);
            initDrawingWindow(intHonestCollaborationLineDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_HypocriteCollaborationLinearDrawingWindow) {
            intHypocriteCollaborationLineDrawingWindow = new IntHypocriteCollaborationLineDrawingWindow(worlds);
            initDrawingWindow(intHypocriteCollaborationLineDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_ResistanceLinearDrawingWindow) {
            intResistanceStatsLinearDrawingWindow = new IntResistanceStatsLinearDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intResistanceStatsLinearDrawingWindow, widthHalf, heightHalf);
        }


        if (Config.INT_DRAWING_SHOW_FluctuationLinearDrawingWindow) {
            intFluctuationStatsLinearDrawingWindow = new IntFluctuationStatsLinearDrawingWindow(worlds, simulationConfig);
            initDrawingWindow(intFluctuationStatsLinearDrawingWindow, widthHalf, heightHalf);
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

    public IntResistancePerNumberStatsLinearDrawingWindow getIntEffectiveFlucStatsLinearDrawingWindow() {
        return intResistancePerNumberStatsLinearDrawingWindow;
    }

    public IntRocPointDrawingWindow getIntRocPointDrawingWindow() {
        return intRocPointDrawingWindow;
    }
}
