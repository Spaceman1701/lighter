package fun.connor.lighter.handler;

import java.util.HashMap;

public class ResponseBuilder<T> {
    private HashMap<String, String> headers;
    private int status;
    private T content;

    public ResponseBuilder() {
        headers = new HashMap<>();
    }

    public ResponseBuilder<T> putHeader(final String header, final String value) {
        headers.put(header, value);
        return this;
    }

    public ResponseBuilder<T> status(final int status) {
        this.status = status;
        return this;
    }

    public ResponseBuilder<T> content(final T content) {
        this.content = content;
        return this;
    }

    public Response<T> build() {
        return new Response<>(status, headers, content);
    }
}
