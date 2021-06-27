package _type;

public enum TtOutLogMethodSection {
    Main("MAIN"),
    UpdateNextStep("UpNxSt"),
    UpdateTravelHistory("UpTrvHis"),
    TakeAStepTowardTheTarget("TTTrg"),
    Generator_InitializingStates("Gn-InSt"),
    UpdateTargets("UpTrgs"),
    UpdateWatchList("UpWtch"),
    DoYouKnowWhereIs("DoYouKn"),
    TrustData_AddItem("Td-AddItem"),
    TrustMng_GetTrustValue("TutMng-getTV"),
    Env_UpdateAgentCount("Env_UpAgCnt"),
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
