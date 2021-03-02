import com.google.gson.Gson;
import profiler.CapacityProfiler;
import stateTransition.DefState;
import stateTransition.Environment;
import utils.Globals;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Main {

    public static void init() {

    }


    public static void main(String[] args) throws IOException {
        System.out.print("\n");
        System.out.print("  _________________________");
        System.out.print("\n");
        System.out.print("    In the name of ALLAH");
        System.out.print("\n");
        System.out.print("IOT Trust Evaluation Simulator");
        System.out.print("\n");
        System.out.print("  _________________________");
        System.out.print("\n");
        System.out.print("\n");

        String capFileName = "D:\\01-Project\\34-selfDrivingCars\\02-V2VNetwork\\src\\main\\java\\SimData\\Sim0.json";
        String envFileName = "D:\\01-Project\\34-selfDrivingCars\\02-V2VNetwork\\src\\main\\java\\SimData\\environment.json";
        Gson gson = new Gson();

        FileReader prfReader = new FileReader(capFileName);
        Globals.profiler = gson.fromJson(prfReader, CapacityProfiler.class);
        Globals.profiler.init();

        System.out.println("popCount:"+Globals.profiler.populationCount);
        System.out.println("bunch:"+Globals.profiler.bunchCount());
        System.out.println("sim Round:"+Globals.profiler.simulationRound);

        FileReader envReader = new FileReader(envFileName);
        Globals.environment = gson.fromJson(envReader, Environment.class);

        System.out.println("stateCount:"+Globals.environment.getStateCount());
        System.out.println("actionCount:"+Globals.environment.getActionCount());
        System.out.println("transitionCount:"+Globals.environment.getTransitionCount());

        int x = 20;
        int y = 20;

        for (int pop = 0; pop < Globals.environment.getTransitionCount() ; pop++)
        {
            DefState startState = Globals.environment.getStartState(pop);
            ArrayList<DefState> endState = Globals.environment.getTransitionFrom(startState);
            System.out.println("size " + endState.size() + " " + startState.getInDegree());




            if(startState.hasLocation() == false)
            {
                Globals.environment.getStartState(pop).setLocation(x,y);
            }
            int inDegree = -startState.getInDegree()/2;
            int XX = startState.getX();
            int YY = startState.getY();
            for (int en = 0 ; en < endState.size() ; en++)
                if( endState.get(en).hasLocation() == false )
                {
                    endState.get(en).setLocation(XX + 40,
                                                YY + (inDegree+en)*15);
                }
            x = XX + 40;
        }

        Simulator simulator = new Simulator();
//        simulator.simulate();


    }

}
