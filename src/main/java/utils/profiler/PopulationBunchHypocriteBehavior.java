package utils.profiler;

import com.google.gson.annotations.Expose;

public class PopulationBunchHypocriteBehavior {

    @Expose
    private int honestPercent;          // Only honest agents percentage. these agents navigate others to correct targets.
    @Expose
    private int adversaryPercent;       // only dishonest and adversary percentage. these agents navigate others to pitfalls.
    @Expose
    private int mischiefPercent;        // only mischief percentage. these agents navigate others to a random target, not correct targets.

    //============================//============================

    public int getHonestPercent() {
        return honestPercent;
    }

    public void setHonestPercent(int honestPercent) {
        this.honestPercent = honestPercent;
    }

    public int getAdversaryPercent() {
        return adversaryPercent;
    }

    public void setAdversaryPercent(int adversaryPercent) {
        this.adversaryPercent = adversaryPercent;
    }

    public int getMischiefPercent() {
        return mischiefPercent;
    }

    public void setMischiefPercent(int mischiefPercent) {
        this.mischiefPercent = mischiefPercent;
    }
}
