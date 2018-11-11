package fun.connor.lighter.compiler.validation;

import fun.connor.lighter.compiler.model.ReportFormatable;

import java.util.Optional;

public class ValidationError implements ReportFormatable {
    private String message;
    private LocationHint locationHint;
    private ErrorCause cause;

    public ValidationError(String message, LocationHint locationHint) {
        this.message = message;
        this.locationHint = locationHint;
    }

    public ValidationError(String message) {
        this(message, null);
    }

    void setLocationHint(LocationHint hint) {
        System.out.println("location hint set!");
        this.locationHint = hint;
    }

    @Override
    public String toStringRelative(String prefix) {
        String[] messageByLine = message.split("\n");

        StringBuilder builder = new StringBuilder();
        for (String line : messageByLine) {
            builder.append(prefix).append(line).append("\n");
        }
        return builder.toString();
    }

    Optional<LocationHint> getLocationHint() {
        return Optional.ofNullable(locationHint);
    }

    @Override
    public String toString() {
        return message;
    }
}
