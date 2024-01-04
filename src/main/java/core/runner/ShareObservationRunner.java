package core.runner;

import WSM.society.agent.Agent;
import WSM.society.agent.WatchedAgent;
import WSM.trust.TrustManager;
import core.utils.Config;
import core.utils.Globals;

import java.util.List;

public class ShareObservationRunner extends Thread {

    private static ShareObservationRunner[] runners;

    public static void execute() throws InterruptedException {
        for (ShareObservationRunner ru : runners) {
            ru.setWorldTime(Globals.WORLD_TIMER);
        }

        boolean isContinue = false;
        while (!isContinue) {
            Thread.sleep(50);
            isContinue = true;
            for (ShareObservationRunner runner : runners) {
                if (!runner.isFinished()) {
                    isContinue = false;
                    break;
                }
            }
        }
    }

    public static void init(List<Agent> agents, TrustManager trustManager) {

        int agentsCount = agents.size();

        runners = new ShareObservationRunner[Config.RUNTIME_THREAD_COUNT];
        //int episodeAgentCount = agentsCount / Config.RUNTIME_THREAD_COUNT;
        int oldIndex = 0;
//        for (int j = 0, runnerLength = runners.length; j < runnerLength; j++) {
//            int index = episodeAgentCount * (j + 1);
//            if (index >= agentsCount) {
//                index = agentsCount;
//            }
//            runners[j] = new ShareObservationRunner(j, agents, oldIndex, index, trustManager);
//            runners[j].start();
//            oldIndex = index;
//
//        }
        for (int j = 0, runnerLength = runners.length; j < runnerLength; j++) {
            runners[j] = new ShareObservationRunner(j, agents, j, agentsCount, trustManager, runnerLength);
            runners[j].start();

        }
    }

    //============================//============================//============================

    public ShareObservationRunner(int identity, List<Agent> agents, int start, int end, TrustManager trustManager, int runnerLength) {
        this.identity = identity;

        this.agents = agents;
        this.start = start;
        this.end = end;
        this.trustManager = trustManager;
        this.step = runnerLength;
        finished = false;
    }

    private int worldTime = -1;
    private int oldWorldTime = -1;
    private int identity;
    private boolean finished;


    @Override
    public void run() {
        while (true) {
            while (oldWorldTime == worldTime) {
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(" " + identity + "  SHR) " + Globals.SIMULATION_TIMER + ":" + worldTime + ">   " + start + "  ++++ > " + end + "  started.");


            finished = false;
            for (int i = start; i < end; i += step) {
                Agent issuer = agents.get(i);
                if (issuer.getCapacity().getObservationCap() > 0) {
                    List<WatchedAgent> watchedAgents = issuer.getWatchedAgents();
                    if (watchedAgents != null && !watchedAgents.isEmpty()) {
                        for (int j = 0, agentsSize = watchedAgents.size(); j < agentsSize; j++) {
                            Agent receiver = agents.get(j);
                            if (issuer.getId() != receiver.getId()) {
                                trustManager.shareObservations(issuer, receiver);
                            }
                        }
                    }
                }
            }
            oldWorldTime = worldTime;
            finished = true;
            System.out.println(" " + identity + "  SHR) " + Globals.SIMULATION_TIMER + ":" + worldTime + "]   " + start + " ---- > " + end + "  finished.");

        }
    }

    private final List<Agent> agents;
    private final int start;
    private final int end;
    private final TrustManager trustManager;
    private final int step;


    public boolean isFinished() {
        return finished;
    }

    public void setWorldTime(int worldTime) {
        this.worldTime = worldTime;
        finished = false;
    }
}
