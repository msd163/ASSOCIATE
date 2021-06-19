package simulateLayer.profiler;

public class PopulationBunchBehaviorParam {

    private int honestPercent;                                   // Only honest agents percentage. these agents navigate others to correct targets.
    private int adversaryPercent;                                // only dishonest and adversary percentage. these agents navigate others to pitfalls.
    private int mischiefPercent;                                 // only mischief percentage. these agents navigate others to a random target, not correct targets.
    private PopulationBunchHypocriteBehavior hypocriteBehavior;      // an hypocrite agent that
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

    public PopulationBunchHypocriteBehavior getHypocriteBehavior() {
        return hypocriteBehavior;
    }

    public void setHypocriteBehavior(PopulationBunchHypocriteBehavior hypocriteBehavior) {
        this.hypocriteBehavior = hypocriteBehavior;
    }
}
