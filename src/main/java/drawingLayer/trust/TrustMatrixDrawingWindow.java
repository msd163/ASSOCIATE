package drawingLayer.trust;

import _type.TtDiagramThemeMode;
import drawingLayer.DrawingWindow;
import societyLayer.agentSubLayer.Agent;
import societyLayer.agentSubLayer.World;
import trustLayer.TrustMatrix;
import utils.Config;
import utils.Globals;
import utils.Point;

import java.awt.*;
import java.util.List;

public class TrustMatrixDrawingWindow extends DrawingWindow {

    private TrustMatrix matrix;
    Float[][] trustMatrix;
    private List<Agent> sAgents;

    private int matCount;

    @Override
    public void resetParams() {
        pnOffset = new utils.Point(0, 0);
        pnOffsetOld = new utils.Point(0, 0);
        pnStartPoint = new utils.Point(0, 0);
        pnOffsetOld.x = pnOffset.x;
        pnOffsetOld.y = pnOffset.y;
        scaleOffset = new Point(0, 0);
        _hs = 2;
        _vs = 2;
        scale = 7f;
    }

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
        _hs = _vs = 1;
    }

    Agent agentInCol;
    Agent agentInRow;
    int trusteeData[][];

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle + " :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }
        if (isShowSimInfo) {
            g.drawString("Certified Count: " + world.getEnvironment().getCertifiedCount(), 100, 70);
        }

        if (showChartsFlag[0]) {

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

            //-- drawing X and Y axis as background lines
            g.setColor(Config.THEME_MODE == TtDiagramThemeMode.Dark ? Globals.Color$.darkGray1 : Globals.Color$.lightGray2);
            // draw Y axis
            g.fillRect(pnX, 1, 5, _vs * (matCount + 4) * 5 + 1);
            // draw X axis
            g.fillRect(1, pnY, _vs * (matCount + 4) * 5 + 1, 5);


            //-- drawing grid line for percentage of capPower of states
            g.setColor(Config.THEME_MODE == TtDiagramThemeMode.Dark ? Globals.Color$.darkGray1 : Globals.Color$.lightGray);
            g.drawLine(pnX - 110, 1, pnX - 110, _vs * matCount * 5 + 1);
            g.drawLine(pnX - 85, 1, pnX - 85, _vs * matCount * 5 + 1);
            g.drawLine(pnX - 60, 1, pnX - 60, _vs * matCount * 5 + 1);
            g.drawLine(pnX - 35, 1, pnX - 35, _vs * matCount * 5 + 1);

            //============================//============================


            for (int rowIndex = 0, row = 1; rowIndex < matCount; rowIndex++, row++) {
                agentInRow = sAgents.get(rowIndex);
                if (agentInRow == null || agentInRow.getBehavior() == null || agentInRow.getBehavior().getBehaviorState() == null) {
                    continue;
                }
                g.setColor(Globals.Color$.getNormal(agentInRow.getBehavior().getBehaviorState()));

                //============================ axisX
                g.fillOval(row * 5 * _hs, pnY, 5, 5);

                //============================ axisY
                //-- drawing axis Y
                g.fillOval(pnX, row * 5 * _vs, 5, 5);

                //-- drawing capPower in side of axis Y
                g.setColor(
                        Config.THEME_MODE == TtDiagramThemeMode.Dark ?
                                Globals.Color$.getLight(agentInRow.getBehavior().getCurrentBehaviorState()) :
                                Globals.Color$.getNormal(agentInRow.getBehavior().getCurrentBehaviorState())
                );
                g.fillRect(pnX - agentInRow.getCapacity().getCapPower() - 10, _vs * row * 5 + 1, agentInRow.getCapacity().getCapPower(), 3);


                //-- drawing sign of agents that are candidate for certification
                if (world.getSimulationConfig().getCert().isIsUseCertification()) {
                    if (agentInRow.getTrust().isHasCandidateForCertification()) {
                        //-- If Use DaGra method
                        if (world.getSimulationConfig().getCert().isIsUseDaGra()) {
                            //-- Drawing curve or circle according to CertCertification status
                            if (agentInRow.getDaGra() != null && agentInRow.getDaGra().getMy() != null) {
                                switch (agentInRow.getDaGra().getMy().getStatus()) {
                                    case NoContract:
                                        g.setColor(Globals.Color$.lightYellow);
                                        g.fillArc(pnX - 7, _vs * row * 5 + 1, 5, 5, 0, 10);
                                        g.fillArc(_hs * row * 5, pnY - 7, 5, 5, 0, 10);
                                        break;

                                    case Expired:
                                        g.setColor(Globals.Color$.lightRed1);
                                        g.fillArc(pnX - 7, _vs * row * 5 + 1, 5, 5, 0, 35);
                                        g.fillArc(_hs * row * 5, pnY - 7, 5, 5, 0, 35);
                                        break;

                                    case Request_New:
                                        g.setColor(Globals.Color$.yellow);
                                        g.fillArc(pnX - 7, _vs * row * 5 + 1, 5, 5, 0, 55);
                                        g.fillArc(_hs * row * 5, pnY - 7, 5, 5, 0, 55);
                                        break;

                                    case Request_Signing:
                                        g.setColor(Globals.Color$.yellow);
                                        g.fillArc(pnX - 7, _vs * row * 5 + 1, 5, 5, 0, 90);
                                        g.fillArc(_hs * row * 5, pnY - 7, 5, 5, 0, 90);
                                        break;

                                    case Request_Verifying:
                                        g.setColor(Globals.Color$.yellow);
                                        g.fillArc(pnX - 7, _vs * row * 5 + 1, 5, 5, 0, 180);
                                        g.fillArc(_hs * row * 5, pnY - 7, 5, 5, 0, 180);
                                        break;

                                    case Accept_New:
                                        g.setColor(Globals.Color$.lightGreen);
                                        g.fillArc(pnX - 7, _vs * row * 5 + 1, 5, 5, 0, 210);
                                        g.fillArc(_hs * row * 5, pnY - 7, 5, 5, 0, 210);
                                        break;

                                    case Accept_Signing:
                                        g.setColor(agentInRow.getDaGra().getMy().getFinalTrustValue() < 0 ? Globals.Color$.red : Globals.Color$.green);
                                        g.fillArc(pnX - 7, _vs * row * 5 + 1, 5, 5, 0, 260);
                                        g.fillArc(_hs * row * 5, pnY - 7, 5, 5, 0, 260);
                                        break;

                                    case Accept_Verifying:
                                        g.setColor(agentInRow.getDaGra().getMy().getFinalTrustValue() < 0 ? Globals.Color$.red : Globals.Color$.green);
                                        g.fillArc(pnX - 7, _vs * row * 5 + 1, 5, 5, 0, 320);
                                        g.fillArc(_hs * row * 5, pnY - 7, 5, 5, 0, 320);
                                        break;
                                    case Accept_Accept:
                                        g.setColor(agentInRow.getDaGra().getMy().getFinalTrustValue() < 0 ? Globals.Color$.darkRed : Globals.Color$.darkGreen1);
                                        g.fillArc(pnX - 7, _vs * row * 5 + 1, 5, 5, 0, 359);
                                        g.fillArc(_hs * row * 5, pnY - 7, 5, 5, 0, 359);
                                        break;

                                }
                            } else {
                                g.setColor(Color.LIGHT_GRAY);
                                g.fillArc(pnX - 7, _vs * row * 5 + 1, 5, 5, 0, 5);
                                g.fillArc(_hs * row * 5, pnY - 7, 5, 5, 0, 5);

                            }
                        }

                        // If dont use DaGra method
                        else {

                            g.setColor(Color.YELLOW);
                            g.fillOval(pnX - 7, _vs * row * 5 + 1, 5, 5);
                            g.fillOval(_hs * row * 5, pnY - 7, 5, 5);

                        }
                    }
                }

                //-- drawing sign of agents with internet
                if (agentInRow.getCapacity().isHasInternet()) {
                    g.setColor(Config.THEME_MODE == TtDiagramThemeMode.Dark ? Globals.Color$.orange : Globals.Color$.darkRed);
                    g.fillOval(pnX - 6, _vs * row * 5 + 2, 3, 3);
                    g.fillOval(_hs * row * 5 + 1, pnY - 6, 3, 3);

                }

                //-- updating trust data array that are shown in below of axis X
                for (int col = 1, colIndex = 0; colIndex < matCount; colIndex++, col++) {
                    agentInCol = sAgents.get(colIndex);
                    float trVal = this.trustMatrix[rowIndex][colIndex];
                    if (trVal > 0) {
                        trusteeData[colIndex][0]++;
                        if (agentInCol.getBehavior().getHasAdversaryState()) {
                            trusteeData[colIndex][2]++;
                            g.setColor(Globals.Color$.$trustAdversary);
                            g.drawOval(_hs * col * 5 - 2, _vs * row * 5 - 2, 9, 9);
                        } else if (agentInCol.getBehavior().getHasMischief()) {
                            trusteeData[colIndex][2]++;
                            g.setColor(Globals.Color$.$trustMischief);
                            g.drawOval(_hs * col * 5 - 2, _vs * row * 5 - 2, 9, 9);
                        } else if (agentInCol.getBehavior().getHasHypocriteState()) {
                            trusteeData[colIndex][2]++;
                            g.setColor(Color.MAGENTA);
                            g.drawOval(_hs * col * 5 - 2, _vs * row * 5 - 2, 9, 9);
                        }
                        g.setColor(Globals.Color$.$trustHonest);

                    } else if (trVal < 0) {
                        trusteeData[colIndex][0]++;
                        if (agentInCol.getBehavior().getHasHonestState()) {
                            trusteeData[colIndex][1]++;
                            g.setColor(Globals.Color$.$trustHonest);
                            g.drawOval(_hs * col * 5 - 2, _vs * row * 5 - 2, 9, 9);
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

                    g.fillRect(_hs * col * 5 + ((5 - bigness) / 2), _vs * row * 5 + ((5 - bigness) / 2), bigness, bigness);
                }
            }

            //-- Drawing trust count info in bottom of axis X
            for (int cnt = 1, idx = 0, trusteeDataLength = trusteeData.length; idx < trusteeDataLength; idx++, cnt++) {
                Agent agent = sAgents.get(idx);
                if (agent == null || agent.getBehavior() == null || agent.getBehavior().getBehaviorState() == null) {
                    continue;
                }
                int[] trustDataArray = trusteeData[idx];
                g.setColor(agent.getBehavior().getHasHonestState() ?
                        Globals.Color$.darkGreen : Globals.Color$.orange
                );
                // total trustee data
                g.fillRect(_hs * cnt * 5, pnY - trustDataArray[0] - 10, 5, trustDataArray[0]);
                // FP data
                g.setColor(Globals.Color$.darkGray2);
                g.fillRect(_hs * cnt * 5 + 1, pnY - trustDataArray[1] - 10, 3, trustDataArray[1]);
                // FN data
                g.setColor(Globals.Color$.getDark(agent.getBehavior().getBehaviorState()));
                g.fillRect(_hs * cnt * 5 + 1, pnY - trustDataArray[1] - trustDataArray[2] - 10, 3, trustDataArray[2]);
            }

            //============================//============================ Drawing fixed texts of coordination
            g.setColor(Config.THEME_MODE == TtDiagramThemeMode.Dark ? Globals.Color$.yellow : Globals.Color$.darkGray1);

            if (isShowSimInfo) {
                g.scale(1, -1);
                g.drawString("0", -35, -10);
                g.drawString("0", 10, 35);

                g.drawString("Trustee", 100, -pnY + 50);
                g.drawString("Trustier", pnX - 100, -100);
                g.scale(1, -1);
            }

        }
    }
}
