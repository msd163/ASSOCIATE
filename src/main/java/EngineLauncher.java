import SiM.engine.Engine;
import core.utils.Globals;


public class EngineLauncher {

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


        Engine engine = new Engine();
        engine.simulate();

        System.out.print("\n");
        System.out.print("\n");
        System.out.print("Main function finished.");
        Globals.FINISHED = true;

    }

}
