package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StatGenerator {

    File file;
    FileWriter writer;

    public void init(String statPath) {
        file = new File(statPath);

        try {
            writer = new FileWriter(file);
            writer.write("# Simulation report. \n# Date: " + ParsCalendar.getInstance().getShortDateTime());
            writer.flush();

        } catch (IOException ignored) {
        }
    }

    public void addHeader(WorldStatistics stat) {
        try {
            writer.write("\n" + getCsvHeader(stat));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addStat(WorldStatistics stat) {
        try {
            writer.write("\n" + getCsvRec(stat));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCsvHeader(WorldStatistics stat) {
        return
                "WorldTime," +
                        "FullStateCount," +
                        "StatesWithNoTarget," +
                        "AgentsWithNoTargetState," +
                        "AllInTargetAgents," +
                        "InTargetAgentsInThisTime," +
                        "UpdatedNextStatesOfAgents," +
                        "SuccessTravelToGoToNeighbor," +
                        "FailedTravelToGoToNeighbor," +
                        "RandomTravelToNeighbors," +
                        "AgentsInPitfall," +
                        "TotalTravelToNeighbor"

                ;
    }

    private String getCsvRec(WorldStatistics stat) {
        return
                stat.getWorldTime() + "," +
                        stat.getFullStateCount() + "," +
                        stat.getStatesWithNoTarget() + "," +
                        stat.getAgentsWithNoTargetState() + "," +
                        stat.getAllInTargetAgents() + "," +
                        stat.getInTargetAgentsInThisTime() + "," +
                        stat.getUpdatedNextStepsOfAgents() + "," +
                        stat.getSuccessTravelToGoToNeighbor() + "," +
                        stat.getFailedTravelToGoToNeighbor() + "," +
                        stat.getRandomTravelToNeighbors() + "," +
                        stat.getAgentsInPitfall() + "," +
                        stat.getTotalTravelToNeighbor()

                ;
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
