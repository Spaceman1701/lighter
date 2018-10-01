package fun.connor.lighter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LighterRequestResolver {
    void resolve(HttpServletRequest request, HttpServletResponse response);
}
