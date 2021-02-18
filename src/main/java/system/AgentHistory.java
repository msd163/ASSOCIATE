package system;

import java.util.Date;

public class AgentHistory {

    public AgentHistory(Agent doerAgent, int maxServiceCap) {
        this.doerAgent = doerAgent;
        this.serviceCap = maxServiceCap;

        serviceMetaInfos = new ServiceMetaInfo[maxServiceCap];

        trustScore = 0;
        createTime = new Date().getTime();
        serviceIndex = -1;
    }

    private Agent doerAgent;

    private ServiceMetaInfo[] serviceMetaInfos;
    private int serviceCap;
    private int serviceIndex;
    private int serviceSize;
    private long createTime;                    // time of creating
    private long lastUpdateTime;                // time of the latest service addition

    private float trustScore;
    //============================//============================//============================

    public int addToHistory(Service service, Agent publisher, boolean isExperience) {

        if (isExperience) {
            for (int i = 0, serviceMetaInfosLength = serviceMetaInfos.length; i < serviceMetaInfosLength; i++) {
                ServiceMetaInfo info = serviceMetaInfos[i];
                if (info != null && info.getService().getId() == service.getId()) {
                    // System.out.println("|    >  added previously: " + service.getId());
                    return i;
                }
            }
        }

        if (serviceSize < serviceCap) {
            serviceSize++;
        }
        serviceIndex++;

        //todo: [policy] : removing the oldest trust value or the trust with minimum effect factor
        if (serviceIndex >= serviceCap) {
            serviceIndex = 0;
        }
        //todo: [policy] : Coefficient of serviceMetaInfo have to be calculated
        ServiceMetaInfo info = new ServiceMetaInfo(service, publisher, isExperience ? 0.5f : 1.0f);

        serviceMetaInfos[serviceIndex] = info;

        // calculating trust score
        //todo: [policy] : defining accurate function to calculate the trust score
        trustScore += service.getResult();

        lastUpdateTime = new Date().getTime();

        return serviceIndex;
    }

    //============================//============================//============================

    public AgentHistory clone() {
        AgentHistory history = new AgentHistory(doerAgent, serviceCap);
        history.trustScore = this.trustScore;
        history.serviceMetaInfos = serviceMetaInfos;
        history.serviceIndex = this.serviceIndex;
        history.serviceSize = this.serviceSize;
        return history;
    }

    public Agent getDoerAgent() {
        return doerAgent;
    }

    public ServiceMetaInfo[] getServiceMetaInfos() {
        return serviceMetaInfos;
    }

    public float getTrustScore() {
        return trustScore;
    }
}
