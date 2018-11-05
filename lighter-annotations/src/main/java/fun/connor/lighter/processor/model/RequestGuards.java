package fun.connor.lighter.processor.model;

import java.util.HashMap;
import java.util.Map;

public class RequestGuards {
    private Map<Class, RequestGuardFactory> requestGuards;

    public RequestGuards(Map<Class, RequestGuardFactory> requestGuards) {
        this.requestGuards = requestGuards;
    }

    public RequestGuardFactory getRequestGuard(Class toProduce) {
        return requestGuards.get(toProduce);
    }
}
