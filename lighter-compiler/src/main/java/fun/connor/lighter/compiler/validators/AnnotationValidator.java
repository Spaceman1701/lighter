package fun.connor.lighter.compiler.validators;

import fun.connor.lighter.compiler.validation.Validatable;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public abstract class AnnotationValidator<T extends Annotation> implements Validatable {

    protected Element annotatedElement;
    protected T annotation;

    public AnnotationValidator(Element annotatedElement, T annotation) {
        this.annotatedElement = annotatedElement;
        this.annotation = annotation;
    }


}
