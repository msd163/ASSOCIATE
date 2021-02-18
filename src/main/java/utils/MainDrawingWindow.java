package utils;

import system.Agent;
import system.World;

import java.awt.*;

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

        for (Agent agent : world.getAgents()) {

            agent.draw((Graphics2D) g);

        }
    }


}
