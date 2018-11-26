package fun.connor.lighter.compiler;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Simple, static compile time type utilities.
 */
public class MoreTypes {

    private MoreTypes() {}

    /**
     * Checks if a {@link TypeMirror} represents the same type as a {@link Class}
     * @param typeMirror the type mirror
     * @param clazz the compiled class
     * @return <code>true</code> iff the arguments represent the same type
     */
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

    /**
     * Checks if a {@link TypeMirror} represents any of the {@link java.util.Optional} types. (Including
     * {@link OptionalInt}, {@link OptionalDouble}, {@link OptionalLong})
     * @param type the type to check
     * @return <code>true</code> iff the argument represents an optional type
     */
    public static boolean isTypeOptional(TypeMirror type) {
        return isTypeMirrorOfClass(type, Optional.class)
                || isTypeMirrorOfClass(type, OptionalInt.class)
                || isTypeMirrorOfClass(type, OptionalDouble.class)
                || isTypeMirrorOfClass(type, OptionalLong.class);
    }
}
