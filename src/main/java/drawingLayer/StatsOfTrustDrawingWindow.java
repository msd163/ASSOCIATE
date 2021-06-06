package drawingLayer;

import systemLayer.World;
import utils.Config;
import utils.Globals;
import utils.WorldStatistics;

import java.awt.*;

public class StatsOfTrustDrawingWindow extends DrawingWindow {

    private World world;

    private int axisX = 0;


    private int totalHonestTrust = 0;
    private int totalDishonestTrust = 0;
    private int lastEpisode = -1;
    //============================//============================  panning params

    public StatsOfTrustDrawingWindow(World world) {
        super();
        this.world = world;
    }

    @Override
    public void paint(Graphics gr) {

        g = (Graphics2D) gr;
        g.setBackground(Color.BLACK);

        axisX = 0;

        g.clearRect(0, 0, getWidth(), getHeight());

        setBackground(Color.BLACK);
        g.setColor(Color.YELLOW);

        //============================//============================ Translate for panning and scaling

        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));

        g.drawString("World Time                : " + Globals.WORLD_TIMER, 100, 50);
        g.drawString("Episode                    : " + Globals.EPISODE, 100, 90);

        if (Globals.WORLD_TIMER < Config.WORLD_LIFE_TIME) {
            g.setColor(Color.GREEN);
            g.drawString("Total Trust To HONEST :   " + totalHonestTrust, 100, 150);
            g.setColor(Color.YELLOW);
            g.drawString("Trust To HONEST           :   " + world.getStatistics()[Globals.WORLD_TIMER].getIttTrustToHonest(), 100, 190);
            g.setColor(Color.RED);
            g.drawString("Total Trust To DisHONEST  :   " + totalDishonestTrust, 100, 230);
            g.setColor(Color.ORANGE);
            g.drawString("Trust To DisHONEST :   " + world.getStatistics()[Globals.WORLD_TIMER].getIttTrustToDishonest(), 100, 270);
        }

        //============================//============================//============================

        g.translate(pnOffset.x + scaleOffset.x, pnOffset.y + scaleOffset.y);
        g.scale(scale, -scale);
        g.translate(100, -getHeight() / scale + 100);

        //============================ Translate

        g.drawLine(0, 0, getWidth(), 0);

        WorldStatistics[] statistics = world.getStatistics();

        for (int i = 0, statisticsLength = statistics.length; i < Globals.WORLD_TIMER && i < statisticsLength; i++) {
            WorldStatistics stat = statistics[i];
            axisX += 5;
            if (lastEpisode != stat.getEpisode()) {
                totalHonestTrust = 0;
                totalDishonestTrust = 0;
                lastEpisode = stat.getEpisode();
            }
            totalHonestTrust += stat.getIttTrustToHonest();
            totalDishonestTrust += stat.getIttTrustToDishonest();

            //============================ Bound Rectangle
            //g.drawRect(0, 0, world.getWidth(), world.getHeight());
            g.setColor(Color.YELLOW);
            g.fillOval(axisX, stat.getIttTrustToHonest(), 5, 5);
            ///
            g.setColor(Color.GREEN);
            g.fillOval(axisX, totalHonestTrust, 5, 5);
            //============================
            g.setColor(Color.pink);
            g.fillOval(axisX, stat.getIttTrustToDishonest(), 5, 5);
            ///
            g.setColor(Color.RED);
            g.fillOval(axisX, totalDishonestTrust, 5, 5);

        }

    }
}
