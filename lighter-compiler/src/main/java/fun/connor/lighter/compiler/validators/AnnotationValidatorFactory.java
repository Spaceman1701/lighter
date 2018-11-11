package fun.connor.lighter.compiler.validators;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

@FunctionalInterface
public interface AnnotationValidatorFactory<T extends Annotation> {
    AnnotationValidator<T> newInstance(Element annotatedElement, T annotation);
}
