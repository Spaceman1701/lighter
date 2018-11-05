package fun.connor.lighter.processor.step;

import fun.connor.lighter.processor.error.AbstractCompilerError;
import fun.connor.lighter.processor.error.AnnotationValidationError;
import fun.connor.lighter.processor.error.RouteError;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Model;

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
        Set<AbstractCompilerError> errors = new HashSet<>();

        errors.addAll(checkControllerFragments(controllers));
        errors.addAll(checkEndpointRoutes(controllers));

        return new StepResult(errors);
    }

    private Set<AbstractCompilerError> checkControllerFragments(List<Controller> controllers) {
        Set<AbstractCompilerError> errors = new HashSet<>();
        for (Controller a : controllers) { //TODO: hash-table implementation here
            for (Controller b : controllers) {
                if (a != b) {
                    if (a.getRouteFragment().captures(b.getRouteFragment())) {
                        AbstractCompilerError error =
                                new RouteError(a.getElement(), b.getElement(), a.getRouteFragment(), b.getRouteFragment());
                        errors.add(error);
                    }
                }
            }
        }
        return errors;
    }

    private Set<AnnotationValidationError> checkEndpointRoutes(List<Controller> controllers) {
        return new HashSet<>();
    }
}
