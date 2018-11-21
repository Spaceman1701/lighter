package fun.connor.lighter.testutils;

import fun.connor.lighter.handler.Request;
import fun.connor.lighter.http.HttpHeaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRequest implements Request {

    private Map<String, List<String>> headers;
    private String requestPath;
    private String method;
    private String body;

    public TestRequest() {
        headers = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        if (!headers.containsKey(key)) {
            headers.put(key, new ArrayList<>());
        }
        this.headers.get(key).add(value);
    }

    public void addHeader(String key, List<String> value) {
        if (!headers.containsKey(key)) {
            headers.put(key, new ArrayList<>());
        }
        this.headers.get(key).addAll(value);
    }

    public void addHeaders(Map<String, List<String>> headers) {
        this.headers.putAll(headers);
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String getHeaderValue(String header) {
        if (headers.containsKey(header) && !headers.get(header).isEmpty()) {
            return headers.get(header).get(0);
        }
        return null;
    }

    @Override
    public List<String> getHeaderValues(String header) {
        return headers.get(header);
    }

    @Override
    public String getContentType() {
        return getHeaderValue(HttpHeaders.CONTENT_TYPE);
    }

    @Override
    public String getRequestPath() {
        return requestPath;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getBody() {
        return body;
    }
}
