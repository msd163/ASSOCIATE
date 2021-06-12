package utils.profiler;

public class PopulationBunchBehaviorParam {

    private int honestPercent;                                   // Only honest agents percentage. these agents navigate others to correct targets.
    private int adversaryPercent;                                // only dishonest and adversary percentage. these agents navigate others to pitfalls.
    private int mischiefPercent;                                 // only mischief percentage. these agents navigate others to a random target, not correct targets.
    private IntelligentAdversaryBehavior intelligentAdversary;      // an intelligent adversary agent that

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

    public IntelligentAdversaryBehavior getIntelligentAdversary() {
        return intelligentAdversary;
    }

    public void setIntelligentAdversary(IntelligentAdversaryBehavior intelligentAdversary) {
        this.intelligentAdversary = intelligentAdversary;
    }
}
