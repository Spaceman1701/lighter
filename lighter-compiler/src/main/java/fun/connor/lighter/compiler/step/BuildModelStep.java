package fun.connor.lighter.compiler.step;

import fun.connor.lighter.compiler.model.Controller;
import fun.connor.lighter.compiler.model.Model;
import fun.connor.lighter.compiler.step.build.ControllerVisitor;
import fun.connor.lighter.declarative.ResourceController;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Constructs the application model from annotated classes. This step must run after
 * the {@link ValidationStep} as it assumes that all invariants validated by that step
 * are held.
 */
public class BuildModelStep extends CompilerStep {

    public BuildModelStep(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public Set<EnvironmentRequirement> getRequiredEnv() {
        return new HashSet<>();
    }

    @Override
    public StepResult process(RoundEnvironment roundEnv) {
        TypeElement responseType =
                env.getElementUtils().getTypeElement(ResourceController.class.getCanonicalName());

        Set<? extends Element> controllerElements = roundEnv.getElementsAnnotatedWith(responseType);

        List<Controller> controllers = new ArrayList<>();

        for (Element e : controllerElements) {
            TypeElement type = (TypeElement) e;
            controllers.add(type.accept(new ControllerVisitor(), null));
        }

        return new StepResult("model", new Model(controllers));
    }

}
