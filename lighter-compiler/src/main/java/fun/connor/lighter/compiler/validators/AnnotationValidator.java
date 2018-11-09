package fun.connor.lighter.compiler.validators;

import javax.lang.model.element.Element;

public interface AnnotationValidator {

    void validate(Element annotatedElement) throws IllegalArgumentException;
}
