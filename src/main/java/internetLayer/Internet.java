package internetLayer;

import societyLayer.agentSubLayer.Agent;
import societyLayer.agentSubLayer.World;

import java.util.ArrayList;
import java.util.List;

public class Internet {

    private World world;
    private List<Agent> intAgents;
    private int updateTime;


    public Internet(World world) {
        updateTime = -1;
        this.world = world;
        intAgents = null;

    }

    public List<Agent> getAgentList() {

        if (intAgents == null) {
            intAgents = new ArrayList<>();
            for (Agent agent : world.getAgents()) {
                if (agent.getCapacity().isHasInternet()) {
                    intAgents.add(agent);
                }
            }
            System.out.println("intAgents.size() " + intAgents.size());
        }

   /*     if (updateTime == Globals.WORLD_TIMER) {
            return intAgents;
        }

        intAgents.clear();
        for (Agent agent : world.getAgents()) {
            if (agent.getCapacity().isHasInternet()) {
                intAgents.add(agent);
            }
        }
        updateTime = Globals.WORLD_TIMER;*/
        return intAgents;
    }


}
