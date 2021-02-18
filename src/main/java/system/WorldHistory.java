package system;

public class WorldHistory {

    public WorldHistory(int totalServiceCount, int dishonestServiceCount, int honestServiceCount) {
        this.totalServiceCount = totalServiceCount;
        this.dishonestServiceCount = dishonestServiceCount;
        this.honestServiceCount = honestServiceCount;
    }

    private int totalServiceCount;
    private int dishonestServiceCount;
    private int honestServiceCount;



    public int getTotalServiceCount() {
        return totalServiceCount;
    }

    public int getDishonestServiceCount() {
        return dishonestServiceCount;
    }

    public float getDishonestServiceRatio() {
        return (float) dishonestServiceCount / totalServiceCount;
    }

    public int getHonestServiceCount() {
        return honestServiceCount;
    }

    public float getHonestServiceRatio() {
        return (float) honestServiceCount / totalServiceCount;
    }
}
