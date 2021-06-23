package trustLayer;

import simulateLayer.SimulationConfigItem;
import stateLayer.StateX;
import stateLayer.TravelHistory;
import systemLayer.Agent;
import systemLayer.WatchedAgent;
import trustLayer.data.*;
import utils.Globals;

import java.util.ArrayList;
import java.util.List;

public class TrustManager {


    private SimulationConfigItem simulationConfigItem;

    public TrustManager(SimulationConfigItem simConfig) {
        this.simulationConfigItem = simConfig;
    }

    //============================//============================//============================

    private float getForgottenValue(int time) {
        return (float) Math.pow(1 - simulationConfigItem.getTrustForgottenCoeff(), Globals.WORLD_TIMER - time);
    }

    private float getRewardByOrder(int index) {
        if (index < 0) {
            System.out.println(">> Error::getScoreByOrder: index is less than ZERO: " + index);
        }
        return 1 / (float) ((index + 1) * (index + 1));

    }

    public float getTrustValue(Agent requester, Agent responder) {
        AgentTrust trust = requester.getTrust();
        if (trust.getLastUpdateTrustValues()[responder.getIndex()] == Globals.WORLD_TIMER) {
            return trust.getTrustValues()[responder.getIndex()];
        }
        float trustValue = calcTrustValue(requester, responder);

        trust.getLastUpdateTrustValues()[responder.getIndex()] = Globals.WORLD_TIMER;
        trust.getTrustValues()[requester.getIndex()] = trustValue;

        //-- trust of recommendation
/*        if (simulationConfigItem.isUseTrustRecommendation()) {
            for (TrustRecommendation recommendation : master.getTrust().getRecommendations()) {
                if (recommendation.getTrustee().getId() == trustee.getId()) {
                    //todo: need to be overviewed and revised.
                    calculatedTrust =
                            (1 - simulationConfigItem.getTrustRecommendationCoeff()) * calculatedTrust +
                                    simulationConfigItem.getTrustRecommendationCoeff() * recommendation.getFinalRecommendedTrustLevel();
                    break;
                }
            }
        }*/


        //-- For preventing stack over flow bug
        //System.out.println(master.getId() + "-" + trustee.getId() + "  wt:" + (float) Globals.WORLD_TIMER + " -----ctrt:" + calculatedTrust);
        return trustValue;
    }

    private float calcTrustValue(Agent requester, Agent responder) {

        List<Float> sntrs = getSortedNormalizedTrustRewards(requester, responder);

        float trustValue = 0;
        int index = 0;
        for (int i = 0, tsSize = sntrs.size(); i < tsSize; i++) {
            Float t = sntrs.get(i);
            trustValue += ((t) / ((index + 2) * (index + 2)));
            // System.out.println(i + ": index: " + index + " | " + t + "  > " + xxxxx);
            index++;
        }

        return trustValue;
    }

    private List<Float> getSortedNormalizedTrustRewards(Agent requester, Agent responder) {

        List<Float> normList = new ArrayList<>();

        TrustExperience experience = getExperience(requester, responder);
        if (experience != null) {
            normList.addAll(normalizeWithForgottenFactor(experience.getItems()));
        }

        TrustIndirectExperience indirectExperience = getIndirectExperience(requester, responder);
        if (indirectExperience != null) {
            normList.addAll(normalizeWithForgottenFactorAndTrustValue(requester, indirectExperience.getItems()));
        }


        TrustObservation observation = getObservation(requester, responder);
        if (observation != null) {
            normList.addAll(normalizeWithForgottenFactor(observation.getItems()));
        }

        TrustIndirectObservation indirectObservation = getIndirectObservation(requester, responder);
        if (indirectObservation != null) {
            normList.addAll(normalizeWithForgottenFactorAndTrustValue(requester, indirectObservation.getItems()));
        }

        normList.sort((Float f1, Float f2) -> {
                    if (Math.abs(f1) > Math.abs(f2)) {
                        return -1;
                    }
                    if (Math.abs(f1) < Math.abs(f2)) {
                        return 1;
                    }
                    return 0;
                }
        );

        return normList;
    }

    private List<Float> normalizeWithForgottenFactor(List<TrustDataItem> items) {

        List<Float> norList = new ArrayList<>();

        for (TrustDataItem item : items) {
            norList.add(item.getReward() * getForgottenValue(item.getTime()));
        }
        return norList;
    }

