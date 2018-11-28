package fun.connor.lighter.handler;

import fun.connor.lighter.response.Response;

import java.util.Map;


/**
 * Functional interface for resolving HTTP requests. Instances of this class represent the
 * terminal point of any request handled by Lighter. After a request is resolved by a
 * LighterRequestResolver, Lighter will immediately begin handling the response. The request
 * will never be forwarded to any further resolvers. Implementations of this interface should
 * be backend agnostic.
 * <p>
 *     Most applications will not directly interact with this interface as the lighter-compiler will
 *     generate implementations of it for the application.
 * </p>
 */
@FunctionalInterface
public interface LighterRequestResolver {
    /**
     * Generate a response for the given request. This method is called no more than once on exactly one
     * instance for every request. The only reason that resolve would not be called on a request is if
     * a critical error occurred that prevented any response from being generated.
     * @param pathParams the raw named bindings for request path parameters
     * @param queryParams the raw named bindings for request query parameters
     * @param request the {@link Request} object representing the request to be resolved
     * @return a response to the given request
     */
    Response<?> resolve(Map<String, String> pathParams, Map<String, String> queryParams, Request request);

    /**
     * Checks if this resolver requires the request body be read from the input stream.
     * This method is currently unused. However, may be used for future optimizations.
     * @return {@code true} iff the {@link LighterRequestResolver#resolve(Map, Map, Request)} method
     * will require reading the request body from the input stream
     */
    default boolean requiresBody() {
        return false;
    }
}
