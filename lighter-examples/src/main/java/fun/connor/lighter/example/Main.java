package fun.connor.lighter.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fun.connor.lighter.Lighter;
import fun.connor.lighter.autoconfig.AutomaticRouteConfigurationLoader;
import fun.connor.lighter.example.modules.ExampleModule;

public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Injector injector = Guice.createInjector(new ExampleModule());

        Lighter l = new Lighter(injector::getInstance);

        l.addRouter(AutomaticRouteConfigurationLoader.loadAutomaticConfiguration());

        System.out.println("just testing the build");

        l.start();
    }
}
