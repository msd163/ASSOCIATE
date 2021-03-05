package stateTransition;

import utils.Globals;

public class MissionGenerator {
    public int getNewMission()
    {
        int x = Globals.environment.getStateCount();
        int r = Globals.RANDOM.nextInt(x);
        return r;
    }
}
