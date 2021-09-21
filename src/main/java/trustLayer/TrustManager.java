package trustLayer;

import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import environmentLayer.StateX;
import environmentLayer.TravelHistory;
import simulateLayer.SimulationConfigItem;
import systemLayer.Agent;
import systemLayer.WatchedAgent;
import trustLayer.data.*;
import utils.Globals;
import utils.OutLog____;

import java.util.ArrayList;
import java.util.List;

public class TrustManager {


    private SimulationConfigItem simulationConfigItem;

    public TrustManager(SimulationConfigItem simConfig) {
        this.simulationConfigItem = simConfig;
    }

    //============================//============================//============================ Certification Verifier

    /**
     * Checking if the responder has valid certification in the network.
     *
     * @param verifier
     * @param toBeVerified
     * @return TRUE/FALSE
     */
    private boolean isHasValidCertificationInDaGra(Agent verifier, Agent toBeVerified) {

        if (verifier.getDaGra() != null) {
            return verifier.getDaGra().isValidCertificationFor(toBeVerified);
        }

        for (WatchedAgent watchedAgent : verifier.getWatchedAgents()) {
            // For preventing request from the agent that we want verify it.
            if (watchedAgent.getAgent().getId() == toBeVerified.getId()) {
                continue;
            }

            if (verifier.getTrust().getTrustAbstracts()[watchedAgent.getAgent().getIndex()].getTrustValue() > 0) {
                if (watchedAgent.getAgent().getDaGra() != null) {
                    //todo: it is need to check other agents with DaGra if current agent has no validity info . continuing for loop
                    System.out.println("------------>>>> Verifier: " + watchedAgent.getAgent().getId());
                    return watchedAgent.getAgent().getDaGra().isValidCertificationFor(toBeVerified);
                }
            }
        }

        return false;
    }
    //============================//============================//============================

    private float getForgottenValue(int time) {
        if (simulationConfigItem.getTrustForgottenCoeff() == 0) {
            return 1.0f;
        }
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
        if (trust.getTrustAbstracts()[responder.getIndex()].getUpdateTime() == Globals.WORLD_TIMER) {
            return trust.getTrustAbstracts()[responder.getIndex()].getTrustValue();
        }

        float innerTrustValue;
        if (
                simulationConfigItem.getCert().isIsUseCertification()                   // certification mode is enabled in config file
                        && responder.getTrust().isHasCandidateForCertification()        // the responder agent has capability of gaining certification
                        &&                                                              // if DaGra mode is disabled of the responder has a valid certification
                        (
                                !simulationConfigItem.getCert().isIsUseDaGra()                  // do not use DaGra
                                        ||
                                        isHasValidCertificationInDaGra(requester, responder)    // the responder gained a valid certification from DaGra
                        )
        ) {
            innerTrustValue = 1.0f;
            OutLog____.pl(TtOutLogMethodSection.TrustMng_GetTrustValue, TtOutLogStatus.SUCCESS, "CRT: Responder (" + responder.getId() + ") with Certificate. Requester: " + requester.getId());
            System.out.println("------------------------------------------------------------- Certification requester: " + requester.getId() + " | responder: " + responder.getId() + "");
        } else {
            innerTrustValue = calcInnerTrustValue(requester, responder);

            //-- trust of recommendation
            if (simulationConfigItem.isUseRecommendation()) {
                float calculatedTrustValueFromRecom = calcRecommendedTrustValue(requester, responder);
                //-- If there is any trust value from recommendation
                if (
                        (simulationConfigItem.isIsUseNegativeRecommendationEffect() && calculatedTrustValueFromRecom != 0) ||
                                (!simulationConfigItem.isIsUseNegativeRecommendationEffect() && calculatedTrustValueFromRecom > 0)
                ) {
                    //-- In safe mode, if there is no inner trust value, final trust value will be recommendation trust value.
                    if (simulationConfigItem.isIsSafeUseRecommendation()) {
                        if (innerTrustValue != 0) {
                            innerTrustValue =
                                    (simulationConfigItem.getRecommendationCoeff() * calculatedTrustValueFromRecom)
                                            + ((1 - simulationConfigItem.getRecommendationCoeff()) * innerTrustValue)
                            ;

                        } else
                        //-- If inner trust value is Zero
                        {
                            innerTrustValue = calculatedTrustValueFromRecom;
                        }
                    }
                    //-- If dont use safe mode recommendation
                    else {
                        innerTrustValue =
                                (simulationConfigItem.getRecommendationCoeff() * calculatedTrustValueFromRecom)
                                        + ((1 - simulationConfigItem.getRecommendationCoeff()) * innerTrustValue)
                        ;
                    }

                }
            }
        }

        trust.getTrustAbstracts()[responder.getIndex()].update(innerTrustValue);

        //System.out.println(master.getId() + "-" + trustee.getId() + "  wt:" + (float) Globals.WORLD_TIMER + " -----ctrt:" + calculatedTrust);
        return innerTrustValue;
    }

