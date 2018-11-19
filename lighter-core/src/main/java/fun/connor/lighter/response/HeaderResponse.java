package fun.connor.lighter.response;

import java.util.HashMap;
import java.util.Map;

public class HeaderResponse<T> implements ResponseDecorator<T, T> {

    private String key;
    private String value;

    private HeaderResponse(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static<T> HeaderResponse<T> from(String key, String value) {
        return new HeaderResponse<>(key, value);
    }

    @Override
    public ResponseState<T> apply(ResponseState<T> from) {
        Map<String, String> newHeaders = new HashMap<>(from.getHeaders());
        newHeaders.put(key, value);
        return new ResponseState<>(from.getContent(), from.getStatus(), from.getHeaders());
    }
}
