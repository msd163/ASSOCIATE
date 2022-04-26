package simulateLayer.statistics;

import utils.Config;

import java.util.HashMap;
import java.util.Map;

public class WorldStatistics {


    private WorldStatistics prevStats;      // previous statistics for calculating 'allTrustToHonest' and 'allTrustToDishonest'

    private int worldTime;                  // time of world in this statistics
    private int episode;                    // episode of world in this statistics
    //============================
    private int allAgentsInTarget;          //
    private int allAgentsInPitfall;
    private int allRandomTravelToNeighbor;
    //============================
    private int ittAgentsInTarget;
    private int ittAgentsInPitfall;
    private int ittRandomTravelToNeighbors;
    private int ittSuccessTravelToNeighbor;
    private int ittFailedTravelToNeighbor;
    private int ittUpdatedNextStep;
    //============================
    private int fullStateCount;
    private int agentsWithNoTargetState;
    private int statesWithNoTarget;
    //============================
    private int allTrustToHonest;                   // all agents that trust to a 'honest' agent
    private int allTrustToAdversary;                // all agents that trust to a 'adversary' agent
    private int allTrustToHypocrite;                // all agents that trust to a 'int.adversary' agent
    private int allTrustToMischief;                 // all agents that trust to a 'mischief' agent

    private int ittTrustToHonest;                   // agents that trust to a 'honest' agent in this time
    private int ittTrustToAdversary;                // agents that trust to a 'dishonest' agent in this time
    private int ittTrustToHypocrite;                // agents that trust to a 'int.adversary' agent
    private int ittTrustToMischief;                 // agents that trust to a 'mischief' agent

    // ============================
    private int ittFalsePositiveTrust;
    private int ittFalseNegativeTrust;
    private int ittTruePositiveTrust;
    private int ittTrueNegativeTrust;

    // ============================
    private int allFalsePositiveTrust;
    private int allFalseNegativeTrust;
    private int allTruePositiveTrust;
    private int allTrueNegativeTrust;

    //============================
    private Map<Integer, AgentStatistics> agentStatistics;


    //--  (Number of correct assessments)/Number of all assessments)
    private float trustAccuracy;
    private float allTrustAccuracy;

    //--  Sensitivity refers to the test's ability to correctly detect ill patients who do have the condition
    //--  (Number of true positive assessment)/(Number of all positive assessment)
    //--  True Positive rate
    private float trustSensitivity;
    private float allTrustSensitivity;

    //--  Specificity relates to the test's ability to correctly reject healthy patients without a condition. Specificity of a test is the proportion of who truly do not have the condition who test negative for the condition.
    //--  (Number of true negative assessment)/(Number of all negative assessment)
    //--  True Negative rate
    private float trustSpecificity;
    private float allTrustSpecificity;

    //============================//============================

    private WorldStatistics xPrevStats;

    private int timedAverageTarget;
    private int timedAveragePitfall;

    //============================//============================


    public WorldStatistics(WorldStatistics prevStats, int agentCount, WorldStatistics xPrevStats) {
        this.prevStats = prevStats;
        this.xPrevStats = xPrevStats;
        worldTime
                = allAgentsInTarget
                = ittAgentsInTarget
                = fullStateCount
                = ittSuccessTravelToNeighbor
                = ittFailedTravelToNeighbor
                = agentsWithNoTargetState
                = ittUpdatedNextStep
                = ittRandomTravelToNeighbors
                = statesWithNoTarget
                = allAgentsInPitfall
                = ittAgentsInPitfall
                = ittTrustToAdversary
                = ittTrustToHonest
                = ittFalsePositiveTrust
                = ittFalseNegativeTrust
                = ittTruePositiveTrust
                = ittTrueNegativeTrust
                = 0;

        agentStatistics = new HashMap<>(agentCount);

    }

    public void init(int episode) {
        this.episode = episode;

        if (prevStats != null && prevStats.episode == episode) {
            allTrustToHonest = prevStats.getAllTrustToHonest();
            allTrustToAdversary = prevStats.getAllTrustToAdversary();
            allTrustToHypocrite = prevStats.getAllTrustToHypocrite();
            allTrustToMischief = prevStats.getAllTrustToMischief();
            allAgentsInTarget = prevStats.getAllAgentsInTarget();
            allAgentsInPitfall = prevStats.getAllAgentsInPitfall();
            allRandomTravelToNeighbor = prevStats.getAllRandomTravelToNeighbor();
            allFalsePositiveTrust = prevStats.getAllFalsePositiveTrust();
            allFalseNegativeTrust = prevStats.getAllFalseNegativeTrust();
            allTruePositiveTrust = prevStats.getAllTruePositiveTrust();
            allTrueNegativeTrust = prevStats.getAllTrueNegativeTrust();

        } else {

            allTrustToAdversary
                    = allTrustToHonest
                    = allTrustToHypocrite
                    = allTrustToMischief
                    = allAgentsInTarget
                    = allAgentsInPitfall
                    = allRandomTravelToNeighbor
                    = allFalsePositiveTrust
                    = allFalseNegativeTrust
                    = allTruePositiveTrust
                    = allTrueNegativeTrust
                    = 0;

        }
    }

