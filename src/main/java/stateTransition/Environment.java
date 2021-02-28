package stateTransition;

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
}
