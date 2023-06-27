package _type;

public enum TtExecutionTarget {
    Windows(""),
    Linux("/"),
    ;

    private final String path;
    TtExecutionTarget(String p) {
        this.path = p;
    }

    public String getPath() {
        return path;
    }
}


