package fun.connor.lighter.compiler.validation;

import fun.connor.lighter.compiler.model.ReportFormatable;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static Builder builder() {
        return new Builder();
    }

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

        public Builder addError(ValidationError error) {
            if (reportLocation != null && !error.getLocationHint().isPresent()) {
                error.setLocationHint(reportLocation);
            }
            errors.add(error);
            return this;
        }

        public Builder addChild(ValidationReport.Builder builder) {
            if (builder.reportLocation == null && reportLocation != null) {
                builder.setLocationHint(reportLocation);
            }
            children.add(builder);
            return this;
        }

        public ValidationReport build() {
            return new ValidationReport(this);
        }
    }
}
