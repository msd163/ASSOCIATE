package stateTransition;

import com.sun.javafx.geom.Point2D;
import system.World;
import utils.Globals;

import java.util.ArrayList;

public class Environment {

    private int stateCount;
    private StateTrans stateTrans[];
    private World world;

    public void init(World world) {
        stateCount = stateTrans == null ? 0 : stateTrans.length;
        this.world = world;

        // Assigning location to environment states
        int r = 1;
        double theta = 0;
        for (int pop = 0; pop < stateCount; pop++) {
            StateTrans curState = getTransition(pop);
            ArrayList<StateTrans> final_idx = curState.getTargets();
            int size = final_idx.size();

            System.out.println("state " + pop + " have out degree " + getTransitionOutDegree(pop));
            if (!curState.isHasLoc()) {
                curState.setLocation(new Point2D((int) (r * 80 * Math.sin(theta)),
                        (int) (r * 80 * Math.cos(theta))));
                theta = theta + (3.14159 / 12.0);
                if (theta > (2.0 * 3.14159)) {
                    theta = 0.0;
                    r++;
                }
                curState.setHasLoc(true);
            }

            StateTrans final_temp;
            StateTrans x;
            for (int i = 0; i < size; i++) {
                x = final_idx.get(i);
                final_temp = getTransition(x.getId());
                if (final_temp.isHasLoc()) {
                    final_temp.setLocation(
                            new Point2D(
                                    (int) (r * 80 * Math.sin(theta) + curState.getLocation().x),
                                    (int) (r * 80 * Math.cos(theta)) + curState.getLocation().y));
                    theta = theta + (3.14159 / 12.0);
                    if (theta > (2.0 * 3.14159)) {
                        theta = 0.0;
                        r++;
                    }
                }
            }
        }

        System.out.println(toString());
    }

    //============================//============================

    public int getStateCount() {
        return stateCount;
    }

    public void setStateCount(int stateCount) {
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
        tabIndex++;

        StringBuilder sts = new StringBuilder(ti + "[");
        if (stateTrans != null) {
            for (StateTrans b : stateTrans) {
                sts.append(b.toString(tabIndex)).append(",");
            }
        }
        sts.append(ti).append("]");

        return tx + "Environment{" +
                ti + "  stateCount=" + stateCount +
                ti + ", stateTrans=" + sts +
                tx + '}';
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public StateTrans getRandomState() {

        int i = Globals.RANDOM.nextInt(stateCount);

        return stateTrans[i];
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
