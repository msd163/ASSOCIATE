package utils;

public class WorldStatistics {
    private int worldTime;
    private int allInTargetAgents;
    private int inTargetAgentsInThisTime;
    private int fullStateCount;
    private int successTravelToGoToNeighbor;
    private int failedTravelToGoToNeighbor;
    private int agentsWithNoTargetState;
    private int updatedNextStepsOfAgents;
    private int randomTravelToNeighbors;
    private int statesWithNoTarget;
    private int agentsInPitfall;

    //============================//============================


    public WorldStatistics() {
        worldTime
                = allInTargetAgents
                = inTargetAgentsInThisTime
                = fullStateCount
                = successTravelToGoToNeighbor
                = failedTravelToGoToNeighbor
                = agentsWithNoTargetState
                = updatedNextStepsOfAgents
                = randomTravelToNeighbors
                = statesWithNoTarget
                = agentsInPitfall
                = 0;
    }

    public void addAllInTargetAgents() {
        allInTargetAgents++;
    }

    public void addInTargetAgentsInThisTime() {
        inTargetAgentsInThisTime++;
    }

    public void addFullStateCount() {
        fullStateCount++;
    }

    public void addSuccessTravelToGoToNeighbor() {
        successTravelToGoToNeighbor++;
    }

    public void addFailedTravelToGoToNeighbor() {
        failedTravelToGoToNeighbor++;
    }

    public void addAgentsWithNoTargetState() {
        agentsWithNoTargetState++;
    }

    public void addStatesWithNoTarget() {
        statesWithNoTarget++;
    }

    public void addUpdatedNextStepsOfAgents() {
        updatedNextStepsOfAgents++;
    }

    public void addRandomTravelToNeighbors() {
        randomTravelToNeighbors++;
    }

    public void addAgentsInPitfall() {
        agentsInPitfall++;
    }

    public int getTotalTravelToNeighbor() {
        return randomTravelToNeighbors + successTravelToGoToNeighbor;
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
                ti + ", allInTargetAgents = " + allInTargetAgents +
                ti + ", inTargetAgentsInThisTime = " + inTargetAgentsInThisTime +
                ti + ", updatedNextStatesOfAgents = " + updatedNextStepsOfAgents +
                ti + ", successTravelToGoToNeighbor = " + successTravelToGoToNeighbor +
                ti + ", failedTravelToGoToNeighbor = " + failedTravelToGoToNeighbor +
                ti + ", randomTravelToNeighbors = " + randomTravelToNeighbors +
                ti + ", agentsInPitfall = " + agentsInPitfall +
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

    public int getAllInTargetAgents() {
        return allInTargetAgents;
    }

    public int getInTargetAgentsInThisTime() {
        return inTargetAgentsInThisTime;
    }

    public int getFullStateCount() {
        return fullStateCount;
    }

    public int getSuccessTravelToGoToNeighbor() {
        return successTravelToGoToNeighbor;
    }

    public int getFailedTravelToGoToNeighbor() {
        return failedTravelToGoToNeighbor;
    }

    public int getAgentsWithNoTargetState() {
        return agentsWithNoTargetState;
    }

    public int getUpdatedNextStepsOfAgents() {
        return updatedNextStepsOfAgents;
    }

    public int getRandomTravelToNeighbors() {
        return randomTravelToNeighbors;
    }

    public int getStatesWithNoTarget() {
        return statesWithNoTarget;
    }

    public int getAgentsInPitfall() {
        return agentsInPitfall;
    }

}
