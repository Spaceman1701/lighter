package fun.connor.lighter.processor.model;

public interface Validatable {
    void validate(ValidationReport.Builder reportBuilder);
}
