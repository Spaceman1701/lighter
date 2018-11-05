package fun.connor.lighter.processor.model;

import javax.lang.model.type.DeclaredType;
import java.util.Map;

public class RequestGuards {
    private Map<DeclaredType, RequestGuardFactory> requestGuards;

    public RequestGuards(Map<DeclaredType, RequestGuardFactory> requestGuards) {
        this.requestGuards = requestGuards;
    }

    public RequestGuardFactory getRequestGuard(DeclaredType toProduce) {
        return requestGuards.get(toProduce);
    }
}
