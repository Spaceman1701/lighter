package fun.connor.lighter.adapter;

import java.util.function.Predicate;

/**
 * A decorator which transforms a {@link TypeAdapterFactory} into a {@link FilteringTypeAdaptorFactory}
 * with the given predicate. Unlike the {@link TypeAdaptorFilter} decorator, this class <strong>replaces</strong>
 * the predicate of the given factory, if it has one
 */
public class BasicAdaptorFilter implements FilteringTypeAdaptorFactory {

    private Predicate<TypeRequirement> predicate;
    private TypeAdapterFactory factory;

    /**
     * Construct a new decorator for the given factory with the given predicate
     * @param predicate The predicate to add to the factory.
     * @param factory the factory to decorate
     */
    public BasicAdaptorFilter(Predicate<TypeRequirement> predicate, TypeAdapterFactory factory) {
        this.predicate = predicate;
        this.factory = factory;
    }

    @Override
    public Predicate<TypeRequirement> applies() {
        return predicate;
    }

    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz, String mediaType) {
        return factory.getAdapter(clazz, mediaType);
    }
}
