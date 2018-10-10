package fun.connor.lighter.undertow;

import fun.connor.lighter.handler.Request;
import io.undertow.server.HttpServerExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class UndertowRequest implements Request {

    private HttpServerExchange exchange;

    public UndertowRequest(final HttpServerExchange exchange) {
        this.exchange = exchange;
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getInputStream()))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException("bad"); //TODO: bad
        }
    }
}
