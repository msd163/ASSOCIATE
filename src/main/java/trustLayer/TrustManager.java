package trustLayer;

import _type.TtIsValidatedInObservations;
import routingLayer.RoutingHelp;
import stateLayer.TravelHistory;
import systemLayer.Agent;
import systemLayer.WatchedAgent;
import utils.Globals;
import simulateLayer.SimulationConfigItem;

import java.util.ArrayList;
import java.util.List;

public class TrustManager {


    private SimulationConfigItem simulationConfigItem;

    public TrustManager(SimulationConfigItem simConfig) {
        this.simulationConfigItem = simConfig;
    }

    public void reduceTrustForPitfall(Agent agent) {
        setScoreTrust(agent, false);
    }

    public void increaseTrustForSuccessTarget(Agent agent) {
        setScoreTrust(agent, true);
    }

    private void setScoreTrust(Agent agent, boolean isPositive) {

        //============================
        int _tuCap = agent.getTrust().getHistoryCap();
        if (_tuCap <= 0) {
            return;
        }

        //============================
        ArrayList<TravelHistory> travelHistory = agent.getTravelHistories();
        if (travelHistory == null || travelHistory.isEmpty()) {
            return;
        }

        //============================
        AgentTrust __trust = agent.getTrust();
        TrustHistory[] __trustHistory = __trust.getHistories();

        int effect = isPositive ? 1 : -1;

        int helperId = -1;
        int consideredHistoryCount = 0;
        for (int tvhIndex = travelHistory.size() - 1; tvhIndex > -1; tvhIndex--) {

            TravelHistory tvh = travelHistory.get(tvhIndex);

            // If the history in the first initialized one of the travel is done randomly or by itself info
            if (tvh.getHelper() == null) {
                continue;
            }

            // If the history considered previously in trust calculation, break.
            if (tvh.isTrustCalculated()) {
                break;
            }

            // If the helper in current travel history considered in trust calculation previously, continue ...
            if (helperId == tvh.getHelper().getId()) {
                continue;
            }

            //============================//============================ // Check if the agent added to trustHistory previously and return it's ID.
            for (int k = 0, historyLen = __trustHistory.length; k < historyLen; k++) {
                TrustHistory _tuh = __trustHistory[k];
                if (_tuh != null && _tuh.getAgent().getId() == tvh.getHelper().getId()) {
                    __trust.setHistoryIndex(k);

                    __trust.addHistory(effect * getScoreByOrder(consideredHistoryCount++));
                    travelHistory.get(tvhIndex).setIsTrustCalculated(true);
                    return;
                }
            }

            //============================//============================  // If the agent not added previously
            __trust.addHistorySizeIfPossible();

            // Replacing new history item with an exist one according selected method.
            switch (__trust.getTrustReplaceHistoryMethod()) {

                case Sequential_Circular:
                    __trust.addHistoryIndex();
                    if (__trust.isIndexExceedFromCap()) {
                        __trust.setHistoryIndex(0);
                    }
                    break;

                case RemoveLastUpdated:
                    int historyIndex;
                    TrustHistory oldHistory = __trust.getHistories()[0];
                    historyIndex = 0;
                    if (oldHistory != null) {
                        for (int k = 1, historiesLength = __trustHistory.length; k < historiesLength; k++) {
                            TrustHistory _th = __trustHistory[k];

                            if (_th == null) {
                                historyIndex = k;
                                break;
                            }
                            if (oldHistory.getLastVisitTime() > _th.getLastVisitTime()) {
                                historyIndex = k;
                                oldHistory = _th;
                            }
                        }
                    }
                    __trust.setHistoryIndex(historyIndex);
                    break;
            }

            __trust.createNewHistory(tvh.getHelper(), effect * getScoreByOrder(consideredHistoryCount++));
            travelHistory.get(tvhIndex).setIsTrustCalculated(true);

        }
    }

