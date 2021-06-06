package _type;

public enum TtTrustReplaceHistoryMethod {
    Sequential_Circular,
    RemoveLastUpdated,
    ;

    public static TtTrustReplaceHistoryMethod getByOrdinal(int ordinal) {
        for (TtTrustReplaceHistoryMethod value : values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        return Sequential_Circular;
    }
}
