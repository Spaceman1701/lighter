package fun.connor.lighter.compiler.model;

import javax.lang.model.type.DeclaredType;

/**
 * Represents a {@link RequestGuardFactory} implementation in the application.
 */
public class RequestGuardFactory {

    private final DeclaredType producedType;
    private final DeclaredType type;

    public RequestGuardFactory(DeclaredType type, DeclaredType produces) {
        this.producedType = produces;
        this.type = type;
    }

    public DeclaredType getType() {
        return type;
    }

    public DeclaredType getProduces() {
        return producedType;
    }
}
