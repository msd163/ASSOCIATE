package stateTransition;

import utils.Globals;

public class DefTransition {
    private int st_one_idx;
    private int st_two_idx;
    private int act_idx;

    public int getSt_one_idx() {
        return st_one_idx;
    }

    public void setSt_one_idx(int st_one_idx) {
        this.st_one_idx = st_one_idx;
        Globals.environment.states[st_one_idx].incInDegree();
    }

    public int getSt_two_idx() {
        return st_two_idx;
    }

    public void setSt_two_idx(int st_two_idx) {
        this.st_two_idx = st_two_idx;
        Globals.environment.states[st_two_idx].incOutDegree();
    }

    public int getAct_idx() {
        return act_idx;
    }

    public void setAct_idx(int act_idx) {
        this.act_idx = act_idx;
    }


}
