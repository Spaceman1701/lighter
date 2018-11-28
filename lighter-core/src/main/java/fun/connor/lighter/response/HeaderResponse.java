package fun.connor.lighter.response;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link ResponseDecorator} for adding headers to response. Does not change
 * the response.
 * @param <T>
 */
public class HeaderResponse<T> implements ResponseDecorator<T, T> {

    private String key;
    private String value;

    private HeaderResponse(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Create a HeaderResponse with the provided name and value
     * @param key the header name
     * @param value the header value
     * @param <T> the type of the response body
     * @return a response transformer that adds the header to the response
     */
    public static<T> HeaderResponse<T> from(String key, String value) {
        return new HeaderResponse<>(key, value);
    }

    @Override
    public ResponseState<T> apply(ResponseState<T> from) {
        Map<String, String> newHeaders = new HashMap<>(from.getHeaders());
        newHeaders.put(key, value);
        return new ResponseState<>(from.getContent(), from.getStatus(), newHeaders);
    }
}
