package fun.connor.lighter.compiler.validators;

import fun.connor.lighter.compiler.validation.ValidationError;
import fun.connor.lighter.compiler.validation.ValidationReport;
import fun.connor.lighter.compiler.validation.cause.ErrorCause;
import fun.connor.lighter.declarative.ResourceController;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;

import static fun.connor.lighter.compiler.validators.LocationValidator.Predicates.*;

public class EndpointAnnotationValidator<T extends Annotation> extends AnnotationValidator<T> {

    public EndpointAnnotationValidator(Element annotatedElement, T annotation) {
        super(annotatedElement, annotation);
    }

    @Override
    public void validate(ValidationReport.Builder reportBuilder) {
        String annotationName = annotation.getClass().getInterfaces()[0].getSimpleName(); //proxy annotation type.

        LocationValidator.builder()
                .element(annotatedElement)
                .locationHint(reportBuilder.getLocationHint())
                .errorCause(ErrorCause.BAD_ENDPOINT_ANNOTATION_LOCATION)
                .message("@" + annotationName + " can only be placed on concrete public methods")
                .withPredicates(requireModifier(Modifier.PUBLIC), requireModifier(Modifier.ABSTRACT).negate())
                .build().validate(reportBuilder);

        LocationValidator.builder()
                .element(annotatedElement)
                .locationHint(reportBuilder.getLocationHint())
                .errorCause(ErrorCause.BAD_ENDPOINT_ANNOTATION_LOCATION)
                .message(getResourceControllerRequiredMessage(annotationName))
                .withPredicates(enclosingHasAnnotation(ResourceController.class))
                .build().validate(reportBuilder);
    }

    private String getResourceControllerRequiredMessage(String annotationName) {
        TypeElement enclosedType = (TypeElement) annotatedElement.getEnclosingElement();
        String enclosingTypeName = enclosedType.getSimpleName().toString();
        return "@" + annotationName + " must be placed in an @ResourceController annotated class. \n" +
                "The actual enclosing type, " + enclosingTypeName + ", is not a resource controller";
    }
}
