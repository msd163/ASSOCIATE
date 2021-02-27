import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import profiler.CapacityProfiler;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
        Gson gson = new Gson();

        CapacityProfiler profiler = new CapacityProfiler();

//        FileWriter writer = new FileWriter(capFileName);
//        writer.write("in the name of Allah");
//        gson.toJson(profiler, writer);
//        writer.close();

        FileReader reader = new FileReader(capFileName);
        profiler = gson.fromJson(reader, CapacityProfiler.class);

        System.out.println("popCount:"+profiler.populationCount);
        System.out.println("bunch:"+profiler.bunchCount());



//        profiler.LoadCapacityProfile("D:\\01-Project\\34-selfDrivingCars\\02-V2VNetwork\\src\\main\\java\\SimData\\Sim0.txt");//"../src/main/java/SimData/Sim0.txt");
//        for (int b = 0; b < profiler.bunchCount() ; b++)
//        {
//            System.out.println("this is new bunch");
//            for (int c = 0 ; c < TtCapacityProfiler.CapacityParameterCount.ordinal() ; c++)
//                System.out.print(profiler.getCapNextValue(b, TtCapacityProfiler.values()[c]) + " " );
//
//        }

        Simulator simulator = new Simulator();
//        simulator.simulate();


    }

}
