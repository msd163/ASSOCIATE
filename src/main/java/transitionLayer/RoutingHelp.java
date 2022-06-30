package transitionLayer;

import _type.TtIsValidatedInObservations;
import societyLayer.environmentSubLayer.StateX;
import societyLayer.agentSubLayer.Agent;

public class RoutingHelp {

    private Agent helperAgent;
    private int stepFromHelperToTarget;
    private int stepFromAgentToHelper;
    private StateX nextState;
    private float trustValue;
    private TtIsValidatedInObservations validation;

    public RoutingHelp() {
        validation = TtIsValidatedInObservations.Unknown;
    }

    //============================//============================

    public int getFinalStepFromAgentToTarget() {
        return stepFromAgentToHelper + stepFromHelperToTarget;
    }

    public Agent getHelperAgent() {
        return helperAgent;
    }

    public void setHelperAgent(Agent helperAgent) {
        this.helperAgent = helperAgent;
    }

    public int getStepFromHelperToTarget() {
        return stepFromHelperToTarget;
    }

    public void setStepFromHelperToTarget(int stepFromHelperToTarget) {
        this.stepFromHelperToTarget = stepFromHelperToTarget;
    }

    public StateX getNextState() {
        return nextState;
    }

    public void setNextState(StateX nextState) {
        this.nextState = nextState;
    }

    public int getStepFromAgentToHelper() {
        return stepFromAgentToHelper;
    }

    public void setStepFromAgentToHelper(int stepFromAgentToHelper) {
        this.stepFromAgentToHelper = stepFromAgentToHelper;
    }

    public float getTrustValue() {
        return trustValue;
    }

    public void setTrustValue(float trustValue) {
        this.trustValue = trustValue;
    }

    public void setValidation(TtIsValidatedInObservations validation) {
        this.validation = validation;
    }

    public TtIsValidatedInObservations getValidation() {
        return validation;
    }
}
