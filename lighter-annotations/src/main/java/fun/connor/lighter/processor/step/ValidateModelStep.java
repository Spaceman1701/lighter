package fun.connor.lighter.processor.step;

import fun.connor.lighter.processor.error.AbstractCompilerError;
import fun.connor.lighter.processor.error.AnnotationValidationError;
import fun.connor.lighter.processor.error.PreformattedError;
import fun.connor.lighter.processor.error.RouteError;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Model;
import fun.connor.lighter.processor.model.ValidationReport;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<Controller> controllers = model.getControllers();

        ValidationReport.Builder modelReportBuilder = ValidationReport.builder("while validating model");
        model.validate(modelReportBuilder);
        ValidationReport modelReport = modelReportBuilder.build();

        return new StepResult(modelReport);
    }
}
