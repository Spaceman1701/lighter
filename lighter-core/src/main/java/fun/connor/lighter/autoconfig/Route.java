package fun.connor.lighter.autoconfig;


public class Route {
    private final String method;
    private final String template;
    private final ResolverFactory handler;

    Route(final String method, final String template, final ResolverFactory handler) {
        this.method = method;
        this.template = template;
        this.handler = handler;
    }


    public String getMethod() {
        return method;
    }

    public String getTemplate() {
        return template;
    }

    public ResolverFactory getHandlerFactory() {
        return handler;
    }

    @Override
    public String toString() {
        return method + ": " + template;
    }
}
