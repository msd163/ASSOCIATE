package simulateLayer.statistics;

import societyLayer.agentSubLayer.Agent;

public class AgentStatistics {

    private Agent agent;
    private int asTrustee_RequestCount;
    private int asTrustee_FP;
    private int asTrustee_TP;
    private int asTrustee_FN;
    private int asTrustee_TN;
    private int asRequester_RequestCount;
    private int asRequester_FP;
    private int asRequester_TP;
    private int asRequester_FN;
    private int asRequester_TN;

    public AgentStatistics(Agent agent) {
        this.agent = agent;
        asRequester_RequestCount
                = asTrustee_RequestCount
                = asTrustee_FP
                = asTrustee_TP
                = asTrustee_FN
                = asTrustee_TN
                = asRequester_FP
                = asRequester_TP
                = asRequester_FN
                = asRequester_TN
                = 0;
    }

    public AgentStatistics(Agent agent, AgentStatistics prevStats) {
        this.agent = agent;
      //  asRequester_RequestCount = prevStats.asTrustee_RequestCount;
       // asTrustee_RequestCount = prevStats.asTrustee_RequestCount;
//        asTrustee_FP = prevStats.asTrustee_FP;
//        asTrustee_TP = prevStats.asTrustee_TP;
//        asTrustee_FN = prevStats.asTrustee_FN;
//        asTrustee_TN = prevStats.asRequester_TN;
//        asRequester_FP = prevStats.asRequester_FP;
//        asRequester_TP = prevStats.asRequester_TP;
//        asRequester_FN = prevStats.asRequester_FN;
//        asRequester_TN = prevStats.asRequester_TN;
    }

    //============================//============================


    public void addAsTrustee_FP() {
        asTrustee_FP++;
        asTrustee_RequestCount++;
    }

    public void addAsTrustee_TP() {
        asTrustee_TP++;
        asTrustee_RequestCount++;
    }

    public void addAsTrustee_FN() {
        asTrustee_FN++;
        asTrustee_RequestCount++;
    }

    public void addAsTrustee_TN() {
        asTrustee_TN++;
        asTrustee_RequestCount++;
    }

    public void addAsRequester_FP() {
        asRequester_FP++;
    }

    public void addAsRequester_TP() {
        asRequester_TP++;
    }

    public void addAsRequester_FN() {
        asRequester_FN++;
    }

    public void addAsRequester_TN() {
        asRequester_TN++;
    }

    public void addAsRequester_RequestCount() {
        asRequester_RequestCount++;
    }

    public void addAsTrustee_RequestCount() {
        asTrustee_RequestCount++;
    }

    //============================//============================

    public Agent getAgent() {
        return agent;
    }

    public int getAsTrustee_FP() {
        return asTrustee_FP;
    }

    public int getAsTrustee_TP() {
        return asTrustee_TP;
    }

    public int getAsTrustee_FN() {
        return asTrustee_FN;
    }

    public int getAsTrustee_TN() {
        return asTrustee_TN;
    }

    public int getAsRequester_FP() {
        return asRequester_FP;
    }

    public int getAsRequester_TP() {
        return asRequester_TP;
    }

    public int getAsRequester_FN() {
        return asRequester_FN;
    }

    public int getAsRequester_TN() {
        return asRequester_TN;
    }

    public int getAsTrustee_RequestCount() {
        return asTrustee_RequestCount;
    }

    public int getAsRequester_RequestCount() {
        return asRequester_RequestCount;
    }
}
