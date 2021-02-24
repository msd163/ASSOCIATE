package main.java.profiler;

import static main.java.profiler.EeCapacityParameter.*;
import main.java.utils.DefParameter;

import java.io.InputStream;
import java.util.List;

enum EeCapacityParameter{
    WatchCount,
    WatchHistoryDepth,
    CapacityParameterCount
};

public class CapacityProfiler {
    List<List<DefParameter>> BunchOfIndividualsCapacity;

    public void LoadCapacityProfile(String fileName)
    {
        //todo: file read procedure should be completed.

        InputStream capFile = new InputStream(fileName);
        BunchOfIndividualsCapacity.clear();
        while (!capFile.end())
        {
            BunchOfIndividualsCapacity.add(
                    LoadCapacityProfileOfSingleBunch(capFile.readLine()
                    );
        }
    }
    private List<DefParameter> LoadCapacityProfileOfSingleBunch(String SingleLineOfFile)
    {
        List<DefParameter> SingleBunch = null;
        DefParameter temp;
        String[] cap = SingleLineOfFile.split(" ");
        SingleBunch.clear();
        for (int p = 0; p < CapacityParameterCount; p++)
        {
            SingleBunch.add(new DefParameter(cap[p]));
        }

        return SingleBunch;
    }

    public int getCapNextValue(int BunchNumber,int CapNumber)
    {
        return BunchOfIndividualsCapacity.get(BunchNumber).get(CapNumber).nextValue();
    }


}
