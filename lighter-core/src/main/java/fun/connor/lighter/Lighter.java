package fun.connor.lighter;

import fun.connor.lighter.autoconfig.RouteConfiguration;
import fun.connor.lighter.injection.InjectionObjectFactory;

public class Lighter {

    private InjectionObjectFactory injectionObjectFactory;

    public Lighter(InjectionObjectFactory injectionObjectFactory) {
        this.injectionObjectFactory = injectionObjectFactory;
    }

    public void addRouter(RouteConfiguration router) {

    }

    public void start() {

    }
}
