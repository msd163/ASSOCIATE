import com.google.gson.Gson;
import com.sun.javafx.geom.Point2D;
import static java.lang.Math.*;
import static java.lang.Math.sin;
import profiler.CapacityProfiler;
import stateTransition.DefTransition;
import stateTransition.Environment;
import utils.Globals;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
        Globals.profiler = gson.fromJson( prfReader , CapacityProfiler.class);
        Globals.profiler.init();

        System.out.println("popCount:"+Globals.profiler.getPopulationCount());

        System.out.println("bunch:"+Globals.profiler.bunchCount());
        System.out.println("sim Round:"+Globals.profiler.getSimulationRound());

        FileReader envReader = new FileReader(envFileName);
        Globals.environment = gson.fromJson(envReader, Environment.class);

        System.out.println("stateCount:"+Globals.environment.getStateCount());


        int r = 1;
        double theta = 0;
        for (int pop = 0; pop < Globals.environment.getStateCount() ; pop++)
        {
            DefTransition curTran = Globals.environment.getTransition(pop);
            ArrayList<Integer> endState = Globals.environment.getTransitionFrom(pop);
            ArrayList<Integer> final_idx = curTran.getFinal_idx();
            int size =  final_idx.size();

            System.out.println("state " + pop + " have out degree " + Globals.environment.getTransitionOutDegree(pop));
            if(curTran.hasLoc == false)
            {
                curTran.setLocation(new Point2D((float)(r * 80 * sin(theta) ),
                                                (float)(r * 80 * cos(theta))));
                theta = theta + (3.14159 / 12.0);
                if(theta > (2.0 * 3.14159)) {
                    theta = 0.0;
                    r++;
                }
            }

            DefTransition final_temp;
            Integer x;
            for (int i=0;i<size;i++)
            {
                x = final_idx.get(i);
                final_temp = Globals.environment.getTransition(x);
                if( final_temp.hasLoc == false )
                {
                    final_temp.setLocation(
                            new Point2D(
                                    (float)(r * 80 * sin(theta) + curTran.getLocation().x),
                                    (float)(r * 80 * cos(theta))+ curTran.getLocation().y));
                    theta = theta + (3.14159 / 12.0);
                    if(theta > (2.0 * 3.14159)) {
                        theta = 0.0;
                        r++;
                    }
                }
            }
        }

        Simulator simulator = new Simulator();
        simulator.simulate();


    }

}
