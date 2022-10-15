package utils.runner;

import societyLayer.agentSubLayer.Agent;
import trustLayer.TrustManager;
import utils.Config;
import utils.Globals;

import java.util.List;

public class AgentObservationRunner extends Thread {

    private static AgentObservationRunner[] runners;

    public static void execute() throws InterruptedException {
        for (AgentObservationRunner ru : runners) {
            ru.setWorldTime(Globals.WORLD_TIMER);
        }

        boolean isContinue = false;
        while (!isContinue) {
            Thread.sleep(50);
            isContinue = true;
            for (AgentObservationRunner runner : runners) {
                if (!runner.isFinished()) {
                    isContinue = false;
                    break;
                }
            }
        }
    }

    public static void init(List<Agent> agents, TrustManager trustManager) {

        int agentsCount = agents.size();

        runners = new AgentObservationRunner[Config.RUNTIME_THREAD_COUNT];
        int episodeAgentCount = (agentsCount + Config.RUNTIME_THREAD_COUNT - 1) / Config.RUNTIME_THREAD_COUNT;
        int oldIndex = 0;
        for (int j = 0, runnerLength = runners.length; j < runnerLength; j++) {
            int index = episodeAgentCount * (j + 1);
            if (index >= agentsCount) {
                index = agentsCount;
            }
            runners[j] = new AgentObservationRunner(j, agents, oldIndex, index, trustManager);
            runners[j].start();
            oldIndex = index;

        }
    }

    //============================//============================//============================

    public AgentObservationRunner(int identity, List<Agent> agents, int start, int end, TrustManager trustManager) {
        this.identity = identity;

        this.agents = agents;
        this.start = start;
        this.end = end;
        this.trustManager = trustManager;
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
            System.out.println(" " + identity + "  OBS) " + worldTime + ">   " + start + "  ++++ > " + end + "  started.");

            finished = false;
            for (int i = start; i < end; i++) {
                Agent agent = agents.get(i);
                if (agent.getCapacity().getObservationCap() > 0) {
                    trustManager.observe(agent);
                }
            }
            oldWorldTime = worldTime;
            finished = true;
            System.out.println(" " + identity + "  OBS) " + worldTime + "]   " + start + " ---- > " + end + "  finished.");

        }
    }

    private final List<Agent> agents;
    private final int start;
    private final int end;
    private final TrustManager trustManager;


    public boolean isFinished() {
        return finished;
    }

    public void setWorldTime(int worldTime) {
        this.worldTime = worldTime;
        finished = false;
    }
}
