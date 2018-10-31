package fun.connor.lighter.processor.model;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class ModelUtils {

    private ModelUtils() {};

    public static boolean typeMirrorEqualsType(TypeMirror typeMirror, Class type) {
        if (typeMirror.getKind() == TypeKind.DECLARED && !type.isPrimitive()) {
            DeclaredType declaredType = (DeclaredType) typeMirror;
            TypeElement element = (TypeElement) declaredType.asElement();
            return (element.getQualifiedName().toString().equals(type.getCanonicalName()));
        } else if (typeMirror.getKind().isPrimitive() && type.isPrimitive()) {
            return typeMirror.getKind().toString().equals(type.getSimpleName().toUpperCase());
        }
        return false;
    }

    public static TypeMirror extractOptionalType(DeclaredType optionalType) {
        return optionalType.getTypeArguments().get(0);
    }
}
