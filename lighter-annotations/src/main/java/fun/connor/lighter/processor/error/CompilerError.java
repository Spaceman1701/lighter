package fun.connor.lighter.processor.error;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class CompilerError {

    private String detail;
    private Element target;
    private String annotationName;

    public CompilerError(Element target, String detail, String annotationName) {
        this.detail = detail;
        if (detail == null) {
            this.detail = "unknown error";
        }
        this.target = target;
        this.annotationName = annotationName;
    }

    private String getDetailedElementName(Element e) {
        switch (target.getKind()) {
            case CLASS:
                return makeClassName(e);
            case METHOD:
                return makeMethodName(e);
            case PARAMETER:
                return makeParameterName(e);
            default:
                return makeOtherName(e);
        }
    }

    public String getDetailedTargetName() {
        return getDetailedElementName(target);
    }

    private String makeOtherName(Element e) {
        return e.getSimpleName().toString(); //TODO: improve this
    }

    private String makeParameterName(Element e) {
        String name = e.getSimpleName().toString();
        String methodName = makeMethodName(e.getEnclosingElement());
        return "parameter " + name + " on method " + methodName;
    }

    private String makeMethodName(Element e) {
        String name = e.getSimpleName().toString();
        String className = makeClassName(e.getEnclosingElement());
        return className + "::" + name;
    }

    private String makeClassName(Element e) {
        TypeElement type = (TypeElement) e;
        return type.getQualifiedName().toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other.getClass() == this.getClass()) {
            CompilerError otherError = (CompilerError) other;
            return otherError.getDetailedTargetName().equals(getDetailedTargetName())
                    && otherError.detail.equals(detail);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getDetailedTargetName().hashCode() + detail.hashCode();
    }

    @Override
    public String toString() {
        return "At " + getDetailedTargetName() +
                " (annotated with" + annotationName + "): " + detail;
    }
}
