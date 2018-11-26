package fun.connor.lighter.compiler.validation.cause;

/**
 * Enumeration of all high-level causes of compiler errors. These exist to provide
 * generic context to errors, aid error searchability, and aid testing
 */
public enum  ErrorCause {
    UNKNOWN                             (0, "an error occurred"),
    INDISTINGUISHABLE_ROUTES            (1, "there were indistinguishable routes"),
    BAD_RESOURCE_CONTROLLER_LOCATION    (2, "@ResourceController cannot be placed here"),
    BAD_ENDPOINT_ANNOTATION_LOCATION    (3, "Endpoint annotations cannot be placed here"),
    BAD_QUERY_PARAMS_LOCATION           (4, "@QueryParams cannot be placed here"),
    BAD_BODY_LOCATION                   (5, "@Body cannot be placed here"),
    BAD_PORODUCES_REQ_GUARD_LOCATION    (6, "@ProducesRequestGuard cannot be placed here"),
    BAD_RESOURCE_CONTROLLER_PATH        (7, "An illegal path was provided to @ResourceController"),
    BAD_ENDPOINT_PATH                   (8, "An illegal path was provided to an endpoint annotation"),
    CODE_GENERATION_ERROR               (9, "An IOException occurred when writing a code file"),
    CANNOT_MAP_PARAMETER                (10, "Method is missing a parameter"),
    ILLEGAL_ROUTE_SYNTAX                (11, "The defined route stub is invalid");


    private final int id;
    private final String message;

    ErrorCause(int id, String message) {
        this.id = id;
        String idString = String.format("%03d", id);
        this.message = "LC" + idString + ": " + message;
    }

    /**
     * @return the unique id of this error cause
     */
    public int getId() {
        return id;
    }

    /**
     * @return a generic message for this error cause
     */
    public String getMessage() {
        return message;
    }
}
