package fun.connor.lighter.global;

import fun.connor.lighter.handler.Request;
import fun.connor.lighter.response.Response;

@FunctionalInterface
public interface GlobalRequestTransformer {
    Response<?> apply(Request request, Response<?> response);
}