    public void add_All_AgentsInTarget() {
        allAgentsInTarget++;
    }

    public void add_Itt_AgentsInTarget() {
        ittAgentsInTarget++;
        allAgentsInTarget++;

    }

    public void addFullStateCount() {
        fullStateCount++;
    }

    public void add_Itt_SuccessTravelToNeighbor() {
        ittSuccessTravelToNeighbor++;
    }

    public void add_Itt_FailedTravelToNeighbor() {
        ittFailedTravelToNeighbor++;
    }

    public void addAgentsWithNoTargetState() {
        agentsWithNoTargetState++;
    }

    public void addStatesWithNoTarget() {
        statesWithNoTarget++;
    }

    public void add_Itt_UpdatedNextStep() {
        ittUpdatedNextStep++;
    }

    public void addRandomTravelToNeighbors() {
        ittRandomTravelToNeighbors++;
        allRandomTravelToNeighbor++;
    }

    public void add_All_AgentsInPitfall() {
        allAgentsInPitfall++;
    }

    public int getTotalTravelToNeighbor() {
        return ittRandomTravelToNeighbors + ittSuccessTravelToNeighbor;
    }

    public void add_Itt_AgentsInPitfall() {
        ittAgentsInPitfall++;
        allAgentsInPitfall++;

    }

    public void add_Itt_TrustToHonest() {
        ittTrustToHonest++;
        allTrustToHonest++;
    }

    public void add_Itt_TrustToAdversary() {
        ittTrustToAdversary++;
        allTrustToAdversary++;
    }

    public void add_Itt_TrustToHypocrite() {
        ittTrustToHypocrite++;
        allTrustToHypocrite++;
    }

    public void add_Itt_TrustToMischief() {
        ittTrustToMischief++;
        allTrustToMischief++;
    }

    public void add_Itt_FalsePositiveTrust() {
        ittFalsePositiveTrust++;
        allFalsePositiveTrust++;

    }

    public void add_Itt_FalseNegativeTrust() {
        ittFalseNegativeTrust++;
        allFalseNegativeTrust++;
    }

    public void add_Itt_TruePositiveTrust() {
        ittTruePositiveTrust++;
        allTruePositiveTrust++;
    }

    public void add_Itt_TrueNegativeTrust() {
        ittTrueNegativeTrust++;
        allTrueNegativeTrust++;
    }

    public void calcTrustParams() {
        int tp_fn = ittTruePositiveTrust + ittFalseNegativeTrust;
        int tn_fp = ittTrueNegativeTrust + ittFalsePositiveTrust;
        int all_ = ittTruePositiveTrust + ittTrueNegativeTrust + ittFalsePositiveTrust + ittFalseNegativeTrust;
        trustSensitivity = tp_fn == 0 ? -1 : (float) ittTruePositiveTrust / tp_fn;
        trustSpecificity = tn_fp == 0 ? -1 : (float) ittTrueNegativeTrust / tn_fp;
        trustAccuracy = all_ == 0 ? -1 : (float) (ittTruePositiveTrust + ittTrueNegativeTrust) / all_;

        tp_fn = allTruePositiveTrust + allFalseNegativeTrust;
        tn_fp = allTrueNegativeTrust + allFalsePositiveTrust;
        all_ = allTruePositiveTrust + allTrueNegativeTrust + allFalsePositiveTrust + allFalseNegativeTrust;
        allTrustSensitivity = tp_fn == 0 ? -1 : (float) allTruePositiveTrust / tp_fn;
        allTrustSpecificity = tn_fp == 0 ? -1 : (float) allTrueNegativeTrust / tn_fp;
        allTrustAccuracy = all_ == 0 ? -1 : (float) (allTruePositiveTrust + allTrueNegativeTrust) / all_;


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
                ti + "  worldTime = " + worldTime +
                ti + ", fullStateCount = " + fullStateCount +
                ti + ", statesWithNoTarget = " + statesWithNoTarget +
                ti + ", ------" +
                ti + ", agentsWithNoTargetState = " + agentsWithNoTargetState +
                ti + ", allInTargetAgents = " + allAgentsInTarget +
                ti + ", ittAgentsInTarget = " + ittAgentsInTarget +
                ti + ", ittUpdatedNextStep = " + ittUpdatedNextStep +
                ti + ", ittSuccessTravelToNeighbor = " + ittSuccessTravelToNeighbor +
                ti + ", ittFailedTravelToNeighbor = " + ittFailedTravelToNeighbor +
                ti + ", ittRandomTravelToNeighbors = " + ittRandomTravelToNeighbors +
                ti + ", allAgentsInPitfall = " + allAgentsInPitfall +
                ti + ", ittAgentsInPitfall = " + ittAgentsInPitfall +
                ti + ", totalTravelOfAgents = " + getTotalTravelToNeighbor() +
                tx + '}';

    }

