package utils;

import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import environmentLayer.StateX;
import systemLayer.Agent;

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
}
