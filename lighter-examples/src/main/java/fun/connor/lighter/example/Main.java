package fun.connor.lighter.example;

import fun.connor.lighter.Lighter;
import fun.connor.lighter.autoconfig.AutomaticRouteConfigurationLoader;

public class Main {

    public static void main(String[] args) {
        Lighter l = new Lighter(null);

        l.addRouter(AutomaticRouteConfigurationLoader.loadAutomaticConfiguration());

        System.out.println("just testing the build");
    }
}
