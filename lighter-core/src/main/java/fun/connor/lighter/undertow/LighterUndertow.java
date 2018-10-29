package fun.connor.lighter.undertow;

import fun.connor.lighter.Lighter;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.autoconfig.LighterRouter;
import fun.connor.lighter.autoconfig.RouteConfiguration;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.injection.InjectionObjectFactory;
import fun.connor.lighter.marshal.SimpleGsonMarshaller;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;

import java.util.ArrayList;
import java.util.List;

public class LighterUndertow implements Lighter {
    private Undertow undertow;
    private InjectionObjectFactory objectFactory;
    private TypeAdapterFactory adapterFactory;
    private String hostName;
    private int port;


    private LighterUndertow
            (Undertow.Builder undertowBuilder, List<LighterRouter> routers,
             InjectionObjectFactory objectFactory, TypeAdapterFactory adapterFactory,
             String hostName, int port) {
        this.objectFactory = objectFactory;
        this.adapterFactory = adapterFactory;
        this.hostName = hostName;
        this.port = port;

        configureRouting(undertowBuilder, routers);
        this.undertow = undertowBuilder.build();
    }


    private void configureRouting(Undertow.Builder undertowBuilder, List<LighterRouter> routers) {
        RoutingHandler routingHandler = Handlers.routing(false);


        routers.stream()
            .flatMap(router -> router.getRoutes().stream())
            .forEach(route -> {
                LighterRequestResolver resolver = route.getHandlerFactory().newInstance(objectFactory, adapterFactory);
                UndertowHttpHandler undertowHttpHandler = new UndertowHttpHandler(resolver);
                System.out.println("adding route: " + route);
                routingHandler.add(route.getMethod(), route.getTemplate(), undertowHttpHandler);
            });


        undertowBuilder.addHttpListener(port, hostName)
                .setHandler(routingHandler);
    }

    public void start() {
        undertow.start();
    }

    public void stop() {
        undertow.stop();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Undertow.Builder undertowBuilder;
        private List<LighterRouter> routers;
        private InjectionObjectFactory injectionObjectFactory;
        private TypeAdapterFactory typeAdapterFactory;

        private String hostName;
        private int port;

        private Builder() {
            undertowBuilder = Undertow.builder();
            routers = new ArrayList<>();
        }

        public Builder addRouter(RouteConfiguration configuration) {
            routers.add(configuration.getRouter());
            return this;
        }

        public Builder injectionFactory(InjectionObjectFactory factory) {
            this.injectionObjectFactory = factory;
            return this;
        }

        public Builder adapterFactory(TypeAdapterFactory factory) {
            this.typeAdapterFactory = factory;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder hostHame(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public LighterUndertow build() {
            return new LighterUndertow
                    (undertowBuilder, routers, injectionObjectFactory, typeAdapterFactory, hostName, port);
        }
    }

}
