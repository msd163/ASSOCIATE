package core.enums;

public enum TtTrustMethodology {
    FullyRandomly,
    //--
    NoTrust_RandomPath,
    NoTrust_ShortPath,
    //--
    TrustMode_RandomPath,
    TrustMode_ShortPath,
    //--
    TrustMode_SafeMode
    ;

    public static TtTrustMethodology getByOrdinal(int ordinal) {
        for (TtTrustMethodology value : values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        return NoTrust_RandomPath;
    }
}
