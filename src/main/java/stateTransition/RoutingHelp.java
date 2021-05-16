package stateTransition;

import system.Agent;

public class RoutingHelp {

    private Agent helperAgent;
    private int stepToTarget;
    private StateX nextState;

    //============================//============================


    public Agent getHelperAgent() {
        return helperAgent;
    }

    public void setHelperAgent(Agent helperAgent) {
        this.helperAgent = helperAgent;
    }

    public int getStepToTarget() {
        return stepToTarget;
    }

    public void setStepToTarget(int stepToTarget) {
        this.stepToTarget = stepToTarget;
    }

    public StateX getNextState() {
        return nextState;
    }

    public void setNextState(StateX nextState) {
        this.nextState = nextState;
    }
}
