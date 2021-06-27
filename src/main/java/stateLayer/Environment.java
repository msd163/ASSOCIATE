package stateLayer;

import com.google.gson.annotations.Expose;
import systemLayer.Agent;
import systemLayer.World;
import utils.Globals;
import utils.ParsCalendar;
import utils.Point;

import java.util.ArrayList;

public class Environment {

    private static final double PI = 3.14159;

    public Environment() {
        String month = ParsCalendar.getInstance().getMonth();
        String day = ParsCalendar.getInstance().getDay();

        String hours = ParsCalendar.getInstance().getHours();
        String minutes = ParsCalendar.getInstance().getMinutes();


        code = month + day + "-" + hours + minutes;

        proMax = new EnvironmentProfilerMaxParams();
    }


    //============================//============================
    @Expose
    private EnvironmentProfilerMaxParams proMax;

    @Expose
    private String code = "";                               // unique code for identifying and tracing reports

    @Expose
    private String description = "";                          // Only for generating environment-x.json file

    //============================
    @Expose
    private String _C1 = "______________________________";    // Only for generating environment-x.json file

    //============================
    @Expose
    private int stateCount;
    @Expose
    private int pitfallCount;                       // final generated value from pitfallCount. used for exporting in environment.json file
    @Expose
    private int transitionCount;
    @Expose
    private int minimumTarget = Integer.MAX_VALUE;          // Only for generating environment-x.json file
    @Expose
    private int maximumTarget = 0;                          // Only for generating environment-x.json file

    //============================
    @Expose
    private String _C2 = "______________________________";    // Only for generating environment-x.json file

    //============================
    @Expose
    private int agentsCount;
    @Expose
    private int adversaryCount;                             // Only for generating environment-x.json file
    @Expose
    private int honestCount;                                // Only for generating environment-x.json file
    @Expose
    private int hypocriteCount;                   // Only for generating environment-x.json file
    @Expose
    private int mischiefCount;                                // Only for generating environment-x.json file
    @Expose
    private int certifiedCount;                               // Only for generating environment-x.json file. agents with certification

    @Expose
    private String _C3 = "______________________________";    // Only for generating environment-x.json file

    @Expose
    private ArrayList<StateX> states;

    //============================//============================//============================

    @Expose(deserialize = false, serialize = false)
    private World world;

    private TransitionX[] transitions;

    //============================//============================//============================

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

    public void initForSimulation(World world) throws Exception {
        this.world = world;
        initForGenerate();
    }

