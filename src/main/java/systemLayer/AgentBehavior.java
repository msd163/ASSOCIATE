package systemLayer;

import _type.TtTrustBehavioralStrategy;
import com.google.gson.annotations.Expose;
import utils.Globals;

public class AgentBehavior {


    public AgentBehavior(TtTrustBehavioralStrategy strategy, int honestDiscretePercentage) {
        this.strategy = strategy;
        switch (strategy) {
            case OnlyHonest:
                honestDegree = 1.0f;
                break;
            case OnlyDishonest:
                honestDegree = 0.0f;
            case Discrete:
                honestDegree = Globals.RANDOM.nextInt(100) < honestDiscretePercentage ? 1.0f : 0.0f;
                break;
            case Fuzzy:
                honestDegree = Globals.RANDOM.nextFloat();
                break;
        }
    }

    @Expose
    private TtTrustBehavioralStrategy strategy;
    @Expose
    private float honestDegree;
    @Expose
    private boolean honestState;

    //============================//============================//============================
    public boolean updateHonestState() {
        if (strategy == TtTrustBehavioralStrategy.Fuzzy) {
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
