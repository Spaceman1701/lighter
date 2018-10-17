package fun.connor.lighter.processor.step;

import fun.connor.lighter.declarative.ResourceController;
import fun.connor.lighter.processor.error.CompilerError;
import fun.connor.lighter.processor.processors.Controller;
import fun.connor.lighter.processor.processors.Model;

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
        Set<CompilerError> errors = new HashSet<>();

        errors.addAll(checkControllerFragments(controllers));
        errors.addAll(checkEndpointRoutes(controllers));

        return new StepResult(errors);
    }

    private Set<CompilerError> checkControllerFragments(List<Controller> controllers) {
        Set<CompilerError> errors = new HashSet<>();
        for (Controller a : controllers) { //TODO: hash-table implementation here
            for (Controller b : controllers) {
                if (a.getRouteFragment().captures(b.getRouteFragment())) {
                    errors.add(new CompilerError(null, "bad route", ResourceController.class.getCanonicalName()));
                }
            }
        }
        return errors;
    }

    private Set<CompilerError> checkEndpointRoutes(List<Controller> controllers) {
        return new HashSet<>();
    }
}
