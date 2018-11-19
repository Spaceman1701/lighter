package fun.connor.lighter.response;

public interface ResponseDecorator<T, R> {
    Response<R> apply(Response<T> from);
}
