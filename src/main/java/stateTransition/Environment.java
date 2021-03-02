package stateTransition;

import java.util.ArrayList;

public class Environment {
    private int stateCount;
    private int actionCount;
    private int transitionCount;
    private int initialState;

    public int getInitialState() {
        return initialState;
    }

    public void setInitialState(int initialState) {
        this.initialState = initialState;
    }

    public DefState states[];
    public DefAction actions[];
    public DefTransition transitions[];

    public int getStateCount() {
        return stateCount;
    }

    public void setStateCount(int stateCount) {
        states = new DefState[stateCount];
        this.stateCount = stateCount;
    }

    public int getActionCount() {
        return actionCount;
    }

    public void setActionCount(int actionCount) {
        actions = new DefAction[actionCount];
        this.actionCount = actionCount;
    }

    public int getTransitionCount() {
        return transitionCount;
    }

    public void setTransitionCount(int transitionCount) {
        transitions = new DefTransition[transitionCount];
        this.transitionCount = transitionCount;
    }

    public DefState getStartState(int transitionID) {
        return states[transitions[ transitionID ].getSt_one_idx()];
    }
    public DefState getEndState(int transitionID) {
        return states[transitions[ transitionID ].getSt_two_idx()];
    }

    public ArrayList<DefState> getTransitionFrom(DefState startState) {
        ArrayList<DefState> to = new ArrayList<DefState>();

        for (int i=0;i<stateCount;i++) {
            if (transitions[i].getSt_one_idx() == startState.getID())
                to.add(states[transitions[i].getSt_two_idx()]);
            if(to.size() == states[startState.getID()].getInDegree())
                break;
        }
        return to;
    }
}
