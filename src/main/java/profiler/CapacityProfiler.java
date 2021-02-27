package profiler;
import java.util.List;

public class CapacityProfiler {
    public int populationCount;
    List<PopulationBunch> BunchOfIndividualsCapacity;

    public int bunchCount() {
        return BunchOfIndividualsCapacity.size();
    }
}
