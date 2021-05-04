package profiler;
import utils.DefParameter;

import java.util.ArrayList;

public class CapacityProfiler {

    private String populationCount;
    private String simulationRound;
    private String world_width;
    private String maxVelocityX;
    private String maxVelocityY;


    private DefParameter _populationCount;
    private DefParameter _simulationRound;
    private DefParameter _world_width;
    private DefParameter _maxVelocityX;
    private DefParameter _maxVelocityY;

    private int currentBunch;

    //============================//============================


    public CapacityProfiler()
    {
        currentBunch = 0;
    }

    //============================//============================

    public ArrayList<PopulationBunch> BunchOfIndividualsCapacity;

    public int getPopulationCount() {
        return _populationCount.nextValue();
    }

    public void setPopulationCount(String populationCount) {
        this._populationCount.setParameter( populationCount );
    }

    public int getSimulationRound() {
        return _simulationRound.nextValue();
    }

    public void setSimulationRound(String simulationRound) {
        this._simulationRound.setParameter( simulationRound );
    }


    public int getWorld_width() {
        return _world_width.nextValue();
    }

    public void setWorld_width(String world_width) {
        this._world_width.setParameter(world_width);
    }

    public int getMaxVelocityX() {
        return _maxVelocityX.nextValue();
    }

    public void setMaxVelocityX(String maxVelocityX) {
        this._maxVelocityX.setParameter( maxVelocityX );
    }

    public int getMaxVelocityY() {
        return _maxVelocityY.nextValue();
    }

    public void setMaxVelocityY(String maxVelocityY) {
        this._maxVelocityY.setParameter( maxVelocityY );
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


    @Override
    public String toString() {
        return "CapacityProfiler{" +
                "populationCount='" + populationCount + '\'' +
                ", simulationRound='" + simulationRound + '\'' +
                ", world_width='" + world_width + '\'' +
                ", maxVelocityX='" + maxVelocityX + '\'' +
                ", maxVelocityY='" + maxVelocityY + '\'' +
                ", _populationCount=" + _populationCount +
                ", _simulationRound=" + _simulationRound +
                ", _world_width=" + _world_width +
                ", _maxVelocityX=" + _maxVelocityX +
                ", _maxVelocityY=" + _maxVelocityY +
                ", BunchOfIndividualsCapacity=" + BunchOfIndividualsCapacity +
                ", currentBunch=" + currentBunch +
                '}';
    }
}
