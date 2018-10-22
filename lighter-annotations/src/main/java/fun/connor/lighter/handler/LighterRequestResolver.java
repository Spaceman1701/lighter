package fun.connor.lighter.handler;

import java.util.Map;

public interface LighterRequestResolver {
    Response<?> resolve(Map<String, String> pathParams, Map<String, String> queryParams, Request request);


}
