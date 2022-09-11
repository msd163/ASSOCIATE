package simulateLayer.statistics;

import societyLayer.environmentSubLayer.Environment;
import utils.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WorldStatistics {

    private int[] honestCollaboration;
    private int currentHonestCollaboration;

    private int negativePop;

    private int positivePop;


    private WorldStatistics prevStats;      // previous statistics for calculating 'allTrustToHonest' and 'allTrustToDishonest'

    private Environment environment;
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
    private long allFluctuation;

    private long allFluctuationRound;
    private int[] allEffectiveFluctuationResistanceNumber;
    //============================
    private Map<Integer, AgentStatistics> agentStatistics;


    //--  (Number of correct assessments)/Number of all assessments)
    private float trustAccuracy;
    private float allTrustAccuracy;

    //--  Sensitivity refers to the test's ability to correctly detect ill patients who do have the condition
    //--  (Number of true positive assessment)/(Number of all positive assessment)
    //--  True Positive rate
    private float trustSensitivity;
    private float trustTpRate;
    private float trustTnRate;
    private float trustFpRate;
    private float trustFnRate;
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


    public WorldStatistics(WorldStatistics prevStats, int agentCount, WorldStatistics xPrevStats, Environment environment) {
        this.environment = environment;
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

        allFluctuation
                = allFluctuationRound
                = 0;

        allEffectiveFluctuationResistanceNumber = new int[20];
        Arrays.fill(allEffectiveFluctuationResistanceNumber, 0);

        agentStatistics = new HashMap<>(agentCount);
        honestCollaboration = new int[Config.STATISTICS_AVERAGE_TIME_WINDOW];
        for (int i = 0; i < honestCollaboration.length; i++) {
            honestCollaboration[i] = 0;
        }
        currentHonestCollaboration = 0;

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
            allFluctuation = prevStats.getAllFluctuation();
            allFluctuationRound = prevStats.getAllFluctuationRound();
            for (int i = 0; i < allEffectiveFluctuationResistanceNumber.length; i++) {
                allEffectiveFluctuationResistanceNumber[i] = prevStats.getAllEffectiveFluctuationResistanceNumber()[i];
            }
            for (int i = 0; i < Config.STATISTICS_AVERAGE_TIME_WINDOW; i++) {
                honestCollaboration[i] = prevStats.getHonestCollaboration(i);
            }
            currentHonestCollaboration = prevStats.getCurrentHonestCollaboration();
            resetCollaboration();
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

            allFluctuation
                    = allFluctuationRound
                    = 0;
            allEffectiveFluctuationResistanceNumber = new int[20];
            Arrays.fill(allEffectiveFluctuationResistanceNumber, 0);

        }
    }

    private int getHonestCollaboration(int i) {
        return honestCollaboration[i];
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


    public void add_AllFluctuation() {
        allFluctuation++;
    }

    public void add_AllFluctuationRound() {
        allFluctuationRound++;
    }

    public void add_EffectiveFluctuationResistanceNumber(int N) {
        if (N < 20) {
            allEffectiveFluctuationResistanceNumber[N]++;
        }
    }

    public void calcTrustParams() {

        int pAll = positivePop;

        int nAll = negativePop;


        int tp_fn = ittTruePositiveTrust + ittFalseNegativeTrust;
        int tn_fp = ittTrueNegativeTrust + ittFalsePositiveTrust;
        int all_1 = ittTruePositiveTrust + ittTrueNegativeTrust + ittFalsePositiveTrust + ittFalseNegativeTrust;
        int all_ = nAll + pAll;//ittTruePositiveTrust + ittTrueNegativeTrust + ittFalsePositiveTrust + ittFalseNegativeTrust;
        trustSensitivity = tp_fn == 0 ? -1 : (float) ittTruePositiveTrust / tp_fn;
        trustSpecificity = tn_fp == 0 ? -1 : (float) ittTrueNegativeTrust / tn_fp;
        trustAccuracy = all_ == 0 ? -1 : (float) (ittTruePositiveTrust + ittTrueNegativeTrust) / all_;

        tp_fn = allTruePositiveTrust + allFalseNegativeTrust;
        tn_fp = allTrueNegativeTrust + allFalsePositiveTrust;
        all_ = allTruePositiveTrust + allTrueNegativeTrust + allFalsePositiveTrust + allFalseNegativeTrust;
        allTrustSensitivity = tp_fn == 0 ? -1 : (float) allTruePositiveTrust / tp_fn;
        allTrustSpecificity = tn_fp == 0 ? -1 : (float) allTrueNegativeTrust / tn_fp;
        allTrustAccuracy = all_ == 0 ? -1 : (float) (allTruePositiveTrust + allTrueNegativeTrust) / all_;

        //double tpRate = (double) ittTruePositiveTrust / pAll;
        //double tnRate = (double) ittTrueNegativeTrust / nAll;

        trustTpRate = pAll == 0 ? -1 : (float) ittTruePositiveTrust / pAll;
        trustTnRate = nAll == 0 ? -1 : (float) ittTrueNegativeTrust / nAll;
        trustFpRate = pAll == 0 ? -1 : (float) ittFalsePositiveTrust / pAll;
        trustFnRate = nAll == 0 ? -1 : (float) ittFalseNegativeTrust / nAll;

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
        return allAgentsInTarget;
    }

    public int getIttAgentsInTarget() {
        return ittAgentsInTarget;
    }

    public int getFullStateCount() {
        return fullStateCount;
    }

    public int getIttSuccessTravelToNeighbor() {
        return ittSuccessTravelToNeighbor;
    }

    public int getIttFailedTravelToNeighbor() {
        return ittFailedTravelToNeighbor;
    }

    public int getAgentsWithNoTargetState() {
        return agentsWithNoTargetState;
    }

    public int getIttUpdatedNextStep() {
        return ittUpdatedNextStep;
    }

    public int getIttRandomTravelToNeighbors() {
        return ittRandomTravelToNeighbors;
    }

    public int getStatesWithNoTarget() {
        return statesWithNoTarget;
    }

    public int getAllAgentsInPitfall() {
        return allAgentsInPitfall;
    }

    public int getIttAgentsInPitfall() {
        return ittAgentsInPitfall;
    }

    public int getIttTrustToAdversary() {
        return ittTrustToAdversary;
    }

    public int getIttTrustToHonest() {
        return ittTrustToHonest;
    }

    public int getEpisode() {
        return episode;
    }

    public int getAllTrustToAdversary() {
        return allTrustToAdversary;
    }

    public int getAllTrustToHonest() {
        return allTrustToHonest;
    }

    public int getIttFalsePositiveTrust() {
        return ittFalsePositiveTrust;
    }

    public int getIttFalseNegativeTrust() {
        return ittFalseNegativeTrust;
    }

    public int getIttTruePositiveTrust() {
        return ittTruePositiveTrust;
    }

    public int getIttTrueNegativeTrust() {
        return ittTrueNegativeTrust;
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
        return allTrustToMischief;
    }

    public int getAllTrustToHypocrite() {
        return allTrustToHypocrite;
    }

    public int getIttTrustToHypocrite() {
        return ittTrustToHypocrite;
    }

    public int getIttTrustToMischief() {
        return ittTrustToMischief;
    }

    public int getAllFalsePositiveTrust() {
        return allFalsePositiveTrust;
    }

    public int getAllFalseNegativeTrust() {
        return allFalseNegativeTrust;
    }

    public int getAllTruePositiveTrust() {
        return allTruePositiveTrust;
    }

    public int getAllTrueNegativeTrust() {
        return allTrueNegativeTrust;
    }

    public WorldStatistics getxPrevStats() {
        return xPrevStats;
    }

    public float getTrustTpRate() {
        return trustTpRate;
    }

    public float getTrustTnRate() {
        return trustTnRate;
    }

    public int getTrustTpRate100I() {
        return (int) (trustTpRate * 100);
    }

    public int getTrustTnRate100I() {
        return (int) (trustTnRate * 100);
    }

    public float getTrustFpRate() {
        return trustFpRate;
    }

    public float getTrustFnRate() {
        return trustFnRate;
    }

    public int getTrustFpRate100I() {
        return (int) (trustFpRate * 100);
    }

    public int getTrustFnRate100I() {
        return (int) (trustFnRate * 100);
    }

    //============================//============================//============================ Timed Average

    public int getTimedAvgAgentTarget() {

        if (xPrevStats == null) {
            timedAverageTarget = allAgentsInTarget / (worldTime == 0 ? 1 : worldTime);
        } else {
            timedAverageTarget = (allAgentsInTarget - xPrevStats.allAgentsInTarget) / Config.STATISTICS_AVERAGE_TIME_WINDOW;
        }

        return timedAverageTarget;
    }

    public int getTimedAvgAgentInPitfall() {
        if (xPrevStats == null) {
            timedAveragePitfall = allAgentsInPitfall / (worldTime == 0 ? 1 : worldTime);
        } else {
            timedAveragePitfall = (allAgentsInPitfall - xPrevStats.allAgentsInPitfall) / Config.STATISTICS_AVERAGE_TIME_WINDOW;
        }

        return timedAveragePitfall;
    }

    public int getTimedAvgRandomTravel() {
        if (xPrevStats == null) {
            return (allRandomTravelToNeighbor / (worldTime == 0 ? 1 : worldTime));
        } else {
            return ((allRandomTravelToNeighbor - xPrevStats.allRandomTravelToNeighbor) / Config.STATISTICS_AVERAGE_TIME_WINDOW);
        }

    }

    public int getTimedAvgTruePositive() {
        if (xPrevStats == null) {
            return (allTruePositiveTrust / (worldTime == 0 ? 1 : worldTime));
        } else {
            return ((allTruePositiveTrust - xPrevStats.allTruePositiveTrust) / Config.STATISTICS_AVERAGE_TIME_WINDOW);
        }
    }

    public int getTimedAvgTrueNegative() {
        if (xPrevStats == null) {
            return (allTrueNegativeTrust / (worldTime == 0 ? 1 : worldTime));
        } else {
            return ((allTrueNegativeTrust - xPrevStats.allTrueNegativeTrust) / Config.STATISTICS_AVERAGE_TIME_WINDOW);
        }
    }

    public int getTimedAvgFalsePositive() {
        if (xPrevStats == null) {
            return (allFalsePositiveTrust / (worldTime == 0 ? 1 : worldTime));
        } else {
            return ((allFalsePositiveTrust - xPrevStats.allFalsePositiveTrust) / Config.STATISTICS_AVERAGE_TIME_WINDOW);
        }
    }

    public int getTimedAvgFalseNegative() {
        if (xPrevStats == null) {
            return (allFalseNegativeTrust / (worldTime == 0 ? 1 : worldTime));
        } else {
            return ((allFalseNegativeTrust - xPrevStats.allFalseNegativeTrust) / Config.STATISTICS_AVERAGE_TIME_WINDOW);
        }
    }

    public int getTimeAvgTrustToAdversary() {
        if (xPrevStats == null) {
            return (allTrustToAdversary / (worldTime == 0 ? 1 : worldTime));
        } else {
            return ((allTrustToAdversary - xPrevStats.allTrustToAdversary) / Config.STATISTICS_AVERAGE_TIME_WINDOW);
        }
    }

    public int getTimeAvgTrustToHonest() {
        if (xPrevStats == null) {
            return (allTrustToHonest / (worldTime == 0 ? 1 : worldTime));
        } else {
            return ((allTrustToHonest - xPrevStats.allTrustToHonest) / Config.STATISTICS_AVERAGE_TIME_WINDOW);
        }
    }

    public int getTimeAvgTrustToMischief() {
        if (xPrevStats == null) {
            return (allTrustToMischief / (worldTime == 0 ? 1 : worldTime));
        } else {
            return ((allTrustToMischief - xPrevStats.allTrustToMischief) / Config.STATISTICS_AVERAGE_TIME_WINDOW);
        }
    }

    public int getTimeAvgTrustToHypocrite() {
        if (xPrevStats == null) {
            return (allTrustToHypocrite / (worldTime == 0 ? 1 : worldTime));
        } else {
            return ((allTrustToHypocrite - xPrevStats.allTrustToHypocrite) / Config.STATISTICS_AVERAGE_TIME_WINDOW);
        }

    }

    public int getAllRandomTravelToNeighbor() {
        return allRandomTravelToNeighbor;
    }

    public long getAllFluctuation() {
        return allFluctuation;
    }

    public long getAllFluctuationRound() {
        return allFluctuationRound;
    }


    public int[] getAllEffectiveFluctuationResistanceNumber() {
        return allEffectiveFluctuationResistanceNumber;
    }

    int[] currentEffectiveFluctuationResistanceNumber;
    int[] currentAvgEffectiveFluctuationResistanceNumber;

    public int[] getEffectiveFluctuationResistanceNumber() {

        if (prevStats == null) {
            return allEffectiveFluctuationResistanceNumber;
        }

        if (currentEffectiveFluctuationResistanceNumber == null ||
                currentEffectiveFluctuationResistanceNumber.length != allEffectiveFluctuationResistanceNumber.length) {
            currentEffectiveFluctuationResistanceNumber = new int[allEffectiveFluctuationResistanceNumber.length];
        }


        for (int i = 0; i < allEffectiveFluctuationResistanceNumber.length; i++) {
            currentEffectiveFluctuationResistanceNumber[i] = allEffectiveFluctuationResistanceNumber[i] - prevStats.getAllEffectiveFluctuationResistanceNumber()[i];
        }
        return currentEffectiveFluctuationResistanceNumber;
    }


    public int[] getAvgEffectiveFluctuationResistanceNumber() {

        if (currentAvgEffectiveFluctuationResistanceNumber == null ||
                currentAvgEffectiveFluctuationResistanceNumber.length != allEffectiveFluctuationResistanceNumber.length) {
            currentAvgEffectiveFluctuationResistanceNumber = new int[allEffectiveFluctuationResistanceNumber.length];
        }

        if (xPrevStats == null) {
            for (int i = 0; i < allEffectiveFluctuationResistanceNumber.length; i++) {
                currentAvgEffectiveFluctuationResistanceNumber[i] = allEffectiveFluctuationResistanceNumber[i] /
                        (worldTime == 0 ? 1 : worldTime);
            }
        } else {
            for (int i = 0; i < allEffectiveFluctuationResistanceNumber.length; i++) {
                currentAvgEffectiveFluctuationResistanceNumber[i] =
                        (allEffectiveFluctuationResistanceNumber[i] - xPrevStats.allEffectiveFluctuationResistanceNumber[i])
                                / Config.STATISTICS_AVERAGE_TIME_WINDOW;
            }
        }
        return currentAvgEffectiveFluctuationResistanceNumber;
    }

    public void setAllEffectiveFluctuationResistanceNumber(int[] allEffectiveFluctuationResistanceNumber) {
        this.allEffectiveFluctuationResistanceNumber = allEffectiveFluctuationResistanceNumber;
    }

    public void add_NegativePop() {
        positivePop++;
    }

    public void add_HonestCollaboration() {
        honestCollaboration[currentHonestCollaboration]++;
    }

    public void add_PositivePop() {
        positivePop++;
    }

    public void resetPopulation() {
        negativePop = 0;
        positivePop = 0;

    }

    public void resetCollaboration() {
        currentHonestCollaboration++;
        if(currentHonestCollaboration>=Config.STATISTICS_AVERAGE_TIME_WINDOW)
        {
            currentHonestCollaboration=0;
        }
        honestCollaboration[currentHonestCollaboration] = 0;
    }

    public int getHonestCollaboration() {
        return honestCollaboration[currentHonestCollaboration];
    }

    public int getAvgHonestCollaboration() {

        if (worldTime < Config.STATISTICS_AVERAGE_TIME_WINDOW) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < Config.STATISTICS_AVERAGE_TIME_WINDOW; i++) {
            sum += honestCollaboration[i];
        }

        return sum / Config.STATISTICS_AVERAGE_TIME_WINDOW;
    }

    public int getCurrentHonestCollaboration() {
        return currentHonestCollaboration;
    }
}
