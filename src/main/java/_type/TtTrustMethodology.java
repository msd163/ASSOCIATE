package _type;

public enum TtTrustMethodology {
    NoTrust,
    BasicTrust_OnlyByItsHistory
    ;

    public static TtTrustMethodology getByOrdinal(int ordinal) {
        for (TtTrustMethodology value : values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        return NoTrust;
    }
}
