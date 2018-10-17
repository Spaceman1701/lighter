package fun.connor.lighter.processor.step.build;

import fun.connor.lighter.processor.processors.Controller;
import fun.connor.lighter.processor.processors.Endpoint;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.AbstractElementVisitor8;
import javax.lang.model.util.AbstractTypeVisitor8;
import java.util.ArrayList;
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

        List<Endpoint> endpoints = typeElement.getEnclosedElements().stream()
                .map((Element e) -> e.accept(new EndpointVisitor(), null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new Controller(typeElement, endpoints);
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
