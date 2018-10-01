package fun.connor.lighter.route;

import com.google.common.collect.ImmutableMap;

public class Route {
    private final String routeStr;
    private final ImmutableMap<String, String> queryArgs;
    private final ImmutableMap<String, String> pathArgs;

    public Route(final String route) {
        this.routeStr = route;
        this.queryArgs = createQueryArgs();
        this.pathArgs = createPathArgs();
    }

    private ImmutableMap<String, String> createQueryArgs() {
        return null;
    }

    private ImmutableMap<String, String> createPathArgs() {
        return null;
    }
}
