package utils;

import stateTransition.DefState;
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
        DefState startState;
        DefState endState;
        for (int x = 0 ; x < Globals.environment.getStateCount() ; x++)
        {
            Globals.environment.states[x].resetDepicted();
        }
        for (int x = 0 ; x < Globals.environment.getTransitionCount() ; x++)
        {
            startState = Globals.environment.getStartState(x);
            endState = Globals.environment.getEndState(x);
            g.setColor(Color.green);
            if(startState.depicted() == false) {
                g.drawRect(startState.getX(), startState.getY(), 5, 5);
                startState.setDepicted();
            }
            if(endState.depicted() == false) {
                g.drawRect(endState.getX(), endState.getY(), 5, 5);
                endState.setDepicted();
            }
            g.setColor(Color.yellow);
            g.drawLine(startState.getX()+5,startState.getY()+2,endState.getX(),endState.getY()+2);
        }


//        for (Agent agent : world.getAgents()) {
//
//            agent.draw((Graphics2D) g);
//
//        }
    }


}
