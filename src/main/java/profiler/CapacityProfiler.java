package profiler;


import _type.TtCapacityProfiler;
import utils.DefParameter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class CapacityProfiler {
    public int populationCount;
    public int simulationRound;
    public ArrayList<PopulationBunch> BunchOfIndividualsCapacity;

    public CapacityProfiler()
    {
        currentBunch = 0;
    }
    public void init()
    {
        for(int i=0;i<bunchCount();i++)
        {
            BunchOfIndividualsCapacity.get(i).initDefParams();
        }
    }

    public int bunchCount() {
        return BunchOfIndividualsCapacity.size();
    }

    public PopulationBunch CurrentBunch()
    {
        return BunchOfIndividualsCapacity.get(currentBunch);
    }

    public void NextBunch()
    {
        currentBunch++;
        if(currentBunch >= BunchOfIndividualsCapacity.size())
            currentBunch = 0;
    }
    public void PrevBunch()
    {
        currentBunch--;
        if(currentBunch<0)
            currentBunch = BunchOfIndividualsCapacity.size()-1;
    }
    public void ResetBunch()
    {
        currentBunch = 0;
    }

    private int currentBunch;
}
