package fun.connor.lighter.processor.validators;

import javax.lang.model.element.Element;

public class QueryParamsValidator extends EndpointAnnotationValidator {



    @Override
    public void validate(Element annotatedElement) {
        super.validate(annotatedElement);

        if (!AnnotationValidationUtils.hasEndpointAnnotation(annotatedElement)) {
            throw new IllegalArgumentException("no endpoint definition annotation present");
        }
    }

}
