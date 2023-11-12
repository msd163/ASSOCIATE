package simulateLayer.statistics;

import utils.Config;

public class WorldStatisticsCollaboration {

    public WorldStatisticsCollaboration(WorldStatisticsCollaboration xPrevStatisticsCollaboration, WorldStatisticsCollaboration prevStatCollab) {

        this.xPrevStatCollab = xPrevStatisticsCollaboration;
        this.prevStatCollab = prevStatCollab;

        allHonestCollaboration
                = allHonestCollaborationInRound
                = allTrustToHonestInRound
                = 0;

        allHypocriteCollaboration
                = allHypocriteCollaborationInRound
                = allTrustToHypocriteInRound
                = 0;

        allHonestDominanceToDishonestCollaborationInRound
                = allHonestWithDishonestCollaborationInRound
                = 0;


    }

    public void init(WorldStatisticsCollaboration hypo) {
        allHonestCollaboration = hypo.allHonestCollaboration;
        allHonestCollaborationInRound = hypo.allHonestCollaborationInRound;
        allTrustToHonestInRound = hypo.allTrustToHonestInRound;

        allHypocriteCollaboration = hypo.allHypocriteCollaboration;
        allHypocriteCollaborationInRound = hypo.allHypocriteCollaborationInRound;
        allTrustToHypocriteInRound = hypo.allTrustToHypocriteInRound;

        allHonestWithDishonestCollaborationInRound = hypo.allHonestWithDishonestCollaborationInRound;
        allHonestDominanceToDishonestCollaborationInRound = hypo.allHonestDominanceToDishonestCollaborationInRound;
    }

    private final WorldStatisticsCollaboration xPrevStatCollab;
    private final WorldStatisticsCollaboration prevStatCollab;

    private int allHonestCollaboration;
    private int allHonestCollaborationInRound;
    private int allTrustToHonestInRound;

    private int allHypocriteCollaboration;
    private int allHypocriteCollaborationInRound;
    private int allTrustToHypocriteInRound;

    private int allDishonestCollaborationInRound;

    private int allHonestWithDishonestCollaborationInRound;

    private int allHonestDominanceToDishonestCollaborationInRound;

    private float dominanceInRound;

    private int worldTime;

    //============================//============================
    private int calcAverage(int coeff, int currentVal, Integer xPrevVal, int average) {

        if (xPrevVal == null) {
            return (int) ((float) currentVal / (worldTime == 0 ? 1 : worldTime));
        } else {
            return (int) ((coeff) * (float) (currentVal - xPrevVal) / average);
        }
    }

    private int calcAverage(int currentVal, Integer xPrevVal, int average) {
        return calcAverage(1, currentVal, xPrevVal, average);
    }

    //============================//============================//============================

    public void setWorldTime(int worldTime) {
        this.worldTime = worldTime;
    }

    //============================//============================//============================


    //============================


    //============================//============================
    public void add_allHonestCollaborationInRound() {
        allHonestCollaborationInRound++;
    }

    public void add_allDishonestCollaborationInRound() {
        allDishonestCollaborationInRound++;
    }

    public void add_allHonestWithDishonestCollaborationInRound() {
        allHonestWithDishonestCollaborationInRound++;
//        dominanceInRound = (((float) allHonestDominanceToDishonestCollaborationInRound / allHonestWithDishonestCollaborationInRound) * 2) - 1;
    }

    public void add_allHonestDominanceToDishonestCollaborationInRound() {
        allHonestDominanceToDishonestCollaborationInRound++;
//        if (allHonestWithDishonestCollaborationInRound > 0) {
//            dominanceInRound = (((float) allHonestDominanceToDishonestCollaborationInRound / allHonestWithDishonestCollaborationInRound) * 2) - 1;
//        } else {
//            dominanceInRound = 0;
//        }
    }

    public void add_allHonestCollaboration() {
        allHonestCollaboration++;
    }

    public void add_allTrustToHonestInRound() {
        allTrustToHonestInRound++;
    }

    //============================

    public void add_allHypocriteCollaborationInRound() {
        allHypocriteCollaborationInRound++;
    }

    public void add_allHypocriteCollaboration() {
        allHypocriteCollaboration++;
    }

    public void add_allTrustToHypocriteInRound() {
        allTrustToHypocriteInRound++;
    }


    //============================//============================
    public int getIttHonestCollaboration() {
        if (prevStatCollab == null) {
            return allHonestCollaboration;
        }
        return allHonestCollaboration - prevStatCollab.allHonestCollaboration;
    }

