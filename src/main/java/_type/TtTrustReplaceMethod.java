package _type;

public enum TtTrustReplaceMethod {
    Sequential_Circular,
    RemoveLastUpdated,
    ;

    public static TtTrustReplaceMethod getByOrdinal(int ordinal) {
        for (TtTrustReplaceMethod value : values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        return Sequential_Circular;
    }
}
