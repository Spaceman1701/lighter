package fun.connor.lighter.autoconfig;

public class AutomaticRouteConfigurationLoader {

    private static final String CLASS_NAME = "fun.connor.lighter.generated.routing.GeneratedRouteConfiguration";

    private AutomaticRouteConfigurationLoader() {
    }

    public static RouteConfiguration loadAutomaticConfiguration() {
        try {
            Class<?> clazz = AutomaticRouteConfigurationLoader.class.getClassLoader().loadClass(CLASS_NAME);
            return (RouteConfiguration) clazz.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AutoConfigException("Error loading autoconfiguration data");
        }
    }
}
