package profiler;


public class PopulationBunch {
    public main.java.utils.DefParameter BunchCount;
    public main.java.utils.DefParameter HistoryServiceRecordCap;
    public main.java.utils.DefParameter historyCap;
    public main.java.utils.DefParameter watchListCapacity;
    public main.java.utils.DefParameter watchRadius;
    public main.java.utils.DefParameter concurrentDoingServiceCap;

    public void setBunchCount(String p)
    {
        BunchCount.setParameter(p);
    }
    public int getBunchCount()
    {
        return BunchCount.nextValue();
    }

    public int getHistoryServiceRecordCap()
    {
        return HistoryServiceRecordCap.nextValue();
    }
    public void setHistoryServiceRecordCap(String p)
    {
        HistoryServiceRecordCap.setParameter(p);
    }

    public int getHistoryCap()
    {
        return historyCap.nextValue();
    }
    public int getWatchListCapacity()
    {
        return watchListCapacity.nextValue();
    }
    public int getWatchRadius()
    {
        return watchRadius.nextValue();
    }
    public int getConcurrentDoingServiceCap()
    {
        return concurrentDoingServiceCap.nextValue();
    }
}
