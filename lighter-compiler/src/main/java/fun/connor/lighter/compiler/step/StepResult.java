package fun.connor.lighter.compiler.step;

import fun.connor.lighter.compiler.validation.ValidationReport;

import java.util.Optional;

public class StepResult {
    private final ValidationReport validationReport;
    private final String resultName;
    private final Object result;

    StepResult(ValidationReport validationReport, String resultName, Object result) {
        this.validationReport = validationReport;
        this.resultName = resultName;
        this.result = result;
    }

    StepResult(ValidationReport validationReport) {
        this(validationReport, null, null);
    }

    StepResult(String resultName, Object result) {
        this(null, resultName, result);
    }

    StepResult() {
        this(null, null, null);
    }

    public Optional<ValidationReport> getErrors() {
        return Optional.ofNullable(validationReport);
    }

    public String getResultName() {
        return resultName;
    }

    public Object getResult() {
        return result;
    }

    public boolean hasResult() {
        return result != null;
    }
}
