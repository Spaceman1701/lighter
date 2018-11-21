package fun.connor.lighter.undertow;

import fun.connor.lighter.handler.Request;
import fun.connor.lighter.http.HttpHeaders;
import io.undertow.server.HttpServerExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class UndertowRequest implements Request {

    private HttpServerExchange exchange;

    private String body;

    public UndertowRequest(final HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public String getContentType() {
        return getHeaderValue(HttpHeaders.CONTENT_TYPE);
    }

    @Override
    public String getHeaderValue(String header) {
        return exchange.getRequestHeaders().getFirst(header);
    }

    @Override
    public List<String> getHeaderValues(String header) {
        return exchange.getRequestHeaders().get(header);
    }

    @Override
    public String getRequestPath() {
        return exchange.getRequestPath();
    }

    @Override
    public String getMethod() {
        return exchange.getRequestMethod().toString();
    }

    @Override
    public String getBody() {
        if (body == null) {
            exchange.startBlocking();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getInputStream()))) {
                body = reader.lines().collect(Collectors.joining());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return body;
    }
}
