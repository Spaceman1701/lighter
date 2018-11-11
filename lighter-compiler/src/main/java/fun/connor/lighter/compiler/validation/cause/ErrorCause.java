package fun.connor.lighter.compiler.validation.cause;

public enum  ErrorCause {
    UNKNOWN                             (0, "an error occurred"),
    INDISTINGUISHABLE_ROUTES            (1, "there were indistinguishable routes"),
    BAD_RESOURCE_CONTROLLER_LOCATION    (2, "@ResourceController cannot be placed here"),
    BAD_ENDPOINT_ANNOTATION_LOCATION    (3, "Endpoint annotations cannot be placed here"),
    BAD_QUERY_PARAMS_LOCATION           (4, "@QueryParams cannot be placed here"),
    BAD_BODY_LOCATION                   (5, "@Body cannot be placed here"),
    BAD_PORODUCES_REQ_GUARD_LOCATION    (6, "@ProducesRequestGuard cannot be placed here"),
    BAD_RESOURCE_CONTROLLER_PATH        (7, "An illegal path was provided to @ResourceController"),
    BAD_ENDPOINT_PATH                   (8, "An illegal path was provided to an endpoint annotation");


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
