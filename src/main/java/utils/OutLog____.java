package utils;

import _type.TtOutLogMethodSection;
import _type.TtOutLogStatus;
import stateLayer.StateX;
import systemLayer.Agent;

public class OutLog____ {

    public static void pl(TtOutLogMethodSection methodSection, TtOutLogStatus status, String message, Agent agent, StateX stateX, StateX goalState) {
/*
        System.out.println("[" + methodSection.getCode() + "] (" + status + ") ---------|> " + message + " <|---------    "

                + (agent != null ? "| agent: " + agent.getId() + " " : " ")
                + (stateX != null ? "| state: " + stateX.getId() + " " : " ")
                + (goalState != null ? "| goalState: " + goalState.getId() + " " : " ")
        );*/
    }


    public static void pl(TtOutLogMethodSection methodSection, TtOutLogStatus status, String message) {
        pl(methodSection, status, message, null, null, null);

    }
}
