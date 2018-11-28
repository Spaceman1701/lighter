package fun.connor.lighter.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fun.connor.lighter.Lighter;
import fun.connor.lighter.autoconfig.AutoConfigFactory;
import fun.connor.lighter.autoconfig.ReverseInjector;
import fun.connor.lighter.example.modules.ExampleModule;
import fun.connor.lighter.adapter.DelegatingAdaptorFactory;
import fun.connor.lighter.marshal.gson.GsonTypeAdapterFactory;
import fun.connor.lighter.marshal.java.JavaTypesAdaptorFactory;
import fun.connor.lighter.undertow.LighterUndertow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Example main application class. This example uses google Guice for dependency injection.
 * Commented code contains example use for Dagger.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Injector injector = Guice.createInjector(new ExampleModule());

        DelegatingAdaptorFactory adaptorFactory = DelegatingAdaptorFactory.builder()
                .addDelegateFactory(new JavaTypesAdaptorFactory())
                .addDelegateFactory(GsonTypeAdapterFactory.create())
                .build();

        AutoConfigFactory autoConfig = AutoConfigFactory.getInstance();
        ReverseInjector lighterConfigBean = autoConfig.loadReverseInjector();

//        ExampleComponent component = DaggerExampleComponent.builder()
//                .build();

        injector.injectMembers(lighterConfigBean);

//        component.inject((GeneratedReverseInjector)lighterConfigBean);

        Lighter l = LighterUndertow.builder()
                .adapterFactory(adaptorFactory)
                .injectionFactory(lighterConfigBean.getInjector())
                .addRouter(AutoConfigFactory.loadAutomaticConfiguration())
                .hostName("0.0.0.0")
                .port(8000)
                .build();


        l.start();
        long finishTime = System.currentTimeMillis();
        logger.info("BOOTED CONTAINER: " + (finishTime - startTime) + " ms");
    }
}
