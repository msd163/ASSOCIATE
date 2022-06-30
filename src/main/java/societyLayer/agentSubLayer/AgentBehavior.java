package societyLayer.agentSubLayer;

import _type.TtBehaviorState;
import com.google.gson.annotations.Expose;
import utils.Globals;
import simulateLayer.profiler.PopulationBunchHypocriteBehavior;
import simulateLayer.profiler.PopulationBunchBehaviorParam;

public class AgentBehavior {


    public AgentBehavior(PopulationBunchBehaviorParam behavior) {
        int rand = Globals.RANDOM.nextInt(100);

        if (rand < behavior.getAdversaryPercent()) {
            behaviorState = TtBehaviorState.Adversary;
        } else if (rand < behavior.getMischiefPercent() + behavior.getAdversaryPercent()) {
            behaviorState = TtBehaviorState.Mischief;
        } else if (rand < behavior.getMischiefPercent() + behavior.getAdversaryPercent() + behavior.getHonestPercent()) {
            behaviorState = TtBehaviorState.Honest;
        } else {
            behaviorState = TtBehaviorState.Hypocrite;
            hypocriteBehavior = behavior.getHypocriteBehavior();
        }
        updateBehaviorState();
    }

    //-- Actual behavior state of the agent
    @Expose
    private TtBehaviorState behaviorState;

    //-- current behavior of the agent according it's strategy
    private TtBehaviorState currentBehaviorState;

    @Expose
    private PopulationBunchHypocriteBehavior hypocriteBehavior;

    public TtBehaviorState getBehaviorState() {
        return behaviorState;
    }

    public boolean getHasHonestState() {
        return behaviorState == TtBehaviorState.Honest;
    }

    public boolean getHasAdversaryState() {
        return behaviorState == TtBehaviorState.Adversary;
    }

    public boolean getHasHypocriteState() {
        return behaviorState == TtBehaviorState.Hypocrite;
    }

    public boolean getHasMischief() {
        return behaviorState == TtBehaviorState.Mischief;
    }

    public TtBehaviorState getCurrentBehaviorState() {
        return currentBehaviorState;
    }

    //============================//============================//============================
    public TtBehaviorState updateBehaviorState() {
        if (behaviorState == TtBehaviorState.Hypocrite) {
            int rand = Globals.RANDOM.nextInt(100);

            if (rand <= hypocriteBehavior.getAdversaryPercent()) {
                currentBehaviorState = TtBehaviorState.Adversary;
            } else if (rand <= hypocriteBehavior.getMischiefPercent() + hypocriteBehavior.getAdversaryPercent()) {
                currentBehaviorState = TtBehaviorState.Mischief;
            } else {
                currentBehaviorState = TtBehaviorState.Honest;
            }

            return currentBehaviorState;
        }

        currentBehaviorState = behaviorState;
        return currentBehaviorState;
    }

    //============================//============================//============================

}
