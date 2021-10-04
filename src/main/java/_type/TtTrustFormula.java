package _type;

public enum TtTrustFormula {
    None,
    Formula_1,
    Formula_2_Maclaurin,
    ;

    public static TtTrustFormula getByOrdinal(int ordinal) {
        for (TtTrustFormula value : values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        return None;
    }
}
