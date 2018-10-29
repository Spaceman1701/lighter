package fun.connor.lighter.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fun.connor.lighter.Lighter;
import fun.connor.lighter.autoconfig.AutomaticRouteConfigurationLoader;
import fun.connor.lighter.example.modules.ExampleComponent;
import fun.connor.lighter.example.modules.ExampleDaggerInjector;
import fun.connor.lighter.example.modules.ExampleDaggerModule;
import fun.connor.lighter.example.modules.ExampleModule;
import fun.connor.lighter.undertow.LighterUndertow;

public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Injector injector = Guice.createInjector(new ExampleModule());

        Lighter l = LighterUndertow.builder()
                .adapterFactory(null)
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
