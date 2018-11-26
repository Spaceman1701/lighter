package fun.connor.lighter.compiler.validation;

import fun.connor.lighter.compiler.validation.cause.ErrorCause;

import java.util.Optional;

/**
 * An individual validation error. Every error has a message, a cause, and
 * (optionally) a {@link LocationHint}. An instance of this class should
 * represent exactly one error.
 */
public class ValidationError {
    private String message;
    private LocationHint locationHint;
    private ErrorCause cause;

    /**
     * Construct a validation error with a message, location hint, and cause
     * @param message the message
     * @param locationHint the location hint
     * @param cause the cause
     */
    public ValidationError(String message, LocationHint locationHint, ErrorCause cause) {
        this.message = message;
        this.locationHint = locationHint;
        this.cause = cause;
    }

    /**
     * Construct an error with an unknown cause and no location hint
     * @param message the error message
     */
    public ValidationError(String message) {
        this(message, null, ErrorCause.UNKNOWN);
    }

    /**
     * Construct an error with no location
     * @param message the message
     * @param cause the cause
     */
    public ValidationError(String message, ErrorCause cause) {
        this(message, null, cause);
    }

    /**
     * Set the location hint of this error, overriding any previously set value
     * @param hint the new location hint
     */
    void setLocationHint(LocationHint hint) {
        this.locationHint = hint;
    }

    /**
     * @return either this errors location hint or empty if it does not have one
     */
    Optional<LocationHint> getLocationHint() {
        return Optional.ofNullable(locationHint);
    }

    /**
     * @return a pretty-printed version of this error suitable for showing to the user
     */
    @Override
    public String toString() {
        return cause.getMessage() + "\n" + message;
    }
}
