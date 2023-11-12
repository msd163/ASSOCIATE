package drawingLayer.dag;

import _type.TtDaGraContractStatus;
import drawingLayer.DrawingWindow;
import societyLayer.agentSubLayer.Agent;
import societyLayer.agentSubLayer.World;
import trustLayer.consensus.CertContract;
import trustLayer.consensus.CertSign;
import trustLayer.consensus.CertVerify;
import utils.Globals;
import utils.Point;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DagGraphDrawingWindow extends DrawingWindow {

    //============================//============================  panning params

    public DagGraphDrawingWindow(World world) {
        super();
        this.world = world;
        this.prevPoints = new Point[4];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Dag Graph";
        scale = 0.5f;
        setName("dag_stt");
    }


    Map<Integer, Integer> agentMap = new HashMap<>();
    int lastAgentYID = 1;

    private void drawVerifyConnection(CertContract src, CertContract dst, Color activeColor, Color inactiveColor) {

        g.setStroke(lineThicknessObj_x);
        g.setColor(dst.getStatus() == TtDaGraContractStatus.Expired ? inactiveColor : activeColor);
        g.drawLine(src.getDrawX(), src.getDrawY(), dst.getDrawX(), dst.getDrawY());

//        drawDottedLine(dst.getDrawX(), dst.getDrawY(),src.getDrawX(), src.getDrawY(),1);
    }

    private void drawSignConnection(CertContract src, CertContract dst, Color activeColor, Color inactiveColor) {

        g.setStroke(new BasicStroke(lineThickness * 2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        g.setColor(dst.getStatus() == TtDaGraContractStatus.Expired ? inactiveColor : activeColor);
        g.drawLine(src.getDrawX(), src.getDrawY(), dst.getDrawX(), dst.getDrawY());

    }

    private void calculateContractPosition(CertContract contract) {
        int agentIndex = contract.getRequester().getIndex();
        if (agentMap.containsKey(agentIndex)) {
            contract.setDrawY(_vs * 3 * agentMap.get(agentIndex));
        } else {
            contract.setDrawY(_vs * 3 * lastAgentYID);
            agentMap.put(agentIndex, lastAgentYID);
            lastAgentYID++;
        }

        contract.setDrawX(contract.getRequestTime() * _hs);

    }

    private void drawContract(CertContract contract) {
        int size = 24 + symbolIncreaseSizeOnCurves;
        int drawX = contract.getDrawX() - 12;
        int drawY = contract.getDrawY() - 12;

        switch (contract.getStatus()) {
            case NoContract:
                g.setColor(Globals.Color$.$dagContract_nc);
                g.fillArc(drawX, drawY, size, size, 0, 10);
                break;

            case Expired:
                g.setColor(Globals.Color$.$dagContract_ex);
                g.fillArc(drawX, drawY, size, size, 0, 359);
                break;

            case Request_New:
                g.setColor(Globals.Color$.$dagContract_rn);
                g.fillArc(drawX, drawY, size, size, 0, 55);
                break;

            case Request_Signing:
                g.setColor(Globals.Color$.$dagContract_rs);
                g.fillArc(drawX, drawY, size, size, 0, 90);
                break;

            case Request_Verifying:
                g.setColor(Globals.Color$.$dagContract_rv);
                g.fillArc(drawX, drawY, size, size, 0, 180);
                break;

            case Accept_New:
                g.setColor(Globals.Color$.$dagContract_an);
                g.fillArc(drawX, drawY, size, size, 0, 210);
                break;

            case Accept_Signing:
                g.setColor(Globals.Color$.$dagContract_as);
                g.setColor(contract.getFinalTrustValue() < 0 ? Globals.Color$.red : Globals.Color$.darkGreen);
                g.fillArc(drawX, drawY, size, size, 0, 260);
                break;

            case Accept_Verifying:
                g.setColor(Globals.Color$.$dagContract_av);
                g.setColor(contract.getFinalTrustValue() < 0 ? Globals.Color$.red : Globals.Color$.darkGreen);
                g.fillArc(drawX, drawY, size, size, 0, 320);
                break;
            case Accept_Accept:
                g.setColor(Globals.Color$.$dagContract_aa);
                g.setColor(contract.getFinalTrustValue() < 0 ? Globals.Color$.red : Globals.Color$.darkGreen);
                g.fillArc(drawX, drawY, size, size, 0, 359);
                break;
        }

    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle + " :: " + world.getDrawingTitle(), world.getSimulationConfigInfo())) {
            return;
        }
        axisX = 0;

        Agent selectedAgent = null;

        for (Agent agent : world.getAgents()) {
            if (agent.getDaGra() != null && agent.getDaGra().getMy() != null) {
                selectedAgent = agent;
            }
        }
        if (selectedAgent == null) {
            return;
        }


        printStatsInfo(1, "noContractCount", world.getWdStatistics()[worldTimer].getStatisticsDagra().getNoContractCount(), Globals.Color$.$dagContract_nc);
        printStatsInfo(2, "expiredContractCount", world.getWdStatistics()[worldTimer].getStatisticsDagra().getExpiredContractCount(), Globals.Color$.$dagContract_ex);
        printStatsInfo(3, "requestNewCount", world.getWdStatistics()[worldTimer].getStatisticsDagra().getRequestNewCount(), Globals.Color$.$dagContract_rn);
        printStatsInfo(4, "requestSingingCount", world.getWdStatistics()[worldTimer].getStatisticsDagra().getRequestSingingCount(), Globals.Color$.$dagContract_rs);
        printStatsInfo(5, "requestVerifyingCount", world.getWdStatistics()[worldTimer].getStatisticsDagra().getRequestVerifyingCount(), Globals.Color$.$dagContract_rv);
        printStatsInfo(6, "acceptNewCount", world.getWdStatistics()[worldTimer].getStatisticsDagra().getAcceptNewCount(), Globals.Color$.$dagContract_an);
        printStatsInfo(7, "acceptSigningCount", world.getWdStatistics()[worldTimer].getStatisticsDagra().getAcceptSigningCount(), Globals.Color$.$dagContract_as);
        printStatsInfo(8, "acceptVerifyingCount", world.getWdStatistics()[worldTimer].getStatisticsDagra().getAcceptVerifyingCount(), Globals.Color$.$dagContract_av);
        printStatsInfo(9, "acceptAcceptCount", world.getWdStatistics()[worldTimer].getStatisticsDagra().getAcceptAcceptCount(), Globals.Color$.$dagContract_aa);

        reverseNormalizeCoordination();

        //============================//============================ Draw X-axis line
        g.setColor(Color.YELLOW);

        //============================//============================//============================
        g.translate(0, -1);
        axisX = 0;

        g.translate(0, -getRealUpHeight(0) - 100);
        loAxisX = 0;

        if (selectedAgent.getDaGra().getContracts() != null) {
            for (CertContract contract : selectedAgent.getDaGra().getContracts()) {

                calculateContractPosition(contract);

                //--------------------------------------------------

                for (CertSign certSign : contract.getSignedContracts()) {
                    drawSignConnection(contract, certSign.getSigned(), Globals.Color$.$dagSignConnection_active, Globals.Color$.$dagSignConnection_expired);
                }

                for (CertVerify verifiedContract : contract.getVerifiedContracts()) {
                    drawVerifyConnection(contract, verifiedContract.getVerified(), Globals.Color$.$dagVerifyConnection_active, Globals.Color$.$dagVerifyConnection_expired);
                }
            }

            for (CertContract contract : selectedAgent.getDaGra().getContracts()) {
                drawContract(contract);
            }
        }


    }
}
