package fun.connor.lighter.processor.step;

import fun.connor.lighter.declarative.ResourceController;
import fun.connor.lighter.processor.error.CompilerError;
import fun.connor.lighter.processor.processors.Controller;
import fun.connor.lighter.processor.processors.Endpoint;
import fun.connor.lighter.processor.step.build.ControllerVisitor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Set<Controller> controllers = new HashSet<>();

        for (Element e : controllerElements) {
            TypeElement type = (TypeElement) e;
            buildEndpoints(type);
            Controller controller = new Controller(env, roundEnv, type);
            controllers.add(controller);
        }


        return new StepResult(new HashSet<>());
    }

    private List<Endpoint> buildEndpoints(TypeElement controllerElement) {
        List<? extends Element> innerElements = controllerElement.getEnclosedElements();

        controllerElement.accept(new ControllerVisitor(), null);

        return null;
    }
}
