package fun.connor.lighter.autoconfig;

import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.injection.InjectionObjectFactory;

/**
 * A functional interface for a simple factory to create
 * {@link LighterRequestResolver} instances. Each factory
 * is expected to represent a specific {@link LighterRequestResolver} implementation.
 * However, this is not a requirement.
 */
@FunctionalInterface
public interface ResolverFactory {

    /**
     * Create a new request resolver
     * @param genericFactory the {@link InjectionObjectFactory} for the application
     * @param marshaller the top-level {@link TypeAdapterFactory} for the application
     * @return a new {@link LighterRequestResolver}
     */
    LighterRequestResolver newInstance(InjectionObjectFactory genericFactory, TypeAdapterFactory marshaller);
}
