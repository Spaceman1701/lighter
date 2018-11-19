package fun.connor.lighter.response;

import java.util.Map;

public interface Response<T> {
    T getContent();
    int getStatus();
    Map<String, String> getHeaders();
}
