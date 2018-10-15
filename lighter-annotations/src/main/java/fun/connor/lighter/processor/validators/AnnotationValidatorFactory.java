package fun.connor.lighter.processor.validators;

import java.util.HashMap;
import java.util.Map;

public class AnnotationValidatorFactory {

    private Map<String, AnnotationValidator> validatorMap;

    public AnnotationValidatorFactory() {
        validatorMap = new HashMap<>();
    }

    public void registerValidator(String name, AnnotationValidator validator) {
        validatorMap.put(name, validator);
    }

    public AnnotationValidator getInstance(String annotationName) {
        return validatorMap.get(annotationName);
    }
}
