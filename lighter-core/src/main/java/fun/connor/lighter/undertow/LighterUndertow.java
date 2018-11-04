package fun.connor.lighter.undertow;

import fun.connor.lighter.Lighter;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.autoconfig.LighterRouter;
import fun.connor.lighter.autoconfig.RouteConfiguration;
import fun.connor.lighter.console.LighterStartupPrinter;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.injection.InjectionObjectFactory;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LighterUndertow implements Lighter {
    private static final Logger log = LoggerFactory.getLogger(LighterUndertow.class);

    private Undertow undertow;
    private InjectionObjectFactory objectFactory;
    private TypeAdapterFactory adapterFactory;
    private String hostName;
    private int port;

    private boolean showBanner;


    private LighterUndertow
            (Undertow.Builder undertowBuilder, List<LighterRouter> routers,
             InjectionObjectFactory objectFactory, TypeAdapterFactory adapterFactory,
             String hostName, int port, boolean showBanner) {
        this.objectFactory = objectFactory;
        this.adapterFactory = adapterFactory;
        this.hostName = hostName;
        this.port = port;
        this.showBanner = showBanner;


        System.setProperty("org.jboss.logging.provider", "slf4j");


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
                log.debug("adding route: {}", route);
                routingHandler.add(route.getMethod(), route.getTemplate(), undertowHttpHandler);
            });


        undertowBuilder.addHttpListener(port, hostName)
                .setHandler(routingHandler);
    }

    public void start() {
        showBannerIfConfigured();
        undertow.start();
    }

    private void showBannerIfConfigured() {
        if (showBanner) {
            LighterStartupPrinter startupPrinter = new LighterStartupPrinter(System.out);
            startupPrinter.printBanner();
        }
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

        private boolean showBanner;

        private Builder() {
            undertowBuilder = Undertow.builder();
            routers = new ArrayList<>();

            showBanner = true;
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

        public Builder hostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public Builder showBanner(boolean show) {
            this.showBanner = show;
            return this;
        }

        public LighterUndertow build() {
            return new LighterUndertow
                    (undertowBuilder, routers, injectionObjectFactory, typeAdapterFactory, hostName, port, showBanner);
        }
    }

}
