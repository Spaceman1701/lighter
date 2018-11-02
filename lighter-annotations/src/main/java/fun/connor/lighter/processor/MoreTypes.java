package fun.connor.lighter.processor;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class MoreTypes {
    public static boolean isTypeMirrorOfClass(TypeMirror typeMirror, Class clazz) {
        if (typeMirror.getKind() == TypeKind.DECLARED && !clazz.isPrimitive()) {
            DeclaredType declaredType = (DeclaredType) typeMirror;
            TypeElement element = (TypeElement) declaredType.asElement();
            return (element.getQualifiedName().toString().equals(clazz.getCanonicalName()));
        } else if (typeMirror.getKind().isPrimitive() && clazz.isPrimitive()) {
            return typeMirror.getKind().toString().equals(clazz.getSimpleName().toUpperCase());
        }
        return false;
    }

    public static boolean isTypeOptional(TypeMirror type) {
        return isTypeMirrorOfClass(type, Optional.class)
                || isTypeMirrorOfClass(type, OptionalInt.class)
                || isTypeMirrorOfClass(type, OptionalDouble.class)
                || isTypeMirrorOfClass(type, OptionalLong.class);
    }
}
