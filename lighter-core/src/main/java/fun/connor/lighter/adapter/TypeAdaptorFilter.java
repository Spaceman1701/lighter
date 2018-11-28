package fun.connor.lighter.adapter;

import java.util.function.Predicate;

/**
 * Decorator for adding filtering conditions to {@link FilteringTypeAdaptorFactory}. The predicate
 * provided to this decorator and the original factory's predicate are combined using a logical AND.
 * <br><br>
 * By using this decorator and the {@link DelegatingAdaptorFactory}, any logical predicate can be formed. This
 * allows for arbitrarily complex filters to be created for selecting TypeAdapters.
 */
public class TypeAdaptorFilter implements FilteringTypeAdaptorFactory {

    private Predicate<TypeRequirement> predicate;
    private FilteringTypeAdaptorFactory factory;


    /**
     * Construct a new TypeAdaptorFilter from the given predicate and the given factory
     * @param predicate the predicate to add to the factory
     * @param factory the original factory to decorate
     */
    public TypeAdaptorFilter(Predicate<TypeRequirement> predicate, FilteringTypeAdaptorFactory factory) {
        this.predicate = predicate;
        this.factory = factory;
    }

    /**
     * See the documentation for {@link FilteringTypeAdaptorFactory#applies()}.
     * @return the logical AND of the original factory's predicate and this decorator's predicate
     */
    @Override
    public Predicate<TypeRequirement> applies() {
        return predicate.and(factory.applies());
    }

    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz, String mediaType) {
        return factory.getAdapter(clazz, mediaType);
    }
}
