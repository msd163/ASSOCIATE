package _type;

public enum TtIsValidatedInObservations {
    Unknown,
    Valid,
    Invalid

    ;

    public static TtIsValidatedInObservations getByOrdinal(int ordinal) {
        for (TtIsValidatedInObservations value : values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        return Unknown;
    }
}
