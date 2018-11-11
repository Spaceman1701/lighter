package fun.connor.lighter.compiler.validators;

import fun.connor.lighter.compiler.validation.Validatable;
import fun.connor.lighter.compiler.validation.ValidationReport;
import fun.connor.lighter.declarative.ResourceController;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import static fun.connor.lighter.compiler.validators.LocationValidator.Predicates.*;

public class ResourceControllerValidator extends AnnotationValidator<ResourceController> {


    public ResourceControllerValidator(Element annotatedElement, ResourceController annotation) {
        super(annotatedElement, annotation);
    }

    @Override
    public void validate(ValidationReport.Builder reportBuilder) {
        LocationValidator locationValidator = LocationValidator.builder()
                .element(annotatedElement)
                .message("@ResourceController can only be placed on concrete public classes")
                .withPredicates(requireModifier(Modifier.PUBLIC), requireConcreteClass)
                .build();

        locationValidator.validate(reportBuilder);

        //TODO: route syntax checking
    }
}
