package stateTransition;

import com.sun.javafx.geom.Point2D;
import utils.Globals;

import java.util.ArrayList;

public class DefTransition {
    private int state_idx;
    private ArrayList<Integer> final_idx;
    private ArrayList<Integer> who_is_here;
    private int nodeTrafficCapacity;

    public int getNodeTrafficCapacity() {
        return nodeTrafficCapacity;
    }

    public void setNodeTrafficCapacity(int nodeTrafficCapacity) {
        this.nodeTrafficCapacity = nodeTrafficCapacity;
    }

    public DefTransition()
    {
        hasLoc = false;
    }

    public int  getState_idx() {
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
    public Point2D getLocation()
    {
        return myLoc;
    }
    public void setLocation(Point2D lc)
    {
        myLoc = lc;
        hasLoc =true;
    }

    public boolean I_am_in(Integer st_idx)
    {
        if(getTraffic() < nodeTrafficCapacity) {
            boolean add = true;
            for (int i = 0; i < who_is_here.size(); i++) {
                if (who_is_here.get(i) == st_idx) {
                    add = false;
                }
            }
            if (add == true)
                who_is_here.add(st_idx);
            return true;
        }
        else
        {
            return  false;
        }
    }

    public void I_am_out(Integer st_idx)
    {
        for (int i=0;i<who_is_here.size();i++)
        {
            if(who_is_here.get(i) == st_idx)
            {
                who_is_here.remove(i) ;
                break;
            }
        }
    }

    public int getTraffic()
    {
        return who_is_here.size();
    }
}
