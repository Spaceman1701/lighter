package fun.connor.lighter.response;

import fun.connor.lighter.http.HttpHeaders;
import fun.connor.lighter.http.StatusCodes;

public final class Responses {

    private Responses() {}


    public static <T> Response<T> json(T content) {
        return json(content, StatusCodes.OK);
    }

    public static <T> Response<T> json(T content, int status) {
        return Response.create()
                .with(JsonContent.from(content))
                .with(StatusResponse.from(status));
    }

    public static Response<Void> accepted() {
        return Response.create()
                .with(StatusResponse.from(StatusCodes.ACCEPTED));
    }

    public static Response<Void> redirect(int status, String url) {
        if (!StatusCodes.isRedirect(status)) {
            throw new IllegalArgumentException(status + " is not a valid redirect status code");
        }
        return Response.create()
                .with(HeaderResponse.from(HttpHeaders.LOCATION, url))
                .with(StatusResponse.from(status));
    }

    public static <T> Response<T> noContent(int status) {
        return Response.create()
                .with(StatusResponse.from(status))
                .with((responseState) ->
                        new ResponseState<T>(null, responseState.getStatus(), responseState.getHeaders()));
    }
}
