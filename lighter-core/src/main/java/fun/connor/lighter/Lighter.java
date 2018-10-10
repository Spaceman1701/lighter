package fun.connor.lighter;

import fun.connor.lighter.injection.InjectionObjectFactory;
import fun.connor.lighter.route.RouteConfiguration;

public class Lighter {

    private InjectionObjectFactory injectionObjectFactory;

    public Lighter(InjectionObjectFactory injectionObjectFactory) {
        this.injectionObjectFactory = injectionObjectFactory;
    }

    public void addRouter(RouteConfiguration router) {

    }
}
