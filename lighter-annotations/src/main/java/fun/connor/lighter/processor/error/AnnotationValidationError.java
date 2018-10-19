package fun.connor.lighter.processor.error;

import javax.lang.model.element.Element;

public class AnnotationValidationError extends AbstractCompilerError {

    private String detail;
    private Element target;
    private String annotationName;

    public AnnotationValidationError(Element target, String detail, String annotationName) {
        this.detail = detail;
        if (detail == null) {
            this.detail = "unknown error";
        }
        this.target = target;
        this.annotationName = annotationName;
    }



    @Override
    public String toString() {
        return "At " + getDetailedTargetName() +
                " (annotated with" + annotationName + "): " + detail;
    }

    @Override
    public Element getTarget() {
        return target;
    }

    @Override
    public String getDetail() {
        return detail;
    }
}
