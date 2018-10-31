package fun.connor.lighter.adapter;

import java.util.function.Predicate;

public class BasicAdaptorFilter implements FilteringTypeAdaptorFactory {

    private Predicate<Class<?>> predicate;
    private TypeAdapterFactory factory;

    public BasicAdaptorFilter(Predicate<Class<?>> predicate, TypeAdapterFactory factory) {
        this.predicate = predicate;
        this.factory = factory;
    }

    @Override
    public Predicate<Class<?>> applies() {
        return predicate;
    }

    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz) {
        return factory.getAdapter(clazz);
    }
}
