package stateTransition;

import com.sun.javafx.geom.Point2D;
import utils.Globals;

import java.util.ArrayList;

public class DefTransition {
    private int state_idx;
    private ArrayList<Integer> final_idx;

    public DefTransition()
    {
        hasLoc = false;
    }
    public int getState_idx() {
        return state_idx;
    }

    public void setState_idx(int state_idx) {
        this.state_idx = state_idx;
    }

    public ArrayList<Integer> getFinal_idx() {
        return final_idx;
    }

    public void setFinal_idx(ArrayList<Integer> final_idx) {
        this.final_idx = final_idx;
    }
    public boolean hasLoc;
    private Point2D myLoc;
    public Point2D getLocaiton()
    {
        return myLoc;
    }
    public void setLocaiton(Point2D lc)
    {
        myLoc = lc;
        hasLoc =true;
    }
}
