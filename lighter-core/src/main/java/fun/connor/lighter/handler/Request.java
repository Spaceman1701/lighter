package fun.connor.lighter.handler;

import java.util.List;

public interface Request {

    String getHeaderValue(String header);
    List<String> getHeaderValues(String header);
    String getRequestPath();
    String getMethod();
    String getBody();
}
