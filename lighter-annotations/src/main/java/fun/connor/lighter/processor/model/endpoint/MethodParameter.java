package fun.connor.lighter.processor.model.endpoint;

import fun.connor.lighter.processor.MoreTypes;

import javax.lang.model.type.TypeMirror;

public class MethodParameter {
    public enum Source {
        PATH, QUERY, BODY, CONTEXT, GUARD
    }

    private String name;
    private TypeMirror type;
    private int index;
    private Source source;

    public MethodParameter(int index, TypeMirror type, String name, Source source) {
        this.index = index;
        this.type = type;
        this.name = name;
        this.source = source;
    }

    public boolean isOptional() {
        return MoreTypes.isTypeOptional(type);
    }

    public TypeMirror getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public Source getSource() {
        return source;
    }

    public boolean isPrimitive() {
        return type.getKind().isPrimitive();
    }
}
