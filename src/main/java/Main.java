import main.java._type.TtCapacityProfiler;
import main.java.profiler.CapacityProfiler;

public class Main {


    public static void init() {

    }


    public static void main(String[] args) {
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

        CapacityProfiler profiler = new CapacityProfiler();
        profiler.LoadCapacityProfile("../src/main/java/SimData/Sim0.txt");
        for (int b = 0; b < profiler.bunchCount() ; b++)
        {
            for (int c = 0 ; c < TtCapacityProfiler.CapacityParameterCount.ordinal() ; c++)
            {
                System.out.print(profiler.getCapNextValue(b,TtCapacityProfiler.values()[c]));
            }

        }

        Simulator simulator = new Simulator();
        simulator.simulate();


    }

}
