package trustLayer;

public class TrustHistoryItem {

    private int visitTime;
    private float trustScore;

    //============================//============================//============================

    public int getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(int visitTime) {
        this.visitTime = visitTime;
    }

    public float getTrustScore() {
        return trustScore;
    }

    public void setTrustScore(float trustScore) {
        this.trustScore = trustScore;
    }
}
