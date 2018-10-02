package fun.connor.lighter.route;

import fun.connor.lighter.undertow.LighterRequestResolver;
import fun.connor.lighter.undertow.UndertowHttpHandler;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

public class LighterRouter {

    private RoutingHandler handler;

    public LighterRouter(RoutingHandler handler) {
        this.handler = handler;
    }

    public void addRoute(String method, String template, LighterRequestResolver resolver) {

        handler.add(method, template, new UndertowHttpHandler(resolver));
    }
}
