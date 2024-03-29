package SiM.monitor;

import SiM.monitor.dag.DagGraphDrawingWindow;
import SiM.monitor.trust.*;
import core.enums.TtDrawingWindowLocation;
import SiM.monitor.routing.StateMachineDrawingWindow;
import SiM.monitor.routing.TravelHistoryBarDrawingWindow;
import SiM.monitor.routing.TravelStatsLinearDrawingWindow;
import WSM.World;
import WSM.trust.TrustMatrix;
import core.utils.Config;
import core.utils.Globals;

import javax.swing.*;
import java.awt.*;

public class DrawingWindowRunner extends Thread {


    private World world;
    private StateMachineDrawingWindow stateMachineDrawingWindow;
    private TravelStatsLinearDrawingWindow travelStatsLinearDrawingWindow;
    private TravelHistoryBarDrawingWindow travelHistoryBarDrawingWindow;

    private TrustMatrixDrawingWindow trustMatrixDrawingWindow;

    private TrustStatsLinearDrawingWindow trustStatsLinearDrawingWindow;
    private TrustRecogniseLinearDrawingWindow trustRecogniseLinearDrawingWindow;
    private TrustAnalysisLinearDrawingWindow trustAnalysisLinearDrawingWindow;

    private ExperienceBarDrawingWindow experienceBarDrawingWindow;
    private IndirectExperienceBarDrawingWindow indirectExperienceBarDrawingWindow;

    private ObservationBarDrawingWindow observationBarDrawingWindow;
    private IndirectObservationBarDrawingWindow indirectObservationBarDrawingWindow;

    private RecommendationBarDrawingWindow recommendationBarDrawingWindow;

    private DagGraphDrawingWindow dagGraphDrawingWindow;

    //==========================

    public DrawingWindowRunner(World world) {
        this.world = world;
    }

