package fun.connor.lighter.response;

import java.util.Map;

public class StatusResponse<T> implements ResponseDecorator<T, T> {

    private int status;

    private StatusResponse(int status) {
        this.status = status;
    }

    public static <T> StatusResponse<T> from(int status) {
        return new StatusResponse<>(status);
    }

    @Override
    public Response<T> apply(Response<T> from) {
        return new Response<T>() {
            @Override
            public T getContent() {
                return from.getContent();
            }

            @Override
            public int getStatus() {
                return status;
            }

            @Override
            public Map<String, String> getHeaders() {
                return from.getHeaders();
            }
        };
    }
}
