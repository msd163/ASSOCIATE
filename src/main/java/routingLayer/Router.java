package routingLayer;

import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import _type.TtTrustMethodology;
import stateLayer.StateX;
import stateLayer.TransitionX;
import stateLayer.TravelHistory;
import systemLayer.Agent;
import systemLayer.WatchedAgent;
import systemLayer.WatchedState;
import systemLayer.World;
import utils.Config;
import utils.Globals;
import utils.OutLog____;
import utils.WorldStatistics;

import java.util.ArrayList;
import java.util.List;

public class Router {

    private WorldStatistics statistics___;
    private World world;

    public Router(World world) {
        this.world = world;
    }

    public void setStatistics(WorldStatistics statistics) {
        this.statistics___ = statistics;
    }

    /**
     * Guide the agent towards the target by one step.
     * Each time this function is called, the agent moves one step towards the target.
     *
     * @param agent The agent to be navigated.
     * @return Final state of the agent after navigation.
     */
    public StateX takeAStepTowardTheTarget(Agent agent) {

        //============================
        StateX targetState = agent.getCurrentTarget();
        StateX state = agent.getState();
        //============================

        // There is no targetState for this agent!
        if (targetState == null) {
            statistics___.addAgentsWithNoTargetState();
            return null;
        }

        // If the agent is in target agent
        if (agent.isInTargetState()) {
            statistics___.add_All_AgentsInTarget();
            agent.clearNextSteps();
            agent.addSpentTimeAtTheTarget();
            //if (!agent.isInFinalTarget()) {
            if (agent.getCurrentTargetStateIndex() < Globals.EPISODE) {
                int ct = agent.getCurrentTarget().getId();
                agent.assignNextTargetState();
                OutLog____.pl(TtOutLogMethodSection.TakeAStepTowardTheTarget, TtOutLogStatus.SUCCESS,
                        "Assigning new target to agent (" + agent.getId() + "). current target: " + ct + " | new target: " + agent.getCurrentTarget().getId());
            }
            //}
            return state;
        }

        if (state.isIsPitfall()) {
            statistics___.add_All_AgentsInPitfall();

            OutLog____.pl(
                    TtOutLogMethodSection.TakeAStepTowardTheTarget,
                    TtOutLogStatus.WARN,
                    "In PITFALL",
                    agent, state, agent.getCurrentTarget());

            return state;
        }

        // the state has no output state. The agent is in an state that is pit
        if (state.getTargets().size() == 0) {
            agent.clearNextSteps();
            // Printing map
      /*      String sIds = "";
            for (TravelHistory s : agent.getTravelHistories()) {
                sIds += " | " + s.getStateX().getId() *//*+ "-" + s.getVisitTime()*//*;
            }*/
            //System.out.println(">>ERR>> no target for agent:::gotoSate::agentId: " + agent.getId() + " [ c: " + state.getId() + " >  t: " + targetState.getId() + " ] #  maps: " + sIds);
            OutLog____.pl(
                    TtOutLogMethodSection.TakeAStepTowardTheTarget,
                    TtOutLogStatus.ERROR,
                    "state has no target",
                    agent, state, agent.getCurrentTarget());


            return state;
        }

     /*   // If there is not any states in the agent history, the nextSteps of agent have to be updated.
        if (agent.getNextSteps().isEmpty()) {
            //============================//============================//============================
            //============================//============================
            //============================
            updateNextSteps(agent, targetState);
            //============================
            //============================//============================
            //============================//============================//============================
            statistics___.add_Itt_UpdatedNextStep();
        }*/

        // If the nextSteps is empty after updating the nextSteps, the agent will go to one neighbor randomly.
        if (agent.getNextSteps().isEmpty()) {
            int targetIndex = Globals.RANDOM.nextInt(state.getTargets().size());
            StateX stateX = gotoNeighborState(agent, targetIndex);
            statistics___.addRandomTravelToNeighbors();
            if (stateX.getId() == agent.getCurrentTarget().getId()) {
                statistics___.add_Itt_AgentsInTarget();
                statistics___.add_All_AgentsInTarget();
            } else if (stateX.isIsPitfall()) {
                statistics___.add_Itt_AgentsInPitfall();
                statistics___.add_All_AgentsInPitfall();
            }
            return stateX;
        }

        //-- Getting first entity of the nextSteps of the agent.
        StateX nextState = agent.getNextSteps().get(0);

        if (!isStateTheNeighborOfAgent(agent, nextState)) {
            statistics___.add_Itt_FailedTravelToNeighbor();
            OutLog____.pl(
                    TtOutLogMethodSection.TakeAStepTowardTheTarget,
                    TtOutLogStatus.ERROR,
                    "Next state (" + nextState.getId() + ") is not neighbor",
                    agent, state, agent.getCurrentTarget());

            //-- Clearing agent next steps for regenerating next steps, in order to resolve problem.
            agent.clearNextSteps();

            return null;
        }

        int currentAgentStateId = agent.getState().getId();
        /**============================
         ==============================**/
        StateX finalState = gotoNeighborState(agent, nextState);
        /**============================
         ==============================**/
        //-- Successfully traveling to neighbor.
        if (finalState.getId() == nextState.getId()) {
            agent.getNextSteps().remove(0);
            statistics___.add_Itt_SuccessTravelToNeighbor();
            //
            if (finalState.isIsPitfall()) {
                statistics___.add_Itt_AgentsInPitfall();
                statistics___.add_All_AgentsInPitfall();
                Globals.trustManager.reduceTrustForPitfall(agent);
            }
            //
            else if (finalState.getId() == agent.getCurrentTarget().getId()) {
                statistics___.add_Itt_AgentsInTarget();
                statistics___.add_All_AgentsInTarget();
                Globals.trustManager.increaseTrustForSuccessTarget(agent);
            }
        }
        //-- Failed to travel to neighbor.
        else {
            statistics___.add_Itt_FailedTravelToNeighbor();
            System.out.println(">> Router.takeAStepTowardTheTarget:: [Warning] Agent (" + agent.getId() +
                    ") can not travel from (" + currentAgentStateId +
                    ") to neighbor state (" + nextState.getId() +
                    ") and is now in state (" + finalState.getId() + ").");
        }
        return finalState;
    }

