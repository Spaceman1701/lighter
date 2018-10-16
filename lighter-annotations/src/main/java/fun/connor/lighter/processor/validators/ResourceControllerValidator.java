package fun.connor.lighter.processor.validators;

import fun.connor.lighter.declarative.ResourceController;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;

public class ResourceControllerValidator implements AnnotationValidator {

    @Override
    public void validate(Element annotatedElement) throws IllegalArgumentException {
        if (annotatedElement.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException("ResourceController can only be applied to class types");
        }

        ResourceController rc = annotatedElement.getAnnotation(ResourceController.class);

        String pathTemplate = rc.value();
        if (pathTemplate.isEmpty()) {
            throw new IllegalArgumentException("ResourceController must have a not-empty path");
        }
        //do some syntax checking

    }
}
