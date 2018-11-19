package fun.connor.lighter.response;

public interface ResponseDecorator<T, R> {
    ResponseState<R> apply(ResponseState<T> from);
}