    public int getAvgHonestCollaboration() {
        return calcAverage(allHonestCollaboration,
                xPrevStatCollab == null ? null : xPrevStatCollab.allHonestCollaboration,
                Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION);
    }

    public int getIttHonestCollaborationInRound() {
        if (prevStatCollab == null) {
            return allHonestCollaborationInRound;
        }
        return allHonestCollaborationInRound - prevStatCollab.allHonestCollaborationInRound;
    }

    public int getAvgHonestCollaborationInRound() {
        return calcAverage(allHonestCollaborationInRound, xPrevStatCollab == null ? null : xPrevStatCollab.allHonestCollaborationInRound,
                Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION);

    }

    public int getAvgDishonestCollaborationInRound() {
        return calcAverage(allDishonestCollaborationInRound, xPrevStatCollab == null ? null : xPrevStatCollab.allDishonestCollaborationInRound,
                Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION);
    }

    public float getDominanceInRound() {
        return dominanceInRound;
    }

    public int getAvgDominanceInRound100() {

        int allHonAvg = calcAverage(allHonestDominanceToDishonestCollaborationInRound,
                xPrevStatCollab == null ? null : xPrevStatCollab.allHonestDominanceToDishonestCollaborationInRound,
                Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_DOMINANCE);

        int allHonWithDisAvg = calcAverage(allHonestWithDishonestCollaborationInRound,
                xPrevStatCollab == null ? null : xPrevStatCollab.allHonestWithDishonestCollaborationInRound,
                Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_DOMINANCE);


        dominanceInRound = (((float) allHonAvg / allHonWithDisAvg));

        return (int) (dominanceInRound * 200)-100;
    }

    public int getIttTrustToHonestInRound() {
        if (prevStatCollab == null) {
            return allTrustToHonestInRound;
        }
        return allTrustToHonestInRound - prevStatCollab.allTrustToHonestInRound;
    }

    public int getAvgTrustToHonestInRound() {
        return calcAverage(allTrustToHonestInRound, xPrevStatCollab == null ? null : xPrevStatCollab.allTrustToHonestInRound,
                Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION);
    }

    public int getAvgHonestTrustPerCollab100() {
        return (int) (100 * (float) getAvgTrustToHonestInRound() / getAvgHonestCollaborationInRound());
//        return calcAverage(
//                (int) (100 * (float) allTrustToHonestInRound / allHonestCollaborationInRound),
//                xPrevStatCollab == null ? null :(int) (100 * (float) xPrevStatCollab.allTrustToHonestInRound / xPrevStatCollab.allHonestCollaborationInRound));
    }

    //============================

    public int getIttHypocriteCollaboration() {
        if (prevStatCollab == null) {
            return allHypocriteCollaboration;
        }
        return allHypocriteCollaboration - prevStatCollab.allHypocriteCollaboration;
    }

    public int getAvgHypocriteCollaboration() {
        return calcAverage(allHypocriteCollaboration, xPrevStatCollab == null ? null : xPrevStatCollab.allHypocriteCollaboration,
                Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION);
    }

    public int getIttHypocriteCollaborationInRound() {
        if (prevStatCollab == null) {
            return allHypocriteCollaborationInRound;
        }
        return allHypocriteCollaborationInRound - prevStatCollab.allHypocriteCollaborationInRound;
    }

    public int getAvgHypocriteCollaborationInRound() {
        return calcAverage(allHypocriteCollaborationInRound, xPrevStatCollab == null ? null : xPrevStatCollab.allHypocriteCollaborationInRound,
                Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION);

    }

    public int getIttTrustToHypocriteInRound() {
        if (prevStatCollab == null) {
            return allTrustToHypocriteInRound;
        }
        return allTrustToHypocriteInRound - prevStatCollab.allTrustToHypocriteInRound;
    }

    public int getAvgTrustToHypocriteInRound() {
        return calcAverage(allTrustToHypocriteInRound, xPrevStatCollab == null ? null : xPrevStatCollab.allTrustToHypocriteInRound,
                Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_COLLABORATION);
    }

    public int getAvgHypocriteTrustPerCollab100() {
        return (int) (100 * (float) getAvgTrustToHypocriteInRound() / getAvgHypocriteCollaborationInRound());

//        return calcAverage(
//                (int) (100 * (float) allTrustToHypocriteInRound / allHypocriteCollaborationInRound),
//                xPrevStatCollab == null ? null : (int) (100 * (float) xPrevStatCollab.allTrustToHypocriteInRound / xPrevStatCollab.allHypocriteCollaborationInRound));
    }
}
