package fun.connor.lighter.response;

import fun.connor.lighter.http.HttpHeaders;
import fun.connor.lighter.http.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link ResponseDecorator} implementation that sets the response body and
 * sets the content-type to application/json. This transformer changes the
 * type of the response body.
 * @param <T> the new response body type
 */
public class JsonContent<T> implements ResponseDecorator<Void, T> {

    private T content;

    private JsonContent(T content) {
        this.content = content;
    }

    /**
     * Creates a JsonContent for the provided content.
     * @param content the object that should become the request body
     * @param <T> the type of the request body
     * @return a transformer which will add the request body and set the content-type header
     */
    public static <T> JsonContent<T> from(T content) {
        return new JsonContent<>(content);
    }

    @Override
    public ResponseState<T> apply(ResponseState<Void> from) {
        Map<String, String> newHeaders = new HashMap<>(from.getHeaders());
        newHeaders.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        return new ResponseState<>(content, from.getStatus(), newHeaders);
    }
}
