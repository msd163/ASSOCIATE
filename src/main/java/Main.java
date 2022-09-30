import simulateLayer.Simulator;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws Exception {
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

          Simulator simulator = new Simulator();
        simulator.simulate();

        System.out.print("\n");
        System.out.print("\n");
        System.out.print("Main function finished.");

    }

   /* private static void trt() {

        List<Float> ts = new ArrayList<>();
        ts.add(0.5f);
        ts.add(-0.1f);
        ts.add(0.1f);
        ts.add(-0.51f);
        ts.add(0.61f);
        ts.add(-1f);
        ts.add(1f);


        float faFo = 0.1f;


        for (int i = 0; i < ts.size(); i++) {
            Float t = ts.get(i);
            float rff = (float) Math.pow(1 - faFo, i);
            ts.set(i, t * rff);
        }
        int ii = 0;
        for (Float t : ts) {
            System.out.println(ii++ + " > tFaFo: " + t);
        }

        System.out.println();
        System.out.println();

        ts.sort((Float f1, Float f2) -> {
                    if (Math.abs(f1) > Math.abs(f2)) {
                        return -1;
                    }
                    if (Math.abs(f1) < Math.abs(f2)) {
                        return 1;
                    }
                    return 0;
                }
        );

        ii = 0;
        for (Float t : ts) {
            System.out.println(ii++ + " > sorted tFaFo: " + t);
        }


        float xxxxx = 0;
        int index = 0;
        for (int i = 0, tsSize = ts.size(); i < tsSize; i++) {
            Float t = ts.get(i);
            xxxxx += ((t) / ((index + 2) * (index + 2)));
            System.out.println(i + ": index: " + index + " | " + t + "  > " + xxxxx);
            index++;
        }

        System.out.println("==============");
        System.out.printf("trt: " + (xxxxx));

    }
*/
}
