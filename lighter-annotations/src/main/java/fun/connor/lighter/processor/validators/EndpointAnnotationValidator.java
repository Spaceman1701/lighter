package fun.connor.lighter.processor.validators;

import fun.connor.lighter.declarative.ResourceController;

import javax.lang.model.element.Element;

public class EndpointAnnotationValidator implements AnnotationValidator {
    @Override
    public void validate(Element annotatedElement) throws IllegalArgumentException {
        Element parent = annotatedElement.getEnclosingElement();

        if (!parent.getKind().isClass()) {
            throw new IllegalArgumentException("method is not a member of a named class");
        }

        if (parent.getAnnotation(ResourceController.class) == null) {
            throw new IllegalArgumentException("owning class does not have ResourceController annotation");
        }
    }
}
