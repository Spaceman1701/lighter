package fun.connor.lighter.compiler.validation;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.util.Optional;

public class LocationHint {

    private Element element;
    private AnnotationMirror annotationMirror;


    public LocationHint(Element element, AnnotationMirror annotationMirror) {
        this.element = element;
        this.annotationMirror = annotationMirror;
    }

    public LocationHint(Element element) {
        this(element, null);
    }


    Optional<AnnotationMirror> getAnnotatioMirror() {
        return Optional.ofNullable(annotationMirror);
    }

    Element getElement() {
        return element;
    }
}
