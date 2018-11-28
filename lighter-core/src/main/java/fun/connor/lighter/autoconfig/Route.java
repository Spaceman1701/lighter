package fun.connor.lighter.autoconfig;


/**
 * Single mapping between an HTTP method, URI, and a request resolver. This class
 * provides no verification for it's inputs.
 */
public class Route {
    private final String method;
    private final String template;
    private final ResolverFactory handler;

    Route(final String method, final String template, final ResolverFactory handler) {
        this.method = method;
        this.template = template;
        this.handler = handler;
    }


    /**
     * @return the HTTP method string for this route
     */
    public String getMethod() {
        return method;
    }

    /**
     * @return the URI template for this route
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @return the resolver for requests which match this route
     */
    public ResolverFactory getHandlerFactory() {
        return handler;
    }

    /**
     * @return a debug printable representation of the HTTP method and the URI template
     */
    @Override
    public String toString() {
        return method + ": " + template;
    }
}
