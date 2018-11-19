package fun.connor.lighter.response;

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
    public Response<T> apply(Response<Void> from) {
        return new Response<T>() {
            @Override
            public T getContent() {
                return content;
            }

            @Override
            public int getStatus() {
                return from.getStatus();
            }

            @Override
            public Map<String, String> getHeaders() {
                return from.getHeaders();
            }
        };
    }
}
