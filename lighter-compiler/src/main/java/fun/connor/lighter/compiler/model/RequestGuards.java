package fun.connor.lighter.compiler.model;

import javax.lang.model.type.DeclaredType;
import java.util.Collection;
import java.util.Map;

/**
 * Top level Request Guard model class which represents the mapping between
 * {@link fun.connor.lighter.handler.RequestGuard}s and {@link fun.connor.lighter.handler.RequestGuardFactory}s.
 */
public class RequestGuards {
    private Map<DeclaredType, RequestGuardFactory> requestGuards;

    public RequestGuards(Map<DeclaredType, RequestGuardFactory> requestGuards) {
        this.requestGuards = requestGuards;
    }

    public RequestGuardFactory getRequestGuard(DeclaredType toProduce) {
        RequestGuardFactory result = requestGuards.get(toProduce);
        if (result == null) {
            throw new IllegalArgumentException("Could not find request guard for type: " + toProduce.asElement().getSimpleName().toString());
        }
        return result;
    }

    public Collection<RequestGuardFactory> getAll() {
        return requestGuards.values();
    }
}
