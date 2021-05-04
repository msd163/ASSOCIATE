package stateTransition;

import com.sun.javafx.geom.Point2D;

import java.util.ArrayList;
import java.util.Arrays;

public class Environment {

    private int stateCount;

    private StateTrans stateTrans[];

    public void init() {
        stateCount = stateTrans == null ? 0 : stateTrans.length;

        int r = 1;
        double theta = 0;
        for (int pop = 0; pop < stateCount; pop++) {
            StateTrans curState = getTransition(pop);
            ArrayList<StateTrans> final_idx = curState.getTargets();
            int size = final_idx.size();

            System.out.println("state " + pop + " have out degree " + getTransitionOutDegree(pop));
            if (!curState.isHasLoc()) {
                curState.setLocation(new Point2D((float) (r * 80 * Math.sin(theta)),
                        (float) (r * 80 * Math.cos(theta))));
                theta = theta + (3.14159 / 12.0);
                if (theta > (2.0 * 3.14159)) {
                    theta = 0.0;
                    r++;
                }
            }

            StateTrans final_temp;
            StateTrans x;
            for (int i = 0; i < size; i++) {
                x = final_idx.get(i);
                final_temp = getTransition(x.getId());
                if (final_temp.isHasLoc()) {
                    final_temp.setLocation(
                            new Point2D(
                                    (float) (r * 80 * Math.sin(theta) + curState.getLocation().x),
                                    (float) (r * 80 * Math.cos(theta)) + curState.getLocation().y));
                    theta = theta + (3.14159 / 12.0);
                    if (theta > (2.0 * 3.14159)) {
                        theta = 0.0;
                        r++;
                    }
                }
            }
        }
    }

    //============================//============================

    public int getStateCount() {
        return stateCount;
    }

    public void setStateCount(int stateCount) {
        stateTrans = new StateTrans[stateCount];
        this.stateCount = stateCount;
    }


    public StateTrans getTransition(int stateId) {
        return stateTrans[stateId];
    }

    public int getTransitionOutDegree(int pop) {
        return stateTrans[pop].getTargets().size();
    }

    public StateTrans[] getStateTrans() {
        return stateTrans;
    }

    public void setStateTrans(StateTrans[] stateTrans) {
        this.stateTrans = stateTrans;
    }

    @Override
    public String toString() {
        return "Environment{" +
                "stateCount=" + stateCount +
                ", stateTrans=" + Arrays.toString(stateTrans) +
                '}';
    }

    public StateTrans getRandomState() {


        return null;
    }
}
