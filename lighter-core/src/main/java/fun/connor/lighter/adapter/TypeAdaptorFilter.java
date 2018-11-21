package fun.connor.lighter.adapter;

import java.util.function.Predicate;

public class TypeAdaptorFilter implements FilteringTypeAdaptorFactory {

    private Predicate<TypeRequirement> predicate;
    private FilteringTypeAdaptorFactory factory;


    public TypeAdaptorFilter(Predicate<TypeRequirement> predicate, FilteringTypeAdaptorFactory factory) {
        this.predicate = predicate;
        this.factory = factory;
    }

    @Override
    public Predicate<TypeRequirement> applies() {
        return predicate.and(factory.applies());
    }

    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz, String mediaType) {
        return factory.getAdapter(clazz, mediaType);
    }
}
