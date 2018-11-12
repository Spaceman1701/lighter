package fun.connor.lighter.compiler.validation;

public interface Validatable {
    void validate(ValidationReport.Builder reportBuilder);
}
