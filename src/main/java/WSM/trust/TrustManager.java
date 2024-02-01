package WSM.trust;

import WSM.trust.data.*;
import core.enums.TtOutLogMethodSection;
import core.enums.TtOutLogStatus;
import SiM.profiler.config.TrustConfigItem;
import SiM.statistics.WorldStatistics;
import WSM.society.agent.Agent;
import WSM.society.agent.WatchedAgent;
import WSM.society.environment.StateX;
import WSM.society.environment.TravelHistory;
import core.utils.Config;
import core.utils.Globals;
import core.utils.OutLog____;

import java.util.ArrayList;
import java.util.List;

public class TrustManager {


    private TrustConfigItem trustConfigItem;
    private WorldStatistics[] wdStatistics;

    public TrustManager(TrustConfigItem simConfig, WorldStatistics[] wdStatistics) {
        this.trustConfigItem = simConfig;
        this.wdStatistics = wdStatistics;
    }

    //============================//============================//============================ Certification Verifier

    /**
     * Checking if the responder has valid certification in the network.
     *
     * @param verifier
     * @param toBeVerified
     * @return TRUE/FALSE
     */
    private Float getValidCertificationTrustValueInDaGra(Agent verifier, Agent toBeVerified) {

        if (verifier.getDaGra() != null) {
            return verifier.getDaGra().getValidCertificationTrustValue(toBeVerified);
        }

        List<WatchedAgent> watchedAgents = verifier.getWatchedAgents();
        if (trustConfigItem.getCert().getIsUseAllDagraAgentToVerify_DaGra()) {
            for (Agent agent : verifier.getWorld().getAgents()) {
                if (agent.getDaGra() != null && agent.getDaGra().getMy() != null) {
                    //todo:
                    return agent.getDaGra().getValidCertificationTrustValue(toBeVerified);
                }
            }
        } else {
            for (int i = 0, watchedAgentsSize = watchedAgents.size(); i < watchedAgentsSize; i++) {
                try {
                    WatchedAgent watchedAgent = watchedAgents.get(i);
                    // For preventing request from the agent that we want to verify it.
                    if (watchedAgent == null || watchedAgent.getAgent().getId() == toBeVerified.getId()) {
                        continue;
                    }

                    //if (verifier.getTrust().getTrustAbstracts()[watchedAgent.getAgent().getIndex()].getTrustValue() > 0) {
                    if (watchedAgent.getAgent().getDaGra() != null) {
                        //todo: it is need to check other agents with DaGra if current agent has no validity info . Continuing for loop
//                    System.out.println("------------>>>> Verifier: " + watchedAgent.getAgent().getId());
                        return watchedAgent.getAgent().getDaGra().getValidCertificationTrustValue(toBeVerified);
                    }
                }catch (Exception e){

                }
                //}
            }
        }

        return null;
    }
    //============================//============================//============================

    private float getForgottenFactor(int time) {
        if (trustConfigItem.getTrustForgottenCoeff() == 0) {
            return 1.0f;
        }
        return (float) Math.pow(1 - trustConfigItem.getTrustForgottenCoeff(), Globals.WORLD_TIMER - time);
    }

    private float getScoreByOrder(int index) {
        if (index < 0) {
            System.out.println(">> Error::getScoreByOrder: index is less than ZERO: " + index);
        }
        switch (trustConfigItem.getTtTrustFormula()) {
            case Formula_1:
                return 1 / (float) ((index + 1) * (index + 1));
            case Formula_2_Maclaurin:
                float alpha = trustConfigItem.getTrustFormula2MaclaurinAlpha();
                return (float) Math.pow(alpha, index);
            //return 1 / (float) (Math.pow(2, index));
            default:
                return 1 / (float) (index + 1);
        }
//        return 1 / (float) ((index + 1) * (index + 1));

    }

