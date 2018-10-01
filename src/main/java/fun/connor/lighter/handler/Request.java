package fun.connor.lighter.handler;

import java.util.Map;

public class Request<T> {
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private T body;
}