    //============================//============================
    public int getWorldTime() {
        return worldTime;
    }

    public void setWorldTime(int worldTime) {
        this.worldTime = worldTime;
    }

    public int getAllAgentsInTarget() {
        return allAgentsInTarget * 4;
    }

    public int getIttAgentsInTarget() {
        return ittAgentsInTarget * 4;
    }

    public int getFullStateCount() {
        return fullStateCount;
    }

    public int getIttSuccessTravelToNeighbor() {
        return ittSuccessTravelToNeighbor * 4;
    }

    public int getIttFailedTravelToNeighbor() {
        return ittFailedTravelToNeighbor * 4;
    }

    public int getAgentsWithNoTargetState() {
        return agentsWithNoTargetState * 4;
    }

    public int getIttUpdatedNextStep() {
        return ittUpdatedNextStep * 4;
    }

    public int getIttRandomTravelToNeighbors() {
        return ittRandomTravelToNeighbors * 4;
    }

    public int getStatesWithNoTarget() {
        return statesWithNoTarget;
    }

    public int getAllAgentsInPitfall() {
        return allAgentsInPitfall * 4;
    }

    public int getIttAgentsInPitfall() {
        return ittAgentsInPitfall * 4;
    }

    public int getIttTrustToAdversary() {
        return ittTrustToAdversary * 4;
    }

    public int getIttTrustToHonest() {
        return ittTrustToHonest * 4;
    }

    public int getEpisode() {
        return episode;
    }

    public int getAllTrustToAdversary() {
        return allTrustToAdversary * 4;
    }

    public int getAllTrustToHonest() {
        return allTrustToHonest * 4;
    }

    public int getIttFalsePositiveTrust() {
        return ittFalsePositiveTrust * 4;
    }

    public int getIttFalseNegativeTrust() {
        return ittFalseNegativeTrust * 4;
    }

    public int getIttTruePositiveTrust() {
        return ittTruePositiveTrust * 4;
    }

    public int getIttTrueNegativeTrust() {
        return ittTrueNegativeTrust * 4;
    }

    public float getTrustAccuracy() {
        return trustAccuracy;
    }

    public int getTrustAccuracyI100() {
        return (int) (trustAccuracy * 100);
    }

    public float getTrustSensitivity() {
        return trustSensitivity;
    }

    public int getTrustSensitivityI100() {
        return (int) (trustSensitivity * 100);
    }

    public float getTrustSpecificity() {
        return trustSpecificity;
    }

    public int getTrustSpecificityI100() {
        return (int) (trustSpecificity * 100);
    }

    public float getAllTrustAccuracy() {
        return allTrustAccuracy;
    }

    public int getAllTrustAccuracyI100() {
        return (int) (allTrustAccuracy * 100);
    }

    public float getAllTrustSensitivity() {
        return allTrustSensitivity;
    }

    public int getAllTrustSensitivityI100() {
        return (int) (allTrustSensitivity * 100);
    }

    public float getAllTrustSpecificity() {
        return allTrustSpecificity;
    }

    public int getAllTrustSpecificityI100() {
        return (int) (allTrustSpecificity * 100);
    }

    public int getAllTrustToMischief() {
        return allTrustToMischief * 4;
    }

    public int getAllTrustToHypocrite() {
        return allTrustToHypocrite * 4;
    }

    public int getIttTrustToHypocrite() {
        return ittTrustToHypocrite * 4;
    }

    public int getIttTrustToMischief() {
        return ittTrustToMischief * 4;
    }

    public int getAllFalsePositiveTrust() {
        return allFalsePositiveTrust * 4;
    }

    public int getAllFalseNegativeTrust() {
        return allFalseNegativeTrust * 4;
    }

    public int getAllTruePositiveTrust() {
        return allTruePositiveTrust * 4;
    }