    /**
     * Updating next states to reach the 'goalState;.
     *
     * @param agent The agent to be navigated.
     */
    public void updateNextSteps(Agent agent) {

        if (!agent.getNextSteps().isEmpty()) {
            return;
        }

        if (Config.TRUST_METHODOLOGY == TtTrustMethodology.FullyRandomly) {
            return;
        }

        StateX goalState = agent.getCurrentTarget();

        //============================//============================  WatchedStates
        List<WatchedState> watchedStates = agent.getWatchedStates();

        //============================
        agent.clearNextSteps();
        //============================ (1) If the agent can watch the goalState in its target.
        if (watchedStates != null) {
            for (WatchedState ws : watchedStates) {
                if (ws.getStateX().getId() == goalState.getId()) {
                    agent.getNextSteps().addAll(ws.getPath());
                    OutLog____.pl(TtOutLogMethodSection.UpdateNextStep, TtOutLogStatus.SUCCESS, "Self Visiting", agent, agent.getState(), goalState);
                    return;
                }
            }
        }

        //============================//============================ WatchedAgents
        List<WatchedAgent> watchedAgents = agent.getWatchedAgents();

        if (watchedAgents == null) {
            OutLog____.pl(TtOutLogMethodSection.UpdateNextStep, TtOutLogStatus.FAILED, "watchedAgents is NULL", agent, agent.getState(), goalState);
            return;
        }

        RoutingHelp routingHelp;

        //-- Asking from watched list and filling routingHelps. all watchedAgents than know where is the goal state
        ArrayList<RoutingHelp> routingHelps = new ArrayList<>();
        for (int i = 0, watchedAgentsSize = watchedAgents.size(); i < watchedAgentsSize; i++) {
            WatchedAgent wa = watchedAgents.get(i);
            routingHelp = doYouKnowWhereIs(wa.getAgent(), goalState);

            if (routingHelp != null) {
                routingHelp.setStepFromAgentToHelper(wa.getPathSize());
                //============================//============================ _Trust
                routingHelp.setTrustLevel(Globals.trustManager.getTrustLevel(agent, wa.getAgent()));
                //============================//============================
                routingHelps.add(routingHelp);
            }
        }

        // If there is no routerHelper...
        if (routingHelps.isEmpty()) {
            OutLog____.pl(TtOutLogMethodSection.UpdateNextStep, TtOutLogStatus.FAILED, "There is no routerHelp that know where is the goal state", agent, agent.getState(), goalState);
            return;
        }

        //todo: Implementing trust mechanism

        List<RoutingHelp> sortedRoutingHelps = routingHelps;
        switch (Config.TRUST_METHODOLOGY) {

            case BasicTrust_OnlyByItsHistory:
                sortedRoutingHelps = basicTrustMechanism(agent, goalState, routingHelps);
                if (sortedRoutingHelps == null) return;
                break;

            case NoTrust_ShortPath:
                sortRoutingByShortestPath(sortedRoutingHelps);
            case NoTrust_RandomPath:
            default:
                break;

        }

        agent.clearNextSteps();
        RoutingHelp help = sortedRoutingHelps.get(0);
        // Adding path from agent state to helper (agent) state
        for (WatchedAgent wa : watchedAgents) {
            if (wa.getAgent().getId() == help.getHelperAgent().getId()) {
                agent.getNextSteps().addAll(wa.getPath());
                agent.setHelper(help.getHelperAgent());
                break;
            }
        }

        // Adding the output path to target that reported by helper.
        // This path moves only one step towards the target.
        if (help.getNextState() != null) {
            agent.getNextSteps().add(help.getNextState());
            agent.setHelper(help.getHelperAgent());
        }

        if (help.getHelperAgent().getBehavior().getIsHonest()) {
            statistics___.add_Itt_TrustToHonest();
        } else {
            statistics___.add_Itt_TrustToDishonest();
        }
//        }
    }

