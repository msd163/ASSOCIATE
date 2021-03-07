package utils;

import com.sun.javafx.geom.Point2D;
import stateTransition.DefState;
import stateTransition.DefTransition;
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
        DefState startState;
        DefState endState;
        for (int x = 0 ; x < Globals.environment.getStateCount() ; x++)
        {
            DefTransition start = Globals.environment.transitions[x];
            Point2D xx = start.getLocation();
            ArrayList<Integer> final_idx = start.getFinal_idx();
            g.drawRect((int)xx.x, (int)xx.y, 5, 5);
            for (int i = 0 ; i < final_idx.size() ; i++)
            {
                g.drawLine((int)xx.x,(int)xx.y,
                        (int)Globals.environment.transitions[final_idx.get(i)].getLocation().x,
                        (int)Globals.environment.transitions[final_idx.get(i)].getLocation().y);
            }
        }



//        for (Agent agent : world.getAgents()) {
//
//            agent.draw((Graphics2D) g);
//
//        }
    }


}
