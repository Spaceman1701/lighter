package fun.connor.lighter.route;

import fun.connor.lighter.undertow.LighterRequestResolver;
import org.bigtesting.routd.Route;
import org.bigtesting.routd.Router;

import java.util.Map;

public class LighterRouter {

    private Router router;
    private Map<Route, LighterRequestResolver> resolvers;

    public LighterRouter(Router router) {
        this.router = router;
    }

    public void addRoute(String routeStr, LighterRequestResolver resolver) {
        Route route = new Route(routeStr);

        router.add(route);
        resolvers.put(route, resolver);
    }

    public LighterRequestResolver getResolver(String path) {
        return resolvers.get(router.route(path));
    }
}
