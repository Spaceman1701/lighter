package fun.connor.lighter.response;

import fun.connor.lighter.http.HttpHeaders;
import fun.connor.lighter.http.StatusCodes;

/**
 * Utility class for building common HTTP response types.
 */
public final class Responses {

    private Responses() {}


    /**
     * Create status 200 - OK response with the Content-Type set to application/json.
     * @param content the response body
     * @param <T> the type of teh response body
     * @return a new response
     */
    public static <T> Response<T> json(T content) {
        return json(content, StatusCodes.OK);
    }

    /**
     * Create a Content-Type application/json response with a provided status code
     * @param content the response body
     * @param status the response status
     * @param <T> the type of the response body
     * @return a new response
     */
    public static <T> Response<T> json(T content, int status) {
        return Response.create()
                .with(JsonContent.from(content))
                .with(StatusResponse.from(status));
    }

    /**
     * Create a response with no body and status 202 - ACCEPTED.
     * @return a new response
     */
    public static Response<Void> accepted() {
        return Response.create()
                .with(StatusResponse.from(StatusCodes.ACCEPTED));
    }

    /**
     * Create a redirect response to redirect browsers or other clients. This response
     * has no body. If provided status is not a 3xx redirect code, this method throws
     * an {@link IllegalArgumentException}
     * @param status a valid redirect status code
     * @param url the url to redirect to
     * @return the response
     */
    public static Response<Void> redirect(int status, String url) {
        if (!StatusCodes.isRedirect(status)) {
            throw new IllegalArgumentException(status + " is not a valid redirect status code");
        }
        return Response.create()
                .with(HeaderResponse.from(HttpHeaders.LOCATION, url))
                .with(StatusResponse.from(status));
    }

    /**
     * Return a response with no content but with a content type. This is useful when some
     * conditions of an endpoint method return a response with content and some do not.
     * @param status the response status
     * @param <T> the type of the response body
     * @return a new response
     */
    public static <T> Response<T> noContent(int status) {
        return Response.create()
                .with(StatusResponse.from(status))
                .with((responseState) ->
                        new ResponseState<T>(null, responseState.getStatus(), responseState.getHeaders()));
    }
}
