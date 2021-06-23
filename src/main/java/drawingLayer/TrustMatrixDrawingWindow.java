package drawingLayer;

import systemLayer.Agent;
import systemLayer.World;
import trustLayer.TrustMatrix;
import utils.Globals;

import java.awt.*;
import java.util.List;

public class TrustMatrixDrawingWindow extends DrawingWindow {

    private TrustMatrix matrix;
    Float[][] trustMatrix;
    private List<Agent> sAgents;

    private int matCount;


    public TrustMatrixDrawingWindow(TrustMatrix matrixGenerator, World world) {
        super();
        this.world = world;
        this.matrix = matrixGenerator;
        matCount = this.matrix.getAgentCount();
        trustMatrix = this.matrix.getTrustMatrix();
        sAgents = matrixGenerator.getsAgents();
        axisY = axisX = matCount * 5;
        trusteeData = new int[matCount][3];     // 0: totalTrust  |  1: FP | 2: FN
    }

    Agent agentInCol;
    Agent agentInRow;
    int trusteeData[][];

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, "Matrix :: "+ world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }


        int pnScl_x = pnOffset.x + scaleOffset.x;
        int pnScl_y = pnOffset.y + scaleOffset.y;
        g.translate(pnScl_x, pnScl_y);
        g.scale(scale, -scale);
        g.translate(110, (-getHeight() +translateInTitle_Y+ translateInNormCoord_Y)/ scale );

        int pnY = (pnScl_y > 0 ? (int) (pnScl_y / scale) + 10 : 0) - 5;
        int pnX = (pnScl_x < 0 ? -(int) (pnScl_x / scale) + 10 : 0) - 5;

        for (int i = 0, trusteeDataLength = trusteeData.length; i < trusteeDataLength; i++) {
            trusteeData[i][0] = trusteeData[i][1] = trusteeData[i][2] = 0;
        }
        //============================//============================

        for (int row = 0; row < matCount; row++) {
            agentInRow = sAgents.get(row);

            g.setColor(Globals.Color$.getNormal(agentInRow.getBehavior().getBehaviorState()));

            //============================ axisX
            g.fillRect(row * 5, pnY, 5, 5);

            //============================ axisY
            //-- drawing axis Y
            g.fillRect(pnX, row * 5, 5, 5);
            //-- drawing capPower in side of axis Y
            g.setColor(Globals.Color$.getLight(agentInRow.getBehavior().getCurrentBehaviorState()));
            g.fillRect(pnX - agentInRow.getCapacity().getCapPower() - 10, row * 5 + 1, agentInRow.getCapacity().getCapPower(), 3);

            //-- updating trust data array that are shown in below of axis X
            for (int col = 0; col < matCount; col++) {
                agentInCol = sAgents.get(col);
                float trVal = this.trustMatrix[row][col];
                if (trVal > 0) {
                    trusteeData[col][0]++;
                    if (agentInCol.getBehavior().getHasAdversaryState()) {
                        trusteeData[col][2]++;
                        g.setColor(Color.RED);
                        g.drawOval(col * 5 - 2, row * 5 - 2, 9, 9);
                    } else if (agentInCol.getBehavior().getHasMischief()) {
                        trusteeData[col][2]++;
                        g.setColor(Color.WHITE);
                        g.drawOval(col * 5 - 2, row * 5 - 2, 9, 9);
                    } else if (agentInCol.getBehavior().getHasHypocriteState()) {
                        trusteeData[col][2]++;
                        g.setColor(Color.MAGENTA);
                        g.drawOval(col * 5 - 2, row * 5 - 2, 9, 9);
                    }
                    g.setColor(Color.GREEN);

                } else if (trVal < 0) {
                    trusteeData[col][0]++;
                    if (agentInCol.getBehavior().getHasHonestState()) {
                        trusteeData[col][1]++;
                        g.setColor(Color.GREEN);
                        g.drawOval(col * 5 - 2, row * 5 - 2, 9, 9);
                    }
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }

                //-- Calculation bigness of trust value
                trVal = Math.abs(trVal);

                int bigness =
                        trVal <= 0.01f ? 1
                                : trVal <= 0.1 ? 2
                                : trVal <= 0.3 ? 3
                                : trVal <= 0.6 ? 4
                                : 5;

                g.fillRect(col * 5 + ((5 - bigness) / 2), row * 5 + ((5 - bigness) / 2), bigness, bigness);

            }
        }

        for (int i = 0, trusteeDataLength = trusteeData.length; i < trusteeDataLength; i++) {
            int[] td = trusteeData[i];
            g.setColor(Globals.Color$.getLight(sAgents.get(i).getBehavior().getCurrentBehaviorState()));
            g.fillRect(i * 5, pnY - td[0] - 10, 5, td[0]);
            g.setColor(Color.CYAN);
            g.fillRect(i * 5, pnY - td[1] - 10, 5, td[1]);
            g.setColor(Color.RED);
            g.fillRect(i * 5, pnY - td[1] - td[2] - 10, 5, td[2]);
        }

        //============================//============================ Drawing fixed texts of coordination
        g.setColor(Color.YELLOW);

        g.scale(1, -1);
        g.drawString("0", -35, -10);
        g.drawString("0", 10, 35);

        g.drawString("Trustee", 100, -pnY + 50);
        g.drawString("Trustier", pnX - 100, -100);
        g.scale(1, -1);

    }
}
