package core.utils;

import core.enums.TtOutLogMethodSection;
import core.enums.TtOutLogStatus;
import WSM.society.environment.StateX;
import WSM.society.agent.Agent;

public class OutLog____ {

    public static void pl(TtOutLogMethodSection methodSection, TtOutLogStatus status, String message, Agent agent, StateX stateX, StateX goalState) {

        if (status != TtOutLogStatus.ERROR) {
            return;
        }

        System.out.println("[" + methodSection.getCode() + "] (" + status + ") ---------|> " + message + " <|---------    "

                + (agent != null ? "| agent: " + agent.getId() + " " : " ")
                + (stateX != null ? "| state: " + stateX.getId() + " " : " ")
                + (goalState != null ? "| goalState: " + goalState.getId() + " " : " ")
        );
    }


    public static void pl(TtOutLogMethodSection methodSection, TtOutLogStatus status, String message) {
        pl(methodSection, status, message, null, null, null);

    }

    public static void pl(String message) {
        pl(null, TtOutLogStatus.SUCCESS, message, null, null, null);

    }
}
