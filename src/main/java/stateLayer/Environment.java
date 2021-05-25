package stateLayer;

import _type.TtSimulationMode;
import com.google.gson.annotations.Expose;
import systemLayer.World;
import systemLayer.profiler.DefParameter;
import utils.Config;
import utils.Globals;
import utils.Point;

import java.util.ArrayList;

public class Environment {

    private static final double PI = 3.14159;

    //============================
    @Expose
    private boolean isAutoGenerated;
    //============================ For pureEnvironment processing

    // Maximum count of agents in state
    private String stateCapacity;
    private DefParameter stateCapacityD;

    // The number of targets for each state
    private String stateTargetCount;
    private DefParameter stateTargetCountD;

    //============================

    @Expose
    private int stateCount;
    @Expose
    private int agentsCount;
    // maximum count of agents that this environment can keep.
    // it created for checking if agents count that set for world is not bigger that maximum capability of the environment.
    private int maximumAgentCapability;
    @Expose
    private StateX[] states;
    private World world;
    private TransitionX[] transitions;

    /**
     * Assigning point to states in environment
     *
     * @param stateX
     * @param base
     * @param targetIndex
     * @param radius
     */
    private void assignPoint(StateX stateX, Point base, int targetIndex, int targetCount, int radius) {

        if (!stateX.isHasLoc()) {

            double theta;
            int newRadius = radius;
            if (stateX.hasTarget()) {
                newRadius *= 2;
            }
            float radiusFactor = 1;

            if (targetIndex >= targetCount) {
                targetIndex = 0;
                radiusFactor += 0.5f;
            }
            theta = targetIndex * (PI / targetCount);

            boolean isConflict;
            // this do-while is for preventing overlapping state rectangles
            do {
                isConflict = false;
                stateX.setLocation(
                        new Point(
                                base.getX() + (int) (newRadius * radiusFactor * Math.sin(theta)),
                                base.getY() + (int) (newRadius * radiusFactor * Math.cos(theta)))
                );


                for (StateX state : states) {
                    if (state.isHasLoc() &&
                            (stateX.getBoundedRectangle().isOverlapping(state.getBoundedRectangle()))
                    ) {
                   /*     targetIndex++;
                        if (targetIndex >= targetCount) {
                            targetIndex = 0;
                            radiusFactor += 0.5f;
                        }
                        theta = targetIndex * (PI / targetCount);*/

                        radiusFactor += 0.5f;
                        isConflict = true;
                        break;
                    }
                }
            } while (isConflict);


            stateX.setHasLoc(true);
        }

        if (stateX.hasTarget()) {
            ArrayList<StateX> targets = stateX.getTargets();
            for (int i = 0, targetsSize = targets.size(); i < targetsSize; i++) {
                if (!targets.get(i).isHasLoc()) {
                    assignPoint(targets.get(i), stateX.getLocation(), i, targetsSize, radius);
                }
            }
        }
    }

    public void init(World world) {
        maximumAgentCapability = 0;
        if (Config.SIMULATION_MODE == TtSimulationMode.PureEnvironment) {
            stateCapacityD = new DefParameter(stateCapacity);
        }

        this.world = world;

        stateCount = states == null ? 0 : states.length;

        if (stateCount > 0) {

            int transCount = 0;     // Count of transitions

            //============================  Updating state targets and setting state capability
            for (StateX state : states) {
                // updating targets
                state.updateTargets(this);
                if (Config.SIMULATION_MODE == TtSimulationMode.PureEnvironment) {
                    // set state capability
                    state.setCapacity(getStateCapacityValue());
                }
                // transaction count
                transCount += state.getTargets().size();
                maximumAgentCapability+= state.getCapacity();
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

    public void updateAgentsCount() {
        agentsCount = 0;
        for (StateX state : states) {
            if (state.getAgents() != null) {
                agentsCount += state.getAgents().size();
            }
        }
    }

    public void reassigningStateLocationAndTransPath() {

        //-- Assigning location to environment states and setting state capacity
        Point base = new Point(
                600,
                300
        );
        // space size between states
        int radius = getWorld().getAgentsCount() * 2;
        for (int i = 0, statesLength = states.length; i < statesLength; i++) {
            assignPoint(states[i], base, i, statesLength, radius);
        }

        //-- updating transaction path
        for (TransitionX transition : getTransitions()) {
            transition.updatePath();
        }
    }
    //============================//============================

    public int getStateCapacityValue() {
        return stateCapacityD.nextValue();
    }

    public int getStateTargetCountValue() {
        return stateTargetCountD.nextValue();
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

    public String getStateTargetCount() {
        return stateTargetCount;
    }

    public void setStateTargetCount(String stateTargetCount) {
        this.stateTargetCount = stateTargetCount;
    }

    public void setStateTargetCountD(DefParameter stateTargetCountD) {
        this.stateTargetCountD = stateTargetCountD;
    }

    public boolean isIsAutoGenerated() {
        return isAutoGenerated;
    }

    public void setIsAutoGenerated(boolean autoGenerated) {
        isAutoGenerated = autoGenerated;
    }

    public int getAgentsCount() {
        return agentsCount;
    }

    public void setAgentsCount(int agentsCount) {
        this.agentsCount = agentsCount;
    }

    public int getMaximumAgentCapability() {
        return maximumAgentCapability;
    }

    public void setMaximumAgentCapability(int maximumAgentCapability) {
        this.maximumAgentCapability = maximumAgentCapability;
    }
}
