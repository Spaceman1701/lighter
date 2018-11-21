package fun.connor.lighter.adapter;

import java.util.function.Predicate;

public class BasicAdaptorFilter implements FilteringTypeAdaptorFactory {

    private Predicate<TypeRequirement> predicate;
    private TypeAdapterFactory factory;

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
