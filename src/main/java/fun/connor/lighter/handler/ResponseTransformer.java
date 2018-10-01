package fun.connor.lighter.handler;

@FunctionalInterface
public interface ResponseTransformer<IN, OUT> {
    Response<OUT> transform(Response<? extends IN> response);
}
