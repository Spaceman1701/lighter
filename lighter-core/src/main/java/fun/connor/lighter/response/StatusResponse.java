package fun.connor.lighter.response;


/**
 * {@link ResponseDecorator} implementation that sets the response status. This method
 * does not change the response body content.
 * @param <T> the response body content.
 */
public class StatusResponse<T> implements ResponseDecorator<T, T> {

    private int status;

    private StatusResponse(int status) {
        this.status = status;
    }

    /**
     * Create a StatusResponse that sets the response status to the provided value
     * @param status the status
     * @param <T> the type of the response body content
     * @return A transformation that sets the response status.
     */
    public static <T> StatusResponse<T> from(int status) {
        return new StatusResponse<>(status);
    }

    @Override
    public ResponseState<T> apply(ResponseState<T> from) {
        return new ResponseState<>(from.getContent(), status, from.getHeaders());
    }
}
