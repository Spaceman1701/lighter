package fun.connor.lighter.handler;

public class RequestContext<T, K> {
    private final ResponseBuilder<K> responseBuilder;
    private final Request<T> request;

    public RequestContext(Request<T> request) {
        this.request = request;
        responseBuilder = new ResponseBuilder<>();
    }

    public ResponseBuilder<K> getResponseBuilder() {
        return responseBuilder;
    }

    public Request<T> getRequest() {
        return request;
    }

    public T getRequestBody() {
        return request.getBody();
    }
}
