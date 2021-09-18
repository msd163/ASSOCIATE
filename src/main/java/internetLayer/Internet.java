package internetLayer;

import systemLayer.Agent;
import systemLayer.World;
import utils.Globals;

import java.util.ArrayList;
import java.util.List;

public class Internet {

    private World world;
    private List<Agent> intAgents;
    private int updateTime;


    public Internet(World world) {
        updateTime = -1;
        this.world = world;
        intAgents = new ArrayList<>();
    }

    public List<Agent> getAgentList() {

        if (updateTime == Globals.WORLD_TIMER) {
            return intAgents;
        }

        intAgents.clear();
        for (Agent agent : world.getAgents()) {
            if (agent.getCapacity().isHasInternet()) {
                intAgents.add(agent);
            }
        }
        updateTime = Globals.WORLD_TIMER;
        return intAgents;
    }


}
