package simulateLayer.config.simulation;

import _type.TtDiagramThemeMode;

public class SimulationConfig {

    private int worldLifeTime;
    private int runtimeThreadCount;
    private int themeMode;


    private int worldSleepMillisecond;
    private int worldSleepMillisecondInPause;
    private int worldSleepMillisecondForDrawing;
    private int worldSleepMillisecondForDrawingInPause;

    private float improvementAnalysisCoefficient;

    private int statisticsAverageTimeWindow;
    private int statisticsAverageTimeWindowForResistance;
    private int statisticsAverageTimeWindowForCollaboration;
    private int statisticsAverageTimeWindowForRoc;
    private int statisticsHypocriteDiagnosisThreshold;
    private int statisticsHypocriteResistanceCount;

    private double statisticsScaleUpYAxisNumber;

    private int routingStayInTargetTime;
    private int routingStayInPitfallTime;

    private boolean optimizeMemory;

    private boolean turboCertifiedDagraSingleUpdateMultipleClone;


    private boolean drawingWindowsMaximizing;
    private boolean drawingWindowsDefaultPaintVisibility;


    private boolean drawingShowStateMachineWindow;

    private boolean drawingShowTravelStatsLinearDrawingWindow;
    private boolean drawingShowTravelHistoryBarDrawingWindow;

    private boolean drawingShowTrustMatrixDrawingWindow;
    private boolean drawingShowTrustStatsLinearDrawingWindow;
    private boolean drawingShowTrustRecogniseLinearDrawingWindow;
    private boolean drawingShowTrustAnalysisLinearDrawingWindow;

    private boolean drawingShowExperienceBarDrawingWindow;
    private boolean drawingShowIndirectExperienceBarDrawingWindow;

    private boolean drawingShowObservationBarDrawingWindow;
    private boolean drawingShowIndirectObservationBarDrawingWindow;

    private boolean drawingShowRecommendationBarDrawingWindow;


    private boolean intDrawingShowIntTravelStatsLinearDrawingWindow;
    private boolean intDrawingShowIntTrustAnalysisLinearDrawingWindow;
    private boolean intDrawingShowIntTrustStatsLinearDrawingWindow;
    private boolean intDrawingShowIntResistancePerNumberLinearDrawingWindow;
    private boolean intDrawingShowRocPointDrawingWindow;
    private boolean intDrawingShowHonestCollaborationLinearDrawingWindow;
    private boolean intDrawingShowHypocriteCollaborationLinearDrawingWindow;
    private boolean intDrawingShowResistanceLinearDrawingWindow;
    private boolean intDrawingShowFluctuationLinearDrawingWindow;


    private boolean statisticsIsGenerate;
    private boolean statisticsIsSaveImage;
    private boolean trustMatrixIsGenerate;
    private boolean trustMatrixIsOn;


    public int getWorldLifeTime() {
        return worldLifeTime;
    }

    public float getImprovementAnalysisCoefficient() {
        return improvementAnalysisCoefficient;
    }

    public int getStatisticsAverageTimeWindow() {
        return statisticsAverageTimeWindow;
    }

    public int getStatisticsAverageTimeWindowForResistance() {
        return statisticsAverageTimeWindowForResistance;
    }

    public int getStatisticsAverageTimeWindowForCollaboration() {
        return statisticsAverageTimeWindowForCollaboration;
    }

    public int getStatisticsAverageTimeWindowForRoc() {
        return statisticsAverageTimeWindowForRoc;
    }

    public int getStatisticsHypocriteDiagnosisThreshold() {
        return statisticsHypocriteDiagnosisThreshold;
    }

    public int getStatisticsHypocriteResistanceCount() {
        return statisticsHypocriteResistanceCount;
    }

    public double getStatisticsScaleUpYAxisNumber() {
        return statisticsScaleUpYAxisNumber;
    }

    public int getRoutingStayInTargetTime() {
        return routingStayInTargetTime;
    }

    public int getRoutingStayInPitfallTime() {
        return routingStayInPitfallTime;
    }

    public boolean isOptimizeMemory() {
        return optimizeMemory;
    }

    public boolean isTurboCertifiedDagraSingleUpdateMultipleClone() {
        return turboCertifiedDagraSingleUpdateMultipleClone;
    }

    public TtDiagramThemeMode getThemeMode() {
        return TtDiagramThemeMode.getByOrdinal(themeMode);
    }

