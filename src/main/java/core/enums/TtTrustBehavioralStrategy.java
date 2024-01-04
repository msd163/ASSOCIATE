package core.enums;

public enum TtTrustBehavioralStrategy {
    OnlyHonest,
    OnlyDishonest,
    Discrete,
    Fuzzy;

    public static TtTrustBehavioralStrategy getByOrdinal(int ordinal) {
        for (TtTrustBehavioralStrategy value : values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        return OnlyHonest;
    }
}
