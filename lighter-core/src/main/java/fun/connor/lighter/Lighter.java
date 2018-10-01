package fun.connor.lighter;

import fun.connor.lighter.handler.Response;
import fun.connor.lighter.injection.InjectionObjectFactory;

public class Lighter {

    private InjectionObjectFactory injectionObjectFactory;

    public Lighter(InjectionObjectFactory injectionObjectFactory) {
        this.injectionObjectFactory = injectionObjectFactory;
    }
}
