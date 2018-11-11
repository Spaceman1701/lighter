package fun.connor.lighter.compiler.validation.cause;

public enum  ErrorCause {
    UNKNOWN                     (0, "an error occurred"),
    INDISTINGUISHABLE_ROUTES    (1, "there were indistinguishable routes");


    private final int id;
    private final String message;

    ErrorCause(int id, String message) {
        this.id = id;
        String idString = String.format("%03d", id);
        this.message = "LC" + idString + ": " + message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
