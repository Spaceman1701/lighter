package fun.connor.lighter.response;

import java.util.HashMap;
import java.util.Map;

public class Response<T> {

    private T content;
    private int status;
    private Map<String, String> headers;

    private Response(ResponseState<T> state) {
        this.content = state.getContent();
        this.status = state.getStatus();
        this.headers = state.getHeaders();
    }

    public static Response<Void> create() {
        return new Response<>(new ResponseState<>(null, 200, new HashMap<>()));
    }

    public <R> Response<R> with(ResponseDecorator<T, R> transformer) {
        return new Response<>(transformer.apply(new ResponseState<>(content, status, headers)));
    }

    public boolean hasContent() {
        return content != null;
    }

    public T getContent() {
        return content;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
