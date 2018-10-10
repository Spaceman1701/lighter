package fun.connor.lighter.handler;

public class RequestContext<T> {
    private final ResponseBuilder<T> responseBuilder;
    private final Request request;

    public RequestContext(Request request) {
        this.request = request;
        responseBuilder = new ResponseBuilder<>();
    }

    public ResponseBuilder<T> getResponseBuilder() {
        return responseBuilder;
    }

    public Request getRequest() {
        return request;
    }
}
