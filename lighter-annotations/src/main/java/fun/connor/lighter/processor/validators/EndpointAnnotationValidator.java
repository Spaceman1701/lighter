package fun.connor.lighter.processor.validators;

import fun.connor.lighter.declarative.ResourceController;
import fun.connor.lighter.handler.Response;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class EndpointAnnotationValidator implements AnnotationValidator {

    private ProcessingEnvironment environment;

    public EndpointAnnotationValidator(ProcessingEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void validate(Element annotatedElement) throws IllegalArgumentException {
        Element parent = annotatedElement.getEnclosingElement();

        if (!parent.getKind().isClass()) {
            throw new IllegalArgumentException("method is not a member of a named class");
        }

        if (parent.getAnnotation(ResourceController.class) == null) {
            throw new IllegalArgumentException("owning class does not have ResourceController annotation");
        }

        validateReturnType((ExecutableElement) annotatedElement);
    }

    private void validateReturnType(ExecutableElement method) {
        TypeMirror returnTypeMirror = method.getReturnType();

        TypeElement responseType = environment.getElementUtils().getTypeElement(Response.class.getCanonicalName());
        TypeElement returnType = (TypeElement)environment.getTypeUtils().asElement(returnTypeMirror);

        if (responseType.getQualifiedName() != returnType.getQualifiedName()) {
            throw new IllegalArgumentException("return type is " + returnType.getQualifiedName() +
                    " but must be " + responseType.getQualifiedName());
        }
    }
}
