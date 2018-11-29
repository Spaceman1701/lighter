package fun.connor.lighter.compiler.model;

/**
 * A single component of a {@link Route}. Routes are fundamentally constructed from an
 * ordered collections of RouteParts. RouteParts have a String representation and are
 * categorized as different {@link Kind}s.
 */
public class RoutePart {

    /**
     * The kind of a {@link RoutePart}. This is what type of
     * template component this part represents.
     */
    public enum Kind {
        /**
         * Normal part - matches parts with exactly the same as it
         */
        NORMAL,

        /**
         * Parameter definition - matches anything and
         * represents a bindable value at runtime
         */
        PARAMETER,

        /**
         * Wildcard character - represents a global which greedly matches several route parts
         */
        WILDCARD
    }

    private final String string;
    private final Kind kind;

    RoutePart(String string, Kind kind) {
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
