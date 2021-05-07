package stateTransition;

import profiler.DefParameter;
import system.World;
import utils.Globals;
import utils.Point;

import java.util.ArrayList;

public class Environment {

    private static final double PI = 3.14159;
    private int stateCount;
    private String stateCapacity;
    private StateX[] states;
    private World world;
    private TransitionX[] transitions;

    private DefParameter stateCapacityD;

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
            ArrayList<StateX> targets = stateX.getTargets();
            for (int i = 0, targetsSize = targets.size(); i < targetsSize; i++) {
                if (!targets.get(i).isHasLoc()) {
                    assignPoint(targets.get(i), stateX.getLocation(), i, radius);
                }
            }
        }
    }

    public void init(World world) {
        stateCapacityD = new DefParameter(stateCapacity);

        stateCount = states == null ? 0 : states.length;
        this.world = world;

        if (stateCount > 0) {

            int transCount = 0;     // Count of transitions

            //============================  Updating state targets
            for (StateX state : states) {
                state.updateTargets(this);
                transCount += state.getTargets().size();
            }

            // Assigning location to environment states and setting state capacity
            Point base = new Point(
                    600,
                    300
            );
            // space size between states
            int radius = getWorld().getAgentsCount() * 5;
            for (int i = 0, statesLength = states.length; i < statesLength; i++) {
                assignPoint(states[i], base, i, radius);
                states[i].setCapacity(getStateCapacityValue());
            }

            //============================ Creating Transition list
            transitions = new TransitionX[transCount];
            int transIndex = 0;
            for (StateX state : states) {
                if (state.getTargets().size() > 0) {
                    for (StateX target : state.getTargets()) {
                        transitions[transIndex] = new TransitionX(state, target);
                        state.addTargetTrans(transitions[transIndex]);
                        target.addSourceTrans(transitions[transIndex]);
                        transIndex++;
                    }
                }
            }

        } else {
            transitions = new TransitionX[0];
        }
       // System.out.println(toString());
    }

    public void updateTransitionsPath() {
        for (TransitionX transition : getTransitions()) {
            transition.updatePath();
        }
    }
    //============================//============================

    public int getStateCapacityValue() {
        return stateCapacityD.nextValue();
    }

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

    public TransitionX[] getTransitions() {
        return transitions;
    }

    public void setStateCapacity(String stateCapacity) {
        this.stateCapacity = stateCapacity;
    }

    public String getStateCapacity() {
        return stateCapacity;
    }
}