    public void initForGenerate() throws Exception {
        // maximum count of agents that this environment can keep.
        // it created for checking if agents count that set for world is not bigger that maximum capability of the environment.
        int maximumAgentCapability = 0;
        transitionCount = 0;
        if (stateCount > 0) {

            //============================  Updating state targets and setting state capability
            for (StateX state : states) {
                state.updateTargets(this);

                // transaction count
                transitionCount += state.getTargets().size();
                maximumAgentCapability += state.getCapacity();
            }

            //============================ Creating Transition list
            transitions = new TransitionX[transitionCount];
            int transIndex = 0;
            for (StateX state : states) {
                int targetSize = state.getTargets().size();
                if (targetSize > 0) {
                    if (targetSize > maximumTarget) {
                        maximumTarget = targetSize;
                    } else if (targetSize < minimumTarget) {
                        minimumTarget = targetSize;
                    }

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

        updateAgentsCount();


        if (maximumAgentCapability < agentsCount) {
            throw new Exception(">> Error: Agents count is bigger than maximum capability of environment:  " + agentsCount + " > " + maximumAgentCapability + ". simulation_X.json -> \"agentsCount\": " + agentsCount);
        }
    }

    public void updateAgentsCount() throws Exception {
        agentsCount
                = mischiefCount
                = honestCount
                = adversaryCount
                = certifiedCount
                = hypocriteCount = 0;

        for (StateX state : states) {
            if (state.getAgents() != null) {
                agentsCount += state.getAgents().size();
                for (Agent agent : state.getAgents()) {
                    agent.updateProfile();
                    if (agent.getBehavior().getHasHypocriteState()) {
                        hypocriteCount++;
                    } else if (agent.getBehavior().getHasHonestState()) {
                        honestCount++;
                    } else if (agent.getBehavior().getHasAdversaryState()) {
                        adversaryCount++;
                    } else if (agent.getBehavior().getHasMischief()) {
                        mischiefCount++;
                    }

                    if (agent.getTrust().isHasCertification()) {
                        if (!agent.getBehavior().getHasHonestState()) {
                            throw new Exception("Wrong certification for " + agent.getBehavior().getBehaviorState() + " agent.");
                        }
                        certifiedCount++;
                    }

                }
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
        for (int i = 0, statesLength = states.size(); i < statesLength; i++) {
            assignPoint(states.get(i), base, i, statesLength, radius);
        }

        //-- updating transaction path
        System.out.println("Transition count: " + getTransitions().length);

        for (TransitionX transition : getTransitions()) {
            transition.updatePath();
        }
    }

    public void updateProMaxParams() {
        proMax.setMaxExperienceCap(Globals.ProfileBunchMax.maxExperienceCap);
        proMax.setMaxExperienceItemCap(Globals.ProfileBunchMax.maxExperienceItemCap);
        proMax.setMaxIndirectExperienceCap(Globals.ProfileBunchMax.maxIndirectExperienceCap);
        proMax.setMaxIndirectExperienceItemCap(Globals.ProfileBunchMax.maxIndirectExperienceItemCap);

        proMax.setMaxObservationCap(Globals.ProfileBunchMax.maxObservationCap);
        proMax.setMaxObservationItemCap(Globals.ProfileBunchMax.maxObservationItemCap);
        proMax.setMaxIndirectObservationCap(Globals.ProfileBunchMax.maxIndirectObservationCap);
        proMax.setMaxIndirectObservationItemCap(Globals.ProfileBunchMax.maxIndirectObservationItemCap);

        proMax.setMaxTrustRecommendationItemCap(Globals.ProfileBunchMax.maxTrustRecommendationItemCap);
        proMax.setMaxTrustRecommendationCap(Globals.ProfileBunchMax.maxTrustRecommendationCap);
        proMax.setMaxWatchListCap(Globals.ProfileBunchMax.maxWatchListCap);
        proMax.setMaxWatchDepth(Globals.ProfileBunchMax.maxWatchDepth);
        proMax.setMaxTravelHistoryCap(Globals.ProfileBunchMax.maxTravelHistoryCap);

        proMax.setMaxAgentTargetCount(Globals.ProfileBunchMax.maxAgentTargetCount);
    }

    //============================//============================

    public int getPitfallCount() {
        return pitfallCount;
    }

    public int getTransitionCount() {
        return transitionCount;
    }

    public int getMinimumTarget() {
        return minimumTarget;
    }

    public int getMaximumTarget() {
        return maximumTarget;
    }

    public int getAdversaryCount() {
        return adversaryCount;
    }

    public int getHypocriteCount() {
        return hypocriteCount;
    }

    public int getMischiefCount() {
        return mischiefCount;
    }

    public int getHonestCount() {
        return honestCount;
    }

    public void setPitfallCount(int pitfallCount) {
        this.pitfallCount = pitfallCount;
    }

    public int getStateCount() {
        return stateCount;
    }

    public void setStateCount(int stateCount) {
        this.stateCount = stateCount;
    }

    public StateX getState(int stateId) {
        for (StateX state : states) {
            if (state.getId() == stateId) {
                return state;
            }
        }
        return null;
    }


    public ArrayList<StateX> getStates() {
        return states;
    }

    public void setStates(ArrayList<StateX> states) {
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

        return states.get(i);
    }

    public StateX getRandomSafeState() {

        int i = Globals.RANDOM.nextInt(stateCount);


        return states.get(i);
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

    public int getAgentsCount() {
        return agentsCount;
    }

    public void setAgentsCount(int agentsCount) {
        this.agentsCount = agentsCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public EnvironmentProfilerMaxParams getProMax() {
        return proMax;
    }

    public int getCertifiedCount() {
        return certifiedCount;
    }
    public void addCertifiedCount() {
         certifiedCount++;
    }
}
