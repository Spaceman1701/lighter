package fun.connor.lighter.compiler.validators;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationValidatorMap {

    private Map<Class<? extends Annotation>, AnnotationValidatorFactory> map;

    public AnnotationValidatorMap() {
        this.map = new HashMap<>();
    }

    public <T extends Annotation> void register(Class<T> annotation, AnnotationValidatorFactory<T> factory) {
        map.put(annotation, factory);
    }

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
