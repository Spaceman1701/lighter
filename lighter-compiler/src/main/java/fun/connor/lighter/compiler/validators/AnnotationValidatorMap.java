package fun.connor.lighter.compiler.validators;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Lookup table for annotation validators based on annotation type. This reduces code
 * duplication when validating many annotation types. This class takes advantage of the generic
 * type parameters on {@link AnnotationValidator} and {@link AnnotationValidatorFactory} to ensure
 * type safety in it's API
 */
public class AnnotationValidatorMap {

    private Map<Class<? extends Annotation>, AnnotationValidatorFactory> map;

    /**
     * Construct an empty map
     */
    public AnnotationValidatorMap() {
        this.map = new HashMap<>();
    }

    /**
     * Register an {@link AnnotationValidatorFactory} for a specific annotation class
     * @param annotation the annotation class
     * @param factory the factory to be registered
     * @param <T> the annotation type
     */
    public <T extends Annotation> void register(Class<T> annotation, AnnotationValidatorFactory<T> factory) {
        map.put(annotation, factory);
    }

    /**
     * Get an instance of {@link AnnotationValidator} for the provided annotated element. This class
     * guarantees that no {@link ClassCastException} can occur here.
     * @param annotatedElement the annotated element
     * @param annotation the annotation to validate
     * @param clazz the annotation class
     * @param <T> the annotation type
     * @return an {@link} annotation validator suitable to validate the annotation specified by the parameters
     */
    @SuppressWarnings("unchecked") //invariants are enforced by the register method
    public <T extends Annotation> AnnotationValidator<T> getAnnotationValidator
            (Element annotatedElement, Annotation annotation, Class<T> clazz) {
        AnnotationValidatorFactory validatorFactory = map.get(clazz);
        if (validatorFactory == null) {
            return null;
        } else {
            return (AnnotationValidator<T>) map.get(clazz).newInstance(annotatedElement, annotation);
        }
    }
}
