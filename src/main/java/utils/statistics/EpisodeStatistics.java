package utils.statistics;

import utils.Globals;

public class EpisodeStatistics {

    private int episode;                    // episode of world in this statistics
    private int fromTime;
    private int toTime;
    //============================
    private int midAgentsInTarget;          //
    private int midAgentsInPitfall;
    //============================

    //============================
    private int midTrustToHonest;                   // mid agents that trust to a 'honest' agent
    private int midTrustToAdversary;                // mid agents that trust to a 'adversary' agent
    private int midTrustToIntelligentAdversary;     // mid agents that trust to a 'int.adversary' agent
    private int midTrustToMischief;                 // mid agents that trust to a 'mischief' agent

    // ============================
    private int midFalsePositiveTrust;
    private int midFalseNegativeTrust;
    private int midTruePositiveTrust;
    private int midTrueNegativeTrust;
    //============================


    //--  (Number of correct assessments)/Number of mid assessments)
    private float trustAccuracy;

    //--  Sensitivity refers to the test's ability to correctly detect ill patients who do have the condition
    //--  (Number of true positive assessment)/(Number of mid positive assessment)
    //--  True Positive rate
    private float trustSensitivity;

    //--  Specificity relates to the test's ability to correctly reject healthy patients without a condition. Specificity of a test is the proportion of who truly do not have the condition who test negative for the condition.
    //--  (Number of true negative assessment)/(Number of mid negative assessment)
    //--  True Negative rate
    private float trustSpecificity;

    //============================//============================


    public EpisodeStatistics() {
        reset();

    }

    private void reset() {
        midAgentsInTarget
                = midAgentsInPitfall

                = midFalsePositiveTrust
                = midFalseNegativeTrust
                = midTruePositiveTrust
                = midTrueNegativeTrust

                = midTrustToAdversary
                = midTrustToHonest
                = midTrustToIntelligentAdversary
                = midTrustToMischief
                = 0;
    }

    public void init(int episode) {
        this.episode = episode;
    }

    public void calcTrustParams() {
        trustSensitivity = (float) midTruePositiveTrust / (midTruePositiveTrust + midFalseNegativeTrust);
        trustSpecificity = (float) midTrueNegativeTrust / (midTrueNegativeTrust + midFalsePositiveTrust);
        trustAccuracy = (float) (midTruePositiveTrust + midTrueNegativeTrust) / (midTruePositiveTrust + midTrueNegativeTrust + midFalsePositiveTrust + midFalseNegativeTrust);
    }

    //============================//============================
    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int tabIndex) {
        tabIndex++;
        StringBuilder tx = new StringBuilder("\n");
        StringBuilder ti = new StringBuilder("\n");
        for (int i = 0; i <= tabIndex; i++) {
            if (i > 1) {
                tx.append("\t");
            }
            ti.append("\t");
        }
        return tx + "WorldStatistics{" +
                ti + ", ------" +
                ti + ", midInTargetAgents = " + midAgentsInTarget +
                ti + ", midAgentsInTarget = " + midAgentsInTarget +
                ti + ", midAgentsInPitfall = " + midAgentsInPitfall +
                tx + '}';

    }

    //============================//============================


    public void setFromTime(int fromTime) {
        this.fromTime = fromTime;
    }

    public void update(WorldStatistics[] wds) {
        toTime = Globals.WORLD_TIMER;
        episode = Globals.EPISODE;
        reset();

        int diff = toTime - fromTime + 1;
        for (int i = fromTime; i < toTime; i++) {
            WorldStatistics wd = wds[i];
            midAgentsInTarget += wd.getAllAgentsInTarget();
            midAgentsInPitfall += wd.getAllAgentsInPitfall();
        }
        midAgentsInTarget /= diff;
        midAgentsInPitfall /= diff;
    }

    //============================//============================


    public int getMidAgentsInTarget() {
        return midAgentsInTarget;
    }

    public int getMidAgentsInPitfall() {
        return midAgentsInPitfall;
    }

    public int getMidTrustToHonest() {
        return midTrustToHonest;
    }

    public int getMidTrustToAdversary() {
        return midTrustToAdversary;
    }

    public int getMidTrustToIntelligentAdversary() {
        return midTrustToIntelligentAdversary;
    }

    public int getMidTrustToMischief() {
        return midTrustToMischief;
    }

    public int getMidFalsePositiveTrust() {
        return midFalsePositiveTrust;
    }

    public int getMidFalseNegativeTrust() {
        return midFalseNegativeTrust;
    }

    public int getMidTruePositiveTrust() {
        return midTruePositiveTrust;
    }

    public int getMidTrueNegativeTrust() {
        return midTrueNegativeTrust;
    }

    public float getTrustAccuracy() {
        return trustAccuracy;
    }

    public float getTrustSensitivity() {
        return trustSensitivity;
    }

    public float getTrustSpecificity() {
        return trustSpecificity;
    }

    public int getEpisode() {
        return episode;
    }

    public int getToTime() {
        return toTime;
    }
}
