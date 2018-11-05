package fun.connor.lighter.processor.model;

import fun.connor.lighter.handler.RequestGuard;

import javax.lang.model.type.DeclaredType;

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
