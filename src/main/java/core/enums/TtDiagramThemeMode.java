package core.enums;

public enum TtDiagramThemeMode {
    Dark,
    Light;

    public static TtDiagramThemeMode getByOrdinal(int ordinal) {
        for (TtDiagramThemeMode value : values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        return Light;
    }
}