    private List<Float> normalizeWithForgottenFactorAndTrustValue(Agent requester, List<TrustDataItem> items) {

        List<Float> norList = new ArrayList<>();

        for (TrustDataItem item : items) {
            float trustValue = getTrustValue(requester, item.getIssuer());
            norList.add(trustValue * item.getReward() * getForgottenValue(item.getTime()));
        }
        return norList;
    }

    //============================//============================//============================ Direct Experience

    public void createFailedExperience(Agent requester, StateX source, StateX destination) {
        createExperience(requester, source, destination, false);
    }

    public void creatSuccessExperience(Agent requester, StateX source, StateX destination) {
        createExperience(requester, source, destination, true);
    }

    /**
     * @param requester
     * @param source
     * @param destination
     * @param isPositive
     */
    private void createExperience(Agent requester, StateX source, StateX destination, boolean isPositive) {

        //============================
        int experienceCap = requester.getTrust().getExperienceCap();
        if (experienceCap <= 0) {
            return;
        }

        //============================
        ArrayList<TravelHistory> travelHistory = requester.getTravelHistories();
        if (travelHistory == null || travelHistory.isEmpty()) {
            return;
        }

        //============================
        AgentTrust __trust = requester.getTrust();
        List<TrustExperience> experiences = __trust.getExperiences();

        int effect = isPositive ? 1 : -1;

        int responderId = -1;
        int consideredExpCount = -1;
        for (int tvhIndex = travelHistory.size() - 1, rewardCount = 0; tvhIndex > -1; tvhIndex--) {

            //-- If experience depth exceeded. this depth defines how many of responder agents considered in reward mechanism.
            if (rewardCount >= simulationConfigItem.getExperienceDepthInRewarding()) {
                return;
            }

            TravelHistory tvh = travelHistory.get(tvhIndex);

            // If the history in the first initialized one of the travel is done randomly or by itself info
            if (tvh.getResponder() == null) {
                continue;
            }

            // If the history considered previously in trust calculation, break.
            if (tvh.isTrustCalculated()) {
                break;
            }

            // If the responder in current travel history considered in trust calculation previously, continue ...
            //-- example: [<id:1,state:1,responderId:6>,<id:2,state:4,responderId:7>,<id:3,state:7,responderId:7>,<id:4,state:22,responderId:7>,
            //             <id:1,state:12,responderId:5>,<id:6,state:3,responderId:(3)>,<id:7,state:44,responderId:(3)>,<id:33,state:1,responderId:(3)>]
            if (responderId == tvh.getResponder().getId()) {
                continue;
            }

            //============================//============================ // Check if the agent added to trustHistory previously and return it's ID.
            boolean isRewarded = false;
            for (int k = 0, expLen = experiences.size(); k < expLen; k++) {
                TrustExperience exp = experiences.get(k);
                if (exp.getResponder().getId() == tvh.getResponder().getId()) {
                    float reward = effect * getRewardByOrder(++consideredExpCount);
                    //-- Adding experience
                    exp.addExperience(requester, source, destination, reward);

                    travelHistory.get(tvhIndex).setIsTrustCalculated(true);
                    responderId = exp.getResponder().getId();
                    rewardCount++;
                    isRewarded = true;
                    break;
                }
            }
            if (isRewarded) {
                continue;
            }

            //============================//============================  // If the agent not added previously

            if (__trust.getExperiences().size() >= __trust.getExperienceCap()) {
                // Replacing new history item with an exist one according selected method.
                switch (__trust.getTrustReplaceMethod()) {
                    case Sequential_Circular:
                        __trust.getExperiences().remove(0);
                        break;

                    case RemoveLastUpdated:
                        int historyIndex;
                        TrustExperience oldHistory = __trust.getExperiences().get(0);
                        historyIndex = 0;
                        for (int k = 1, historiesLength = experiences.size(); k < historiesLength; k++) {
                            TrustExperience tExp = experiences.get(k);

                            if (oldHistory.getLastTime() > tExp.getLastTime()) {
                                historyIndex = k;
                                oldHistory = tExp;
                            }
                        }
                        __trust.getExperiences().remove(historyIndex);
                        break;
                }
            }

            float reward = effect * getRewardByOrder(++consideredExpCount);

            //-- Creating experience
            TrustExperience experience = new TrustExperience(tvh.getResponder());
            experience.addExperience(requester, source, destination, reward);

            responderId = tvh.getResponder().getId();
            rewardCount++;
            travelHistory.get(tvhIndex).setIsTrustCalculated(true);

        }
    }

