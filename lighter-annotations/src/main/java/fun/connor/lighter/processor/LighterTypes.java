package fun.connor.lighter.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.*;

import static fun.connor.lighter.processor.MoreTypes.isTypeMirrorOfClass;

public class LighterTypes implements Types {

    private Types baseTypes;
    private Elements elements;

    public LighterTypes(Types base, Elements elements) {
        this.baseTypes = base;
        this.elements = elements;
    }

    @Override
    public Element asElement(TypeMirror t) {
        return baseTypes.asElement(t);
    }

    @Override
    public boolean isSameType(TypeMirror t1, TypeMirror t2) {
        return baseTypes.isSameType(t1, t2);
    }

    @Override
    public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
        return baseTypes.isSubtype(t1, t2);
    }

    @Override
    public boolean isAssignable(TypeMirror t1, TypeMirror t2) {
        return baseTypes.isAssignable(t1, t2);
    }

    @Override
    public boolean contains(TypeMirror t1, TypeMirror t2) {
        return baseTypes.contains(t1, t2);
    }

    @Override
    public boolean isSubsignature(ExecutableType m1, ExecutableType m2) {
        return baseTypes.isSubsignature(m1, m2);
    }

    @Override
    public List<? extends TypeMirror> directSupertypes(TypeMirror t) {
        return baseTypes.directSupertypes(t);
    }

    @Override
    public TypeMirror erasure(TypeMirror t) {
        return baseTypes.erasure(t);
    }

    @Override
    public TypeElement boxedClass(PrimitiveType p) {
        return baseTypes.boxedClass(p);
    }

    @Override
    public PrimitiveType unboxedType(TypeMirror t) {
        return baseTypes.unboxedType(t);
    }

    @Override
    public TypeMirror capture(TypeMirror t) {
        return baseTypes.capture(t);
    }

    @Override
    public PrimitiveType getPrimitiveType(TypeKind kind) {
        return baseTypes.getPrimitiveType(kind);
    }

    @Override
    public NullType getNullType() {
        return baseTypes.getNullType();
    }

    @Override
    public NoType getNoType(TypeKind kind) {
        return baseTypes.getNoType(kind);
    }

    @Override
    public ArrayType getArrayType(TypeMirror componentType) {
        return baseTypes.getArrayType(componentType);
    }

    @Override
    public WildcardType getWildcardType(TypeMirror extendsBound, TypeMirror superBound) {
        return baseTypes.getWildcardType(extendsBound, superBound);
    }

    @Override
    public DeclaredType getDeclaredType(TypeElement typeElem, TypeMirror... typeArgs) {
        return baseTypes.getDeclaredType(typeElem, typeArgs);
    }

    @Override
    public DeclaredType getDeclaredType(DeclaredType containing, TypeElement typeElem, TypeMirror... typeArgs) {
        return baseTypes.getDeclaredType(containing, typeElem, typeArgs);
    }

    @Override
    public TypeMirror asMemberOf(DeclaredType containing, Element element) {
        return baseTypes.asMemberOf(containing, element);
    }

    public TypeMirror mirrorOfClass(Class clazz) {
        return elements.getTypeElement(clazz.getCanonicalName()).asType();
    }

    public DeclaredType mirrorOfParameterizedClass(Class clazz, DeclaredType... typeArgs) {
        DeclaredType clazzDeclared = (DeclaredType) mirrorOfClass(clazz);
        TypeElement clazzElement = (TypeElement) clazzDeclared.asElement();

        return getDeclaredType(clazzElement, typeArgs);
    }

    public DeclaredType mirrorOfParameterizedClass(Class clazz, Class... typeArgs) {
        DeclaredType[] declaredTypeArgs = Arrays.stream(typeArgs)
                .map(c -> (DeclaredType)mirrorOfClass(c))
                .map(this::erasure)
                .toArray(DeclaredType[]::new);

        return mirrorOfParameterizedClass(clazz, declaredTypeArgs);
    }

    public TypeMirror extractOptionalType(DeclaredType optional) {
        if (isTypeMirrorOfClass(optional, Optional.class)) {
            List<? extends TypeMirror> typeParams = optional.getTypeArguments();
            if (!typeParams.isEmpty()) {
                return typeParams.get(0);
            } else {
                return elements.getTypeElement(Object.class.getCanonicalName()).asType();
            }
        } else {
            return extractPrimitiveOptionalType(optional);
        }
    }

    private TypeMirror extractPrimitiveOptionalType(DeclaredType optional) {
        if (isTypeMirrorOfClass(optional, OptionalInt.class)) {
            return getPrimitiveType(TypeKind.INT);
        } else if (isTypeMirrorOfClass(optional, OptionalLong.class)) {
            return getPrimitiveType(TypeKind.LONG);
        } else if (isTypeMirrorOfClass(optional, OptionalDouble.class)) {
            return getPrimitiveType(TypeKind.DOUBLE);
        }
        throw new IllegalArgumentException(optional.asElement().getSimpleName() + " was not an Optional type");
    }

    public boolean collectionContains(Collection<TypeMirror> collection, TypeMirror t) {
        for (TypeMirror mirror : collection) {
            if (isSameType(mirror, t)) {
                return true;
            }
        }
        return false;
    }
}
