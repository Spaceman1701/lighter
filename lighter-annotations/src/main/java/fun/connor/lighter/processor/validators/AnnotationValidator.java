package fun.connor.lighter.processor.validators;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public interface AnnotationValidator {

    void validate(Element annotatedElement) throws IllegalArgumentException;
}
