package drawingLayer;

import systemLayer.Agent;
import trustLayer.TrustMatrix;

import java.awt.*;

public class TrustMatrixDrawingWindow extends DrawingWindow {

    private TrustMatrix matrix;
    Float[][] trustMatrix;
    private Agent[] agents;

    private int matCount;


    public TrustMatrixDrawingWindow(TrustMatrix matrixGenerator) {
        super();
        this.matrix = matrixGenerator;
        matCount = this.matrix.getAgentCount();
        trustMatrix = this.matrix.getTrustMatrix();
        agents = matrixGenerator.getAgents();
        axisY = axisX = matCount * 5;
    }

    @Override
    public void paint(Graphics gr) {

        g = (Graphics2D) gr;
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, getWidth(), getHeight());
        pauseNotice(g);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        g.drawString(mousePosition.toString(), 10, 10);

        //============================//============================//============================
        //============================ Draw mouse plus
        Point mousePoint = getMousePosition();
        if (mousePoint != null) {
            g.setColor(Color.WHITE);
            //-- (TOP-DOWN) Drawing vertical line for mouse pointer
            g.drawLine(mousePoint.x, 0, mousePoint.x, getHeight());
            //-- (LEFT-RIGHT) Drawing horizontal line for mouse pointer
            g.drawLine(0, mousePoint.y, getWidth(), mousePoint.y);
        }

        int pnScl_x = pnOffset.x + scaleOffset.x;
        int pnScl_y = pnOffset.y + scaleOffset.y;
        g.translate(pnScl_x, pnScl_y);
        g.scale(scale, -scale);
        g.translate(100, -getHeight() / scale + 100);

        int pnY = (pnScl_y > 0 ? (int) (pnScl_y / scale) + 10 : 0) - 5;
        int pnX = (pnScl_x < 0 ? -(int) (pnScl_x / scale) + 10 : 0) - 5;

        //============================//============================

        for (int i = 0; i < matCount; i++) {
            Agent agent = agents[i];
            switch (agent.getBehavior().getBehaviorState()) {
                case Honest:
                    g.setColor(Color.GREEN);
                    break;
                case Adversary:
                    g.setColor(Color.RED);
                    break;
                case Mischief:
                    g.setColor(Color.WHITE);
                    break;
                case IntelligentAdversary:
                    g.setColor(Color.MAGENTA);
                    break;
                default:
                    g.setColor(Color.YELLOW);
            }

            g.fillRect(i * 5, pnY, 5, 5);

            g.fillRect(pnX, i * 5, 5, 5);

            for (int j = 0; j < matCount; j++) {
                Agent agentJ = agents[j];
                float trVal = this.trustMatrix[i][j];
                if (trVal > 0) {
                    if (agentJ.getBehavior().getHasAdversaryState()) {
                        g.setColor(Color.RED);
                        g.drawOval(j * 5 - 2, i * 5 - 2, 9, 9);
                    } else if (agentJ.getBehavior().getHasMischief()) {
                        g.setColor(Color.WHITE);
                        g.drawOval(j * 5 - 2, i * 5 - 2, 9, 9);
                    } else if (agentJ.getBehavior().getHasIntelligentAdversaryState()) {
                        g.setColor(Color.MAGENTA);
                        g.drawOval(j * 5 - 2, i * 5 - 2, 9, 9);
                    }
                    g.setColor(Color.GREEN);

                } else if (trVal < 0) {
                    if (agentJ.getBehavior().getHasHonestState()) {
                        g.setColor(Color.GREEN);
                        g.drawOval(j * 5 - 2, i * 5 - 2, 9, 9);
                    }
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }

                trVal = Math.abs(trVal);

                int bigness =
                        trVal <= 0.5f ? 1
                                : trVal <= 1 ? 2
                                : trVal <= 1.5 ? 3
                                : trVal <= 2 ? 4
                                : 5;

                g.fillRect(j * 5 + ((5 - bigness) / 2), i * 5 + ((5 - bigness) / 2), bigness, bigness);

            }
        }

        g.setColor(Color.YELLOW);

        g.scale(1, -1);
        g.drawString("0", -35, -10);
        g.drawString("0", 10, 35);

        g.drawString("Trustee", 100, -pnY + 50);
        g.drawString("Trustier", pnX - 100, -100);
        g.scale(1, -1);

    }
}
