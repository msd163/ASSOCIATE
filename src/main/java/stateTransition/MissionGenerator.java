package stateTransition;

import utils.Globals;

public class MissionGenerator {
    public DefState getNewMission()
    {
        int x = Globals.environment.getStateCount();
        int r = Globals.RANDOM.nextInt(x);
        return Globals.environment.states[r];
    }
}
