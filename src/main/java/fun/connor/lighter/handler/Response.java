package fun.connor.lighter.handler;

import java.util.Map;

public class Response<T> {
    private final int status;
    private final Map<String, String> customHeaders;
    private final T content;

    public Response(final int status, final Map<String, String> customHeaders, final T content) {
        this.status = status;
        this.customHeaders = customHeaders;
        this.content = content;
    }

    public Response(final int status) {
        this(status, null, null);
    }

    public Response(final int status, final T content) {
        this(status, null, content);
    }

    public boolean hasContent() {
        return content != null;
    }

    public boolean hasCustomHeaders() {
        return customHeaders != null;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getCustomHeaders() {
        return customHeaders;
    }

    public T getContent() {
        return content;
    }
}
