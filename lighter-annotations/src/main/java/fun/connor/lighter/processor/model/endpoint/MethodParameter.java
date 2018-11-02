package fun.connor.lighter.processor.model.endpoint;

import fun.connor.lighter.processor.model.ModelUtils;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;

public class MethodParameter {
    private String name;
    private TypeMirror type;
    private int index;

    public MethodParameter(int index, TypeMirror type, String name) {
        this.index = index;
        this.type = type;
        this.name = name;
    }


    public TypeMirror getType() {
        if (isOptional()) {
            return ModelUtils.extractOptionalType((DeclaredType) type);
        } else {
            return type;
        }
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public boolean isOptional() {
        return ModelUtils.typeMirrorEqualsType(type, Optional.class);
    }

    public boolean isPrimitive() {
        return type.getKind().isPrimitive();
    }
}
