package fun.connor.lighter.processor.validators;

import fun.connor.lighter.declarative.Delete;
import fun.connor.lighter.declarative.Get;
import fun.connor.lighter.declarative.Post;
import fun.connor.lighter.declarative.Put;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public class AnnotationValidationUtils {

    private static final Class[] ENDPOINT_ANNOTATIONS =
            {Get.class, Delete.class, Post.class, Put.class};

    private AnnotationValidationUtils() {
    }

    @SuppressWarnings("unchecked") //TODO: see other ENDPOINT_ANNOTATIONS notes
    static boolean hasEndpointAnnotation(Element element) {
        for (Class a : ENDPOINT_ANNOTATIONS) {
            Class<? extends Annotation> annotationClazz = a; //to make unchecked cast more obvious
            if (element.getAnnotation(annotationClazz) != null) {
                return true;
            }
        }
        return false;
    }
}
