package main.java.profiler;

import main.java.utils.DefParameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

enum EeCapacityParameter{
    WatchCount,
    WatchHistory,
    CapacityCount
};

public class CapacityProfiler {
    List<List<DefParameter>> BunchOfIndividualsCapacity;

    public void LoadCapacityProfile(String fileName)
    {
        InputStream capFile = new InputStream(fileName);
        while (!capFile.end())
        {
            LoadCapacityProfileOfSingleBunch(capFile.readLine());
        }
    }
    private void LoadCapacityProfileOfSingleBunch(String SingleLineOfFile)
    {
        List<DefParameter> SingleBunch = null;
        DefParameter temp;
        String[] cap = SingleLineOfFile.split(" ");
        for (int p=0;p < EeCapacityParameter.CapacityCount;p++)
        {
            SingleBunch.add(new DefParameter(cap[p]));
        }
        BunchOfIndividualsCapacity.add(SingleBunch);
    }



}
