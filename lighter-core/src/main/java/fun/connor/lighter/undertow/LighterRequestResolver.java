package fun.connor.lighter.undertow;

import io.undertow.server.HttpServerExchange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface LighterRequestResolver {
    void resolve(HttpServerExchange exchange, Map<String, String> pathParams, Map<String, String> queryParams);


}