    //============================
    private float calcInnerTrustValue(Agent requester, Agent responder) {

        List<Float> sntrs = getSortedNormalizedTrustRewards(requester, responder);

        float trustValue = 0.0f;
        if (1 == 1) {
            int index = 0;
            for (int i = 0, tsSize = sntrs.size(); i < tsSize; i++) {
                Float t = sntrs.get(i);
                if (t == 0.0f) {
                    break;
                }
                trustValue += ((t) / ((index + 2) * (index + 2)));
                //System.out.println("req: " + requester.getId() + " resp: " + responder.getId() + " | i: " + i + " > index: " + index + " | " + t + "  > " + trustValue);
                index++;
            }
        } else {
           /* float tempT = 0;
            float prev = -1111;
            float sum = 0;
            float count = 0;
            int index = 0;
            for (int i = 0, tsSize = sntrs.size(); i < tsSize; i++) {
                Float t = sntrs.get(i);
                if (t == 0.0f) {
                    break;
                }

                if (prev != t) {
                    sum += tempT;
                    tempT = 0;
                    index = 0;
                    count++;
                }

                tempT += ((t) / ((index + 2) * (index + 2)));
                prev = t;
                //System.out.println("req: " + requester.getId() + " resp: " + responder.getId() + " | i: " + i + " > index: " + index + " | " + t + "  > " + trustValue);
                index++;
            }

            sum += tempT;

            if (count > 0) {
                trustValue = sum / count;
            }*/
        }

        return trustValue;
    }

