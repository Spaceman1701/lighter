package fun.connor.lighter.compiler.validation;

import fun.connor.lighter.compiler.validation.ValidationReport;

public interface Validatable {
    void validate(ValidationReport.Builder reportBuilder);
}
