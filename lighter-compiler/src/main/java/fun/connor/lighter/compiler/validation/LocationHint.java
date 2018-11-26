package fun.connor.lighter.compiler.validation;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.util.Optional;

/**
 * A location hint used for mapping messages to location in Java source code. Location hints
 * have two levels of specificity: Element or Element and Annotation.
 */
public class LocationHint {

    private Element element;
    private AnnotationMirror annotationMirror;


    /**
     * Construct a location hint which points to an annotation
     * @param element the annotated element
     * @param annotationMirror the annotation
     */
    public LocationHint(Element element, AnnotationMirror annotationMirror) {
        this.element = element;
        this.annotationMirror = annotationMirror;
    }

    /**
     * Construct a location hint which points to an {@link Element}
     * @param element the element
     */
    public LocationHint(Element element) {
        this(element, null);
    }


    /**
     * @return either the annotation pointed to by this hint or empty if
     * this error does not point to an annotation
     */
    Optional<AnnotationMirror> getAnnotatioMirror() {
        return Optional.ofNullable(annotationMirror);
    }

    /**
     * @return the element pointed to by this hint
     */
    Element getElement() {
        return element;
    }
}
