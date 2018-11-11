package fun.connor.lighter.compiler.validation;

import fun.connor.lighter.compiler.model.ReportFormatable;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationReport implements ReportFormatable, Printable {

    private static final String INDENT = "  ";

    private final String contextStr;
    private final List<ValidationError> errors;
    private final List<ValidationReport> children;

    private ValidationReport(Builder builder) {
        this.contextStr = builder.contextStr;
        this.errors = builder.errors;
        this.children = builder.children.stream()
                .map(ValidationReport::new)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return toStringRelative("");
    }

    @Override
    public String toStringRelative(String prefix) {
        StringBuilder s = new StringBuilder();
        String nextPrefix = prefix + INDENT;
        int errorCount = errorCount();
        s.append(prefix)
                .append("found ").append(errorCount()).append(errorCount > 1 ? " errors " : " error ")
                .append(contextStr).append(":").append("\n");

        for (ValidationError e : errors) {
            s.append(prefix).append(INDENT).append(e.toStringRelative(nextPrefix));
            s.append("\n");
        }
        for (ValidationReport r : children) {
            if (r.containsErrors()) {
                s.append(prefix).append(INDENT).append(r.toStringRelative(nextPrefix));
                s.append("\n");
            }
        }

        return s.toString();
    }

    public int errorCount() {
        return errors.size() + children.stream()
                .map(ValidationReport::errorCount)
                .reduce((a, b) -> a + b)
                .orElse(0);
    }

    public boolean containsErrors() {
        return !errors.isEmpty() || children.stream()
                .map(ValidationReport::containsErrors)
                .reduce(Boolean::logicalOr).orElse(false);
    }

    public static Builder builder(String contextStr) {
        return new Builder(contextStr);
    }

    public static Builder builder(LocationHint contextHint) {
        return new Builder(contextHint);
    }

    @Override
    public void print(String prefix, ReportPrinter printer) {
        String nextPrefix = prefix + INDENT;

        printer.printContextMessage(prefix, getContextMessage());

        for (ValidationError e : errors) {
            printer.printError(nextPrefix, e);
        }

        for (ValidationReport report : children) {
            if (report.containsErrors()) {
                report.print(nextPrefix, printer);
            }
        }
    }

    private String getContextMessage() {
        return "found " + errorCount() + (errorCount() > 1 ? " error " : " errors ") +
                contextStr + ":";
    }

    public static class Builder {

        private String contextStr;
        private List<ValidationError> errors;
        private List<ValidationReport.Builder> children;
        private LocationHint reportLocation;

        private Builder(String contextStr) {
            this.contextStr = contextStr;
            errors = new ArrayList<>();
            children = new ArrayList<>();
        }

        private Builder(LocationHint contextLocation) {
            this.reportLocation = contextLocation;
            errors = new ArrayList<>();
            children = new ArrayList<>();
            this.contextStr = "";
        }

        public Builder addError(ValidationError error) {
            if (reportLocation != null && !error.getLocationHint().isPresent()) {
                error.setLocationHint(reportLocation);
            }
            errors.add(error);
            return this;
        }

        public Builder setLocation(LocationHint reportLocation) {
            this.reportLocation = reportLocation;
            return this;
        }

        public Builder addChild(ValidationReport.Builder builder) {
            children.add(builder);
            return this;
        }

        public ValidationReport build() {
            return new ValidationReport(this);
        }
    }
}
