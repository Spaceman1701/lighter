package fun.connor.lighter.processor.validators;

import fun.connor.lighter.declarative.Delete;
import fun.connor.lighter.declarative.Get;
import fun.connor.lighter.declarative.Post;
import fun.connor.lighter.declarative.Put;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public class QueryParamsValidator extends EndpointAnnotationValidator {



    @Override
    public void validate(Element annotatedElement) {
        super.validate(annotatedElement);

        if (!AnnotationValidationUtils.hasEndpointAnnotation(annotatedElement)) {
            throw new IllegalArgumentException("no endpoint definition annotation present");
        }
    }

}
