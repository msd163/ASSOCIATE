package profiler;

import utils.DefParameter;

public class PopulationBunch {
    public String BunchCount;
    public String HistoryServiceRecordCap;
    public String historyCap;
    public String watchListCapacity;
    public String watchRadius;
    public String concurrentDoingServiceCap;

    public void initDefParams()
    {
        X_BunchCount = new DefParameter(BunchCount);
        X_HistoryServiceRecordCap = new DefParameter(HistoryServiceRecordCap);
        X_historyCap = new DefParameter(historyCap);
        X_watchListCapacity = new DefParameter(watchListCapacity);
        X_watchRadius = new DefParameter(watchRadius);
        X_concurrentDoingServiceCap = new DefParameter(concurrentDoingServiceCap);
    }

    public int getBunchCount()
    {
        return X_BunchCount.nextValue();
    }

    public int getHistoryServiceRecordCap()
    {
        return X_HistoryServiceRecordCap.nextValue();
    }

    public int getHistoryCap()
    {
        return X_historyCap.nextValue();
    }
    public int getWatchListCapacity()
    {
        return X_watchListCapacity.nextValue();
    }
    public int getWatchRadius()
    {
        return X_watchRadius.nextValue();
    }
    public int getConcurrentDoingServiceCap()
    {
        return X_concurrentDoingServiceCap.nextValue();
    }

    private DefParameter X_BunchCount;
    private DefParameter X_HistoryServiceRecordCap;
    private DefParameter X_historyCap;
    private DefParameter X_watchListCapacity;
    private DefParameter X_watchRadius;
    private DefParameter X_concurrentDoingServiceCap;

}
