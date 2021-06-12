package drawingLayer;

import systemLayer.World;
import utils.Globals;
import utils.WorldStatistics;

import java.awt.*;

public class StatsOfTrustParamsDrawingWindow extends DrawingWindow {

    private World world;

    private float fp;
    private float fn;
    private float tp;
    private float tn;

    //--  (Number of correct assessments)/Number of all assessments)
    private float accuracy;

    //--  Specificity relates to the test's ability to correctly reject healthy patients without a condition. Specificity of a test is the proportion of who truly do not have the condition who test negative for the condition.
    //--  (Number of true negative assessment)/(Number of all negative assessment)
    //--  True Negative rate
    private float specificity;

    //--  Sensitivity refers to the test's ability to correctly detect ill patients who do have the condition
    //--  (Number of true positive assessment)/(Number of all positive assessment)
    //--  True Positive rate
    private float sensitivity;

    /* private float recall;
    private float precision;
    private float falseDiscoveryRate;*/
    //============================//============================  panning params

    public StatsOfTrustParamsDrawingWindow(World world) {
        super();
        this.world = world;
    }

    private int worldTimer;

    @Override
    public void paint(Graphics gr) {

        worldTimer = Globals.WORLD_TIMER - 1;

        if (worldTimer < 0) {
            return;
        }

        g = (Graphics2D) gr;
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, getWidth(), getHeight());
        pauseNotice(g);

        g.setColor(Color.YELLOW);

        axisX = 0;

        //============================//============================ Translate for panning and scaling

        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));

        g.drawString("World Time                : " + (worldTimer), 100, 50);
        g.drawString("Episode                    : " + Globals.EPISODE, 100, 90);

        sensitivity =  world.getStatistics()[worldTimer].getTrustSensitivity();
        specificity =  world.getStatistics()[worldTimer].getTrustSpecificity();
        accuracy =  world.getStatistics()[worldTimer].getTrustAccuracy();

       /*     precision = tp / (fp + tp);
            recall = tp / (tp + fp);
            falseDiscoveryRate = fp / (fp / tp);*/

        g.setColor(Color.GREEN);
        g.drawString("Accuracy               :   " + accuracy, 100, 150);
        g.setColor(Color.YELLOW);
        g.drawString("Sensitivity        :   " + sensitivity, 100, 190);
        g.setColor(Color.RED);
        g.drawString("Specificity        :   " + specificity, 100, 230);


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

        //============================ Translate
        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
        g.scale(scale, -scale);
        g.translate(100, -getHeight() / scale + 100);

        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, getRealWith(), 0);

        WorldStatistics[] statistics = world.getStatistics();

        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];
            axisX += 5;

            fp = stat.getIttFalsePositiveTrust();
            fn = stat.getIttFalseNegativeTrust();
            tp = stat.getIttTruePositiveTrust();
            tn = stat.getIttTrueNegativeTrust();

            sensitivity = tp / (tp + fn);
            specificity = tn / (tn + fp);
            accuracy = (tp + tn) / (tp + tn + fp + fn);

            g.setColor(Color.GREEN);
            g.fillOval(axisX, (int) (accuracy * 200), 5, 5);

            g.setColor(Color.YELLOW);
            g.fillOval(axisX, (int) (sensitivity * 200), 5, 5);

            g.setColor(Color.RED);
            g.fillOval(axisX, (int) (specificity * 200), 5, 5);

        }
    }
}
