package fun.connor.lighter.handler;

import java.util.HashMap;
import java.util.Map;

public class Response<T> {
    private final int status;
    private final Map<String, String> customHeaders;
    private final T content;

    Response(final int status, final Map<String, String> customHeaders, final T content) {
        this.status = status;
        this.customHeaders = customHeaders;
        this.content = content;
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

    public static <S> Builder<S> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private int status;
        private Map<String, String> customHeaders;
        private T content;

        private Builder() {
            customHeaders = new HashMap<>();
        }

        public Builder<T> status(int status) {
            this.status = status;
            return this;
        }

        public Builder<T> putHeader(String header, String content) {
            customHeaders.put(header, content);
            return this;
        }

        public Builder<T> content(T content) {
            this.content = content;
            return this;
        }

        public Response<T> build() {
            return new Response<>(status, customHeaders, content);
        }
    }
}
