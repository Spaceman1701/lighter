package fun.connor.lighter.injection;

/**
 * A functional interface for generic injector interaction. The functional
 * method of this interface has the same signature as typical {@code newInstance}
 * methods on dependency injection {@code Injector} implementations. Lighter will
 * use the {@code InjectionObjectFactory} to construct ANY and ALL user defined classes. Thus,
 * the {@code InjectionObjectFactory} must be able to construct all
 * {@link fun.connor.lighter.declarative.ResourceController} and all
 * {@link fun.connor.lighter.declarative.ProducesRequestGuard} annotated factories.
 */
@FunctionalInterface
public interface InjectionObjectFactory {
    /**
     * Create a new instance of the provided class. If an invalid instance is returned
     * by this method, Lighter will throw a {@link RuntimeException}.
     * @param clazz the class to construct
     * @param <T> the type to construct
     * @return a valid instance of the class
     */
    <T> T newInstance(Class<T> clazz);
}
