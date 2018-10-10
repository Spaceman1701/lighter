package fun.connor.lighter.autoconfig;

import fun.connor.lighter.undertow.LighterRequestResolver;

import java.util.ArrayList;
import java.util.List;

public class LighterRouter {

    private List<Route> routes;

    public LighterRouter() {
        routes = new ArrayList<>();
    }

    public void addRoute(String method, String template, LighterRequestResolver resolver) {
        routes.add(new Route(method, template, resolver));
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
