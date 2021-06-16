package utils.statistics;

import stateLayer.Environment;
import utils.Config;
import utils.Globals;
import utils.ParsCalendar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StatsEnvGenerator {

    File file;
    FileWriter writer;

    public void init(String statPath, String name) {
        file = new File(statPath);

        if (!file.exists()) {
            boolean mkdir = file.mkdir();
            if (mkdir) {
                System.out.println("Statistics directory created: " + statPath);
            }
        }

        statPath += "/sim-" + (Globals.SIMULATION_TIMER < 10 ? "0" + Globals.SIMULATION_TIMER : Globals.SIMULATION_TIMER);

        file = new File(statPath);

        if (!file.exists()) {
            boolean mkdir = file.mkdir();
            if (mkdir) {
                System.out.println("Statistics directory created: " + statPath);
            }
        }

        file = new File(statPath + "/" + name);
        try {
            writer = new FileWriter(file);
        } catch (IOException ignored) {
        }
    }

    public void addComment(Environment environment) {
        try {
            writer.write("# Simulation Report,Env.Code: " + environment.getCode() + ", " + ParsCalendar.getInstance().getShortDateTime() + ", Des: " + environment.getDescription()
                    + "\n# Method: " + Config.TRUST_METHODOLOGY
                    + ", StateCount: " + environment.getStateCount()
                    + ", PitfallCount: " + environment.getPitfallCount()
                    + ", TransitionCount: " + environment.getTransitionCount()
                    + ", Agents: " + environment.getAgentsCount()
                    + ", Honest: " + environment.getHonestCount()
                    + ", Adversary: " + environment.getAdversaryCount()
                    + ", Int.Adversary: " + environment.getIntelligentAdversaryCount()
                    + ", Mischief: " + environment.getMischiefCount()
                    + "\n");
            writer.flush();

        } catch (IOException ignored) {
        }
    }

    public void addHeader() {
        try {
            writer.write(getCsvHeader());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addStat(WorldStatistics stat) {
        try {
            writer.write("\n" + getCsvRec(stat));
            // writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCsvHeader() {
        return
                "WorldTime," +
                        "AllAgentsInTarget," +
                        "AllAgentsInPitfall," +
                        "IttAgentsInTarget," +
                        "IttAgentsInPitfall," +
                        "IttRandomTravelToNeighbors," +
                        "IttUpdatedNextStep," +
                        "IttSuccessTravelToNeighbor," +
                        "IttFailedTravelToNeighbor," +
                        "AgentsWithNoTargetState," +
                        "StatesWithNoTarget," +
                        "FullStateCount," +
                        "TotalTravelToNeighbor"

                ;
    }

    private String getCsvRec(WorldStatistics stat) {
        return
                stat.getWorldTime() + "," +
                        stat.getAllAgentsInTarget() + "," +
                        stat.getAllAgentsInPitfall() + "," +
                        stat.getIttAgentsInTarget() + "," +
                        stat.getIttAgentsInPitfall() + "," +
                        stat.getIttRandomTravelToNeighbors() + "," +
                        stat.getIttUpdatedNextStep() + "," +
                        stat.getIttSuccessTravelToNeighbor() + "," +
                        stat.getIttFailedTravelToNeighbor() + "," +
                        stat.getAgentsWithNoTargetState() + "," +
                        stat.getStatesWithNoTarget() + "," +
                        stat.getFullStateCount() + "," +
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
