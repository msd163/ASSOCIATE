package _type;

public enum TtBehaviorStrategy {
    OnlyHonest,
    OnlyDishonest,
    Discrete,
    Fuzzy;

    public static TtBehaviorStrategy getByOrdinal(int ordinal) {
        for (TtBehaviorStrategy value : values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        return OnlyHonest;
    }
}
