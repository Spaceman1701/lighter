package fun.connor.lighter.compiler.validators;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationValidatorDatabase {

    private Map<String, AnnotationValidator> validatorMap;

    public AnnotationValidatorDatabase() {
        validatorMap = new HashMap<>();
    }

    public void registerValidator(Class<? extends Annotation> target, AnnotationValidator validator) {
        validatorMap.put(target.getCanonicalName(), validator);
    }

    public AnnotationValidator getInstance(String annotationName) {
        return validatorMap.get(annotationName);
    }
}
