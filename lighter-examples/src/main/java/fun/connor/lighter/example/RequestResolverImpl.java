package fun.connor.lighter.example;

import fun.connor.lighter.example.domain.Foobar;
import fun.connor.lighter.example.handlers.FoobarHandler;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.handler.RequestContext;
import fun.connor.lighter.handler.Response;

import java.util.Map;

public class RequestResolverImpl implements LighterRequestResolver {

    private FoobarHandler handler;

    @Override
    public Response<?> resolve(Map<String, String> pathParams, Map<String, String> queryParams, Request request) {
//
//
//        String name = pathParams.get("name");
//        if (name == null) {
//            throw new RuntimeException("bad");
//        }
//
//        String countStr = queryParams.get("count");
//        if (countStr == null) {
//            throw new RuntimeException("bad");
//        }
//
//        int count;
//        try {
//            count = Integer.parseInt(countStr);
//        } catch (NumberFormatException e) {
//            throw new RuntimeException("bad", e);
//        }
//
//        RequestContext<Foobar> context = new RequestContext<>(request);
//
//        Response<Foobar<Integer>> response = handler.getFoobarByName(name, count, null);
//
//        return response;
        return null;
    }
}
