package utils;

public class WorldStatistics {


    private WorldStatistics prevStats;      // previous statistics for calculating 'allTrustToHonest' and 'allTrustToDishonest'

    private int worldTime;                  // time of world in this statistics
    private int episode;                    // episode of world in this statistics
    //============================
    private int allAgentsInTarget;          //
    private int allAgentsInPitfall;
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
    private int allTrustToDishonest;        // all agents that trust to a 'dishonest' agent
    private int allTrustToHonest;           // all agents that trust to a 'honest' agent
    private int ittTrustToDishonest;        // agents that trust to a 'dishonest' agent in this time
    private int ittTrustToHonest;           // agents that trust to a 'honest' agent in this time
    //============================
    private int ittFalsePositiveTrust;
    private int ittFalseNegativeTrust;
    private int ittTruePositiveTrust;
    private int ittTrueNegativeTrust;

    private float trustAccuracy;
    private float trustSensitivity;
    private float trustSpecificity;

    //============================//============================

    public WorldStatistics(WorldStatistics prevStats) {
        this.prevStats = prevStats;
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
                = ittTrustToDishonest
                = ittTrustToHonest
                = ittFalsePositiveTrust
                = ittFalseNegativeTrust
                = ittTruePositiveTrust
                = ittTrueNegativeTrust
                = 0;


    }

    public void init(int episode) {
        this.episode = episode;

        if (prevStats != null && prevStats.episode == episode) {
            allTrustToDishonest = prevStats.getAllTrustToDishonest();
            allTrustToHonest = prevStats.getAllTrustToHonest();
        } else {

            allTrustToDishonest
                    = allTrustToHonest
                    = 0;
        }
    }

    public void add_All_AgentsInTarget() {
        allAgentsInTarget++;
    }

    public void add_Itt_AgentsInTarget() {
        ittAgentsInTarget++;
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
    }

    public void add_All_AgentsInPitfall() {
        allAgentsInPitfall++;
    }

    public int getTotalTravelToNeighbor() {
        return ittRandomTravelToNeighbors + ittSuccessTravelToNeighbor;
    }

    public void add_Itt_AgentsInPitfall() {
        ittAgentsInPitfall++;
    }

    public void add_Itt_TrustToHonest() {
        ittTrustToHonest++;
        allTrustToHonest++;
    }


    public void add_Itt_TrustToDishonest() {
        ittTrustToDishonest++;
        allTrustToDishonest++;
    }

    public void add_Itt_FalsePositiveTrust() {
        ittFalsePositiveTrust++;
    }


    public void add_Itt_FalseNegativeTrust() {
        ittFalseNegativeTrust++;
    }

    public void add_Itt_TruePositiveTrust() {
        ittTruePositiveTrust++;
    }


    public void add_Itt_TrueNegativeTrust() {
        ittTrueNegativeTrust++;
    }

    public void calcTrustParams() {
        trustSensitivity = (float) ittTruePositiveTrust / (ittTruePositiveTrust + ittFalseNegativeTrust);
        trustSpecificity = (float) ittTrueNegativeTrust / (ittTrueNegativeTrust + ittFalsePositiveTrust);
        trustAccuracy = (float) (ittTruePositiveTrust + ittTrueNegativeTrust) / (ittTruePositiveTrust + ittTrueNegativeTrust + ittFalsePositiveTrust + ittFalseNegativeTrust);
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

    public int getIttTrustToDishonest() {
        return ittTrustToDishonest;
    }

    public int getIttTrustToHonest() {
        return ittTrustToHonest;
    }

    public int getEpisode() {
        return episode;
    }

    public int getAllTrustToDishonest() {
        return allTrustToDishonest;
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

    public float getTrustSensitivity() {
        return trustSensitivity;
    }

    public float getTrustSpecificity() {
        return trustSpecificity;
    }
}