    public float getTrustValue(Agent requester, Agent responder) {

        AgentTrust trust = requester.getTrust();
        if (trust.getTrustAbstracts()[responder.getIndex()].getUpdateTime() == Globals.WORLD_TIMER) {
            return trust.getTrustAbstracts()[responder.getIndex()].getTrustValue();
        }

        float innerTrustValue = -64;
        if (trustConfigItem.getCert().isIsUseCertification()                   // Certification mode is enabled in config file
                && responder.getTrust().isHasCandidateForCertification()) {         // The responder agent has capability of gaining certification

            // If DaGra mode is enabled
            if (trustConfigItem.getCert().isIsUseDaGra()) {
                Float trustValueInDaGra = getValidCertificationTrustValueInDaGra(requester, responder);
                if (trustValueInDaGra != null) {
                    innerTrustValue =
                            (trustValueInDaGra > 0) ? +1.0f
                                    : ((trustValueInDaGra < 0) ? -1.0f
                                    : 0.0f);
                    //System.out.println("-------------------------------- DaGra Certification <<" + trustValueInDaGra + ">> requester: " + requester.getId() + " | responder: " + responder.getId() + "");
                }
            }
            // If DaGra mode is disabled, use absolute trust value
            else {
                innerTrustValue = 1.0f;
                OutLog____.pl(TtOutLogMethodSection.TrustMng_GetTrustValue, TtOutLogStatus.SUCCESS, "CRT: Responder (" + responder.getId() + ") with Certificate. Requester: " + requester.getId());
                System.out.println("-------------------------------- Absolute Certification ((1.0)) requester: " + requester.getId() + " | responder: " + responder.getId() + "");
            }
        }

        if (innerTrustValue == -64) {
            innerTrustValue = calcInnerTrustValue(requester, responder);

            //-- trust of recommendation
            if (trustConfigItem.isUseRecommendation()) {
                float calculatedTrustValueFromRecom = calcRecommendedTrustValue(requester, responder);
                //-- If there is any trust value from recommendation
                if (
                        (trustConfigItem.isIsUseNegativeRecommendationEffect() && calculatedTrustValueFromRecom != 0) ||
                                (!trustConfigItem.isIsUseNegativeRecommendationEffect() && calculatedTrustValueFromRecom > 0)
                ) {
                    //-- In safe mode, if there is no inner trust value, final trust value will be recommendation trust value.
                    if (trustConfigItem.isIsSafeUseRecommendation()) {
                        if (innerTrustValue != 0) {
                            innerTrustValue =
                                    (trustConfigItem.getRecommendationCoeff() * calculatedTrustValueFromRecom)
                                            + ((1 - trustConfigItem.getRecommendationCoeff()) * innerTrustValue)
                            ;

                        } else
                        //-- If inner trust value is Zero
                        {
                            innerTrustValue = calculatedTrustValueFromRecom;
                        }
                    }
                    //-- If don't use safe mode recommendation
                    else {
                        innerTrustValue =
                                (trustConfigItem.getRecommendationCoeff() * calculatedTrustValueFromRecom)
                                        + ((1 - trustConfigItem.getRecommendationCoeff()) * innerTrustValue)
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

        List<Float> sntrs = getSortedNormalizedTrustScores(requester, responder);


        //todo: todo is in following comment...
        /*
         * در حالت بسیار نادر ممکن است که قدر مطلق امتیازات موجود در لیست  با هم برابر باشد
         * مثلا 5 قدرمطلق 1 داشته باشیم که مثلا دو عدد -1 بوده و سه عدد +1
         * برای این حالت باید سه اتفاق به صورت همزمان رخ دهد:
         * 1) منتشر کنندگان در یک زمان یکسان امتیازات را منتشر کرده باشند
         * 2) امتیاز داده شده توسط منتشر کننده به اعتماد شونده با هم برابر باشند
         * 3) اعتماد دریافت کننده به همه منتشر کنندگان با هم برابر باشد
         * نکته: منتشر کنندگان با هم متفاوت هستند. یعنی نمی توان یک منتشر کننده دو امتیاز را با سه شرط بالا داشته باشد.
         *
         * در این حالت نیاز است که این اتفاق در لیست مرتب شده شناسایی شود و مرتب سازی بر اساس این که تعداد کدام علامت بیشتر است چیده شود.
         * مثلا اگر سه +1 وجود داشت ابتدا سه +1 در لیست قرار گیرد و سپس دو -1
         * با این کار اولویت امتیازات به عامل هایی که بیشترین امتیاز را داده اند اختصاص داده می شود
         *
         * */

        float trustValue = 0.0f;

        if (sntrs.size() == 0) {
            return trustValue;
        }

        double oldTrustValue = 0;
        double oldScore = 0;
        long fluctuationCount = 0;

        boolean isOccurs = false;
        for (int i = 0, tsSize = sntrs.size(); i < tsSize; i++) {

            Float currentScore = sntrs.get(i);
            if (currentScore == 0.0f) {
                break;
            }

            double valueInSeriesTerm = formulateTrustValue(i, currentScore);
            oldTrustValue = trustValue;
            trustValue += valueInSeriesTerm;


            if (tsSize > 1) {

                if (oldScore * currentScore < 0) {
                    //- The
                    fluctuationCount++;
                    if (oldScore > 0) {
                        wdStatistics[Globals.WORLD_TIMER].getStatisticsHypo().add_allHypoFluctPosToNeg();
                    } else {
                        wdStatistics[Globals.WORLD_TIMER].getStatisticsHypo().add_allHypoFluctNegToPos();
                    }
                }

                if ((oldTrustValue * trustValue < 0)) {
                    if (!isOccurs) {
                        if (oldTrustValue > 0) {
                            wdStatistics[Globals.WORLD_TIMER].getStatisticsHypo().add_allHypoResistanceByNumberAgainstPos(i);
                        } else {
                            wdStatistics[Globals.WORLD_TIMER].getStatisticsHypo().add_allHypoResistanceByNumberAgainstNeg(i);
                        }
                        isOccurs = true;
                    }
                }

            }
            oldScore = currentScore;

        }
        if (fluctuationCount > Config.STATISTICS_HYPOCRITE_DIAGNOSIS_THRESHOLD) {
            wdStatistics[Globals.WORLD_TIMER].getStatisticsHypo().add_allHypoSuspectDiagnosis();
        }


        Float scoreAtTopOfList = sntrs.get(0);
        if (scoreAtTopOfList * trustValue < 0) {
            if (scoreAtTopOfList > 0) {
                wdStatistics[Globals.WORLD_TIMER].getStatisticsHypo().add_allHypoIgnoredPos();
                if (!responder.getBehavior().getHasHonestState()) {
                    wdStatistics[Globals.WORLD_TIMER].getStatisticsHypo().add_allHypoIgnoredPosTP();
                }
            } else {
                wdStatistics[Globals.WORLD_TIMER].getStatisticsHypo().add_allHypoIgnoredNeg();
                if (responder.getBehavior().getHasHonestState()) {
                    wdStatistics[Globals.WORLD_TIMER].getStatisticsHypo().add_allHypoIgnoredNegTP();
                }
            }
        }

        return trustValue;
    }

    private double formulateTrustValue(int index, Float t) {

        switch (trustConfigItem.getTtTrustFormula()) {
            case Formula_1:
                return ((t) / ((index + 2) * (index + 2)));
            case Formula_2_Maclaurin:
                /* //todo: poison
                 * برای این مورد باید اثر آخرین امتیازات کسب شده متضاد را کمتر کنیم.
                 *  برای این منظور می توانیم از توزیع هایی مثل پواسون در فرمولهایی که زمان در آنها دخیل است، مثل ضریب فراموشی، استفاده کرد.
                 * */
                float alpha = trustConfigItem.getTrustFormula2MaclaurinAlpha();
                return (1 - alpha) * t * Math.pow(alpha, index);
            //return 0.5f * t * Math.pow(0.5, index);
        }
        return t;
    }

    private List<Float> getSortedNormalizedTrustScores(Agent requester, Agent responder) {

        List<Float> normList = new ArrayList<>();

        if (trustConfigItem.isIsUseExperience()) {
            TrustExperience experience = getExperience(requester, responder);
            if (experience != null) {
                normList.addAll(normalizeWithForgottenFactor(experience.getItems()));
            }
        }

        if (trustConfigItem.isIsUseIndirectExperience()) {
            TrustIndirectExperience indirectExperience = getIndirectExperience(requester, responder);
            if (indirectExperience != null) {
                normList.addAll(normalizeWithForgottenFactorAndTrustValue(requester, indirectExperience.getItems()));
            }
        }

        if (trustConfigItem.isIsUseObservation()) {
            TrustObservation observation = getObservation(requester, responder);
            if (observation != null) {
                normList.addAll(normalizeWithForgottenFactor(observation.getItems()));
            }
        }

        if (trustConfigItem.isIsUseIndirectObservation()) {
            TrustIndirectObservation indirectObservation = getIndirectObservation(requester, responder);
            if (indirectObservation != null) {
                normList.addAll(normalizeWithForgottenFactorAndTrustValue(requester, indirectObservation.getItems()));
            }
        }

        if (normList.size() == 0) {
            return normList;
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


        float recommValue = 0;
        int index = 0;
        for (int i = 0, tsSize = sntrs.size(); i < tsSize; i++) {
            recommValue += formulateTrustValue(index, sntrs.get(i));
            // System.out.println(i + ": index: " + index + " | " + t + "  > " + xxxxx);
            index++;
        }

        return recommValue;
    }

    //============================
    private List<Float> normalizeWithForgottenFactor(List<TrustDataItem> items) {

        List<Float> norList = new ArrayList<>();

        for (int i = 0, itemsSize = items.size(); i < itemsSize; i++) {
            TrustDataItem item = items.get(i);
            norList.add(item.getScore() * getForgottenFactor(item.getTime()));
        }
        return norList;
    }

    private List<Float> normalizeWithForgottenFactor(TrustDataItem[] items) {

        List<Float> norList = new ArrayList<>();

        for (int i = 0, itemsSize = items.length; i < itemsSize; i++) {
            TrustDataItem item = items[i];
            if (item == null) {
                break;
            }
            norList.add(item.getScore() * getForgottenFactor(item.getTime()));
        }
        return norList;
    }

    private List<Float> normalizeWithForgottenFactorAndTrustValue(Agent requester, List<TrustDataItem> items) {

        List<Float> norList = new ArrayList<>();

        for (int i = 0, itemsSize = items.size(); i < itemsSize; i++) {
            TrustDataItem item = items.get(i);
            //-- For preventing stack overflow in calculating trust, we use trust value that is calculated previously
            //-- Calculating trust value of requester to recommender
            TrustAbstract trustAbstract = requester.getTrust().getTrustAbstracts()[item.getIssuer().getIndex()];
            if (trustAbstract != null) {
                float trustValue = trustAbstract.getTrustValue(); //getTrustValue(requester, item.getIssuer());
                if (trustValue > 0) {
                    norList.add(trustValue * item.getScore() * getForgottenFactor(item.getTime()));
                }
            }
        }
        return norList;
    }

    private List<Float> normalizeWithForgottenFactorAndTrustValue(Agent requester, TrustDataItem[] items) {

        List<Float> norList = new ArrayList<>();

        for (int i = 0, itemsSize = items.length; i < itemsSize; i++) {
            TrustDataItem item = items[i];
            if (item == null) {
                break;
            }
            //-- For preventing stack overflow in calculating trust, we use trust value that is calculated previously
            //-- Calculating trust value of requester to recommender
            TrustAbstract trustAbstract = requester.getTrust().getTrustAbstracts()[item.getIssuer().getIndex()];
            if (trustAbstract != null) {
                float trustValue = trustAbstract.getTrustValue(); //getTrustValue(requester, item.getIssuer());
                if (trustValue > 0) {
                    norList.add(trustValue * item.getScore() * getForgottenFactor(item.getTime()));
                }
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
        TrustDataArray experiences = requester.getTrust().getExperiences();

        int effect = isPositive ? 1 : -1;

        int responderId = -1;
        int consideredExpCount = -1;
        for (int tvhIndex = travelHistory.size() - 1, scoreCount = 0; tvhIndex > -1; tvhIndex--) {

            //-- If experience depth exceeded. this depth defines how many of responder agents considered in score mechanism.
            if (scoreCount >= trustConfigItem.getExperienceDepthInScoring()) {
                return;
            }

            TravelHistory tvh = travelHistory.get(tvhIndex);

            // If the travel is done randomly or by itself info
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

            int indexIfExist = experiences.getIndexIfExist(tvh.getResponder());

            float score = effect * getScoreByOrder(++consideredExpCount);

            if (indexIfExist > -1) {

                //-- Adding experience
                ((TrustExperience) experiences.get(indexIfExist)).addExperience(requester, source, destination, score);

                travelHistory.get(tvhIndex).setIsTrustCalculated(true);
                responderId = experiences.get(indexIfExist).getResponder().getId();
                scoreCount++;

                continue;
            }

            //============================//============================  // If the agent not added previously

            //-- Creating experience
            TrustExperience experience = new TrustExperience(requester, tvh.getResponder());
            experience.addExperience(requester, source, destination, score);
            experiences.add(experience);

            responderId = tvh.getResponder().getId();
            scoreCount++;
            travelHistory.get(tvhIndex).setIsTrustCalculated(true);

        }
    }

    private TrustExperience getExperience(Agent requester, Agent responder) {
        return (TrustExperience) requester.getTrust().getExperiences().get(requester, responder);
    }

    //============================//============================//============================ Indirect Experience

    public boolean shareExperiences(Agent issuer/*experimenter*/, Agent receiver) {

        if (issuer.getTrust().getExperiences().size() == 0 || receiver.getTrust().getIndirectExperienceCap() <= 0) {
            return false;
        }

        TrustDataArray experiences = issuer.getTrust().getExperiences();
        for (int i = 0, experiencesSize = experiences.size(); i < experiencesSize; i++) {
            TrustData experience = experiences.get(i);
            if (experience == null) {
                break;
            }
            TrustDataItem[] items = experience.getItems();
            for (int j = 0, itemsSize = items.length; j < itemsSize; j++) {
                TrustDataItem item = items[j];
                if (item == null) {
                    break;
                }
                addIndirectExperience(item, experience.getResponder(), issuer, receiver);
            }
        }
        return true;
    }

    public boolean addIndirectExperience(TrustDataItem indirectExperienceItem, Agent responder, Agent issuer, Agent receiver) {

        //============================
        TrustDataArray indirectExperiences = receiver.getTrust().getIndirectExperiences();

        //============================//============================ // Check if the agent added to trustHistory previously and return it's ID.
        int indexIfExist = indirectExperiences.getIndexIfExist(responder);

        if (indexIfExist > -1) {
            ((TrustIndirectExperience) indirectExperiences.get(indexIfExist)).addExperience(indirectExperienceItem, issuer);
            return true;
        }

        //============================//============================  // If the agent not added previously

        //-- Creating experience
        TrustIndirectExperience indirectExperience = new TrustIndirectExperience(receiver, responder);
        indirectExperience.addExperience(indirectExperienceItem, issuer);
        indirectExperiences.add(indirectExperience);

        return true;

    }

    private TrustIndirectExperience getIndirectExperience(Agent requester, Agent responder) {

        if (requester.getTrust().getIndirectExperiences().size() == 0) {
            return null;
        }

        TrustDataArray indirectExperiences = requester.getTrust().getIndirectExperiences();
        for (int i = 0, indirectExperiencesSize = indirectExperiences.size(); i < indirectExperiencesSize; i++) {
            TrustData experience = indirectExperiences.get(i);
            if (experience != null && experience.getResponder() != null && responder != null &&
                    experience.getResponder().getId() == responder.getId()) {
                return (TrustIndirectExperience) experience;
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

        List<WatchedAgent> watchedAgents = observer.getWatchedAgents();
        for (int i = 0, watchedAgentsSize = watchedAgents.size(); i < watchedAgentsSize; i++) {
            WatchedAgent wa = watchedAgents.get(i);
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
        TrustDataArray observations = observer.getTrust().getObservations();

        //============================//============================ // Check if the agent added to observations previously and return it's ID.


        int indexIfExist = observations.getIndexIfExist(responder);

        float score = (isInTarget ? 1 : -1) * getScoreByOrder(0);

        if (indexIfExist > -1) {
            //-- Adding experience
            ((TrustObservation) observations.get(indexIfExist)).addObservation(observer, observed, null, null, score);
            return true;
        }

        //============================//============================  // If the agent not added previously

        //-- Creating observation
        TrustObservation observation = new TrustObservation(observer, responder);

        observation.addObservation(observer, observed, null, null, score);
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
        return (TrustObservation) requester.getTrust().getObservations().get(requester, responder);
    }


    //============================//============================//============================ Indirect Observation

    public boolean shareObservations(Agent issuer/*observer*/, Agent receiver) {

        if (issuer.getTrust().getObservations().size() == 0 || receiver.getTrust().getIndirectObservationCap() <= 0) {
            return false;
        }

        TrustDataArray observations = issuer.getTrust().getObservations();
        for (int j = 0, observationsSize = observations.size(); j < observationsSize; j++) {
            TrustData observation = observations.get(j);
            if (observation == null) {
                break;
            }
            // if the receiver show the responder in the current observation, the observation will be share with it.
            if (receiver.isAgentInWatchList(observation.getResponder())) {
                TrustDataItem[] items = observation.getItems();
                for (int i = 0, itemsSize = items.length; i < itemsSize; i++) {
                    TrustDataItem item = items[i];
                    if (item == null) {
                        break;
                    }
                    addIndirectObservation(item, observation.getResponder(), issuer, receiver);
                }
            }
        }

        return true;
    }

    public boolean addIndirectObservation(TrustDataItem indirectObservationItem, Agent responder, Agent issuer/*observer*/, Agent receiver) {

        //============================
        TrustDataArray indirectObservations = receiver.getTrust().getIndirectObservations();

        //============================//============================ // Check if the agent added to trustHistory previously and return it's ID.
        int indexIfExist = indirectObservations.getIndexIfExist(responder);

        if (indexIfExist > -1) {
            TrustIndirectObservation trustIndirectObservation = (TrustIndirectObservation) indirectObservations.get(indexIfExist);
            if (trustIndirectObservation != null) {

//                TrustDataItem[] items = trustIndirectObservation.getItems();
                /*if (items.length > 1) {
                    Arrays.sort(items, (a, b) -> {
                        if (a == null && b == null) {
                            return 0;
                        } else if (a == null) {
                            return 1;
                        } else if (b == null) {
                            return -1;
                        } else {
                            return -Integer.compare(a.getTime(), b.getTime());
                        }

                    });
                }


                System.out.print("\n>> ");
                for (TrustDataItem item : items) {
                    if (item == null) {
                        break;
                    }
                    System.out.print("[" + item.getTime() + " , " + item.getScore() + "] ");
                }
                System.out.print(" | \n ");*/

                trustIndirectObservation.addObservation(indirectObservationItem, issuer);
            }
            return true;
        }

        //============================//============================  // If the agent not added previously

        //-- Creating observation
        TrustIndirectObservation indirectObservation = new TrustIndirectObservation(receiver, responder);
        indirectObservation.addObservation(indirectObservationItem, issuer);
        indirectObservations.add(indirectObservation);

        return true;
    }

    private TrustIndirectObservation getIndirectObservation(Agent requester, Agent responder) {
        return (TrustIndirectObservation) requester.getTrust().getIndirectObservations().get(requester, responder);
    }

    //============================//============================//============================ Recommendation

    public boolean sendRecommendations(Agent recommender/*observer*/, Agent receiver) {

        if (receiver.getTrust().getRecommendationCap() <= 0) {
            return false;
        }

        TrustAbstract[] trustAbstracts = recommender.getTrust().getTrustAbstracts();
        for (int i = 0, trustAbstractsLength = trustAbstracts.length; i < trustAbstractsLength; i++) {
            TrustAbstract trustAbstract = trustAbstracts[i];
            //-- If trustAbstract of recommender is about receiver of recommender, ignore it.
            //-- It is not necessary to save recommendation of itself by self.
            //todo: use recommendation values about us by other for analysing environment sight about us (as receiver of recommendation)
            if (trustAbstract.getResponder().getId() == receiver.getId()) {
                continue;
            }
            if (trustAbstract.getTrustValue() > 0) {
                if (!trustAbstract.isSendThisTrustValueToReceiverAgent(receiver.getId())) {
                    addRecommendation(trustAbstract.getResponder(), recommender, receiver, trustAbstract.getTrustValue());
                    trustAbstract.addInfoOfSentValueToReceiver(receiver.getId());
                }
            } else if (trustAbstract.getTrustValue() < 0 && trustConfigItem.isIsUseNegativeRecommendationEffect()) {
                if (!trustAbstract.isSendThisTrustValueToReceiverAgent(receiver.getId())) {
                    addRecommendation(trustAbstract.getResponder(), recommender, receiver, trustAbstract.getTrustValue());
                    trustAbstract.addInfoOfSentValueToReceiver(receiver.getId());
                }
            }
        }

        return true;
    }

    public synchronized boolean addRecommendation(Agent responder, Agent recommender/*issuer*/, Agent receiver, float trustValue) {


        //============================
        TrustDataArray recommendations = receiver.getTrust().getRecommendations();

        //============================//============================ // Check if the agent added to trustHistory previously and return it's ID.
        int indexIfExist = recommendations.getIndexIfExist(responder);

        if (indexIfExist > -1) {
            ((TrustRecommendation) recommendations.get(indexIfExist)).addRecommendation(recommender, trustValue);
            return true;
        }

        //============================//============================  // If the agent not added previously
        //-- Creating recommendation
        TrustRecommendation recommendation = new TrustRecommendation(receiver, responder);
        recommendation.addRecommendation(recommender, trustValue);
        recommendations.add(recommendation);

        return true;
    }

    private TrustRecommendation getRecommendation(Agent requester, Agent responder) {
        return (TrustRecommendation) requester.getTrust().getRecommendations().get(requester, responder);
    }

    public void sendRecommendationsWithInternet(List<Agent> agentList) {

        for (int i = 0, agentListSize = agentList.size(); i < agentListSize; i++) {
            Agent from = agentList.get(i);
            for (int j = 0, listSize = agentList.size(); j < listSize; j++) {
                Agent to = agentList.get(j);
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
Exception in thread "Thread-13" java.util.ConcurrentModificationException
        at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:911)
        at java.util.ArrayList$Itr.next(ArrayList.java:861)
        at trustLayer.TrustManager.getIndirectObservation(TrustManager.java:905)
        at trustLayer.TrustManager.getSortedNormalizedTrustScores(TrustManager.java:300)
        at trustLayer.TrustManager.calcInnerTrustValue(TrustManager.java:165)
        at trustLayer.TrustManager.getTrustValue(TrustManager.java:120)
        at transitionLayer.Router.updateNextSteps(Router.java:336)
        at utils.runner.AgentUpdaterRunner.run(AgentUpdaterRunner.java:88)
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
