package systemLayer;

import utils.Globals;

public class AgentHistory {

    public AgentHistory(Agent doerAgent, int maxServiceCap) {
        this.doerAgent = doerAgent;
        this.serviceCap = maxServiceCap;

        serviceMetaInfos = new ServiceMetaInfo[maxServiceCap];

        createTime = Globals.WORLD_TIMER;
        serviceIndex = -1;
    }

    private Agent doerAgent;

    private ServiceMetaInfo[] serviceMetaInfos;
    private int serviceCap;
    private int serviceIndex;
    private int serviceSize;
    private int createTime;                    // time of creating
    private int lastUpdateTime;                // time of the latest service addition

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
        ServiceMetaInfo info = new ServiceMetaInfo(service, publisher, isExperience ? 0.5f : 1.0f, serviceCap);

        serviceMetaInfos[serviceIndex] = info;

        lastUpdateTime = Globals.WORLD_TIMER;

        return serviceIndex;
    }

    //============================//============================//============================

    public AgentHistory clone() {
        AgentHistory history = new AgentHistory(doerAgent, serviceCap);
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

    public float getEffectiveTrustLevel() {

        float tl = 0;
        int cnt = 0;
        for (ServiceMetaInfo info : serviceMetaInfos) {
            if (info != null) {
                tl += info.getEffectiveTrustScore();
                cnt++;
            }
        }
        if (cnt > 0) {
            return tl / cnt;
        }
        return 0f;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
}
