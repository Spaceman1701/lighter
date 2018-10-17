package fun.connor.lighter.processor.step;

import fun.connor.lighter.processor.error.CompilerError;

import java.util.Set;

public class StepResult {
    private final Set<CompilerError> errors;
    private final String resultName;
    private final Object result;

    StepResult(Set<CompilerError> errors, String resultName, Object result) {
        this.errors = errors;
        this.resultName = resultName;
        this.result = result;
    }

    StepResult(Set<CompilerError> errors) {
        this(errors, null, null);
    }

    public Set<CompilerError> getErrors() {
        return errors;
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
