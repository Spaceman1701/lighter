package fun.connor.lighter.adapter;

import java.util.function.Predicate;

public class TypeAdaptorFilter implements FilteringTypeAdaptorFactory {

    private Predicate<Class<?>> predicate;
    private FilteringTypeAdaptorFactory factory;


    public TypeAdaptorFilter(Predicate<Class<?>> predicate, FilteringTypeAdaptorFactory factory) {
        this.predicate = predicate;
        this.factory = factory;
    }

    @Override
    public Predicate<Class<?>> applies() {
        return predicate.and(factory.applies());
    }

    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz) {
        return factory.getAdapter(clazz);
    }
}
