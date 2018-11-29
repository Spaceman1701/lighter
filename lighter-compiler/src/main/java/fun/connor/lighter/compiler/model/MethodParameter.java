package fun.connor.lighter.compiler.model;

import fun.connor.lighter.compiler.MoreTypes;

import javax.lang.model.type.TypeMirror;

/**
 * A single parameter on an {@link Endpoint} handler method. Every parameter has a source where the
 * parameters runtime data is generated. Parameters are typed, have a name, and have a specific order
 * on the method they belong to.
 */
public class MethodParameter {
    /**
     * The kind of source that should provide the data that is bound to a
     * method parameter at runtime
     */
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
