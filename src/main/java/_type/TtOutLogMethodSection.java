package _type;

public enum TtOutLogMethodSection {
    Main("MAIN"),
    UpdateNextStep("UpNxSt"),
    UpdateTravelHistory("UpTrvHis"),
    TakeAStepTowardTheTarget("TTTrg"),
    Generator_InitializingStates("Gn-InSt"),
    updateTargets("UpTrgs"),
    //
    ;

    private String code;

    TtOutLogMethodSection(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
