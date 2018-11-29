package fun.connor.lighter.compiler.validators;

import fun.connor.lighter.compiler.model.Route;
import fun.connor.lighter.compiler.validation.ValidationError;
import fun.connor.lighter.compiler.validation.ValidationReport;
import fun.connor.lighter.compiler.validation.cause.ErrorCause;
import fun.connor.lighter.declarative.ResourceController;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.Optional;

import static fun.connor.lighter.compiler.validators.LocationValidator.Predicates.requireConcreteClass;
import static fun.connor.lighter.compiler.validators.LocationValidator.Predicates.requireModifier;

/**
 * {@link AnnotationValidator} implementation which validates {@link ResourceController} annotations.
 */
public class ResourceControllerValidator extends AnnotationValidator<ResourceController> {


    public ResourceControllerValidator(Element annotatedElement, ResourceController annotation) {
        super(annotatedElement, annotation);
    }

    @Override
    public void validate(ValidationReport.Builder reportBuilder) {
        LocationValidator locationValidator = LocationValidator.builder()
                .element(annotatedElement)
                .message("@ResourceController can only be placed on concrete public classes")
                .errorCause(ErrorCause.BAD_RESOURCE_CONTROLLER_LOCATION)
                .withPredicates(requireModifier(Modifier.PUBLIC), requireConcreteClass)
                .build();

        locationValidator.validate(reportBuilder);

        String path = annotation.value();
        if (path.isEmpty()) {
            reportBuilder.addError(new ValidationError(getEmptyPathMessage(), ErrorCause.BAD_RESOURCE_CONTROLLER_PATH));
            return;
        }
        Optional<String> routeSyntaxError = Route.checkRouteTemplateSyntax(path);
        routeSyntaxError.ifPresent(s ->
                reportBuilder.addError(new ValidationError(s, ErrorCause.BAD_RESOURCE_CONTROLLER_PATH)));
    }

    private String getEmptyPathMessage() {
        return "@ResourceController cannot have an empty path fragment. Every resource controller should" +
                " have a unique path";
    }
}
