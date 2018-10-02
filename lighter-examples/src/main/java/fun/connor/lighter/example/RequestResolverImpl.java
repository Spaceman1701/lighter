package fun.connor.lighter.example;

import fun.connor.lighter.example.handlers.FoobarHandler;
import fun.connor.lighter.undertow.LighterRequestResolver;
import io.undertow.server.HttpServerExchange;

import java.util.Map;

public class RequestResolverImpl implements LighterRequestResolver {

    private FoobarHandler handler;

    @Override
    public void resolve(HttpServerExchange exchange, Map<String, String> pathParams, Map<String, String> queryParams) {


        String name = pathParams.get("name");
        if (name == null) {
            throw new RuntimeException("bad");
        }

        String countStr = queryParams.get("count");
        if (countStr == null) {
            throw new RuntimeException("bad");
        }

        int count;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("bad", e);
        }



        handler.getFoobarByName(name, count, null);

    }
}
