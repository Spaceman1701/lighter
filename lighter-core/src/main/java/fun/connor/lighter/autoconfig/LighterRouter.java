package fun.connor.lighter.autoconfig;


import java.util.ArrayList;
import java.util.List;

/**
 * Maintains mappings between routes and application endpoint handlers. Specifically, this class
 * maintains a resolvable set of {@link Route}s. A set of routes is resolvable if there is no possible
 * URI which maps to more than one {@link Route} of equal specificity.
 */
public class LighterRouter {

    private List<Route> routes;

    /**
     * Construct a router with no routes
     */
    public LighterRouter() {
        routes = new ArrayList<>();
    }

    /**
     * Add a route to this router for requests with the given HTTP method and URI template and map these
     * requests to a resolver constructed from the {@link ResolverFactory}.
     * @param method The HTTP method string for this route
     * @param template The URI template that this route matches with
     * @param resolver the resolver that this route maps to
     */
    public void addRoute(String method, String template, ResolverFactory resolver) {
        routes.add(new Route(method, template, resolver));
    }

    /**
     * @return all of the routes maintained by this router
     */
    public List<Route> getRoutes() {
        return routes;
    }
}
