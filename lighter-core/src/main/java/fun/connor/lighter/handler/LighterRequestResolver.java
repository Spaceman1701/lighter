package fun.connor.lighter.handler;

import fun.connor.lighter.response.Response;

import java.util.Map;

@FunctionalInterface
public interface LighterRequestResolver {
    Response<?> resolve(Map<String, String> pathParams, Map<String, String> queryParams, Request request);
}
