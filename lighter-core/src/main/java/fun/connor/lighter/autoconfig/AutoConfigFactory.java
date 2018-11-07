package fun.connor.lighter.autoconfig;

public class AutoConfigFactory {

    private static final String ROUTE_CONFIG_CLASS = "fun.connor.lighter.generated.routing.GeneratedRouteConfiguration";
    private static final String BEAN_CONFIG_CLASS = "fun.connor.lighter.generated.dependency.GeneratedReverseInjector";

    private static AutoConfigFactory INSTANCE = new AutoConfigFactory();

    private AutoConfigFactory() {

    }

    public static AutoConfigFactory getInstance() {
        return INSTANCE;
    }

    public RouteConfiguration loadRouteConfiguration() {
        try {
            Class<?> clazz = AutoConfigFactory.class.getClassLoader().loadClass(ROUTE_CONFIG_CLASS);
            return (RouteConfiguration) clazz.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AutoConfigException("Error loading autoconfiguration data");
        }
    }


    public ReverseInjector loadReverseInjector() {
        try {
            Class<?> injectorClass = AutoConfigFactory.class.getClassLoader().loadClass(BEAN_CONFIG_CLASS);
            return (ReverseInjector) injectorClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AutoConfigException("Error loading reverse inject");
        }
    }


    public static RouteConfiguration loadAutomaticConfiguration() {
        AutoConfigFactory instance = getInstance();
        return instance.loadRouteConfiguration();
    }
}
