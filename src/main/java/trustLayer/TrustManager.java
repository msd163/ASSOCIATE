package trustLayer;

import stateLayer.TravelHistory;
import systemLayer.Agent;
import utils.Config;
import utils.Globals;

import java.util.ArrayList;

public class TrustManager {


    public TrustManager() {
    }

    public static int calculateTrust(Agent agent) {


        return 0;
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
        if (Config.TRUST_SHARE_Recommendation) {
            for (TrustRecommendation recommendation : master.getTrust().getRecommendations()) {
                if (recommendation.getTrustee().getId() == trustee.getId()) {
                    calculatedTrust =
                            (1 - Config.TRUST_RECOMMENDATION_COEFF) * calculatedTrust
                                    + Config.TRUST_RECOMMENDATION_COEFF * recommendation.getFinalRecommendedTrustLevel();
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
}