    private TrustExperience getExperience(Agent requester, Agent responder) {

        if (requester.getTrust().getExperiences().size() == 0) {
            return null;
        }

        for (TrustExperience experience : requester.getTrust().getExperiences()) {
            if (experience.getResponder().getId() == responder.getId()) {
                return experience;
            }
        }
        return null;
    }

    //============================//============================//============================ Indirect Experience

    private boolean addIndirectExperience(TrustDataItem indirectExperienceItem, Agent responder, Agent issuer, Agent receiver) {

        //============================
        int experienceCap = receiver.getTrust().getIndirectExperienceCap();
        if (experienceCap <= 0) {
            return false;
        }

        indirectExperienceItem.setIssuer(issuer);

        //============================
        AgentTrust __trust = receiver.getTrust();
        List<TrustIndirectExperience> experiences = __trust.getIndirectExperiences();


        //============================//============================ // Check if the agent added to trustHistory previously and return it's ID.
        boolean isAdded = false;
        for (int k = 0, expLen = experiences.size(); k < expLen; k++) {
            TrustIndirectExperience exp = experiences.get(k);
            if (exp.getResponder().getId() == responder.getId()) {
                //-- Adding experience
                exp.addExperience(indirectExperienceItem);

                isAdded = true;
                break;
            }
        }
        if (isAdded) {
            return true;
        }

        //============================//============================  // If the agent not added previously

        if (__trust.getIndirectExperiences().size() >= __trust.getIndirectExperienceCap()) {
            // Replacing new history item with an exist one according selected method.
            switch (__trust.getTrustReplaceMethod()) {
                case Sequential_Circular:
                    __trust.getIndirectExperiences().remove(0);
                    break;

                case RemoveLastUpdated:
                    int historyIndex;
                    TrustIndirectExperience oldHistory = __trust.getIndirectExperiences().get(0);
                    historyIndex = 0;
                    for (int k = 1, historiesLength = experiences.size(); k < historiesLength; k++) {
                        TrustIndirectExperience tExp = experiences.get(k);

                        if (oldHistory.getLastTime() > tExp.getLastTime()) {
                            historyIndex = k;
                            oldHistory = tExp;
                        }
                    }
                    __trust.getIndirectExperiences().remove(historyIndex);
                    break;
            }
        }

        //-- Creating experience
        TrustIndirectExperience experience = new TrustIndirectExperience(responder);
        experience.addExperience(indirectExperienceItem);

        return true;

    }

    private TrustIndirectExperience getIndirectExperience(Agent requester, Agent responder) {

        if (requester.getTrust().getIndirectExperiences().size() == 0) {
            return null;
        }

        for (TrustIndirectExperience experience : requester.getTrust().getIndirectExperiences()) {
            if (experience.getResponder().getId() == responder.getId()) {
                return experience;
            }
        }
        return null;
    }

    //============================//============================//============================ Observation

    public void observe(Agent observerAgent) {
        for (WatchedAgent wa : observerAgent.getWatchedAgents()) {
            Agent observedAg = wa.getAgent();
            TravelHistory lastTravelHistory = observedAg.getLastTravelHistory();
            if (lastTravelHistory == null) {
                continue;
            }
            if (lastTravelHistory.getResponder() != null) {
                if (lastTravelHistory.isIsPitfall()) {
                    addObservation(observerAgent, observedAg, lastTravelHistory.getResponder(), false);
                } else if (lastTravelHistory.isIsTarget()) {
                    addObservation(observerAgent, observedAg, lastTravelHistory.getResponder(), true);
                }
            }
        }
    }