    private void sortRoutingByShortestPath(List<RoutingHelp> sortedRoutingHelps) {
        // Sorting routerHelpers based on shortest path.
        sortedRoutingHelps.sort((c1, c2) -> {
            if (c1.getFinalStepFromAgentToTarget() < c2.getFinalStepFromAgentToTarget()) {
                return -1;
            } else if (c1.getFinalStepFromAgentToTarget() > c2.getFinalStepFromAgentToTarget()) {
                return 1;
            }
            return 0;
        });
    }

    private List<RoutingHelp> basicTrustMechanism(Agent agent, StateX goalState, ArrayList<RoutingHelp> routingHelps) {
        // Sorting routerHelpers based on bigger trust level.
        routingHelps.sort((c1, c2) -> {
            if (c1.getTrustLevel() > c2.getTrustLevel()) {
                return -1;
            } else if (c1.getTrustLevel() < c2.getTrustLevel()) {
                return 1;
            } else {
                if (c1.getFinalStepFromAgentToTarget() < c2.getFinalStepFromAgentToTarget()) {
                    return -1;
                } else if (c1.getFinalStepFromAgentToTarget() > c2.getFinalStepFromAgentToTarget()) {
                    return 1;
                }
                return 0;
            }
        });

        int srIndex = 0;
        for (int routingHelpsSize = routingHelps.size(); srIndex < routingHelpsSize && srIndex < 6; srIndex++) {
            RoutingHelp help = routingHelps.get(srIndex);
            if (help.getTrustLevel() < 0) {
                break;
            }
        }

        if (srIndex == 0) {
            OutLog____.pl(TtOutLogMethodSection.UpdateNextStep, TtOutLogStatus.ERROR, "can not found trustee in routerHelp", agent, agent.getState(), goalState);
            return null;
        }

        return routingHelps.subList(0, srIndex);
    }

