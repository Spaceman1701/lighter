package fun.connor.lighter.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fun.connor.lighter.Lighter;
import fun.connor.lighter.autoconfig.AutomaticRouteConfigurationLoader;
import fun.connor.lighter.example.modules.ExampleModule;
import fun.connor.lighter.marshal.DelegatingAdaptorFactory;
import fun.connor.lighter.marshal.gson.GsonTypeAdapterFactory;
import fun.connor.lighter.marshal.java.JavaTypesAdaptorFactory;
import fun.connor.lighter.undertow.LighterUndertow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Injector injector = Guice.createInjector(new ExampleModule());

        DelegatingAdaptorFactory adaptorFactory = DelegatingAdaptorFactory.builder()
                .addDelegateFactory(new JavaTypesAdaptorFactory())
                .addDelegateFactory(GsonTypeAdapterFactory.create())
                .build();

        Lighter l = LighterUndertow.builder()
                .adapterFactory(adaptorFactory)
                .injectionFactory(injector::getInstance)
                .addRouter(AutomaticRouteConfigurationLoader.loadAutomaticConfiguration())
                .hostName("0.0.0.0")
                .port(8000)
                .build();


        l.start();
        long finishTime = System.currentTimeMillis();
        logger.info("BOOTED CONTAINER: " + (finishTime - startTime) + " ms");
    }
}
