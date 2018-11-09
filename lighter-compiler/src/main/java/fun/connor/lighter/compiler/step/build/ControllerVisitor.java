package fun.connor.lighter.compiler.step.build;

import fun.connor.lighter.compiler.model.Controller;
import fun.connor.lighter.compiler.model.Endpoint;
import fun.connor.lighter.compiler.model.Route;
import fun.connor.lighter.declarative.ResourceController;

import javax.lang.model.element.*;
import javax.lang.model.util.AbstractElementVisitor8;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ControllerVisitor extends AbstractElementVisitor8<Controller, Void> {

    @Override
    public Controller visitPackage(PackageElement packageElement, Void o) {
        return null;
    }

    @Override
    public Controller visitType(TypeElement typeElement, Void o) {

        Route routeFragment = createControllerRoute(typeElement);

        List<Endpoint> endpoints = typeElement.getEnclosedElements().stream()
                .map((Element e) -> e.accept(new EndpointVisitor(), routeFragment))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return new Controller(typeElement, endpoints, routeFragment);
    }

    private Route createControllerRoute(TypeElement controllerElement) {
        ResourceController controller = controllerElement.getAnnotation(ResourceController.class);
        return new Route(controller.value());
    }

    @Override
    public Controller visitVariable(VariableElement variableElement, Void o) {
        return null;
    }

    @Override
    public Controller visitExecutable(ExecutableElement executableElement, Void o) {
        return null;
    }

    @Override
    public Controller visitTypeParameter(TypeParameterElement typeParameterElement, Void o) {
        return null;
    }
}
