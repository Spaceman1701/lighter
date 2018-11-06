package fun.connor.lighter.autoconfig;

public class AutoConfigFactory {

    private static final String CLASS_NAME = "fun.connor.lighter.generated.routing.GeneratedRouteConfiguration";

    private static AutoConfigFactory INSTANCE = new AutoConfigFactory();

    private AutoConfigFactory() {

    }

    public static AutoConfigFactory getInstance() {
        return INSTANCE;
    }

    public RouteConfiguration loadRouteConfiguration() {
        try {
            Class<?> clazz = AutoConfigFactory.class.getClassLoader().loadClass(CLASS_NAME);
            return (RouteConfiguration) clazz.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AutoConfigException("Error loading autoconfiguration data");
        }
    }


    public ReverseInjector loadReverseInjector() {
        return null;
    }


    public static RouteConfiguration loadAutomaticConfiguration() {
        AutoConfigFactory instance = getInstance();
        return instance.loadRouteConfiguration();
    }
}
