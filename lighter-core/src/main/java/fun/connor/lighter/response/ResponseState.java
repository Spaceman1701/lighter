package fun.connor.lighter.response;

import java.util.Map;

/**
 * State implementation for the response. This is a very simple
 * state pattern used to enable {@link Response} instances to
 * reconstruct themselves.
 * @param <T> the type of the response content
 */
public class ResponseState<T> {
    private final Map<String, String> headers;
    private final int status;
    private final T content;

    public ResponseState(T content, int status, Map<String, String> headers) {
        this.headers = headers;
        this.content = content;
        this.status = status;
    }

    /**
     * @return get the response status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the response body content
     */
    public T getContent() {
        return content;
    }

    /**
     * @return the response headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }
}
