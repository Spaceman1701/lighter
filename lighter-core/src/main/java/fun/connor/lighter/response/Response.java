package fun.connor.lighter.response;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP Response. All Lighter endpoint methods must return an instance of
 * this class. This class is immutable.
 * @param <T> The type of the response body. If the response has no body, this should be {@link Void}
 */
public class Response<T> {

    private final T content;
    private final int status;
    private final Map<String, String> headers;

    private Response(ResponseState<T> state) {
        this.content = state.getContent();
        this.status = state.getStatus();
        this.headers = state.getHeaders();
    }

    /**
     * Create a new response with no headers and no response body. The response status code will
     * be set to 200 - OK.
     * @return the new response
     */
    public static Response<Void> create() {
        return new Response<>(new ResponseState<>(null, 200, new HashMap<>()));
    }

    /**
     * Apply a decorator transformation to this response. The response decorator applies some
     * transformation to the response. The result of the transformation is returned. Examples of this are
     * changing the status, adding a header, or adding a response body
     * <p>
     *     This is intended to be used a fluent method.
     * </p>
     * @param transformer The transformation to apply
     * @param <R> the type of the response body after the transformation is applied
     * @return a response with the applied transformation
     */
    public <R> Response<R> with(ResponseDecorator<T, R> transformer) {
        return new Response<>(transformer.apply(new ResponseState<>(content, status, headers)));
    }

    /**
     * @return @[code true} iff the response has a body
     */
    public boolean hasContent() {
        return content != null;
    }

    /**
     * @return the response body or {@code null} if it does not have one
     */
    public T getContent() {
        return content;
    }

    /**
     * @return the HTTP status code of this response.
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return this response's HTTP headers.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }
}
