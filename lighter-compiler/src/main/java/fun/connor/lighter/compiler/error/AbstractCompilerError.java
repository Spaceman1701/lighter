package fun.connor.lighter.compiler.error;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public abstract class AbstractCompilerError {

    protected String getDetailedElementName(Element e) {
        switch (e.getKind()) {
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
        return getDetailedElementName(getTarget());
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
    public abstract String toString(); //force subclasses to implement

    public abstract Element getTarget();

    public abstract String getDetail();

    @Override
    public boolean equals(Object other) {
        if (other != null && other.getClass() == this.getClass()) {
            AbstractCompilerError otherError = (AbstractCompilerError) other;
            return otherError.getDetailedTargetName().equals(getDetailedTargetName())
                    && otherError.getDetail().equals(getDetail());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getDetailedTargetName().hashCode() + getDetail().hashCode();
    }
}
