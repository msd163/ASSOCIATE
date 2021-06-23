package utils;

import drawingLayer.*;
import drawingLayer.integrated.IntTrustAnalysisLinearDrawingWindow;
import drawingLayer.integrated.IntTravelStatsLinearDrawingWindow;
import drawingLayer.routing.TravelHistoryBarDrawingWindow;
import drawingLayer.routing.StateMachineDrawingWindow;
import drawingLayer.routing.TravelStatsLinearDrawingWindow;
import drawingLayer.trust.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageBuilder {


    public void generateStatisticsImages(StateMachineDrawingWindow stateMachineWindow,
                                         TravelStatsLinearDrawingWindow travelStatsLinearDrawingWindow,
                                         TrustMatrixDrawingWindow trustMatrixDrawingWindow,
                                         TrustStatsLinearDrawingWindow trustStatsLinearDrawingWindow,
                                         TrustRecogniseLinearDrawingWindow trustRecogniseLinearDrawingWindow,
                                         TrustAnalysisLinearDrawingWindow trustAnalysisLinearDrawingWindow,
                                         ObservationBarDrawingWindow observationBarDrawingWindow,
                                         RecommendationBarDrawingWindow recommendationBarDrawingWindow,
                                         TravelHistoryBarDrawingWindow travelHistoryBarDrawingWindow,
                                         ExperienceBarDrawingWindow experienceBarDrawingWindow,
                                         IndirectExperienceBarDrawingWindow indirectExperienceBarDrawingWindow,
                                         IndirectObservationBarDrawingWindow indirectObservationBarDrawingWindow
    ) {
        if (Config.DRAWING_SHOW_stateMachineWindow) {
            //generateStatisticsImage(stateMachineWindow);
        }
        if (Config.DRAWING_SHOW_travelStatsLinearDrawingWindow) {
            generateStatisticsImage(travelStatsLinearDrawingWindow);
        }
        if (Config.DRAWING_SHOW_trustMatrixDrawingWindow) {
            generateStatisticsImage(trustMatrixDrawingWindow);
        }
        if (Config.DRAWING_SHOW_trustStatsLinearDrawingWindow) {
            generateStatisticsImage(trustStatsLinearDrawingWindow);
        }
        if (Config.DRAWING_SHOW_trustRecogniseLinearDrawingWindow) {
            generateStatisticsImage(trustRecogniseLinearDrawingWindow);
        }
        if (Config.DRAWING_SHOW_trustAnalysisLinearDrawingWindow) {
            generateStatisticsImage(trustAnalysisLinearDrawingWindow);
        }
        if (Config.DRAWING_SHOW_travelHistoryBarDrawingWindow) {
            generateStatisticsImage(travelHistoryBarDrawingWindow);
        }
        if (Config.DRAWING_SHOW_experienceBarDrawingWindow) {
            generateStatisticsImage(experienceBarDrawingWindow);
        }
        if (Config.DRAWING_SHOW_indirectExperienceBarDrawingWindow) {
            generateStatisticsImage(indirectExperienceBarDrawingWindow);
        }
        if (Config.DRAWING_SHOW_recommendationBarDrawingWindow) {
            generateStatisticsImage(recommendationBarDrawingWindow);
        }
        if (Config.DRAWING_SHOW_observationBarDrawingWindow) {
            generateStatisticsImage(observationBarDrawingWindow);
        }
        if (Config.DRAWING_SHOW_indirectObservationBarDrawingWindow) {
            generateStatisticsImage(indirectObservationBarDrawingWindow);
        }

    }


    public void generateSimulationImages(IntTravelStatsLinearDrawingWindow intTravelStatsLinearDrawingWindow,
                                         IntTrustAnalysisLinearDrawingWindow intTrustAnalysisLinearDrawingWindow) {
        if (Config.INT_DRAWING_SHOW_intTravelStatsLinearDrawingWindow) {
            generateStatisticsImage(intTravelStatsLinearDrawingWindow);
        }
        if (Config.INT_DRAWING_SHOW_intTrustAnalysisLinearDrawingWindow) {
            generateStatisticsImage(intTrustAnalysisLinearDrawingWindow);
        }

    }

    private static void generateStatisticsImage(DrawingWindow drawingWindow) {
        try {
            drawingWindow.resetParams();

            int w = drawingWindow.getRealWith();
            if (w > 22000) {
                w = 22000;
            }

            int h = 2 * drawingWindow.getRealHeight();
            if (h > 12000) {
                h = 12000;
            }
            BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = image.createGraphics();
            graphics2D.translate(100, drawingWindow.getRealHeight());
            drawingWindow.paint(graphics2D);

            int worldId = drawingWindow.getWorldId();

            String simDir = "";

            if (worldId >= 0) {
                simDir = "/sim-" + (worldId < 10 ? "0" + worldId : worldId);
            }

            image = ImageTrimmer.trim(image);
            ImageIO.write(image, "jpeg", new File(ProjectPath.instance().statisticsDir() + "/" + Globals.STATS_FILE_NAME + simDir + "/" + Globals.STATS_FILE_NAME + ".xmg." + drawingWindow.getName() + ".jpg"));

            graphics2D.dispose();

            System.out.println("|> ImageBuilder::  " + drawingWindow.getName() + " generated. size: " + image.getWidth() + " X " + image.getHeight());

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
