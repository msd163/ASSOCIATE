package utils;

import drawingLayer.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageBuilder {

    public static void generateStatisticsImages(StateMachineDrawingWindow stateMachineWindow, StatsOfEnvDrawingWindow statsOfEnvWindow, TrustMatrixDrawingWindow trustMatrixWindow, StatsOfTrustDrawingWindow trustStatsWindow, StatsOfFalsePoNeDrawingWindow poNeStatsWindow, AnalysisOfTrustParamsDrawingWindow trustParamsDrawingWindow) {
        if (Config.DRAWING_SHOW_STATE_MACHINE) {
            generateStatisticsImage(stateMachineWindow);
        }
        if (Config.DRAWING_SHOW_STAT_OF_ENV) {
            generateStatisticsImage(statsOfEnvWindow);
        }
        if (Config.DRAWING_SHOW_TRUST_MATRIX) {
            generateStatisticsImage(trustMatrixWindow);
        }
        if (Config.DRAWING_SHOW_STATS_OF_TRUST) {
            generateStatisticsImage(trustStatsWindow);
        }
        if (Config.DRAWING_SHOW_STATS_OF_PO_NE) {
            generateStatisticsImage(poNeStatsWindow);
        }
        if (Config.DRAWING_SHOW_ANALYSIS_OF_TRUST_PARAM) {
            generateStatisticsImage(trustParamsDrawingWindow);
        }
    }

    private static void generateStatisticsImage(DrawingWindow drawingWindow) {
        try {
            drawingWindow.resetParams();

            BufferedImage image = new BufferedImage(drawingWindow.getRealWith(), 2 * drawingWindow.getRealHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = image.createGraphics();
            graphics2D.translate(100, drawingWindow.getRealHeight());
            drawingWindow.paint(graphics2D);

            ImageIO.write(ImageTrimmer.trim(image), "jpeg", new File(ProjectPath.instance().statisticsDir() + "/" + Globals.STATS_FILE_NAME + "/" + Globals.STATS_FILE_NAME + ".xmg." + drawingWindow.getName() + ".jpg"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
