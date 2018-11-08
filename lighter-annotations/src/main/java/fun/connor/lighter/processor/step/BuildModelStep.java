package fun.connor.lighter.processor.step;

import fun.connor.lighter.declarative.ResourceController;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Model;
import fun.connor.lighter.processor.step.build.ControllerVisitor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
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

        List<Controller> controllers = new ArrayList<>();

        for (Element e : controllerElements) {
            TypeElement type = (TypeElement) e;
            controllers.add(type.accept(new ControllerVisitor(), null));
        }


        return new StepResult("model", new Model(controllers));
    }

}
