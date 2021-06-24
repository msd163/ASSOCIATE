package trustLayer;

import systemLayer.Agent;
import trustLayer.data.TrustAbstract;
import utils.Globals;
import utils.statistics.WorldStatistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TrustMatrix {

    Float[][] trustMatrix;
    int agentCount = 0;
    List<Agent> sAgents;

    File file;
    FileWriter writer;

    public void init(List<Agent> agents) {
        this.agentCount = agents.size();
        this.sAgents = agents;
        this.trustMatrix = new Float[agentCount][agentCount];

        for (int i = 0; i < agentCount; i++) {
            for (int j = 0; j < agentCount; j++) {
                trustMatrix[i][j] = 0.0f;
            }
        }
    }

    public void update(WorldStatistics statistics) {
        Agent responder;
        for (int i = 0; i < agentCount; i++) {
            Agent agent = sAgents.get(i);
            TrustAbstract[] trustAbstracts = agent.getTrust().getTrustAbstracts();
            for (int k = 0, trustValuesLength = trustAbstracts.length; k < trustValuesLength; k++) {
                responder = sAgents.get(k);
                float tValue = trustAbstracts[k].getTrustValue();
                int updateTime = trustAbstracts[k].getUpdateTime();
                if (/*tValue != 0 && */updateTime == Globals.WORLD_TIMER) {
                    trustMatrix[i][k] = tValue;
                    if (tValue > 0 && !responder.getBehavior().getHasHonestState()) {
                        statistics.add_Itt_FalseNegativeTrust();
                        // statistics.getAgentStatistics()[j].addAsTrustee_FN();
                    }
                    if (tValue < 0 && responder.getBehavior().getHasHonestState()) {
                        statistics.add_Itt_FalsePositiveTrust();
                        //  statistics.getAgentStatistics()[j].addAsTrustee_FP();
                    }
                    if (tValue > 0 && responder.getBehavior().getHasHonestState()) {
                        statistics.add_Itt_TrueNegativeTrust();
                        //  statistics.getAgentStatistics()[j].addAsTrustee_TN();
                    }
                    if (tValue < 0 && !responder.getBehavior().getHasHonestState()) {
                        statistics.add_Itt_TruePositiveTrust();
                        //  statistics.getAgentStatistics()[j].addAsTrustee_TP();
                    }
                    break;

                }
            }
        }
        statistics.calcTrustParams();
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


    public List<Agent> getsAgents() {
        return sAgents;
    }
}
