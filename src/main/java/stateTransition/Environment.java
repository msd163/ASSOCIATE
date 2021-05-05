package stateTransition;

import system.World;
import utils.Globals;
import utils.Point;

public class Environment {

    private static final double PI = 3.14159;
    private int stateCount;
    private StateX states[];
    private World world;


    private void assignPoint(StateX stateX, Point base, int targetIndex, int radius) {

        if (!stateX.isHasLoc()) {

            double theta = 0;
            int newRadius = radius;
            if (stateX.hasTarget()) {
                newRadius *= 2;
                int targetCount = stateX.getTargets().size();
                theta = targetIndex * (PI / targetCount);
            }

            stateX.setLocation(
                    new Point(
                            base.getX() + (int) (newRadius * Math.sin(theta)),
                            base.getY() + (int) (newRadius * Math.cos(theta)))
            );
            stateX.setHasLoc(true);
        }

        if (stateX.hasTarget()) {
            int index = 0;
            for (StateX target : stateX.getTargets()) {
                assignPoint(target, stateX.getLocation(), index, radius);
                index++;
            }
        }
    }

    public void init(World world) {
        stateCount = states == null ? 0 : states.length;
        this.world = world;

        if(stateCount>0) {
            // Assigning location to environment states
            Point base = new Point(
                    world.getWidth() / 2,
                    world.getHeight() / 2
            );
            int radius = Math.min(base.getX(), base.getY()) / stateCount;
            int index = 0;
            for (StateX state : states) {
                assignPoint(state, base, index, radius);
                index++;
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


    public StateX getState(int stateId) {
        return states[stateId];
    }


    public StateX[] getStates() {
        return states;
    }

    public void setStates(StateX[] states) {
        this.states = states;
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
        if (states != null) {
            for (StateX b : states) {
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

    public StateX getRandomState() {

        int i = Globals.RANDOM.nextInt(stateCount);

        return states[i];
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
