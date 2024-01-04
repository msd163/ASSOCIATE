package SiM.monitor;

import SiM.monitor.dag.IntDagStatLinearDrawingWindow;
import SiM.monitor.integrated.*;
import core.config.trust.TrustConfig;
import WSM.society.agent.World;
import core.utils.Config;
import core.utils.Globals;

import javax.swing.*;
import java.awt.*;

public class IntDrawingWindowRunner extends Thread {


    World[] worlds;
    private TrustConfig trustConfig;

    private IntTravelStatsLinearDrawingWindow intTravelStatsLinearDrawingWindow;
    private IntTrustAnalysisLinearDrawingWindow intTrustAnalysisLinearDrawingWindow;
    private IntTrustStatsLinearDrawingWindow intTrustStatsLinearDrawingWindow;
    private IntTrustRecogniseLinearDrawingWindow intTrustRecogniseLinearDrawingWindow;
    private IntResistancePerNumberStatsLinearDrawingWindow intResistancePerNumberStatsLinearDrawingWindow;
    private IntRocPointDrawingWindow intRocPointDrawingWindow;
    private IntHonestCollaborationLineDrawingWindow intHonestCollaborationLineDrawingWindow;
    private IntDishonestCollaborationLineDrawingWindow intDishonestCollaborationLineDrawingWindow;
    private IntHypocriteCollaborationLineDrawingWindow intHypocriteCollaborationLineDrawingWindow;
    private IntResistanceStatsLinearDrawingWindow intResistanceStatsLinearDrawingWindow;
    private IntFluctuationStatsLinearDrawingWindow intFluctuationStatsLinearDrawingWindow;
    private IntDominanceCollaborationLineDrawingWindow intDominanceCollaborationLineDrawingWindow;
    private IntDagStatLinearDrawingWindow intDagStatLinearDrawingWindow;


