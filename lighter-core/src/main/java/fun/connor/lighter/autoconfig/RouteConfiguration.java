package fun.connor.lighter.autoconfig;

/**
 * Simple factory for providing preconfigured {@link LighterRouter} instances.
 */
public interface RouteConfiguration {
    /**
     * Provides a preconfigured instance of {@link LighterRouter}. It is expected that
     * instances provided by this method are not empty. However, returning an empty instance
     * is legal. However, this method should return instances with the same configuration every
     * time. Since {@link LighterRouter} instances are mutable, this method is should not return the
     * same instance upon repeated calls.
     * @return a preconfigured {@link LighterRouter}
     */
    LighterRouter getRouter();
}
