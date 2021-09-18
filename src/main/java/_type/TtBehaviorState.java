package _type;

public enum TtBehaviorState {
    Honest,
    Adversary,
    Mischief,
    Hypocrite,
    ;

    public static TtBehaviorState getByOrdinal(int ordinal) {
        for (TtBehaviorState value : values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        return Honest;
    }
}
