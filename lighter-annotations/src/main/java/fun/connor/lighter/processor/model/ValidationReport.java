package fun.connor.lighter.processor.model;

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
        s.append(prefix).append(contextStr).append(":").append("\n");

        for (ValidationError e : errors) {
            s.append(prefix).append(INDENT).append(e.toStringRelative(nextPrefix));
        }
        for (ValidationReport r : children) {
            s.append(prefix).append(INDENT).append(r.toStringRelative(nextPrefix));
        }

        return s.toString();
    }

    public boolean containsErrors() {
        return !errors.isEmpty() || children.stream()
                .map(ValidationReport::containsErrors)
                .reduce(Boolean::logicalOr).orElse(false);
    }

    public static Builder builder(String contextStr) {
        return new Builder(contextStr);
    }

    public static Builder builder() {
        return new Builder(null);
    }

    public static class Builder {

        private String contextStr;
        private boolean hasContext;
        private List<ValidationError> errors;
        private List<ValidationReport.Builder> children;

        private Builder(String contextStr) {
            this.contextStr = contextStr;
            this.hasContext = contextStr != null;
            errors = new ArrayList<>();
            children = new ArrayList<>();
        }

        public Builder addError(ValidationError error) {
            errors.add(error);
            return this;
        }

        public Builder addChild(ValidationReport.Builder builder) {
            if (builder.hasContext) {
                children.add(builder);
            } else {
                errors.addAll(builder.errors);
            }
            return this;
        }

        public ValidationReport build() {
            return new ValidationReport(this);
        }
    }
}
