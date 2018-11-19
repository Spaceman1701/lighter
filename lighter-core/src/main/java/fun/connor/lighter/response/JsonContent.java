package fun.connor.lighter.response;

import java.util.HashMap;
import java.util.Map;

public class JsonContent<T> implements ResponseDecorator<Void, T> {

    private T content;

    private JsonContent(T content) {
        this.content = content;
    }

    public static <T> JsonContent<T> from(T content) {
        return new JsonContent<>(content);
    }

    @Override
    public ResponseState<T> apply(ResponseState<Void> from) {
        Map<String, String> newHeaders = new HashMap<>(from.getHeaders());
        newHeaders.put("content-type", "application/json");
        return new ResponseState<>(content, from.getStatus(), newHeaders);
    }
}