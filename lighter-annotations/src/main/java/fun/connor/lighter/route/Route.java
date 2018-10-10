package fun.connor.lighter.route;


import fun.connor.lighter.handler.LighterRequestResolver;

public class Route {
    private final String method;
    private final String template;
    private final LighterRequestResolver handler;

    Route(final String method, final String template, final LighterRequestResolver handler) {
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

    public LighterRequestResolver getHandler() {
        return handler;
    }
}
