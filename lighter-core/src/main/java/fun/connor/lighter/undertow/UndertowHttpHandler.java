package fun.connor.lighter.undertow;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class UndertowHttpHandler implements HttpHandler{

    private LighterRequestResolver resolver;

    public UndertowHttpHandler(LighterRequestResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String url = exchange.getRequestURL();

        resolver.resolve(exchange, null, null);
    }
}