    private List<Float> getSortedNormalizedTrustRewards(Agent requester, Agent responder) {

        List<Float> normList = new ArrayList<>();

        if (simulationConfigItem.isIsUseExperience()) {
            TrustExperience experience = getExperience(requester, responder);
            if (experience != null) {
                normList.addAll(normalizeWithForgottenFactor(experience.getItems()));
            }
        }

        if (simulationConfigItem.isIsUseIndirectExperience()) {
            TrustIndirectExperience indirectExperience = getIndirectExperience(requester, responder);
            if (indirectExperience != null) {
                normList.addAll(normalizeWithForgottenFactorAndTrustValue(requester, indirectExperience.getItems()));
            }
        }

        if (simulationConfigItem.isIsUseObservation()) {
            TrustObservation observation = getObservation(requester, responder);
            if (observation != null) {
                normList.addAll(normalizeWithForgottenFactor(observation.getItems()));
            }
        }

        if (simulationConfigItem.isIsUseIndirectObservation()) {
            TrustIndirectObservation indirectObservation = getIndirectObservation(requester, responder);
            if (indirectObservation != null) {
                normList.addAll(normalizeWithForgottenFactorAndTrustValue(requester, indirectObservation.getItems()));
            }
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

    //============================
    private float calcRecommendedTrustValue(Agent requester, Agent responder) {

        TrustRecommendation recommendation = getRecommendation(requester, responder);

        if (recommendation == null) {
            return 0.0f;
        }

        List<Float> sntrs = normalizeWithForgottenFactorAndTrustValue(requester, recommendation.getItems());

        sntrs.sort((Float f1, Float f2) -> {
                    if (Math.abs(f1) > Math.abs(f2)) {
                        return -1;
                    }
                    if (Math.abs(f1) < Math.abs(f2)) {
                        return 1;
                    }
                    return 0;
                }
        );


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

    //============================
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
            //-- For preventing stack over flow in calculating trust, we use trust value that is calculated previously
            //-- Calculating trust value of requester to recommender
            float trustValue = requester.getTrust().getTrustAbstracts()[item.getIssuer().getIndex()].getTrustValue(); //getTrustValue(requester, item.getIssuer());
            if (trustValue > 0) {
                norList.add(trustValue * item.getReward() * getForgottenValue(item.getTime()));
            }
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
            TrustExperience experience = new TrustExperience(requester, tvh.getResponder());
            experience.addExperience(requester, source, destination, reward);
            experiences.add(experience);

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

    public boolean shareExperiences(Agent issuer/*experimenter*/, Agent receiver) {

        if (issuer.getTrust().getExperiences().size() == 0 || receiver.getTrust().getIndirectExperienceCap() <= 0) {
            return false;
        }

        for (TrustExperience experience : issuer.getTrust().getExperiences()) {
            for (TrustDataItem item : experience.getItems()) {
                addIndirectExperience(item, experience.getResponder(), issuer, receiver);
            }
        }
        return true;
    }

    public boolean addIndirectExperience(TrustDataItem indirectExperienceItem, Agent responder, Agent issuer, Agent receiver) {

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
        TrustIndirectExperience experience = new TrustIndirectExperience(receiver, responder);
        experience.addExperience(indirectExperienceItem);
        experiences.add(experience);

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

    public void observe(Agent observer) {
        //============================
        if (observer.getTrust().getObservationCap() <= 0) {
            return;
        }

        for (WatchedAgent wa : observer.getWatchedAgents()) {
            Agent observedAg = wa.getAgent();
            TravelHistory lastTravelHistory = observedAg.getLastTravelHistory();
            if (lastTravelHistory == null) {
                continue;
            }
            if (lastTravelHistory.getResponder() != null) {
                if (lastTravelHistory.isIsPitfall()) {
                    addObservation(observer, observedAg, lastTravelHistory.getResponder(), false);
                } else if (lastTravelHistory.isIsTarget()) {
                    addObservation(observer, observedAg, lastTravelHistory.getResponder(), true);
                }
            }
        }
    }

    private boolean addObservation(Agent observer, Agent observed, Agent responder, boolean isInTarget) {

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
        TrustObservation observation = new TrustObservation(observer, responder);
        float reward = (isInTarget ? 1 : -1) * getRewardByOrder(0);
        observation.addObservation(observer, observed, null, null, reward);
        observations.add(observation);

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
    } public void ValidateHelperInObservations(Agent requester, RoutingHelp routingHelp) {
        List<Agent> observers = new ArrayList<>();
        List<Float> trusts = new ArrayList<>();
        for (WatchedAgent watchedAgent : requester.getWatchedAgents()) {
           if (watchedAgent.getAgent().hasObservation()) {
                float trustLevel = getTrustValue(requester, watchedAgent.getAgent(), false);
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

    public boolean shareObservations(Agent issuer/*observer*/, Agent receiver) {

        if (issuer.getTrust().getObservations().size() == 0 || receiver.getTrust().getIndirectObservationCap() <= 0) {
            return false;
        }

        for (TrustObservation observation : issuer.getTrust().getObservations()) {
            for (TrustDataItem item : observation.getItems()) {
                addIndirectObservation(item, observation.getResponder(), issuer, receiver);
            }
        }

        return true;
    }

    public boolean addIndirectObservation(TrustDataItem indirectObservationItem, Agent responder, Agent issuer/*observer*/, Agent receiver) {

        indirectObservationItem.setIssuer(issuer);

        //============================
        AgentTrust rcvTrust = receiver.getTrust();
        List<TrustIndirectObservation> rcvIndirObss = rcvTrust.getIndirectObservations();


        //============================//============================ // Check if the agent added to trustHistory previously and return it's ID.
        boolean isAdded = false;
        for (int k = 0, obsLen = rcvIndirObss.size(); k < obsLen; k++) {
            TrustIndirectObservation obs = rcvIndirObss.get(k);
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

        if (rcvTrust.getIndirectObservations().size() >= rcvTrust.getIndirectObservationCap()) {
            // Replacing new history item with an exist one according selected method.
            switch (rcvTrust.getTrustReplaceMethod()) {
                case Sequential_Circular:
                    rcvTrust.getIndirectObservations().remove(0);
                    break;

                case RemoveLastUpdated:
                    int historyIndex;
                    TrustIndirectObservation oldHistory = rcvTrust.getIndirectObservations().get(0);
                    historyIndex = 0;
                    for (int k = 1, historiesLength = rcvIndirObss.size(); k < historiesLength; k++) {
                        TrustIndirectObservation tObs = rcvIndirObss.get(k);

                        if (oldHistory.getLastTime() > tObs.getLastTime()) {
                            historyIndex = k;
                            oldHistory = tObs;
                        }
                    }
                    rcvTrust.getIndirectObservations().remove(historyIndex);
                    break;
            }
        }

        //-- Creating observation
        TrustIndirectObservation observation = new TrustIndirectObservation(receiver, responder);
        observation.addObservation(indirectObservationItem);
        rcvIndirObss.add(observation);

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

    public boolean sendRecommendations(Agent recommender/*observer*/, Agent receiver) {

        if (receiver.getTrust().getRecommendationCap() <= 0) {
            return false;
        }

        for (TrustAbstract trustAbstract : recommender.getTrust().getTrustAbstracts()) {
            //-- If trustAbstract of recommender is about receiver of recommender, ignore it.
            //-- It is not necessary to save recommendation of itself by self.
            //todo: use recommendation values about us by other for analysing environment sight about us (as receiver of recommendation)
            if (trustAbstract.getResponder().getId() == receiver.getId()) {

            }
            if (trustAbstract.getTrustValue() > 0) {
                addRecommendation(trustAbstract.getResponder(), recommender, receiver, trustAbstract.getTrustValue());
            } else if (trustAbstract.getTrustValue() < 0 && simulationConfigItem.isIsUseNegativeRecommendationEffect()) {
                addRecommendation(trustAbstract.getResponder(), recommender, receiver, trustAbstract.getTrustValue());
            }
        }

        return true;
    }

    public boolean addRecommendation(Agent responder, Agent recommender/*issuer*/, Agent receiver, float trustValue) {

        //============================
        AgentTrust rcvTrust = receiver.getTrust();
        List<TrustRecommendation> rcvRcmms = rcvTrust.getRecommendations();

        //============================//============================ // Check if the agent added to recommendation previously and return it's ID.
        boolean isAdded = false;
        for (int k = 0, rcmmLen = rcvRcmms.size(); k < rcmmLen; k++) {
            TrustRecommendation rcmm = rcvRcmms.get(k);
            if (rcmm.getResponder().getId() == responder.getId()) {
                //-- Adding recommendation
                rcmm.addRecommendation(recommender, trustValue);

                isAdded = true;
                break;
            }
        }
        if (isAdded) {
            return true;
        }

        //============================//============================  // If the agent not added previously

        if (rcvTrust.getRecommendations().size() >= rcvTrust.getRecommendationCap()) {
            // Replacing new history item with an exist one according selected method.
            switch (rcvTrust.getTrustReplaceMethod()) {
                case Sequential_Circular:
                    rcvTrust.getRecommendations().remove(0);
                    break;

                case RemoveLastUpdated:
                    int historyIndex;
                    TrustRecommendation oldHistory = rcvTrust.getRecommendations().get(0);
                    historyIndex = 0;
                    for (int k = 1, historiesLength = rcvRcmms.size(); k < historiesLength; k++) {
                        TrustRecommendation tObs = rcvRcmms.get(k);

                        if (oldHistory.getLastTime() > tObs.getLastTime()) {
                            historyIndex = k;
                            oldHistory = tObs;
                        }
                    }
                    rcvTrust.getRecommendations().remove(historyIndex);
                    break;
            }
        }

        //-- Creating recommendation
        TrustRecommendation recommendation = new TrustRecommendation(receiver, responder);
        recommendation.addRecommendation(recommender, trustValue);
        rcvRcmms.add(recommendation);

        return true;
    }

    private TrustRecommendation getRecommendation(Agent requester, Agent responder) {

        if (requester.getTrust().getRecommendations().size() == 0) {
            return null;
        }

        for (TrustRecommendation recommendation : requester.getTrust().getRecommendations()) {
            if (recommendation.getResponder().getId() == responder.getId()) {
                return recommendation;
            }
        }
        return null;
    }

    public void sendRecommendationsWithInternet(List<Agent> agentList) {

        for (Agent from : agentList) {
            for (Agent to : agentList) {
                if (from.getId() == to.getId()) {
                    continue;
                }
                sendRecommendations(from, to);
            }
        }
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
