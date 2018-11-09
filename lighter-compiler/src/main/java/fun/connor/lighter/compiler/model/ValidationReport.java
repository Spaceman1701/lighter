package fun.connor.lighter.compiler.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationReport implements ReportFormatable {

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

    public static class Builder {

        private String contextStr;
        private List<ValidationError> errors;
        private List<ValidationReport.Builder> children;

        private Builder(String contextStr) {
            this.contextStr = contextStr;
            errors = new ArrayList<>();
            children = new ArrayList<>();
        }

        public Builder addError(ValidationError error) {
            errors.add(error);
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
