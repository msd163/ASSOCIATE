package utils;

import stateTransition.Environment;
import stateTransition.StateX;
import system.World;

import java.awt.*;
import java.util.ArrayList;

public class MainDrawingWindow extends Canvas {

    public final static int SHIFT_X = 100;
    public final static int SHIFT_Y = 200;
    private World world;
    private Environment environment;

    public MainDrawingWindow(World world) {
        this.world = world;
        this.environment = world.getEnvironment();
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
       // g.translate(SHIFT_X, SHIFT_Y);

        //============================ Bound Rectangle
        g.drawRect(0, 0, world.getWidth(), world.getHeight());

        //============================

        for (int x = 0; x < environment.getStateCount(); x++) {

            StateX start = environment.getState(x);
            Point point = start.getLocation();
            ArrayList<StateX> targets = start.getTargets();
            g.drawRect(point.getX(), point.getY(), 5, 5);

            for (StateX st : targets) {
                g.drawLine(point.getX(), point.getY(),
                        st.getLocation().getX(),
                        st.getLocation().getY());
            }
        }

      /*  for (Agent agent : world.getAgents()) {

            agent.draw((Graphics2D) g);

        }*/
    }


}
