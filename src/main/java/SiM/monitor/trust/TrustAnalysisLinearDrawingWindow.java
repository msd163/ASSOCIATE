package SiM.monitor.trust;

import SiM.monitor.DrawingWindow;
import WSM.World;
import core.utils.Globals;
import core.utils.Point;
import SiM.statistics.WorldStatistics;

import java.awt.*;

public class TrustAnalysisLinearDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public TrustAnalysisLinearDrawingWindow(World world) {
        super();
        this.world = world;
        this.prevPoints = new Point[3];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Trust Analyzing (A.S.S) Linear Chart";
        setName("tut_anl");
    }


    @Override
    public void paint(Graphics gr) {
        if (!mainPaint(gr, headerTitle + " :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }
        axisX = 0;
        //============================//============================//============================

        printStatsInfo(1, "Accuracy (#of correct/#of all))", world.getWdStatistics()[worldTimer].getTrustAccuracy(), Color.GREEN);

        printStatsInfo(2, "Sensitivity (#of TP/#of all P)", world.getWdStatistics()[worldTimer].getTrustSensitivity(), Color.YELLOW);

        printStatsInfo(3, "Specificity (#of TN/#of all N)", world.getWdStatistics()[worldTimer].getTrustSpecificity(), Color.PINK);

        printStatsInfo(4, "[ X200 Scale ]", Color.WHITE);

        //============================//============================//============================

        reverseNormalizeCoordination();

        WorldStatistics[] statistics = world.getWdStatistics();

        for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < Globals.WORLD_TIMER && sttIdx < statisticsLength; sttIdx++) {
            WorldStatistics stat = statistics[sttIdx];

            int curveIndex = -1;

            if (sttIdx == 0 || stat.getEpisode() != statistics[sttIdx - 1].getEpisode()) {
                axisX += 8;
                prevPoints[0].y = (int) (0.1 * 0.1 * _vs * stat.getTrustAccuracyI100());
                prevPoints[1].y = (int) (0.1 * 0.1 * _vs * stat.getTrustSensitivityI100());
                prevPoints[2].y = (int) (0.1 * 0.1 * _vs * stat.getTrustSpecificityI100());
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = axisX;

            } else {

                prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTrustAccuracyI100());
                prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTrustSensitivityI100());
                prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTrustSpecificityI100());
                prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = axisX;
                axisX += 8;
            }

            drawSymbolOnCurve(axisX, (int) (0.1 * _vs * stat.getTrustAccuracyI100()), Color.GREEN, 1, sttIdx);
            if (prevPoints[0].y >= 0) {
                drawLine(prevPoints[0].x, prevPoints[0].y, axisX, stat.getTrustAccuracyI100(), sttIdx, ++curveIndex);
            }
            drawSymbolOnCurve(axisX, (int) (0.1 * _vs * stat.getTrustSensitivityI100()), Color.YELLOW, 2, sttIdx);
            if (prevPoints[1].y >= 0) {
                drawLine(prevPoints[1].x, prevPoints[1].y, axisX, stat.getTrustSensitivityI100(), sttIdx, ++curveIndex);
            }
            drawSymbolOnCurve(axisX, (int) (0.1 * _vs * stat.getTrustSpecificityI100()), Color.PINK, 3, sttIdx);
            if (prevPoints[2].y >= 0) {
                drawLine(prevPoints[2].x, prevPoints[2].y, axisX, stat.getTrustSpecificityI100(), sttIdx, ++curveIndex);
            }
        }

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);
    }
}