    public int getAllTrueNegativeTrust() {
        return allTrueNegativeTrust * 4;
    }

    public WorldStatistics getxPrevStats() {
        return xPrevStats;
    }

    //============================//============================//============================ Timed Average

    public int getTimedAvgAgentTarget() {

        if (xPrevStats == null) {
            timedAverageTarget = allAgentsInTarget / (worldTime == 0 ? 1 : worldTime);
        } else {
            timedAverageTarget = (allAgentsInTarget - xPrevStats.allAgentsInTarget) / Config.STATISTICS_AVERAGE_TIME_WINDOW;
        }

        return timedAverageTarget * 4;
    }

    public int getTimedAvgAgentInPitfall() {
        if (xPrevStats == null) {
            timedAveragePitfall = allAgentsInPitfall / (worldTime == 0 ? 1 : worldTime);
        } else {
            timedAveragePitfall = (allAgentsInPitfall - xPrevStats.allAgentsInPitfall) / Config.STATISTICS_AVERAGE_TIME_WINDOW;
        }

        return timedAveragePitfall * 4;
    }

    public int getTimedAvgRandomTravel() {
        if (xPrevStats == null) {
            return (allRandomTravelToNeighbor / (worldTime == 0 ? 1 : worldTime)) * 4;
        } else {
            return ((allRandomTravelToNeighbor - xPrevStats.allRandomTravelToNeighbor) / Config.STATISTICS_AVERAGE_TIME_WINDOW) * 4;
        }

    }

    public int getTimedAvgTruePositive() {
        if (xPrevStats == null) {
            return (allTruePositiveTrust / (worldTime == 0 ? 1 : worldTime)) * 4;
        } else {
            return ((allTruePositiveTrust - xPrevStats.allTruePositiveTrust) / Config.STATISTICS_AVERAGE_TIME_WINDOW) * 4;
        }
    }

    public int getTimedAvgTrueNegative() {
        if (xPrevStats == null) {
            return (allTrueNegativeTrust / (worldTime == 0 ? 1 : worldTime)) * 4;
        } else {
            return ((allTrueNegativeTrust - xPrevStats.allTrueNegativeTrust) / Config.STATISTICS_AVERAGE_TIME_WINDOW) * 4;
        }
    }

    public int getTimedAvgFalsePositive() {
        if (xPrevStats == null) {
            return (allFalsePositiveTrust / (worldTime == 0 ? 1 : worldTime)) * 4;
        } else {
            return ((allFalsePositiveTrust - xPrevStats.allFalsePositiveTrust) / Config.STATISTICS_AVERAGE_TIME_WINDOW) * 4;
        }
    }

    public int getTimedAvgFalseNegative() {
        if (xPrevStats == null) {
            return (allFalseNegativeTrust / (worldTime == 0 ? 1 : worldTime)) * 4;
        } else {
            return ((allFalseNegativeTrust - xPrevStats.allFalseNegativeTrust) / Config.STATISTICS_AVERAGE_TIME_WINDOW) * 4;
        }
    }

    public int getTimeAvgTrustToAdversary() {
        if (xPrevStats == null) {
            return (allFalseNegativeTrust / (worldTime == 0 ? 1 : worldTime)) * 4;
        } else {
            return ((allFalseNegativeTrust - xPrevStats.allFalseNegativeTrust) / Config.STATISTICS_AVERAGE_TIME_WINDOW) * 4;
        }
    }

    public int getTimeAvgTrustToHonest() {
        if (xPrevStats == null) {
            return (allTrustToHonest / (worldTime == 0 ? 1 : worldTime)) * 4;
        } else {
            return ((allTrustToHonest - xPrevStats.allTrustToHonest) / Config.STATISTICS_AVERAGE_TIME_WINDOW) * 4;
        }
    }

    public int getTimeAvgTrustToMischief() {
        if (xPrevStats == null) {
            return (allTrustToMischief / (worldTime == 0 ? 1 : worldTime)) * 4;
        } else {
            return ((allTrustToMischief - xPrevStats.allTrustToMischief) / Config.STATISTICS_AVERAGE_TIME_WINDOW) * 4;
        }
    }

    public int getTimeAvgTrustToHypocrite() {
        if (xPrevStats == null) {
            return (allTrustToHypocrite / (worldTime == 0 ? 1 : worldTime)) * 4;
        } else {
            return ((allTrustToHypocrite - xPrevStats.allTrustToHypocrite) / Config.STATISTICS_AVERAGE_TIME_WINDOW) * 4;
        }

    }

    public int getAllRandomTravelToNeighbor() {
        return allRandomTravelToNeighbor * 4;
    }
}
