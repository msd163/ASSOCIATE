package utils;

import com.sun.javafx.geom.Point2D;
import stateTransition.StateTrans;
import system.World;

import java.awt.*;
import java.util.ArrayList;

public class MainDrawingWindow extends Canvas {

    public final static int SHIFT_X = 100;
    public final static int SHIFT_Y = 200;
    private World world;

    public MainDrawingWindow(World world) {
        this.world = world;
    }

    @Override
    public void update(Graphics g) {

        // System.out.printf("\n:::");

        g.clearRect(0, 0, getWidth(), getHeight());

        setBackground(Color.BLACK);
        g.setColor(Color.YELLOW);

        //============================ Title
        g.setFont(new Font("TimesRoman", Font.PLAIN, 38));
        g.drawString(world.toString(), 40, 40);

        //============================ Translate
        g.translate(SHIFT_X, SHIFT_Y);

        //============================ Bound Rectangle
        g.drawRect(0, 0, world.getWidth(), world.getHeight());

        //============================

        for (int x = 0; x < world.getEnvironment().getStateCount(); x++) {

            StateTrans start = world.getEnvironment().getTransition(x);
            Point2D xx = start.getLocation();
            ArrayList<StateTrans> final_idx = start.getTargets();
            g.drawRect((int) xx.x, (int) xx.y, 5, 5);

            for (StateTrans st : final_idx) {
                g.drawLine((int) xx.x, (int) xx.y,
                        (int) st.getLocation().x,
                        (int) st.getLocation().y);
            }
        }


//        for (Agent agent : world.getAgents()) {
//
//            agent.draw((Graphics2D) g);
//
//        }
    }


}
