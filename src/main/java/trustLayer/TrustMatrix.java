package trustLayer;

import systemLayer.Agent;
import utils.WorldStatistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TrustMatrix {

    Float[][] trustMatrix;
    int agentCount = 0;
    Agent[] agents;

    File file;
    FileWriter writer;

    public void init(Agent[] agents) {
        this.agentCount = agents.length;
        this.agents = agents;
        trustMatrix = new Float[agentCount][agentCount];

        for (int i = 0; i < agentCount; i++) {
            for (int j = 0; j < agentCount; j++) {
                trustMatrix[i][j] = 0.0f;
            }
        }
    }

    public void update(WorldStatistics statistics) {
        for (int i = 0, agentsSize = agents.length; i < agentsSize; i++) {
            Agent agent = agents[i];
            TrustHistory[] histories = agent.getTrust().getHistories();
            for (int j = 0, size = agents.length; j < size; j++) {
                Agent trustee = agents[j];
                for (int x = 0; x < histories.length; x++) {
                    TrustHistory history = histories[x];
                    if (history != null) {
                        if (trustee.getId() == history.getAgent().getId()) {
                            trustMatrix[i][j] = history.getFinalTrustLevel();
                            if (history.getFinalTrustLevel() > 0 && !trustee.getBehavior().getIsHonest()) {
                                statistics.add_Itt_FalseNegativeTrust();
                            }
                            if (history.getFinalTrustLevel() < 0 && trustee.getBehavior().getIsHonest()) {
                                statistics.add_Itt_FalsePositiveTrust();
                            }
                        }
                    }
                }
            }
        }
    }

    public void write(String matrixFilePath) {
        file = new File(matrixFilePath);
        try {
            writer = new FileWriter(file);
        } catch (IOException ignored) {
        }
        StringBuilder hiss = new StringBuilder("R\\C");  // Row:0 and Col:0
        for (int i = 0; i < agentCount; i++) {
            hiss.append(",").append(i);
        }
        hiss.append("\n");
        for (int i = 0; i < agentCount; i++) {
            hiss.append(i);
            for (int j = 0; j < agentCount; j++) {
                hiss.append(",").append(trustMatrix[i][j]);
            }
            hiss.append("\n");
        }
        try {
            writer.write(hiss.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Float[][] getTrustMatrix() {
        return trustMatrix;
    }

    public int getAgentCount() {
        return agentCount;
    }


    public Agent[] getAgents() {
        return agents;
    }
}
