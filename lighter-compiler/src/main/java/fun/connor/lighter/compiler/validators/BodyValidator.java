package fun.connor.lighter.compiler.validators;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class BodyValidator implements AnnotationValidator {
    @Override
    public void validate(Element annotatedElement) throws IllegalArgumentException {
        Element parentMethod = annotatedElement.getEnclosingElement();

        if (parentMethod.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException("element must be a method parameter");
        }

        if (!AnnotationValidationUtils.hasEndpointAnnotation(parentMethod)) {
            throw new IllegalArgumentException("method must be an codegen definition");
        }
    }
}
