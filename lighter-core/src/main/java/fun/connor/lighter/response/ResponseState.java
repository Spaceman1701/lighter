package fun.connor.lighter.response;

import java.util.Map;

public class ResponseState<T> {
    private final Map<String, String> headers;
    private final int status;
    private final T content;

    public ResponseState(T content, int status, Map<String, String> headers) {
        this.headers = headers;
        this.content = content;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public T getContent() {
        return content;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
