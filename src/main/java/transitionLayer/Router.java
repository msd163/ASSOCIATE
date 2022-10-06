package transitionLayer;

import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import _type.TtTrustMethodology;
import simulateLayer.config.trust.TrustConfigItem;
import simulateLayer.statistics.WorldStatistics;
import societyLayer.agentSubLayer.Agent;
import societyLayer.agentSubLayer.WatchedAgent;
import societyLayer.agentSubLayer.WatchedState;
import societyLayer.agentSubLayer.World;
import societyLayer.environmentSubLayer.StateX;
import societyLayer.environmentSubLayer.TransitionX;
import societyLayer.environmentSubLayer.TravelHistory;
import trustLayer.TrustManager;
import utils.Config;
import utils.Globals;
import utils.OutLog____;

import java.util.ArrayList;
import java.util.List;

public class Router {

    private WorldStatistics statistics___;
    private World world;
    private TrustManager trustManager;
    private TrustConfigItem trustConfigItem;

    public Router(World world) {
        this.world = world;
        this.trustManager = world.getTrustManager();
        trustConfigItem = world.getSimulationConfig();

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
   /*     if (agent.getCurrentTargetStateIndex() < Globals.EPISODE) {
            int ct = agent.getCurrentTarget().getId();
            agent.assignNextTargetState();
            OutLog____.pl(TtOutLogMethodSection.TakeAStepTowardTheTarget, TtOutLogStatus.SUCCESS,
                    "Assigning new target to agent (" + agent.getId() + "). current target: " + ct + " | new target: " + agent.getCurrentTarget().getId());
        }*/

        // If the agent is in target agent
        if (agent.isInTargetState()) {
            statistics___.add_Itt_AgentsInTarget();
            agent.clearNextSteps();
            agent.addSpentTimeAtTheTarget();
            if (agent.getSpentTimeAtTheTarget() > Config.ROUTING_STAY_IN_TARGET_TIME) {
                int ct = agent.getCurrentTarget().getId();
                agent.assignNextTargetState();
                OutLog____.pl(TtOutLogMethodSection.TakeAStepTowardTheTarget, TtOutLogStatus.SUCCESS,
                        "targetIndex: " + agent.getCurrentTargetStateIndex() + ". Assigning new target to agent (" + agent.getId() + "). current target: " + ct + " | new target: " + agent.getCurrentTarget().getId());

            }
            return state;
        }

        if (state.isIsPitfall()) {
            statistics___.add_Itt_AgentsInPitfall();

            OutLog____.pl(
                    TtOutLogMethodSection.TakeAStepTowardTheTarget,
                    TtOutLogStatus.WARN,
                    "In PITFALL",
                    agent, state, agent.getCurrentTarget());

            agent.clearNextSteps();
            agent.addSpentTimeAtTheTarget();
            if (agent.getSpentTimeAtTheTarget() > Config.ROUTING_STAY_IN_PITFALL_TIME) {
                boolean isAdded = agent.getCurrentTarget().addAgent(agent);
                if (isAdded) {
                    agent.getState().getAgents().remove(agent);
                    agent.setState(agent.getCurrentTarget());

                    int ct = agent.getCurrentTarget().getId();
                    agent.assignNextTargetState();
                    OutLog____.pl(TtOutLogMethodSection.TakeAStepTowardTheTarget, TtOutLogStatus.SUCCESS,
                            "Free From Pitfall: targetIndex: " + agent.getCurrentTargetStateIndex() + ". Assigning new target to agent (" + agent.getId() + "). current target: " + ct + " | new target: " + agent.getCurrentTarget().getId());
                }
            }

            return state;
        }

        // the state has no output state. The agent is in an state that is pit
        if (state.getTargets().size() == 0) {
            agent.clearNextSteps();
            OutLog____.pl(
                    TtOutLogMethodSection.TakeAStepTowardTheTarget,
                    TtOutLogStatus.ERROR,
                    "state has no target",
                    agent, state, agent.getCurrentTarget());


            return state;
        }


        // If the nextSteps is empty after updating the nextSteps, the agent will go to one neighbor randomly.
        if (agent.getNextSteps().isEmpty()) {
            int targetIndex = Globals.RANDOM.nextInt(state.getTargets().size());
            StateX stateX = gotoNeighborState(agent, targetIndex);
            statistics___.addRandomTravelToNeighbors();
            if (stateX.getId() == agent.getCurrentTarget().getId()) {
                statistics___.add_Itt_AgentsInTarget();
            } else if (stateX.isIsPitfall()) {
                statistics___.add_Itt_AgentsInPitfall();
            }
            return stateX;
        }

        //-- Getting first entity of the nextSteps of the agent.
        StateX nextState = agent.getNextSteps().get(0);

        //-- Check if selected nextState is the neighbor of the state
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

        // Go to next state
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
                trustManager.createFailedExperience(agent, world.getEnvironment().getState(currentAgentStateId), finalState);
            }
            //
            else if (finalState.getId() == agent.getCurrentTarget().getId()) {
                statistics___.add_Itt_AgentsInTarget();
                trustManager.creatSuccessExperience(agent, world.getEnvironment().getState(currentAgentStateId), finalState);
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

        if (trustConfigItem.getTtMethod() == TtTrustMethodology.FullyRandomly) {
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
        //System.out.print(" *k*");
        //-- Asking from watched list and filling routingHelps. all watchedAgents than know where is the goal state
        ArrayList<RoutingHelp> routingHelps = new ArrayList<>();
        boolean isHonestCollaboration = false;
        boolean isHypocriteCollaboration = false;
        for (int i = 0, watchedAgentsSize = watchedAgents.size(); i < watchedAgentsSize; i++) {
            WatchedAgent wa = watchedAgents.get(i);
            routingHelp = doYouKnowWhereIs(wa.getAgent(), goalState);

            if (routingHelp != null) {
                if (agent.getId() == routingHelp.getHelperAgent().getId()) {
                    OutLog____.pl(TtOutLogMethodSection.Router, TtOutLogStatus.ERROR, "Requester agent is equals to helper: " + agent.getId());
                    continue;
                }

                if (routingHelp.getHelperAgent().getBehavior().getHasHonestState()) {
                    isHonestCollaboration = true;
                    statistics___.getStatisticsCollab().add_allHonestCollaboration();
                } else if (routingHelp.getHelperAgent().getBehavior().getHasHypocriteState()) {
                    isHypocriteCollaboration = true;
                    statistics___.getStatisticsCollab().add_allHypocriteCollaboration();
                }

                // The SafeMode method needs only one helper. All helper are honest
                // After finding the first helper, we exit form the loop
                if (trustConfigItem.getTtMethod() == TtTrustMethodology.TrustMode_SafeMode) {
                    routingHelps.add(routingHelp);
                    break;

                } else {

                    routingHelp.setStepFromAgentToHelper(wa.getPathSize());
                    //============================//============================ _Calculating _Trust

                    routingHelp.setTrustValue(trustManager.getTrustValue(agent, wa.getAgent()));

                    //============================//============================
                    routingHelps.add(routingHelp);
                }
            }
        }

        if (isHonestCollaboration) {
            statistics___.getStatisticsCollab().add_allHonestCollaborationInRound();
        }
        if (isHypocriteCollaboration) {
            statistics___.getStatisticsCollab().add_allHypocriteCollaborationInRound();
        }

        // If there is no routerHelper...
        if (routingHelps.isEmpty()) {
            OutLog____.pl(TtOutLogMethodSection.UpdateNextStep, TtOutLogStatus.WARN, "There is no routerHelp that know where is the goal state", agent, agent.getState(), goalState);
            return;
        }


        List<RoutingHelp> sortedRoutingHelps = routingHelps;
        switch (trustConfigItem.getTtMethod()) {

            case TrustMode_ShortPath:
                //System.out.print(" *ts*");
                sortedRoutingHelps = sortByTrustMechanism(agent, goalState, routingHelps);
                if (sortedRoutingHelps == null) return;

                //-- Validating routingHelps according to observations
               /* if (simulationConfigItem.isIsValidateByTrustObservation()) {
                    sortedRoutingHelps = refineByObservation(agent, sortedRoutingHelps);
                }*/
                break;

            case NoTrust_ShortPath:
                //System.out.print(" *ns*");
                sortRoutingByShortestPath(sortedRoutingHelps);
                break;
            case TrustMode_SafeMode:
            case NoTrust_RandomPath:
            default:
                break;

        }

        // If there is no routerHelper...
        if (sortedRoutingHelps.isEmpty()) {
            OutLog____.pl(TtOutLogMethodSection.UpdateNextStep, TtOutLogStatus.FAILED, "There is no REFINED routerHelp that know where is the goal state", agent, agent.getState(), goalState);
            return;
        }

        agent.clearNextSteps();
        RoutingHelp help = sortedRoutingHelps.get(0);
        // Adding path from agent state to helper (agent) state
        //System.out.print(" *aw*");
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


        float trustValueOfHelperToAgent = trustManager.getTrustValue(help.getHelperAgent(), agent);
        if (trustConfigItem.isIsUseIndirectExperience()) {
            /* If receiver of experiences (the helper or truster in routing procedure) trusts to the sender (the trustee in routing procedure),
            the helper accepts experiences of the agent
            * */
            if (trustValueOfHelperToAgent > 0) {
                trustManager.shareExperiences(agent, help.getHelperAgent());
            }
            if (trustConfigItem.isIsBidirectionalExperienceSharing()) {
                /* The agent trusts to helper, thus he accepts experiences of helper  */
                trustManager.shareExperiences(help.getHelperAgent(), agent);
            }
        }
        /*
         * */
        if (trustConfigItem.isIsUseIndirectObservation()) {
            if (trustValueOfHelperToAgent > 0) {
                trustManager.shareObservations(agent, help.getHelperAgent());
            }
            if (trustConfigItem.isIsBidirectionalObservationSharing()) {
                trustManager.shareObservations(help.getHelperAgent(), agent);
            }
        }

        if (trustConfigItem.isUseRecommendation()) {
            if (trustValueOfHelperToAgent > 0) {
                trustManager.sendRecommendations(agent, help.getHelperAgent());
            }
            if (trustConfigItem.isIsBidirectionalRecommendationSharing()) {
                trustManager.sendRecommendations(help.getHelperAgent(), agent);
            }
        }

        if (help.getHelperAgent().getBehavior().getHasHonestState()) {
            statistics___.add_Itt_TrustToHonest();
            statistics___.getStatisticsCollab().add_allTrustToHonestInRound();
        } else if (help.getHelperAgent().getBehavior().getHasAdversaryState()) {
            statistics___.add_Itt_TrustToAdversary();
        } else if (help.getHelperAgent().getBehavior().getHasHypocriteState()) {
            statistics___.add_Itt_TrustToHypocrite();
            statistics___.getStatisticsCollab().add_allTrustToHypocriteInRound();

        } else if (help.getHelperAgent().getBehavior().getHasMischief()) {
            statistics___.add_Itt_TrustToMischief();
        }

//       }
    }

    /*  private List<RoutingHelp> refineByObservation(Agent requester, List<RoutingHelp> srh) {

          List<RoutingHelp> refinedList = new ArrayList<>();

          for (RoutingHelp routingHelp : srh) {
              trustManager.ValidateHelperInObservations(requester, routingHelp);
              if (routingHelp.getValidation() != TtIsValidatedInObservations.Invalid) {
                  refinedList.add(routingHelp);
              }
          }

          refinedList.sort((RoutingHelp r1, RoutingHelp r2) -> {
              if (r1.getValidation() == TtIsValidatedInObservations.Valid && r2.getValidation() != TtIsValidatedInObservations.Valid) {
                  return -1;
              }

              if (r1.getValidation() != TtIsValidatedInObservations.Valid && r2.getValidation() == TtIsValidatedInObservations.Valid) {
                  return 1;
              }

              return 0;
          });

          return refinedList;

      }
  */
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

    private List<RoutingHelp> sortByTrustMechanism(Agent agent, StateX goalState, ArrayList<RoutingHelp> routingHelps) {
        // Sorting routerHelpers based on bigger trust level.
        routingHelps.sort((c1, c2) -> {
            if (c1.getTrustValue() > c2.getTrustValue()) {
                return -1;
            } else if (c1.getTrustValue() < c2.getTrustValue()) {
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
        for (int routingHelpsSize = routingHelps.size();
             srIndex < routingHelpsSize && srIndex < trustConfigItem.getMaximumConsideredRoutingHelpInTrustMechanism();
             srIndex++) {
            RoutingHelp help = routingHelps.get(srIndex);
            if (help.getTrustValue() < 0) {
                break;
            }
        }

        if (srIndex == 0) {
            OutLog____.pl(TtOutLogMethodSection.UpdateNextStep, TtOutLogStatus.WARN, "can not found trustee in routerHelp", agent, agent.getState(), goalState);
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
    public StateX gotoNeighborState(Agent agent, StateX nextState) {

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

        if (trustConfigItem.getTtMethod() == TtTrustMethodology.TrustMode_SafeMode) {
            return responseAsHonest(agent, goalState);
        }

        switch (agent.getBehavior().getCurrentBehaviorState()) {
            case Honest:
                return responseAsHonest(agent, goalState);
            case Adversary:
                return responseAsAdversary(agent);
            case Mischief:
                return responseAsMischief(agent);
        }
        return null;
    }

    private StateX findPitfallForAdversary(Agent agent) {

        //-- If there is no pitfall in the world environment, the adversary agent will select a random pitfall
        if (world.getEnvironment().getPitfallCount() <= 0) {
            OutLog____.pl(TtOutLogMethodSection.DoYouKnowWhereIs, TtOutLogStatus.FAILED, "The agent (" + agent.getId() + ") is Adversary but there is no pitfall.");
            return world.getEnvironment().getRandomState();
        }

        //-- Finding a pitfall in travel history of adversary agent.
        ArrayList<TravelHistory> travelHistories = agent.getTravelHistories();
        int lastIndex = travelHistories.size() - 1;
        for (int i = lastIndex; i >= 0; i--) {
            //-- if there is no reverse path for backward to previous state
            if (i < lastIndex && !travelHistories.get(i + 1).hasPathTo(travelHistories.get(i))) {
                break;
            }
            TravelHistory travelHistory = travelHistories.get(i);
            StateX pitState = travelHistory.getPitfallIfExist();
            if (pitState != null) {
                return pitState;
            }
        }

        //-- Selecting a random pitfall
        int pitfallIndex = Globals.RANDOM.nextInt(world.getEnvironment().getPitfallCount());
        int pid = 0;
        for (StateX state : world.getEnvironment().getStates()) {
            if (state.isIsPitfall()) {
                if (pid != pitfallIndex) {
                    pid++;
                    continue;
                }
                return state;
            }
        }

        //-- If can not find a pitfall state, any random state will be returned.
        OutLog____.pl(TtOutLogMethodSection.DoYouKnowWhereIs, TtOutLogStatus.ERROR, "Unable to get a pitfall with index " + pitfallIndex + " for agent (" + agent.getId() + "). current index is " + pid + ". pitfall count is " + world.getEnvironment().getPitfallCount());
        return world.getEnvironment().getRandomState();
    }

    private RoutingHelp responseAsAdversary(Agent agent) {

        return responseAsHonest(agent, findPitfallForAdversary(agent));
    }

    private RoutingHelp responseAsMischief(Agent agent) {

        return responseAsHonest(agent, world.getEnvironment().getRandomState());
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