    public IntDrawingWindowRunner(World[] worlds, TrustConfig trustConfig) {
        this.worlds = worlds;
        this.trustConfig = trustConfig;
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
            if (Config.INT_DRAWING_SHOW_IntTrustRecogniseLinearDrawingWindow) {
                intTrustRecogniseLinearDrawingWindow.repaint();
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
            if (Config.INT_DRAWING_SHOW_DishonestCollaborationLinearDrawingWindow) {
                intDishonestCollaborationLineDrawingWindow.repaint();
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
            if (Config.INT_DRAWING_SHOW_DominanceLinearDrawingWindow) {
                intDominanceCollaborationLineDrawingWindow.repaint();
            }
            if (Config.INT_DRAWING_SHOW_DagStatLinearDrawingWindow) {
                intDagStatLinearDrawingWindow.repaint();
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
        statsFrame.setVisible(true);

        if (Config.DRAWING_WINDOWS_MAXIMIZING) {
            statsFrame.setMinimumSize(new Dimension(2 * widthHalf, 2 * heightHalf));
            statsFrame.setLocation(0, 0);
        } else {
            statsFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
            statsFrame.setLocation(0, heightHalf);
        }

        statsFrame.setTitle(drawingWindow.getHeaderTitle());
    }

    public void initDrawingWindows() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthHalf = (int) screenSize.getWidth() / 2;
        int heightHalf = (int) screenSize.getHeight() / 2;

        //============================ Initializing Diagram Drawing Windows
        if (Config.INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow) {
            intTravelStatsLinearDrawingWindow = new IntTravelStatsLinearDrawingWindow(worlds, trustConfig);
            initDrawingWindow(intTravelStatsLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow) {
            intTrustAnalysisLinearDrawingWindow = new IntTrustAnalysisLinearDrawingWindow(worlds, trustConfig);
            initDrawingWindow(intTrustAnalysisLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_IntTrustStatsLinearDrawingWindow) {
            intTrustStatsLinearDrawingWindow = new IntTrustStatsLinearDrawingWindow(worlds, trustConfig);
            initDrawingWindow(intTrustStatsLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_IntTrustRecogniseLinearDrawingWindow) {
            intTrustRecogniseLinearDrawingWindow = new IntTrustRecogniseLinearDrawingWindow(worlds, trustConfig);
            initDrawingWindow(intTrustRecogniseLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_RocPointDrawingWindow) {
            intRocPointDrawingWindow = new IntRocPointDrawingWindow(worlds, trustConfig);
            initDrawingWindow(intRocPointDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_HonestCollaborationLinearDrawingWindow) {
            intHonestCollaborationLineDrawingWindow = new IntHonestCollaborationLineDrawingWindow(worlds);
            initDrawingWindow(intHonestCollaborationLineDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_DishonestCollaborationLinearDrawingWindow) {
            intDishonestCollaborationLineDrawingWindow = new IntDishonestCollaborationLineDrawingWindow(worlds);
            initDrawingWindow(intDishonestCollaborationLineDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_HypocriteCollaborationLinearDrawingWindow) {
            intHypocriteCollaborationLineDrawingWindow = new IntHypocriteCollaborationLineDrawingWindow(worlds);
            initDrawingWindow(intHypocriteCollaborationLineDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_IntResistancePerNumberLinearDrawingWindow) {
            intResistancePerNumberStatsLinearDrawingWindow = new IntResistancePerNumberStatsLinearDrawingWindow(worlds, trustConfig);
            initDrawingWindow(intResistancePerNumberStatsLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_ResistanceLinearDrawingWindow) {
            intResistanceStatsLinearDrawingWindow = new IntResistanceStatsLinearDrawingWindow(worlds, trustConfig);
            initDrawingWindow(intResistanceStatsLinearDrawingWindow, widthHalf, heightHalf);
        }


        if (Config.INT_DRAWING_SHOW_FluctuationLinearDrawingWindow) {
            intFluctuationStatsLinearDrawingWindow = new IntFluctuationStatsLinearDrawingWindow(worlds, trustConfig);
            initDrawingWindow(intFluctuationStatsLinearDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_DominanceLinearDrawingWindow) {
            intDominanceCollaborationLineDrawingWindow = new IntDominanceCollaborationLineDrawingWindow(worlds);
            initDrawingWindow(intDominanceCollaborationLineDrawingWindow, widthHalf, heightHalf);
        }

        if (Config.INT_DRAWING_SHOW_DagStatLinearDrawingWindow) {
            intDagStatLinearDrawingWindow = new IntDagStatLinearDrawingWindow(worlds, trustConfig);
            initDrawingWindow(intDagStatLinearDrawingWindow, widthHalf, heightHalf);
        }

    }


    //============================//============================//============================


    public IntTravelStatsLinearDrawingWindow getIntTravelStatsLinearDrawingWindow() {
        return intTravelStatsLinearDrawingWindow;
    }

    public IntDagStatLinearDrawingWindow getIntDagStatLinearDrawingWindow() {
        return intDagStatLinearDrawingWindow;
    }

    public IntTrustAnalysisLinearDrawingWindow getIntTrustAnalysisLinearDrawingWindow() {
        return intTrustAnalysisLinearDrawingWindow;
    }

    public IntTrustStatsLinearDrawingWindow getIntTrustStatsLinearDrawingWindow() {
        return intTrustStatsLinearDrawingWindow;
    }

    public IntTrustRecogniseLinearDrawingWindow getIntTrustRecogniseLinearDrawingWindow() {
        return intTrustRecogniseLinearDrawingWindow;
    }

    public IntResistancePerNumberStatsLinearDrawingWindow getIntEffectiveFlucStatsLinearDrawingWindow() {
        return intResistancePerNumberStatsLinearDrawingWindow;
    }

    public IntRocPointDrawingWindow getIntRocPointDrawingWindow() {
        return intRocPointDrawingWindow;
    }
}