    private boolean addObservation(Agent observer, Agent observed, Agent responder, boolean isInTarget) {
        //============================
        int observationCap = observer.getTrust().getObservationCap();
        if (observationCap <= 0) {
            return false;
        }

        //============================
        AgentTrust __trust = observer.getTrust();
        List<TrustObservation> observations = __trust.getObservations();


        //============================//============================ // Check if the agent added to observations previously and return it's ID.
        boolean isAdded = false;
        for (int k = 0, obsLen = observations.size(); k < obsLen; k++) {
            TrustObservation obs = observations.get(k);
            if (obs.getResponder().getId() == responder.getId()) {
                //-- Adding observation
                float reward = (isInTarget ? 1 : -1) * getRewardByOrder(0);
                obs.addObservation(observer, observed, null, null, reward);

                isAdded = true;
                break;
            }
        }
        if (isAdded) {
            return true;
        }

        //============================//============================  // If the agent not added previously

        if (__trust.getObservations().size() >= __trust.getObservationCap()) {
            // Replacing new history item with an exist one according selected method.
            switch (__trust.getTrustReplaceMethod()) {
                case Sequential_Circular:
                    __trust.getObservations().remove(0);
                    break;

                case RemoveLastUpdated:
                    int historyIndex;
                    TrustObservation oldHistory = __trust.getObservations().get(0);
                    historyIndex = 0;
                    for (int k = 1, historiesLength = observations.size(); k < historiesLength; k++) {
                        TrustObservation tExp = observations.get(k);

                        if (oldHistory.getLastTime() > tExp.getLastTime()) {
                            historyIndex = k;
                            oldHistory = tExp;
                        }
                    }
                    __trust.getObservations().remove(historyIndex);
                    break;
            }
        }

        //-- Creating observation
        TrustObservation observation = new TrustObservation(responder);
        float reward = (isInTarget ? 1 : -1) * getRewardByOrder(0);
        observation.addObservation(observer, observed, null, null, reward);

        return true;
    }

   /* public boolean canObserve(Agent observer, Agent agent) {
        for (TrustObservation obs : observer.getTrust().getObservations()) {
            if (obs.getResponder().getId() == agent.getId()) {
                return true;
            }
        }
        return false;
    }

    public TtIsValidatedInObservations isValidInObservation(Agent observer, Agent agent) {
        for (TrustObservation obs : observer.getTrust().getObservations()) {
            if (obs.getResponder().getId() == agent.getId()) {
                return obs.getFinalReward() > 0 ? TtIsValidatedInObservations.Valid : TtIsValidatedInObservations.Invalid;
            }
        }
        return TtIsValidatedInObservations.Unknown;
    }

    public float findTrustScoreInObservation(Agent observer, Agent agent) {
        for (TrustObservation obs : observer.getTrust().getObservations()) {
            if (obs.getResponder().getId() == agent.getId()) {
                return obs.getFinalReward();
            }
        }
        return 0.0f;
    }
*/
   /* public void ValidateHelperInObservations(Agent requester, RoutingHelp routingHelp) {
        List<Agent> observers = new ArrayList<>();
        List<Float> trusts = new ArrayList<>();
        for (WatchedAgent watchedAgent : requester.getWatchedAgents()) {
          *//*  if (watchedAgent.getAgent().hasObservation()) {
                float trustLevel = getTrustValue(requester, watchedAgent.getAgent(), false);
                if (trustLevel > 0 && canObserve(watchedAgent.getAgent(), routingHelp.getHelperAgent())) {
                    observers.add(watchedAgent.getAgent());
                    trusts.add(trustLevel);
                }
            }*//*
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

        TtIsValidatedInObservations validation = isValidInObservation(observers.get(maxIndex), routingHelp.getHelperAgent());
        routingHelp.setValidation(validation);

    }

    Map<String, Float[]> lastUpdateObservationTime = new HashMap<>();

    public float calcFinalTrustLevelAccordingObservation(Agent requester, Agent helper) {


        float finalTrustLevel = 0.0f;
        int watchedCount = 0;
        for (WatchedAgent watchedAgent : requester.getWatchedAgents()) {
            if ((watchedAgent.getAgent().getId() == helper.getId())) {
                continue;
            }
            if (watchedAgent.getAgent().hasObservation()) {
                float trustLevel;
                Float[] lastTimeAndTrustLevel = lastUpdateObservationTime.get(requester.getId() + "-" + watchedAgent.getAgent().getId());
                if (lastTimeAndTrustLevel != null && lastTimeAndTrustLevel[0] == Globals.WORLD_TIMER) {
                    trustLevel = lastTimeAndTrustLevel[1];
                } else {
                    trustLevel = getTrustValue(requester, watchedAgent.getAgent(), false);
                    //-- For preventing stack over flow bug
                    lastUpdateObservationTime.put(requester.getId() + "-" + watchedAgent.getAgent().getId(), new Float[]{(float) Globals.WORLD_TIMER, trustLevel});
                    //System.out.println(requester.getId() + "-" + watchedAgent.getAgent().getId() + " wt: " + Globals.WORLD_TIMER + " trt: " + trustLevel);
                }

                if (trustLevel > 0 && canObserve(watchedAgent.getAgent(), helper)) {
                    watchedCount++;
                    finalTrustLevel += trustLevel * findTrustScoreInObservation(watchedAgent.getAgent(), helper);
                }
            }
        }

        //System.out.println("finalTrustLevel/watchedCount: " + (watchedCount == 0 ? 0.0f : finalTrustLevel / watchedCount));
        return watchedCount == 0 ? 0.0f : finalTrustLevel / watchedCount;
    }
*/

