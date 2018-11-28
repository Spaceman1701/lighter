package fun.connor.lighter.handler;

import fun.connor.lighter.adapter.TypeAdapterFactory;

import java.util.Map;

/**
 * Factory for constructing {@link RequestGuard} instances from HTTP requests. See
 * {@link RequestGuard} for details about this API.
 * @param <T> the {@link RequestGuard} type that this factory produces.
 */
public interface RequestGuardFactory<T extends RequestGuard> {
    /**
     * Provide a {@link RequestGuard} instance based on the request information. The logic
     * of implementations of this method should be used to provide prerequisites
     * @param pathParams the raw named path parameter bindings
     * @param queryParams the raw named query parameter bindings
     * @param request the {@link Request} representing this request
     * @param factory the top level {@link TypeAdapterFactory} for the application
     * @return a new instance of the {@link RequestGuard} produced by this factory. Error cases
     * can return {@code null} in error cases. This API is subject to change.
     */
    T newInstance(Map<String, String> pathParams, Map<String, String> queryParams, Request request,
                  TypeAdapterFactory factory);
}
