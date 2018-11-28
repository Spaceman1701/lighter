package fun.connor.lighter.undertow;

import fun.connor.lighter.Lighter;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.autoconfig.LighterRouter;
import fun.connor.lighter.autoconfig.RouteConfiguration;
import fun.connor.lighter.console.LighterStartupPrinter;
import fun.connor.lighter.GlobalRequestTransformer;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.http.HttpHeaders;
import fun.connor.lighter.injection.InjectionObjectFactory;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Top-level implementation of {@link Lighter} for the Undertow backend. Use the
 * {@link Builder} to construct instances of this class with proper configuration.
 * This implementation does not offer any extra configuration over the generic options.
 */
public class LighterUndertow implements Lighter {
    private static final Logger log = LoggerFactory.getLogger(LighterUndertow.class);

    private Undertow undertow;
    private InjectionObjectFactory objectFactory;
    private TypeAdapterFactory adapterFactory;
    private String hostName;
    private int port;
    private List<GlobalRequestTransformer> requestTransformers;

    private boolean showBanner;


    private LighterUndertow
            (Builder builder) {
        this.objectFactory = builder.injectionObjectFactory;
        this.adapterFactory = builder.typeAdapterFactory;
        this.hostName = builder.hostName;
        this.port = builder.port;
        this.showBanner = builder.showBanner;
        this.requestTransformers = builder.requestTransformers;


        System.setProperty("org.jboss.logging.provider", "slf4j");


        configureRouting(builder.undertowBuilder, builder.routers);
        this.undertow = builder.undertowBuilder.build();
    }


    private void configureRouting(Undertow.Builder undertowBuilder, List<LighterRouter> routers) {
        RoutingHandler routingHandler = Handlers.routing(false);


        routers.stream()
            .flatMap(router -> router.getRoutes().stream())
            .forEach(route -> {
                LighterRequestResolver resolver = route.getHandlerFactory().newInstance(objectFactory, adapterFactory);
                UndertowHttpHandler undertowHttpHandler = new UndertowHttpHandler(resolver, adapterFactory, requestTransformers);
                BlockingHandler handlerWrapper = new BlockingHandler(undertowHttpHandler);
                log.debug("adding route: {}", route);
                routingHandler.add(route.getMethod(), "/" + route.getTemplate(), handlerWrapper);
            });


        //TODO: remove this... required for cross-origin requests that use ES6 fetch
        routingHandler.add("OPTIONS", "/*", exchange -> {
            String headers = String.join(",", "Authorization", "Content-Type");


            exchange.getResponseHeaders().add(HttpString.tryFromString(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN), "*");
            exchange.getResponseHeaders().add(HttpString.tryFromString(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS), headers);

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

    /**
     * {@link Lighter.Builder} implementation for the Undertow backend.
     */
    public static class Builder implements Lighter.Builder {

        private Undertow.Builder undertowBuilder;
        private List<LighterRouter> routers;
        private InjectionObjectFactory injectionObjectFactory;
        private TypeAdapterFactory typeAdapterFactory;
        private List<GlobalRequestTransformer> requestTransformers;

        private String hostName;
        private int port;

        private boolean showBanner;

        private Builder() {
            undertowBuilder = Undertow.builder();
            routers = new ArrayList<>();
            requestTransformers = new ArrayList<>();

            showBanner = true;
        }

        @Override
        public Builder addRouter(RouteConfiguration configuration) {
            routers.add(configuration.getRouter());
            return this;
        }

        @Override
        public Builder injectionFactory(InjectionObjectFactory factory) {
            this.injectionObjectFactory = factory;
            return this;
        }

        @Override
        public Builder adapterFactory(TypeAdapterFactory factory) {
            this.typeAdapterFactory = factory;
            return this;
        }

        @Override
        public Builder port(int port) {
            this.port = port;
            return this;
        }

        @Override
        public Builder hostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        @Override
        public Builder showBanner(boolean show) {
            this.showBanner = show;
            return this;
        }

        @Override
        public Builder addResponseTransformer(GlobalRequestTransformer transformer) {
            this.requestTransformers.add(transformer);
            return this;
        }

        @Override
        public LighterUndertow build() {
            return new LighterUndertow(this);
        }
    }

}
