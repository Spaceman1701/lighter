package fun.connor.lighter.handler;


@FunctionalInterface
public interface ResponseContentTransformer<IN, OUT> extends ResponseTransformer<IN, OUT> {
    @Override
    default Response<OUT> transform(Response<? extends IN> response) {
        return new Response<>(response.getStatus(), response.getCustomHeaders(), transformContent(response.getContent()));
    }

    OUT transformContent(IN content);
}
