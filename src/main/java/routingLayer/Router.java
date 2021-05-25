package routingLayer;

import stateLayer.StateX;
import stateLayer.TransitionX;
import stateLayer.TravelHistory;
import systemLayer.Agent;
import systemLayer.WatchedAgent;
import systemLayer.WatchedState;
import utils.Globals;
import utils.WorldStatistics;

import java.util.ArrayList;
import java.util.List;

public class Router {

    private WorldStatistics statistics;

    public Router() {
    }

    public void setStatistics(WorldStatistics statistics) {
        this.statistics = statistics;
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
        StateX targetState = agent.getTargetState();
        StateX state = agent.getState();
        //============================

        // There is no targetState for this agent!
        if (targetState == null) {
            statistics.addAgentsWithNoTargetState();
            return null;
        }

        // If the agent is in target agent
        if (agent.isInTargetState()) {
            statistics.addAllInTargetAgents();
            agent.clearNextSteps();
            return state;
        }

        if (state.isIsPitfall()) {
            statistics.addAgentsInPitfall();
            System.out.println(">> WARN >> agent " + agent.getId() + " is in pitfall " + state.getId())                            ;
            return state;
        }

        // the state has no output state. The agent is in an state that is pit
        if (state.getTargets().size() == 0) {
            agent.clearNextSteps();
            // Printing map
            String sIds = "";
            for (TravelHistory s : agent.getTravelHistories()) {
                sIds += " | " + s.getStateX().getId() /*+ "-" + s.getVisitTime()*/;
            }
            System.out.println(">>ERR>> no target for agent:::gotoSate::agentId: " + agent.getId() + " [ c: " + state.getId() + " >  t: " + targetState.getId() + " ] #  maps: " + sIds);

            return state;
        }

        // If there is not any states in the agent history, the nextSteps of agent have to be updated.
        if (agent.getNextSteps().isEmpty()) {
            updateNextSteps(agent, targetState);
            statistics.addUpdatedNextStepsOfAgents();
        }

        // If the nextSteps is empty after updating the nextSteps, the agent will go to one neighbor randomly.
        if (agent.getNextSteps().isEmpty()) {
            int targetIndex = Globals.RANDOM.nextInt(state.getTargets().size());
            StateX stateX = gotoNeighborState(agent, targetIndex);
            statistics.addRandomTravelToNeighbors();
            if (stateX.getId() == agent.getTargetState().getId()) {
                statistics.addInTargetAgentsInThisTime();
            }
            return stateX;
        }

        // Traveling to one neighbor according to the first entity of the nextSteps of the agent.
//      StateX stateX = gotoNeighborState(nextSteps.get(0));
        StateX nextState = agent.getNextSteps().get(0);

        if (!isStateTheNeighborOfAgent(agent, nextState)) {
            statistics.addFailedTravelToGoToNeighbor();
            System.out.println("Agent.gotoTarget:: Error:  next state is not neighbor. agent: " + agent.getId() + " state: " + state.getId() + "  nextState: " + nextState.getId());
            return null;
        }
        int currentAgentStateId = agent.getState().getId();
        StateX finalState = gotoNeighborState(agent, nextState);
        // Successfully traveling to neighbor.
        if (finalState.getId() == nextState.getId()) {
            agent.getNextSteps().remove(0);
            statistics.addSuccessTravelToGoToNeighbor();
            if (finalState.getId() == agent.getTargetState().getId()) {
                statistics.addInTargetAgentsInThisTime();
            }

        }
        // Failed to travel to neighbor.
        else {
            statistics.addFailedTravelToGoToNeighbor();
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
     * @param agent     The agent to be navigated.
     * @param goalState Goal state.
     */
    public void updateNextSteps(Agent agent, StateX goalState) {

        //============================
        List<WatchedState> watchedStates = agent.getWatchedStates();
        List<WatchedAgent> watchedAgents = agent.getWatchedAgents();

        //============================

        agent.clearNextSteps();

        RoutingHelp routingHelp;

        // If the agent can watch the goalState in its target.
        if (watchedStates != null) {
            for (WatchedState ws : watchedStates) {
                if (ws.getStateX().getId() == goalState.getId()) {
                    agent.getNextSteps().addAll(ws.getPath());
                    System.out.println(">> Self Visiting > Agent: " + agent.getId() + " | State: " + goalState.getId());
                    return;
                }
            }
        }

        if (watchedAgents == null) {
            return;
        }

        // Asking from watched list that is sorted according to trust level of watched agents.
        ArrayList<RoutingHelp> routingHelps = new ArrayList<>();
        //todo: set the threshold of watchedAgents (5) as a configurable variable
        for (int i = 0, watchedAgentsSize = watchedAgents.size(); i < watchedAgentsSize && i < 5; i++) {
            WatchedAgent wa = watchedAgents.get(i);
            routingHelp = doYouKnowWhereIs(wa.getAgent(), goalState);

            if (routingHelp != null) {
                routingHelp.setStepFromAgentToHelper(wa.getPathSize());
                routingHelps.add(routingHelp);
            }
        }

        // If there is no routerHelper...
        if (routingHelps.isEmpty()) {
            return;
        }

        // Sorting routerHelpers based on shortest path.
        routingHelps.sort((c1, c2) -> {
            if (c1.getFinalStepFromAgentToTarget() < c2.getFinalStepFromAgentToTarget()) {
                return -1;
            } else if (c1.getFinalStepFromAgentToTarget() > c2.getFinalStepFromAgentToTarget()) {
                return 1;
            }
            return 0;
        });

        boolean isSuccessFull = false;
        for (RoutingHelp help : routingHelps) {
            agent.clearNextSteps();

            // Adding path from agent state to helper (agent) state
            for (WatchedAgent wa : watchedAgents) {
                if (wa.getAgent().getId() == help.getHelperAgent().getId()) {
                    agent.getNextSteps().addAll(wa.getPath());
                    break;
                }
            }

            // Adding the output path to target that reported by helper.
            // This path moves only one step towards the target.
            if (help.getNextState() != null) {
                agent.getNextSteps().add(help.getNextState());
            }

            // Check if this route has been taken before or not?
            // If the suggested path (current help) contains steps that have already been taken by the agent, that path will be skipped.
            boolean isVisited = false;
            for (int i = 0, nextStepsSize = agent.getNextSteps().size(); i < nextStepsSize; i++) {
                StateX nextState = agent.getNextSteps().get(i);

                int travelHistoryLastIndex = agent.getTravelHistories().size() - 1;
                for (int j = travelHistoryLastIndex; j >= 0 && j > travelHistoryLastIndex - nextStepsSize - 2; j--) {
                    TravelHistory travelHistory = agent.getTravelHistories().get(j);

                    if (travelHistory.getStateX().getId() == nextState.getId()) {
                        isVisited = true;
                        break;
                    }
                }

            }
            // Successful in finding routeHelper
            if (!isVisited) {
                isSuccessFull = true;
                break;
            }
        }

        // If all of routerHelps contain a loop, selecting the first one as a default
        if (!isSuccessFull) {

            agent.clearNextSteps();
            RoutingHelp help = routingHelps.get(0);
            // Adding path from agent state to helper (agent) state
            for (WatchedAgent wa : watchedAgents) {
                if (wa.getAgent().getId() == help.getHelperAgent().getId()) {
                    agent.getNextSteps().addAll(wa.getPath());
                    break;
                }
            }

            // Adding the output path to target that reported by helper.
            // This path moves only one step towards the target.
            if (help.getNextState() != null) {
                agent.getNextSteps().add(help.getNextState());
            }
        }


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
     * &
     *
     * @param agent     Responder agent
     * @param goalState The goal state
     * @return The neighbor state that routes to goalState
     */
    private RoutingHelp doYouKnowWhereIs(Agent agent, StateX goalState) {

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

                if (i < lastIndex /*&& lastIndex > 0*/) {
                    routingHelp.setNextState(travelHistories.get(lastIndex - 1).getStateX());
                }
                return routingHelp;
            }
        }

        return null;
    }


}
