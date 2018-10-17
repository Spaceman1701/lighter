package fun.connor.lighter.processor.step.build;

import fun.connor.lighter.declarative.Delete;
import fun.connor.lighter.declarative.Get;
import fun.connor.lighter.declarative.Post;
import fun.connor.lighter.declarative.Put;
import fun.connor.lighter.processor.processors.Endpoint;

import javax.lang.model.element.*;
import javax.lang.model.util.AbstractElementVisitor8;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EndpointVisitor extends AbstractElementVisitor8<List<Endpoint>, Void> {

    private static final Class[] ENDPOINT_ANNOTATIONS =
            {Get.class, Delete.class, Post.class, Put.class};

    @Override
    public List<Endpoint> visitPackage(PackageElement packageElement, Void aVoid) {
        return null;
    }

    @Override
    public List<Endpoint> visitType(TypeElement typeElement, Void aVoid) {
        return null;
    }

    @Override
    public List<Endpoint> visitVariable(VariableElement variableElement, Void aVoid) {
        return null;
    }

    @Override
    public List<Endpoint> visitExecutable(ExecutableElement executableElement, Void aVoid) {
        Set<Class<? extends Annotation>> endpointAnnotations = getEndpointAnnotationSet(executableElement);
        if (!endpointAnnotations.isEmpty()) {
            EndpointParser parser = new EndpointParser(executableElement, endpointAnnotations);
            return parser.parse();
        }
        return null;
    }

    @SuppressWarnings("unchecked") //unchecked cannot cause bug here + ENDPOINT_ANNOTATIONS guaranteed to be correct
    private Set<Class<? extends Annotation>> getEndpointAnnotationSet(ExecutableElement element) {
        Set<Class<? extends Annotation>> annotations = new HashSet<>();
        for (Class c : ENDPOINT_ANNOTATIONS) {
            if (element.getAnnotation(c) != null) {
                annotations.add(c);
            }
        }
        return annotations;
    }

    @Override
    public List<Endpoint> visitTypeParameter(TypeParameterElement typeParameterElement, Void aVoid) {
        return null;
    }
}
