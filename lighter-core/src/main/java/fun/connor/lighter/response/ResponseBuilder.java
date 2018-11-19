package fun.connor.lighter.response;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder<T> {

    private Response<T> response;

    private ResponseBuilder(Response<T> response) {
        this.response = response;
    }

    public <R> ResponseBuilder<R> with(ResponseDecorator<T, R> function) {
        return new ResponseBuilder<>(function.apply(response));
    }

    public Response<T> build() {
        return response;
    }

    public static ResponseBuilder<Void> create() {
        return new ResponseBuilder<>(new Response<Void>() {
            @Override
            public Void getContent() {
                return null;
            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public Map<String, String> getHeaders() {
                return new HashMap<>();
            }
        });
    }
}
