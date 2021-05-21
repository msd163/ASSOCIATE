package routingLayer;

import stateLayer.StateMap;
import stateLayer.StateX;
import stateLayer.TransitionX;
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

        if (targetState == null) {
            statistics.addAgentsWithNoTargetState();
            return null;
        }

        if (agent.isInTargetState()) {
            statistics.addAllInTargetAgents();
            agent.clearNextStates();
            return state;
        }

        // the state has no output state
        if (state.getTargets().size() == 0) {
            agent.clearNextStates();
            // Printing map
            String sIds = "";
            for (StateMap s : agent.getStateMaps()) {
                sIds += " | " + s.getStateX().getId() /*+ "-" + s.getVisitTime()*/;
            }
            System.out.println(">>ERR>> no target for agent:::gotoSate::agentId: " + agent.getId() + " [ c: " + state.getId() + " >  t: " + targetState.getId() + " ] #  maps: " + sIds);

            return state;
        }

        if (agent.getNextStates().isEmpty()) {
            updateNextStates(agent, targetState);
            statistics.addUpdatedNextStatesOfAgents();
        }

        if (agent.getNextStates().isEmpty()) {
            int targetIndex = Globals.RANDOM.nextInt(state.getTargets().size());
            StateX stateX = gotoNeighborState(agent, targetIndex);
            statistics.addRandomTravelToNeighbors();
            if (stateX.getId() == agent.getTargetState().getId()) {
                statistics.addInTargetAgentsInThisTime();
            }
            return stateX;
        }

//      StateX stateX = gotoNeighborState(nextStates.get(0));
        StateX nextState = agent.getNextStates().get(0);

        if (!isStateTheNeighborOfAgent(agent, nextState)) {
            statistics.addFailedTravelToGoToNeighbor();
            System.out.println("Agent.gotoTarget:: Error:  next state is not neighbor. agent: " + agent.getId() + " state: " + state.getId() + "  nextState: " + nextState.getId());
            return null;
        }
        int currentAgentStateId = agent.getState().getId();
        StateX finalState = gotoNeighborState(agent, nextState);
        if (finalState.getId() == nextState.getId()) {
            agent.getNextStates().remove(0);
            statistics.addSuccessTravelToGoToNeighbor();
            if (finalState.getId() == agent.getTargetState().getId()) {
                statistics.addInTargetAgentsInThisTime();
            }
        } else {
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
    public void updateNextStates(Agent agent, StateX goalState) {

        //============================
        List<WatchedState> watchedStates = agent.getWatchedStates();
        List<WatchedAgent> watchedAgents = agent.getWatchedAgents();

        //============================

        agent.clearNextStates();

        RoutingHelp routingHelp;

        if (watchedStates != null) {

            for (WatchedState ws : watchedStates) {
                if (ws.getStateX().getId() == goalState.getId()) {
                    agent.getNextStates().addAll(ws.getPath());
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
        for (int i = 0, watchedAgentsSize = watchedAgents.size(); i < watchedAgentsSize && i < 5; i++) {
            WatchedAgent wa = watchedAgents.get(i);
            //todo: check reverse path
            routingHelp = doYouKnowWhereIs(wa.getAgent(), goalState);

            if (routingHelp != null) {
                routingHelp.setStepToTarget(routingHelp.getStepToTarget() + wa.getPathSize());
                routingHelps.add(routingHelp);
            }
        }

        routingHelps.sort((c1, c2) -> {
            if (c1.getStepToTarget() < c2.getStepToTarget()) {
                return -1;
            } else if (c1.getStepToTarget() > c2.getStepToTarget()) {
                return 1;
            }
            return 0;
        });

        for (RoutingHelp help : routingHelps) {
            agent.clearNextStates();

            for (WatchedAgent wa : watchedAgents) {
                if (wa.getAgent().getId() == help.getHelperAgent().getId()) {
                    agent.getNextStates().addAll(wa.getPath());
                    break;
                }
            }

            if (help.getNextState() != null) {
                agent.getNextStates().add(help.getNextState());
            }

            boolean isVisited = false;
            for (int i = 0, nextStatesSize = agent.getNextStates().size(); i < nextStatesSize; i++) {
                StateX nextState = agent.getNextStates().get(i);

                int siteMapLastIndex = agent.getStateMaps().size() - 1;
                for (int j = siteMapLastIndex; j >= 0 && j > siteMapLastIndex - nextStatesSize - 2; j--) {
                    StateMap stateMap = agent.getStateMaps().get(j);

                    if (stateMap.getStateX().getId() == nextState.getId()) {
                        isVisited = true;
                        break;
                    }
                }

            }
            if (!isVisited) {
                break;
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
        ArrayList<StateMap> stateMaps = agent.getStateMaps();
        //============================

        //boolean isFound = false;
        int lastIndex = stateMaps.size() - 1;

        //int allStepsToTarget = 0;
        for (int i = lastIndex; i >= 0; i--) {

            // If there is no reverse path from state 'i' to state 'i-1'
            if (i < lastIndex && !stateMaps.get(i + 1).hasPathTo(stateMaps.get(i))) {
                return null;
            }

            int stepTo = stateMaps.get(i).isAnyPathTo(goalState);
            if (stepTo >= 0) {
                RoutingHelp routingHelp = new RoutingHelp();
                routingHelp.setHelperAgent(agent);
                routingHelp.setStepToTarget(lastIndex - i + stepTo);

                if (i < lastIndex /*&& lastIndex > 0*/) {
                    routingHelp.setNextState(stateMaps.get(lastIndex - 1).getStateX());
                }
                return routingHelp;
            }
        }

        return null;
    }


}
