package drawingLayer.trust;

import drawingLayer.DrawingWindow;
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
        headerTitle = "Trust Matrix";
        setName("tut_mtx");
    }

    Agent agentInCol;
    Agent agentInRow;
    int trusteeData[][];

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle + " :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }

        g.drawString("Certified Count: " + world.getEnvironment().getCertifiedCount(), 100, 70);

        int pnScl_x = pnOffset.x + scaleOffset.x;
        int pnScl_y = pnOffset.y + scaleOffset.y;
        g.translate(pnScl_x, pnScl_y);
        g.scale(scale, -scale);
        g.translate(110, (-getHeight() + translateInTitle_Y + translateInNormCoord_Y) / scale);

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

            //-- drawing grid line for percentage of capPower of states
            g.setColor(Color.darkGray);
            g.drawLine(pnX - 110, 1, pnX - 110, matCount * 5 + 1);
            g.drawLine(pnX - 85, 1, pnX - 85, matCount * 5 + 1);
            g.drawLine(pnX - 60, 1, pnX - 60, matCount * 5 + 1);
            g.drawLine(pnX - 35, 1, pnX - 35, matCount * 5 + 1);

            //-- drawing sign of agents that are candidate for certification
            if (agentInRow.getTrust().isHasCandidateForCertification()) {
                //-- If Use DaGra method
                if (world.getSimulationConfig().getCert().isIsUseDaGra()) {
                    //-- Drawing curve or circle according to CertCertification status
                    if (agentInRow.getDaGra() != null && agentInRow.getDaGra().getMy() != null) {
                        switch (agentInRow.getDaGra().getMy().getStatus()) {
                            case NoContract:
                                g.setColor(Globals.Color$.lightYellow);
                                g.fillArc(pnX - 7, row * 5 + 1, 5, 5, 0, 10);
                                g.fillArc(row * 5, pnY - 7, 5, 5, 0, 10);
                                break;

                            case Expired:
                                g.setColor(Globals.Color$.lightRed);
                                g.fillArc(pnX - 7, row * 5 + 1, 5, 5, 0, 35);
                                g.fillArc(row * 5, pnY - 7, 5, 5, 0, 35);
                                break;

                            case Request_New:
                                g.setColor(Globals.Color$.yellow);
                                g.fillArc(pnX - 7, row * 5 + 1, 5, 5, 0, 55);
                                g.fillArc(row * 5, pnY - 7, 5, 5, 0, 55);
                                break;

                            case Request_Signing:
                                g.setColor(Globals.Color$.yellow);
                                g.fillArc(pnX - 7, row * 5 + 1, 5, 5, 0, 90);
                                g.fillArc(row * 5, pnY - 7, 5, 5, 0, 90);
                                break;

                            case Request_Verifying:
                                g.setColor(Globals.Color$.yellow);
                                g.fillArc(pnX - 7, row * 5 + 1, 5, 5, 0, 180);
                                g.fillArc(row * 5, pnY - 7, 5, 5, 0, 180);
                                break;

                            case Accept_New:
                                g.setColor(Globals.Color$.lightGreen);
                                g.fillArc(pnX - 7, row * 5 + 1, 5, 5, 0, 210);
                                g.fillArc(row * 5, pnY - 7, 5, 5, 0, 210);
                                break;

                            case Accept_Signing:
                                g.setColor(agentInRow.getDaGra().getMy().getFinalTrustValue() < 0 ? Globals.Color$.red : Globals.Color$.green);
                                g.fillArc(pnX - 7, row * 5 + 1, 5, 5, 0, 260);
                                g.fillArc(row * 5, pnY - 7, 5, 5, 0, 260);
                                break;

                            case Accept_Verifying:
                                g.setColor(agentInRow.getDaGra().getMy().getFinalTrustValue() < 0 ? Globals.Color$.red : Globals.Color$.green);
                                g.fillArc(pnX - 7, row * 5 + 1, 5, 5, 0, 320);
                                g.fillArc(row * 5, pnY - 7, 5, 5, 0, 320);
                                break;
                            case Accept_Accept:
                                g.setColor(agentInRow.getDaGra().getMy().getFinalTrustValue() < 0 ? Globals.Color$.darkRed : Globals.Color$.darkGreen);
                                g.fillArc(pnX - 7, row * 5 + 1, 5, 5, 0, 359);
                                g.fillArc(row * 5, pnY - 7, 5, 5, 0, 359);
                                break;

                        }
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillArc(pnX - 7, row * 5 + 1, 5, 5, 0, 5);
                        g.fillArc(row * 5, pnY - 7, 5, 5, 0, 5);

                    }
                }

                // If dont use DaGra method
                else {

                    g.setColor(Color.YELLOW);
                    g.fillOval(pnX - 7, row * 5 + 1, 5, 5);
                    g.fillOval(row * 5, pnY - 7, 5, 5);

                }
            }

            //-- drawing sign of agents with internet
            if (agentInRow.getCapacity().isHasInternet()) {
                g.setColor(Globals.Color$.orange);
                g.fillOval(pnX - 6, row * 5 + 2, 3, 3);
                g.fillOval(row * 5 + 1, pnY - 6, 3, 3);

            }

            //-- updating trust data array that are shown in below of axis X
            for (int col = 0; col < matCount; col++) {
                agentInCol = sAgents.get(col);
                float trVal = this.trustMatrix[row][col];
                if (trVal > 0) {
                    trusteeData[col][0]++;
                    if (agentInCol.getBehavior().getHasAdversaryState()) {
                        trusteeData[col][2]++;
                        g.setColor(Globals.Color$.$trustAdversary);
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
                    g.setColor(Globals.Color$.$trustHonest);

                } else if (trVal < 0) {
                    trusteeData[col][0]++;
                    if (agentInCol.getBehavior().getHasHonestState()) {
                        trusteeData[col][1]++;
                        g.setColor(Globals.Color$.$trustHonest);
                        g.drawOval(col * 5 - 2, row * 5 - 2, 9, 9);
                    }
                    g.setColor(Globals.Color$.$trustAdversary);
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

        //-- Drawing trust count info in bottom of axis X
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
