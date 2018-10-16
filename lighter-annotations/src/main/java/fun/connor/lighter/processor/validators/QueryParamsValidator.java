package fun.connor.lighter.processor.validators;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public class QueryParamsValidator extends EndpointAnnotationValidator {


    public QueryParamsValidator(ProcessingEnvironment environment) {
        super(environment);
    }

    @Override
    public void validate(Element annotatedElement) {
        super.validate(annotatedElement);

        if (!AnnotationValidationUtils.hasEndpointAnnotation(annotatedElement)) {
            throw new IllegalArgumentException("no endpoint definition annotation present");
        }
    }

}
