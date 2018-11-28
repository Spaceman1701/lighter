package fun.connor.lighter.autoconfig;

/**
 * Singleton Factory for loading compile-time generated auto configuration objects. The methods on this
 * class return instances of the auto generated endpoint configuration as well as an injectable
 * container object for special dependency management needs. The most common use of this class is
 * for loading {@link RouteConfiguration} at startup.
 */
public class AutoConfigFactory {

    private static final String ROUTE_CONFIG_CLASS = "fun.connor.lighter.generated.routing.GeneratedRouteConfiguration";
    private static final String BEAN_CONFIG_CLASS = "fun.connor.lighter.generated.dependency.GeneratedReverseInjector";

    private static AutoConfigFactory INSTANCE = new AutoConfigFactory();

    private AutoConfigFactory() {

    }

    /**
     * @return The singleton instance of the factory
     */
    public static AutoConfigFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Loads the compile-time generated endpoint configuration for the application. This operation requires
     * reflection. In the event of any error, a {@link AutoConfigException} is thrown.
     * @return The route configuration that lighter-compiler generated for this application
     */
    public RouteConfiguration loadRouteConfiguration() {
        try {
            Class<?> clazz = AutoConfigFactory.class.getClassLoader().loadClass(ROUTE_CONFIG_CLASS);
            return (RouteConfiguration) clazz.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AutoConfigException("Error loading autoconfiguration data");
        }
    }


    /**
     * Loads the compile-time generated {@link ReverseInjector} for the application. This operation
     * requires reflection. In the event of any error, a {@link AutoConfigException} is thrown. See
     * {@link ReverseInjector} documentation for an explanation of why one might use this method.
     * @return An instance of an appropriate {@link ReverseInjector} implementation for this application.
     */
    public ReverseInjector loadReverseInjector() {
        try {
            Class<?> injectorClass = AutoConfigFactory.class.getClassLoader().loadClass(BEAN_CONFIG_CLASS);
            return (ReverseInjector) injectorClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AutoConfigException("Error loading reverse inject");
        }
    }

    /**
     * Helper method that directly returns the compile time generated {@link RouteConfiguration} for this
     * application. Since most applications do not need to use the other features of this factory, this method
     * can be used to improve code readability. See {@link AutoConfigFactory#loadRouteConfiguration()}
     * @return The route configuration that lighter-compiler generated for this application
     */
    public static RouteConfiguration loadAutomaticConfiguration() {
        AutoConfigFactory instance = getInstance();
        return instance.loadRouteConfiguration();
    }
}
