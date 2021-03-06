package fun.connor.lighter.compiler.step;

import fun.connor.lighter.compiler.model.Model;
import fun.connor.lighter.compiler.validation.ValidationReport;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.util.HashSet;
import java.util.Set;

/**
 * Validates that the application model represents an application that will work at runtime.
 */
public class ValidateModelStep extends CompilerStep {

    private Model model;

    public ValidateModelStep(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public Set<EnvironmentRequirement> getRequiredEnv() {
        EnvironmentRequirement<Model> req =
                new EnvironmentRequirement<>("model", Model.class, this::setModel);
        Set<EnvironmentRequirement> requirements = new HashSet<>();
        requirements.add(req);
        return requirements;
    }

    private void setModel(Model m) {
        this.model = m;
    }

    @Override
    public StepResult process(RoundEnvironment roundEnv) {
        ValidationReport.Builder modelReportBuilder = ValidationReport.builder();
        model.validate(modelReportBuilder);
        ValidationReport modelReport = modelReportBuilder.build();

        return new StepResult(modelReport);
    }
}
