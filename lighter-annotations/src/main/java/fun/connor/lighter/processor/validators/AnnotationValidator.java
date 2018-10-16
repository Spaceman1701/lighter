package fun.connor.lighter.processor.validators;

import javax.lang.model.element.Element;

public interface AnnotationValidator {

    void validate(Element annotatedElement) throws IllegalArgumentException;
}
