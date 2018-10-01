package fun.connor.lighter.handler;

@FunctionalInterface
public interface Handler<T, K> {
    void Handle(Request<T> request, Response<K> response, Handler<?, ?> next);
}