    @Override
    public void run() {
        while (true) {

            if (Config.DRAWING_SHOW_stateMachineWindow) {
                stateMachineDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_travelStatsLinearDrawingWindow) {
                travelStatsLinearDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_trustMatrixDrawingWindow) {
                trustMatrixDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_trustStatsLinearDrawingWindow) {
                trustStatsLinearDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_trustRecogniseLinearDrawingWindow) {
                trustRecogniseLinearDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_trustAnalysisLinearDrawingWindow) {
                trustAnalysisLinearDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_travelHistoryBarDrawingWindow) {
                travelHistoryBarDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_experienceBarDrawingWindow) {
                experienceBarDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_indirectExperienceBarDrawingWindow) {
                indirectExperienceBarDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_recommendationBarDrawingWindow) {
                recommendationBarDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_observationBarDrawingWindow) {
                observationBarDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_indirectObservationBarDrawingWindow) {
                indirectObservationBarDrawingWindow.repaint();
            }
            if (Config.DRAWING_SHOW_dagGraphDrawingWindow) {
                dagGraphDrawingWindow.repaint();
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

    private void initDrawingWindow(int widthHalf, int heightHalf, DrawingWindow stateMachineDW, TtDrawingWindowLocation location) {
        initDrawingWindow(widthHalf, heightHalf, stateMachineDW, location, false);
    }

    private void initDrawingWindow(int widthHalf, int heightHalf, DrawingWindow drawingWindow, TtDrawingWindowLocation location, boolean exitAppOnCLose) {
        drawingWindow.setDoubleBuffered(true);
        JFrame mainFrame = new JFrame();
        mainFrame.getContentPane().add(drawingWindow);
        if (Config.DRAWING_WINDOWS_MAXIMIZING) {
            mainFrame.setMinimumSize(new Dimension(2 * widthHalf, 2 * heightHalf));
            location = TtDrawingWindowLocation.TopLeft;
        } else {
            mainFrame.setMinimumSize(new Dimension(widthHalf, heightHalf));
        }
        mainFrame.setVisible(true);
        if (exitAppOnCLose) {
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        mainFrame.setTitle(drawingWindow.getHeaderTitle());
        switch (location) {
            case TopRight:
                mainFrame.setLocation(widthHalf, 0);
                break;
            case BottomLeft:
                mainFrame.setLocation(0, heightHalf);
                break;
            case BottomRight:
                mainFrame.setLocation(widthHalf, heightHalf);
                break;
            case TopLeft:
            default:
                mainFrame.setLocation(0, 0);
                break;
        }
    }

    public void initDrawingWindows(TrustMatrix matrixGenerator) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthHalf = (int) screenSize.getWidth() / 2;
        int heightHalf = (int) screenSize.getHeight() / 2;
        //============================ Initializing Main Drawing Windows
        stateMachineDrawingWindow = new StateMachineDrawingWindow(world);
        if (Config.DRAWING_SHOW_stateMachineWindow) {
            initDrawingWindow(widthHalf, heightHalf, stateMachineDrawingWindow, TtDrawingWindowLocation.TopLeft, true);
        }
        //============================ Initializing Diagram Drawing Windows
        travelStatsLinearDrawingWindow = new TravelStatsLinearDrawingWindow(world);
        if (Config.DRAWING_SHOW_travelStatsLinearDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, travelStatsLinearDrawingWindow, TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        trustMatrixDrawingWindow = new TrustMatrixDrawingWindow(matrixGenerator, world);
        if (Config.DRAWING_SHOW_trustMatrixDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, trustMatrixDrawingWindow, TtDrawingWindowLocation.TopLeft);
        }

        //============================ Initializing Diagram Drawing Windows
        trustStatsLinearDrawingWindow = new TrustStatsLinearDrawingWindow(world);
        if (Config.DRAWING_SHOW_trustStatsLinearDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, trustStatsLinearDrawingWindow, TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        trustRecogniseLinearDrawingWindow = new TrustRecogniseLinearDrawingWindow(world);
        if (Config.DRAWING_SHOW_trustRecogniseLinearDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, trustRecogniseLinearDrawingWindow, TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        trustAnalysisLinearDrawingWindow = new TrustAnalysisLinearDrawingWindow(world);
        if (Config.DRAWING_SHOW_trustAnalysisLinearDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, trustAnalysisLinearDrawingWindow, TtDrawingWindowLocation.TopRight);
        }

        //============================ Initializing Diagram Drawing Windows
        travelHistoryBarDrawingWindow = new TravelHistoryBarDrawingWindow(world);
        if (Config.DRAWING_SHOW_travelHistoryBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, travelHistoryBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Diagram Drawing Windows
        experienceBarDrawingWindow = new ExperienceBarDrawingWindow(world);
        if (Config.DRAWING_SHOW_experienceBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, experienceBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Diagram Drawing Windows
        indirectExperienceBarDrawingWindow = new IndirectExperienceBarDrawingWindow(world);
        if (Config.DRAWING_SHOW_indirectExperienceBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, indirectExperienceBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Recommendation Drawing Windows
        recommendationBarDrawingWindow = new RecommendationBarDrawingWindow(world);
        if (Config.DRAWING_SHOW_recommendationBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, recommendationBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Observation Drawing Windows
        observationBarDrawingWindow = new ObservationBarDrawingWindow(world);
        if (Config.DRAWING_SHOW_observationBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, observationBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }
        //============================ Initializing IndirectObservation Drawing Windows
        indirectObservationBarDrawingWindow = new IndirectObservationBarDrawingWindow(world);
        if (Config.DRAWING_SHOW_indirectObservationBarDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, indirectObservationBarDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }

        //============================ Initializing Dagra Drawing Windows
        dagGraphDrawingWindow = new DagGraphDrawingWindow(world);
        if (Config.DRAWING_SHOW_dagGraphDrawingWindow) {
            initDrawingWindow(widthHalf, heightHalf, dagGraphDrawingWindow, TtDrawingWindowLocation.BottomRight);
        }
    }


    //============================//============================//============================


    public StateMachineDrawingWindow getStateMachineDrawingWindow() {
        return stateMachineDrawingWindow;
    }

    public TravelStatsLinearDrawingWindow getTravelStatsLinearDrawingWindow() {
        return travelStatsLinearDrawingWindow;
    }

    public TravelHistoryBarDrawingWindow getTravelHistoryBarDrawingWindow() {
        return travelHistoryBarDrawingWindow;
    }

    public TrustMatrixDrawingWindow getTrustMatrixDrawingWindow() {
        return trustMatrixDrawingWindow;
    }

    public TrustStatsLinearDrawingWindow getTrustStatsLinearDrawingWindow() {
        return trustStatsLinearDrawingWindow;
    }

    public TrustRecogniseLinearDrawingWindow getTrustRecogniseLinearDrawingWindow() {
        return trustRecogniseLinearDrawingWindow;
    }

    public TrustAnalysisLinearDrawingWindow getTrustAnalysisLinearDrawingWindow() {
        return trustAnalysisLinearDrawingWindow;
    }

    public ExperienceBarDrawingWindow getExperienceBarDrawingWindow() {
        return experienceBarDrawingWindow;
    }

    public IndirectExperienceBarDrawingWindow getIndirectExperienceBarDrawingWindow() {
        return indirectExperienceBarDrawingWindow;
    }

    public ObservationBarDrawingWindow getObservationBarDrawingWindow() {
        return observationBarDrawingWindow;
    }

    public IndirectObservationBarDrawingWindow getIndirectObservationBarDrawingWindow() {
        return indirectObservationBarDrawingWindow;
    }

    public RecommendationBarDrawingWindow getRecommendationBarDrawingWindow() {
        return recommendationBarDrawingWindow;
    }
}
