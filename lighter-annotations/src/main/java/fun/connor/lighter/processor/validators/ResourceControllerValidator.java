package fun.connor.lighter.processor.validators;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class ResourceControllerValidator implements AnnotationValidator {
    @Override
    public void validate(Element annotatedElement) throws IllegalArgumentException {
        if (annotatedElement.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException("ResourceController can only be applied to class types");
        }


    }
}
