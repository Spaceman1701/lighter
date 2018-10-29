package fun.connor.lighter;

import com.google.gson.Gson;
import fun.connor.lighter.autoconfig.LighterRouter;
import fun.connor.lighter.autoconfig.Route;
import fun.connor.lighter.autoconfig.RouteConfiguration;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.injection.InjectionObjectFactory;
import fun.connor.lighter.marshal.SimpleGsonMarshaller;
import fun.connor.lighter.undertow.UndertowHttpHandler;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;

public class Lighter {

    private InjectionObjectFactory injectionObjectFactory;
    private Undertow.Builder undertowBuilder;

    public Lighter(InjectionObjectFactory injectionObjectFactory) {
        this.injectionObjectFactory = injectionObjectFactory;
        undertowBuilder = Undertow.builder();
    }

    public void addRouter(RouteConfiguration router) {
        LighterRouter r = router.getRouter();

        RoutingHandler routingHandler = Handlers.routing(false);

        SimpleGsonMarshaller marshaller = new SimpleGsonMarshaller();
        for (Route route : r.getRoutes()) {
            LighterRequestResolver resolver = route.getHandlerFactory().newInstance(injectionObjectFactory, marshaller);
            UndertowHttpHandler undertowHttpHandler = new UndertowHttpHandler(resolver);
            System.out.println("adding route: " + route);
            routingHandler.add(route.getMethod(), route.getTemplate(), undertowHttpHandler);
        }

        routingHandler.setFallbackHandler(Handlers.redirect("http://connor.fun"));

        Gson gson = new Gson();


        undertowBuilder.addHttpListener(8080, "localhost")
                .setHandler(routingHandler);
    }


    public void start() {
        Undertow undertow = undertowBuilder.build();
        undertow.start();
    }
}