    public boolean isDrawingWindowsMaximizing() {
        return drawingWindowsMaximizing;
    }

    public boolean isDrawingWindowsDefaultPaintVisibility() {
        return drawingWindowsDefaultPaintVisibility;
    }

    public boolean isDrawingShowStateMachineWindow() {
        return drawingShowStateMachineWindow;
    }

    public boolean isDrawingShowTravelStatsLinearDrawingWindow() {
        return drawingShowTravelStatsLinearDrawingWindow;
    }

    public boolean isDrawingShowTravelHistoryBarDrawingWindow() {
        return drawingShowTravelHistoryBarDrawingWindow;
    }

    public boolean isDrawingShowTrustMatrixDrawingWindow() {
        return drawingShowTrustMatrixDrawingWindow;
    }

    public boolean isDrawingShowTrustStatsLinearDrawingWindow() {
        return drawingShowTrustStatsLinearDrawingWindow;
    }

    public boolean isDrawingShowTrustRecogniseLinearDrawingWindow() {
        return drawingShowTrustRecogniseLinearDrawingWindow;
    }

    public boolean isDrawingShowTrustAnalysisLinearDrawingWindow() {
        return drawingShowTrustAnalysisLinearDrawingWindow;
    }

    public boolean isDrawingShowExperienceBarDrawingWindow() {
        return drawingShowExperienceBarDrawingWindow;
    }

    public boolean isDrawingShowIndirectExperienceBarDrawingWindow() {
        return drawingShowIndirectExperienceBarDrawingWindow;
    }

    public boolean isDrawingShowObservationBarDrawingWindow() {
        return drawingShowObservationBarDrawingWindow;
    }

    public boolean isDrawingShowIndirectObservationBarDrawingWindow() {
        return drawingShowIndirectObservationBarDrawingWindow;
    }

    public boolean isDrawingShowRecommendationBarDrawingWindow() {
        return drawingShowRecommendationBarDrawingWindow;
    }

    public boolean isIntDrawingShowIntTravelStatsLinearDrawingWindow() {
        return intDrawingShowIntTravelStatsLinearDrawingWindow;
    }

    public boolean isIntDrawingShowIntTrustAnalysisLinearDrawingWindow() {
        return intDrawingShowIntTrustAnalysisLinearDrawingWindow;
    }

    public boolean isIntDrawingShowIntTrustStatsLinearDrawingWindow() {
        return intDrawingShowIntTrustStatsLinearDrawingWindow;
    }

    public boolean isIntDrawingShowIntResistancePerNumberLinearDrawingWindow() {
        return intDrawingShowIntResistancePerNumberLinearDrawingWindow;
    }

    public boolean isIntDrawingShowRocPointDrawingWindow() {
        return intDrawingShowRocPointDrawingWindow;
    }

    public boolean isIntDrawingShowHonestCollaborationLinearDrawingWindow() {
        return intDrawingShowHonestCollaborationLinearDrawingWindow;
    }

    public boolean isIntDrawingShowHypocriteCollaborationLinearDrawingWindow() {
        return intDrawingShowHypocriteCollaborationLinearDrawingWindow;
    }

    public boolean isIntDrawingShowResistanceLinearDrawingWindow() {
        return intDrawingShowResistanceLinearDrawingWindow;
    }

    public boolean isIntDrawingShowFluctuationLinearDrawingWindow() {
        return intDrawingShowFluctuationLinearDrawingWindow;
    }

    public boolean isStatisticsIsGenerate() {
        return statisticsIsGenerate;
    }

    public boolean isStatisticsIsSaveImage() {
        return statisticsIsSaveImage;
    }

    public boolean isTrustMatrixIsGenerate() {
        return trustMatrixIsGenerate;
    }

    public boolean isTrustMatrixIsOn() {
        return trustMatrixIsOn;
    }

    public int getRuntimeThreadCount() {
        return runtimeThreadCount;
    }

    public int getWorldSleepMillisecond() {
        return worldSleepMillisecond;
    }

    public int getWorldSleepMillisecondInPause() {
        return worldSleepMillisecondInPause;
    }

    public int getWorldSleepMillisecondForDrawing() {
        return worldSleepMillisecondForDrawing;
    }

    public int getWorldSleepMillisecondForDrawingInPause() {
        return worldSleepMillisecondForDrawingInPause;
    }


}
