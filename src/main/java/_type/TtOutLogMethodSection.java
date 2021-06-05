package _type;

public enum TtOutLogMethodSection {
    Main("MAIN"),
    UpdateNextStep("UpNxSt"),
    UpdateTravelHistory("UpTrvHis"),
    TakeAStepTowardTheTarget("TTTrg"),
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
