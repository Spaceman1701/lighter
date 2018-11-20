package fun.connor.lighter.response;

public final class Responses {

    private Responses() {}


    public static <T> Response<T> json(T content) {
        return json(content, 200);
    }

    public static <T> Response<T> json(T content, int status) {
        return Response.create()
                .with(JsonContent.from(content))
                .with(StatusResponse.from(status));
    }

    public static Response<Void> accepted() {
        return Response.create()
                .with(StatusResponse.from(201));
    }

    public static Response<Void> redirect(int status, String url) {
        return Response.create()
                .with(HeaderResponse.from("Location", url))
                .with(StatusResponse.from(status));
    }

    public static Response<Void> noContent(int status) {
        return Response.create()
                .with(StatusResponse.from(status));
    }
}
