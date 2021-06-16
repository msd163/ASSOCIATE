package _type;

public enum TtTrustMethodology {
    FullyRandomly,
    //--
    NoTrust_RandomPath,
    NoTrust_ShortPath,
    //--
    TrustMode
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
