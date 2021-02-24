package system;

import utils.Globals;

public class AgentCapacity {

    public AgentCapacity(Agent parentAgent) {

        this.agent = parentAgent;
        int worldBF = parentAgent.getWorld().getBignessFactor() / 4;
        int worldAC = parentAgent.getWorld().getAgentsCount();

//        int randPowerFactor;
//        int i = Globals.RANDOM.nextInt(100);
//        // %70 : 1-20
//        // %20 : 21-50
//        // %10 : 51-100
//        i = i < 70 ? 20 : i < 90 ? 50 : 100;
        int randPowerFactor;
        int policyCount = 4;
        int policyProb [][] = new  int[policyCount][2] ;
        int i  = Globals.RANDOM.nextInt(100);
        // %70 : 1-20
        // %20 : 21-50
        // %10 : 51-100
        policyProb[0][0] = 0;  policyProb[0][1] = 0;
        policyProb[1][0] = 70; policyProb[1][1] = 20;
        policyProb[2][0] = 20; policyProb[2][1] = 50;
        policyProb[3][0] = 10; policyProb[3][1] =100;
        for (int j = 1; j < policyCount ; j ++)
        {
            if(i<policyProb[j][0])
            {
                i = Globals.RANDOM.nextInt(policyProb[j][1]-policyProb[j-1][1]) + policyProb[j-1][1];
                break;
            }
        }
//        i = i < 70 ? 20 : i < 90 ? 50 : 100;

        randPowerFactor = i; //Globals.random.nextInt(i) + 1;
//        randPowerFactor = Globals.RANDOM.nextInt(i) + 1;

        capPower = randPowerFactor;

        float capCoeff = (float) capPower / 100;

        watchRadius = (int) (capCoeff * worldBF);

        watchListCapacity = (int) (capCoeff * worldAC);

        concurrentDoingServiceCap = watchListCapacity / 5;
        if (concurrentDoingServiceCap < 1 && watchListCapacity > 2) {
            concurrentDoingServiceCap = 1;
        }

        historyCap = watchListCapacity * 2;
        historyServiceRecordCap = watchListCapacity;

        System.out.println(
                " | capCoeff: " + capCoeff
                        + " | watchRadius: " + watchRadius
                        + " | watchListCap: " + watchListCapacity
                        + " | concurrentDoSerCap: " + concurrentDoingServiceCap
                        + " | historyCap: " + historyCap
                        + " | historySerRecCap: " + historyServiceRecordCap
        );

    }

    private Agent agent;

    private int historyServiceRecordCap;    //size of services in each history
    private int historyCap;


    private int watchListCapacity;
    private int watchRadius;                        // radius of watch

    private int concurrentDoingServiceCap;             // number of concurrent services that can do for requester of a service

    private int capPower;                            // it will be calculated by other params

    public int getWatchListCapacity() {
        return watchListCapacity;
    }

    public int getWatchRadius() {
        return watchRadius;
    }


    public int getCapPower() {
        return capPower;
    }

    @Override
    public String toString() {
        return "\n\tAgentCapacity{" +
                "\n\t\tagentVisitCap=" + historyServiceRecordCap +
                ",\n\t\tserviceRecordCap=" + historyCap +
                ",\n\t\twatchListCapacity=" + watchListCapacity +
                ",\n\t\twatchRadius=" + watchRadius +
                ",\n\t\tconcurrentDoingServiceCap=" + concurrentDoingServiceCap +
                ",\n\t\tcapPower=" + capPower +
                '}';
    }

    public int getConcurrentDoingServiceCap() {
        return concurrentDoingServiceCap;
    }

    public int getHistoryServiceRecordCap() {
        return historyServiceRecordCap;
    }

    public void setHistoryServiceRecordCap(int historyServiceRecordCap) {
        this.historyServiceRecordCap = historyServiceRecordCap;
    }


    public int getHistoryCap() {
        return historyCap;
    }

    public void setHistoryCap(int historyCap) {
        this.historyCap = historyCap;
    }

}
