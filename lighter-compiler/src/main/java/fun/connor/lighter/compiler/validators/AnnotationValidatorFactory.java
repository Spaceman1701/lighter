package fun.connor.lighter.compiler.validators;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

/**
 * Function interface used to construct {@link AnnotationValidator} instances. Notice
 * that the signature of the method on this interface matches the constructor parameters
 * on {@link AnnotationValidator}.
 * @param <T> The annotation type being validated
 */
@FunctionalInterface
public interface AnnotationValidatorFactory<T extends Annotation> {
    AnnotationValidator<T> newInstance(Element annotatedElement, T annotation);
}