    private int getIndexOfHistory(ArrayList<TrustHistory> history, int agentId) {
        int i = 0;
        for (TrustHistory th : history) {
            if (th.getAgent().getId() == agentId) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private float getScoreByOrder(int index) {
        if (index < 0) {
            System.out.println(">> Error::getScoreByOrder: index is less than ZERO: " + index);
        }
        return 1 / (float) ((index + 1) * (index + 1));

    }

    public float getTrustLevel(Agent master, Agent trustee) {
        TrustHistory[] histories = master.getTrust().getHistories();

        float calculatedTrust = 0;
        if (histories != null) {
            for (TrustHistory hh : histories) {
                if (hh != null && hh.getAgent().getId() == trustee.getId()) {
                    calculatedTrust = hh.getFinalTrustLevel();
                    break;
                }
            }
        }
        if (simulationConfigItem.isUseTrustRecommendation()) {
            for (TrustRecommendation recommendation : master.getTrust().getRecommendations()) {
                if (recommendation.getTrustee().getId() == trustee.getId()) {
                    //todo: need to be overviewed and revised.
                    calculatedTrust =
                            (1 - simulationConfigItem.getTrustRecommendationCoeff()) * calculatedTrust
                                    + simulationConfigItem.getTrustRecommendationCoeff() * recommendation.getFinalRecommendedTrustLevel();
                    break;
                }
            }
        }

        return calculatedTrust;
    }

    //============================//============================//============================ Recommendation

    public void shareRecommendation(Agent from, Agent to) {
        //-- If recommendation capacity of receiver is zero
        if (to.getTrust().getRecommendationCap() == 0 || to.getTrust().getRecommendationItemCap() == 0) {
            return;
        }

        //-- If receiver of recommendation hos no trust to recommender
        float trustLevelOfRecommender = getTrustLevel(to, from);
        if (trustLevelOfRecommender <= 0) {
            return;
        }

        //-- Sending results of all trust histories to receiver
        for (TrustHistory history : from.getTrust().getHistories()) {
            if (history != null) {
                sendRecommendationTo(from, to, history.getAgent(), history.getFinalTrustLevel());
            }
        }
    }

    private void sendRecommendationTo(Agent recommender, Agent receiver, Agent trustee, float trustLevelOfTrustee) {

        boolean isAppended = false;
        //-- Check whether the trustee has already been recommended to the receiver
        for (TrustRecommendation rec : receiver.getTrust().getRecommendations()) {
            if (rec.getTrustee().getId() == trustee.getId()) {
                isAppended = true;
                if (rec.getItems().size() >= receiver.getTrust().getRecommendationItemCap()) {
                    rec.getItems().remove(0);
                }
                rec.getItems().add(new TrustRecommendationItem(recommender, trustLevelOfTrustee)
                );
                rec.setLastRecommendTime(Globals.WORLD_TIMER);
                rec.setLastEpisode(Globals.EPISODE);
                break;
            }
        }
        if (isAppended) {
            return;
        }

        //todo: adding remove strategy for recommendations
        if (receiver.getTrust().getRecommendations().size() >= receiver.getTrust().getRecommendationCap()) {
            receiver.getTrust().getRecommendations().remove(0);
        }

        //-- If this recommendation is new
        TrustRecommendation tr = new TrustRecommendation(
                receiver,
                trustee,
                new TrustRecommendationItem(recommender, trustLevelOfTrustee)
        );

        receiver.getTrust().getRecommendations().add(tr);
    }

    //============================//============================//============================ Observation

    public void observe(Agent agent) {
        for (WatchedAgent wa : agent.getWatchedAgents()) {
            Agent obsAgent = wa.getAgent();
            TravelHistory lastTravelHistory = obsAgent.getLastTravelHistory();
            if (lastTravelHistory == null) {
                continue;
            }
            if (lastTravelHistory.getHelper() != null) {
                if (lastTravelHistory.isIsPitfall()) {
                    addToObserve(agent, obsAgent, lastTravelHistory.getHelper(), false);
                } else if (lastTravelHistory.isIsTarget()) {
                    addToObserve(agent, obsAgent, lastTravelHistory.getHelper(), true);
                }
            }
        }
    }

    private void addToObserve(Agent observer, Agent observed, Agent helper, boolean isInTarget) {
        if (observer.getTrust().getObservations().size() >= observer.getTrust().getObservationCap()) {
            observer.getTrust().getObservations().remove(0);
        }

        TrustObservation observation = new TrustObservation(observed, helper, !isInTarget, isInTarget, isInTarget ? 1 : -1);

        observer.getTrust().getObservations().add(observation);
    }


    public boolean canObserve(Agent observer, Agent agent) {
        for (TrustObservation obs : observer.getTrust().getObservations()) {
            if (obs.getResponder().getId() == agent.getId()) {
                return true;
            }
        }
        return false;
    }

    public TtIsValidatedInObservations inValidInObservation(Agent observer, Agent agent) {
        for (TrustObservation obs : observer.getTrust().getObservations()) {
            if (obs.getResponder().getId() == agent.getId()) {
                return obs.isIsFinalTarget() ? TtIsValidatedInObservations.Valid : TtIsValidatedInObservations.Invalid;
            }
        }
        return TtIsValidatedInObservations.Unknown;
    }

    public void ValidateHelperInObservations(Agent requester, RoutingHelp routingHelp) {
        List<Agent> observers = new ArrayList<>();
        List<Float> trusts = new ArrayList<>();
        for (WatchedAgent watchedAgent : requester.getWatchedAgents()) {
            if (watchedAgent.getAgent().hasObservation()) {
                float trustLevel = getTrustLevel(requester, watchedAgent.getAgent());
                if (trustLevel > 0 && canObserve(watchedAgent.getAgent(), routingHelp.getHelperAgent())) {
                    observers.add(watchedAgent.getAgent());
                    trusts.add(trustLevel);
                }
            }
        }

        if (observers.isEmpty()) {
            return;
        }

        float maxTrust = 0;
        int maxIndex = 0;

        for (int i = 0, trustsSize = trusts.size(); i < trustsSize; i++) {
            Float trust = trusts.get(i);
            if (trust > maxTrust) {
                maxIndex = i;
                maxTrust = trust;
            }
        }

        TtIsValidatedInObservations validation = inValidInObservation(observers.get(maxIndex), routingHelp.getHelperAgent());
        routingHelp.setValidation(validation);

        return;
    }
}
