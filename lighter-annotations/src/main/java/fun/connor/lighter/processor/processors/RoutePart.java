package fun.connor.lighter.processor.processors;

public class RoutePart {

    public enum Kind {
        NORMAL, PARAMETER, WILDCARD
    }

    private final String string;
    private final Kind kind;

    public RoutePart(String string, Kind kind) {
        this.string = string;
        this.kind = kind;
    }

    public String getString() {
        return string;
    }

    public Kind getKind() {
        return kind;
    }
}
