package fun.connor.lighter.compiler.model;

public interface Validatable {
    void validate(ValidationReport.Builder reportBuilder);
}
