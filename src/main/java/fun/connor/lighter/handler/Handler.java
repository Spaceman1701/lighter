package fun.connor.lighter.handler;

@FunctionalInterface
public interface Handler {
    void Handle(Request request, Response response);
}
