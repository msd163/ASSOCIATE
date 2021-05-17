package systemLayer;

import _type.TtBehaviorStrategy;
import utils.Config;
import utils.Globals;

public class AgentBehavior {

    public AgentBehavior() {
        switch (Config.TRUST_BEHAVIOR_STRATEGY) {
            case OnlyHonest:
                honestDegree = 1.0f;
                break;
            case OnlyDishonest:
                honestDegree = 0.0f;
            case Discrete:
                honestDegree = Globals.RANDOM.nextInt(100) < Config.TRUST_HONEST_DISCRETE_PERCENT ? 1.0f : 0.0f;
                break;
            case Fuzzy:
                honestDegree = Globals.RANDOM.nextFloat();
                break;
        }
    }

    private float honestDegree;
    private boolean honestState;

    //============================//============================//============================
    public boolean updateHonestState() {
        if (Config.TRUST_BEHAVIOR_STRATEGY == TtBehaviorStrategy.Fuzzy) {
            honestState = Globals.RANDOM.nextFloat() < honestDegree;
            return honestState;
        }
        honestState = honestDegree == 1.f;
        return honestState;
    }

    public boolean getIsHonest() {
        return honestState;
    }
    //============================//============================//============================


    @Override
    public String toString() {
        return "\n\tAgentProfile{" +
                "\n\t\thonestDegree=" + honestDegree +
                ",\n\t\thonestState=" + honestState +
                '}';
    }
}
