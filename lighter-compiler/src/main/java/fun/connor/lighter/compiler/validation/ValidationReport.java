package fun.connor.lighter.compiler.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Validation reports are used to provide compiler errors to the user. Validation errors represent
 * incorrect input and <strong>not</strong> logic errors in the Lighter compiler. Validation reports
 * follow a composite pattern. Every report contains a set of sub-reports and {@link ValidationError}s.
 * <br>
 * Reports are constructed using a builder object. The builder maintains a report-level context to aid
 * in locating user errors. If errors or reports added to the builder do not have their own context information,
 * they inherit the report context. This way most errors gain some location information regardless of where
 * they are generated.
 * <br>
 * ValidationReport implements the {@link Printable} interface which allows it to format it's data for the
 * compiler log.
 */
public class ValidationReport implements Printable {

    private final List<ValidationError> errors;
    private final List<ValidationReport> children;

    private ValidationReport(Builder builder) {
        this.errors = builder.errors;
        this.children = builder.children.stream()
                .map(ValidationReport::new)
                .collect(Collectors.toList());
    }

    public boolean containsErrors() {
        return !errors.isEmpty() || children.stream()
                .map(ValidationReport::containsErrors)
                .reduce(Boolean::logicalOr).orElse(false);
    }

    /**
     * Create a context free {@link Builder}
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Create a builder with context
     * @param contextHint location context
     * @return a new builder
     */
    public static Builder builder(LocationHint contextHint) {
        return new Builder(contextHint);
    }

    @Override
    public void print(ReportPrinter printer) {
        for (ValidationError e : errors) {
            printer.printError(e);
        }

        for (ValidationReport report : children) {
            if (report.containsErrors()) {
                report.print(printer);
            }
        }
    }

    /**
     * Handles creation of reports from errors and sub-reports. Maintains
     * report-level context
     */
    public static class Builder {

        private List<ValidationError> errors;
        private List<ValidationReport.Builder> children;
        private LocationHint reportLocation;

        private Builder() {
            this(null);
        }

        private Builder(LocationHint contextLocation) {
            this.reportLocation = contextLocation;
            errors = new ArrayList<>();
            children = new ArrayList<>();
        }

        private void setLocationHint(LocationHint locationHint) {
            this.reportLocation = locationHint;
            for (ValidationError error : errors) {
                if (!error.getLocationHint().isPresent()) {
                    error.setLocationHint(locationHint);
                }
            }
        }

        /**
         * Add an error to this report. If this report has a location hint and
         * the error does not, the error's location hint will be set to the report
         * location hint
         * @param error the error to add
         * @return self
         */
        public Builder addError(ValidationError error) {
            if (reportLocation != null && !error.getLocationHint().isPresent()) {
                error.setLocationHint(reportLocation);
            }
            errors.add(error);
            return this;
        }

        /**
         * Add a sub-report to this report. If this report has a location hint and
         * the child report does not, the child report's location hint will be set
         * to this report's location hint
         * @param builder the child report builder
         * @return self
         */
        public Builder addChild(ValidationReport.Builder builder) {
            if (builder.reportLocation == null && reportLocation != null) {
                builder.setLocationHint(reportLocation);
            }
            children.add(builder);
            return this;
        }

        /**
         * Constructs a {@link ValidationReport} from this builder's data
         * @return a new ValidationReport
         */
        public ValidationReport build() {
            return new ValidationReport(this);
        }
    }
}
