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
    public ResponseState<T> apply(ResponseState<T> from) {
        return new ResponseState<>(from.getContent(), status, from.getHeaders());
    }
}
