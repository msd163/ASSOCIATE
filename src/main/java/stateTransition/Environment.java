package stateTransition;

import java.util.ArrayList;

public class Environment {
    private int stateCount;

    public DefTransition transitions[];

    public int getStateCount() {
        return stateCount;
    }

    public void setStateCount(int stateCount) {
        transitions = new DefTransition[stateCount];
        this.stateCount = stateCount;
    }

    public ArrayList<Integer> getTransitionFrom(int startState) {
        ArrayList<Integer> to = new ArrayList<Integer>();

        for (int i=0;i<stateCount;i++) {
            if (transitions[i].getState_idx() == startState) {
                {
                    int size = transitions[i].getFinal_idx().size();
                    ArrayList<Integer> final_idx = transitions[i].getFinal_idx();
                    for (int j = 0; j < size; j++)
                        to.add(final_idx.get(j));
                }
                break;
            }
        }
        return to;
    }

    public DefTransition getTransition(int pop) {
        return transitions[pop];
    }

    public int getTransitionOutDegree(int pop) {
        return transitions[pop].getFinal_idx().size();
    }

}
