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

public class Main {

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
                .hostHame("0.0.0.0")
                .port(8000)
                .build();

        System.out.println("just testing the build");

        long finishTime = System.currentTimeMillis();
        l.start();
        System.out.println("total start->stop time was: " + (finishTime - startTime));
    }
}