    private TrustObservation getObservation(Agent requester, Agent responder) {

        if (requester.getTrust().getObservations().size() == 0) {
            return null;
        }

        for (TrustObservation observation : requester.getTrust().getObservations()) {
            if (observation.getResponder().getId() == responder.getId()) {
                return observation;
            }
        }
        return null;
    }


    //============================//============================//============================ Indirect Observation
    private boolean addIndirectObservation(TrustDataItem indirectObservationItem, Agent responder, Agent issuer/*observer*/, Agent receiver) {

        //============================
        int observationCap = receiver.getTrust().getIndirectObservationCap();
        if (observationCap <= 0) {
            return false;
        }

        indirectObservationItem.setIssuer(issuer);

        //============================
        AgentTrust __trust = receiver.getTrust();
        List<TrustIndirectObservation> observations = __trust.getIndirectObservations();


        //============================//============================ // Check if the agent added to trustHistory previously and return it's ID.
        boolean isAdded = false;
        for (int k = 0, obsLen = observations.size(); k < obsLen; k++) {
            TrustIndirectObservation obs = observations.get(k);
            if (obs.getResponder().getId() == responder.getId()) {
                //-- Adding observation
                obs.addObservation(indirectObservationItem);

                isAdded = true;
                break;
            }
        }
        if (isAdded) {
            return true;
        }

        //============================//============================  // If the agent not added previously

        if (__trust.getIndirectObservations().size() >= __trust.getIndirectObservationCap()) {
            // Replacing new history item with an exist one according selected method.
            switch (__trust.getTrustReplaceMethod()) {
                case Sequential_Circular:
                    __trust.getIndirectObservations().remove(0);
                    break;

                case RemoveLastUpdated:
                    int historyIndex;
                    TrustIndirectObservation oldHistory = __trust.getIndirectObservations().get(0);
                    historyIndex = 0;
                    for (int k = 1, historiesLength = observations.size(); k < historiesLength; k++) {
                        TrustIndirectObservation tExp = observations.get(k);

                        if (oldHistory.getLastTime() > tExp.getLastTime()) {
                            historyIndex = k;
                            oldHistory = tExp;
                        }
                    }
                    __trust.getIndirectObservations().remove(historyIndex);
                    break;
            }
        }

        //-- Creating observation
        TrustIndirectObservation observation = new TrustIndirectObservation(responder);
        observation.addObservation(indirectObservationItem);

        return true;
    }

    private TrustIndirectObservation getIndirectObservation(Agent requester, Agent responder) {

        if (requester.getTrust().getIndirectObservations().size() == 0) {
            return null;
        }

        for (TrustIndirectObservation observation : requester.getTrust().getIndirectObservations()) {
            if (observation.getResponder().getId() == responder.getId()) {
                return observation;
            }
        }
        return null;
    }

    //============================//============================//============================ Recommendation
/*

    public void shareRecommendation(Agent from, Agent to) {
        //-- If recommendation capacity of receiver is zero
        if (to.getTrust().getRecommendationCap() == 0 || to.getTrust().getRecommendationItemCap() == 0) {
            return;
        }

        //-- If receiver of recommendation hos no trust to recommender
        float trustLevelOfRecommender = getTrustValue(to, from);
        if (trustLevelOfRecommender <= 0) {
            return;
        }

        //-- Sending results of all trust histories to receiver
        for (TrustExperience history : from.getTrust().getExperiences()) {
            if (history != null) {
                //todo: have to be reviewed
              //  sendRecommendationTo(from, to, history.getResponder(), history.getFinalReward());
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
*/


}