    /**
     * Identifying whether the input state (second param) is one target for the input agent (first param)?
     *
     * @param agent The agent to be navigated.
     * @param state Target state
     * @return True: if the state is the neighbor of the agent.
     */
    private boolean isStateTheNeighborOfAgent(Agent agent, StateX state) {
        //----
        ArrayList<StateX> targets = agent.getState().getTargets();
        //---

        if (targets.isEmpty()) {
            return false;
        }

        for (StateX target : targets) {
            if (state.getId() == target.getId()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Go to neighbor state if possible.
     *
     * @param agent     walker agent
     * @param nextState The state to be go
     * @return final state
     */
    private StateX gotoNeighborState(Agent agent, StateX nextState) {

        //============================
        StateX state = agent.getState();
        //============================

        if (nextState == null) {
            return state;
        }
        // If now is in nextState
        if (nextState.getId() == state.getId()) {
            return state;
        }
        // If nextState is not neighbor state
        if (!isStateTheNeighborOfAgent(agent, nextState)) {
            return state;
        }

        // can we add this agent to new state? if true, it will be added.
        if (nextState.addAgent(agent)) {

            // can we remove this agent.getState() from current state? if true, it will be removed.
            if (state.leave(agent)) {

                // Selecting the transition that will transmit the agent from current state to next state an activating it.
                TransitionX targetTrans = state.getTargetTrans(nextState);
                if (targetTrans != null) {
                    targetTrans.setDrawIsActive(true);
                }
                agent.setState(nextState);
                agent.updateTravelHistory();
                return nextState;
            } else {
                // If it can not able to leave current state, will leave new state.
                nextState.leave(agent);
            }
        }

        return state;
    }

    /**
     * Go to the target with index of 'index'
     *
     * @param agent Walker agent
     * @param index Index of target in targets list
     * @return Final state
     */
    private StateX gotoNeighborState(Agent agent, int index) {
        //============================
        ArrayList<StateX> targets = agent.getState().getTargets();
        //============================

        if (targets.size() > index) {
            StateX nextState = agent.getState().getTargets().get(index);
            return gotoNeighborState(agent, nextState);
        }
        return agent.getState();
    }

    /**
     * Responding according to behavioral profile
     *
     * @param agent     Responder agent
     * @param goalState The goal state
     * @return The neighbor state that routes to goalState
     */
    private RoutingHelp doYouKnowWhereIs(Agent agent, StateX goalState) {

        if (agent.getBehavior().getIsHonest()) {
            return responseAsHonest(agent, goalState);
        }
        return responseAsDishonest(agent, goalState);

    }

    private RoutingHelp responseAsDishonest(Agent agent, StateX goalState) {

        StateX pitState = null;
        for (StateX state : world.getEnvironment().getStates()) {
            if (state.isIsPitfall()) {
                pitState = state;
            }
        }
        if (pitState == null) {
            pitState = world.getEnvironment().getRandomState();
        }
        return responseAsHonest(agent, pitState);
    }

    private RoutingHelp responseAsHonest(Agent agent, StateX goalState) {
        //============================
        ArrayList<TravelHistory> travelHistories = agent.getTravelHistories();
        //============================

        //boolean isFound = false;
        int lastIndex = travelHistories.size() - 1;

        //int allStepsToTarget = 0;
        for (int i = lastIndex; i >= 0; i--) {

            // If there is no reverse path from state 'i' to state 'i-1'
            if (i < lastIndex && !travelHistories.get(i + 1).hasPathTo(travelHistories.get(i))) {
                return null;
            }

            int stepTo = travelHistories.get(i).isAnyPathTo(goalState);
            if (stepTo >= 0) {
                RoutingHelp routingHelp = new RoutingHelp();
                routingHelp.setHelperAgent(agent);
                routingHelp.setStepFromHelperToTarget(lastIndex - i + stepTo);

                // If the goal state is one of inner visited agents of their targets
                if (i < lastIndex /*&& lastIndex > 0*/) {
                    routingHelp.setNextState(travelHistories.get(lastIndex - 1).getStateX());
                }
                // If the goal state is one of targets of current agent
                else if (i == lastIndex) {
                    ArrayList<StateX> targets = travelHistories.get(i).getStateX().getTargets();
                    for (StateX t : targets) {
                        if (t.getId() == goalState.getId()) {
                            routingHelp.setNextState(t);
                            break;
                        }
                    }
                }
                return routingHelp;
            }
        }
        return null;
    }


}
