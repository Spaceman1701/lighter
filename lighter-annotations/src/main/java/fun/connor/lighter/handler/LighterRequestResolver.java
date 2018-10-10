package fun.connor.lighter.handler;

import fun.connor.lighter.handler.Request;

import java.util.Map;

public interface LighterRequestResolver {
    void resolve(Map<String, String> pathParams, Map<String, String> queryParams, Request request);


}
