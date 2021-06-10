package trustLayer;

import _type.TtTrustReplaceHistoryMethod;
import stateLayer.TravelHistory;
import systemLayer.Agent;

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
        return 1 / (float) (index + 1);

    }

    public float getTrustLevel(Agent master, Agent trustee) {
        TrustHistory[] histories = master.getTrust().getHistories();
        if (histories != null) {
            for (TrustHistory hh : histories) {
                if (hh != null && hh.getAgent().getId() == trustee.getId()) {
                    return hh.getFinalTrustLevel();
                }
            }
        }
        return 0;
    }
}
