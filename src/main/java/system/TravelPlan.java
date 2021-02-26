package system;

public class TravelPlan {

    private MapPoint[] goals;

    private int currentGoalIndex;

    //============================


    //============================

    public MapPoint[] getGoals() {
        return goals;
    }

    public void setGoals(MapPoint[] goals) {
        this.goals = goals;
    }

    public int getCurrentGoalIndex() {
        return currentGoalIndex;
    }

    public void setCurrentGoalIndex(int currentGoalIndex) {
        this.currentGoalIndex = currentGoalIndex;
    }
}
