package utils.runner;

import societyLayer.agentSubLayer.Agent;
import transitionLayer.Router;
import utils.Config;
import utils.Globals;

import java.util.List;

public class AgentUpdaterRunner extends Thread {

    private static AgentUpdaterRunner[] runners;

    public static void execute() throws InterruptedException {
        for (AgentUpdaterRunner ru : runners) {
            ru.setWorldTime(Globals.WORLD_TIMER);
        }

        boolean isContinue = false;
        while (!isContinue) {
            Thread.sleep(50);
            isContinue = true;
            for (AgentUpdaterRunner runner : runners) {
                if (!runner.isFinished()) {
                    isContinue = false;
                    break;
                }
            }
        }
    }

    public static void init(List<Agent> agents, Router router) {

        int agentsCount = agents.size();

        runners = new AgentUpdaterRunner[Config.RUNTIME_THREAD_COUNT];
        int episodeAgentCount = (agentsCount + Config.RUNTIME_THREAD_COUNT - 1) / Config.RUNTIME_THREAD_COUNT;
        int oldIndex = 0;
        for (int j = 0, runnerLength = runners.length; j < runnerLength; j++) {
            int index = episodeAgentCount * (j + 1);
            if (index >= agentsCount) {
                index = agentsCount;
            }
            runners[j] = new AgentUpdaterRunner(j, agents, oldIndex, index, router);
            runners[j].start();
            oldIndex = index;

        }
    }

    //============================//============================//============================

    public AgentUpdaterRunner(int identity, List<Agent> agents, int start, int end, Router router) {
        this.identity = identity;

        this.agents = agents;
        this.start = start;
        this.end = end;
        this.router = router;
        finished = false;
    }

    private int worldTime = -1;
    private int oldWorldTime = -1;
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
            System.out.println(" " + identity + "  UPD) " + worldTime + ">   " + start + "  ++++ > " + end + "  started.");

            finished = false;
            for (int i = start; i < end; i++) {
                Agent agent = agents.get(i);
                // System.out.print("World: " + Globals.SIMULATION_TIMER + " Time: " + Globals.WORLD_TIMER + "  | " + i );
                //todo: adding doing service capacity to agents as capacity param
                // System.out.print(" | 1 > profile...");
                agent.updateProfile();
                // System.out.print(" | 2 > watchList...");
                agent.updateWatchList();
                // System.out.print(" | 3 > nextStep...");
                router.updateNextSteps(agent);
            }
            oldWorldTime = worldTime;
            finished = true;
            System.out.println(" " + identity + "  UPD) " + worldTime + "]   " + start + " ---- > " + end + "  finished.");

        }
    }

    private int identity;
    private final List<Agent> agents;
    private final int start;
    private final int end;
    private final Router router;


    public boolean isFinished() {
        return finished;
    }

    public void setWorldTime(int worldTime) {
        this.worldTime = worldTime;
        finished = false;
    }
}
