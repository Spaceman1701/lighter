package fun.connor.lighter;

import fun.connor.lighter.handler.Request;
import fun.connor.lighter.response.Response;

/**
 * Functional interface for defining transformation that should be made to every response before being
 * transmitted.
 * <p>
 *     Implementations of this interface can be attached to {@link fun.connor.lighter.Lighter} instances at
 *     configuration time. Before Lighter writes any request data to an output stream, it will invoke any
 *     GlobalRequestTransformers. The request that is actually written will be the one returned from invoking
 *     the chain of transformers.
 * </p>
 * <p>
 *     Transformers are not called in a guaranteed order.
 * </p>
 */
@FunctionalInterface
public interface GlobalRequestTransformer {
    /**
     * Apply a transformation to a response to the given request.
     * @param request the request the response is intended to serve
     * @param response the current response
     * @return A new response with the transformation applied
     */
    Response<?> apply(Request request, Response<?> response);
}
