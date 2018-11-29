package fun.connor.lighter.compiler.validators;

import fun.connor.lighter.compiler.validation.Validatable;
import fun.connor.lighter.compiler.validation.ValidationError;
import fun.connor.lighter.compiler.validation.ValidationReport;
import fun.connor.lighter.compiler.validation.cause.ErrorCause;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * External validator for annotations that ensures they are placed in a legal location. Uses a
 * set of location predicates to validate the location of the annotated element.
 */
public class LocationValidator implements Validatable {

    private final List<Predicate<Element>> conditions;
    private final Element element;
    private final String message;
    private final ErrorCause errorCause;

    private LocationValidator(Builder builder) {
        this.conditions = builder.conditions;
        this.element = builder.element;
        this.message = builder.message;
        this.errorCause = builder.errorCause;
    }

    @Override
    public void validate(ValidationReport.Builder reportBuilder) {
        for (Predicate<Element> predicate : conditions) {
            if (!predicate.test(element)) {
                reportBuilder.addError(new ValidationError(message, errorCause));
                return;
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }



    public static class Builder {
        private List<Predicate<Element>> conditions;
        private Element element;
        private String message;
        private ErrorCause errorCause;

        private Builder() {
            conditions = new ArrayList<>();
        }

        public final Builder element(Element e) {
            this.element = e;
            return this;
        }

        public final Builder message(String message) {
            this.message = message;
            return this;
        }

        @SafeVarargs
        public final Builder withPredicates(Predicate<Element>... predicates) {
            conditions.addAll(Arrays.asList(predicates));
            return this;
        }

        public final Builder errorCause(ErrorCause cause) {
            this.errorCause = cause;
            return this;
        }

        public LocationValidator build() {
            return new LocationValidator(this);
        }
    }

    static final class Predicates {
        private Predicates() {}

        static final Predicate<Element> requireClass = e -> e.getKind() == ElementKind.CLASS;

        static final Predicate<Element> requireConcreteClass = requireClass.and(e -> {
            TypeElement type = (TypeElement) e;
            return !type.getModifiers().contains(Modifier.ABSTRACT);
        });

        static Predicate<Element> requireModifier(Modifier m) {
            return e -> e.getModifiers().contains(m);
        }

        static Predicate<Element> enclosingHasAnnotation(Class<? extends Annotation> annotation) {
            return element -> {
                Element enclosing = element.getEnclosingElement();
                return (enclosing != null) && (enclosing.getAnnotation(annotation) != null);
            };
        }
    }
}
