package fun.connor.lighter.processor.step.build;

import fun.connor.lighter.processor.processors.Endpoint;

import javax.lang.model.element.*;
import javax.lang.model.util.AbstractElementVisitor8;

public class EndpointVisitor extends AbstractElementVisitor8<Endpoint, Void> {
    @Override
    public Endpoint visitPackage(PackageElement packageElement, Void aVoid) {
        return null;
    }

    @Override
    public Endpoint visitType(TypeElement typeElement, Void aVoid) {
        return null;
    }

    @Override
    public Endpoint visitVariable(VariableElement variableElement, Void aVoid) {
        return null;
    }

    @Override
    public Endpoint visitExecutable(ExecutableElement executableElement, Void aVoid) {
        return null;
    }

    @Override
    public Endpoint visitTypeParameter(TypeParameterElement typeParameterElement, Void aVoid) {
        return null;
    }
}
