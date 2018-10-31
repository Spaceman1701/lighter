package fun.connor.lighter.marshal.java;

import fun.connor.lighter.adapter.FilteringTypeAdaptorFactory;
import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.marshal.DelegatingAdaptorFactory;

import java.util.function.Predicate;

public class JavaTypesAdaptorFactory implements FilteringTypeAdaptorFactory {

    private DelegatingAdaptorFactory factory;

    public JavaTypesAdaptorFactory() {
        factory = DelegatingAdaptorFactory.builder()
                .addDelegateFactory(new BooleanTypeAdapterFactory())
                .addDelegateFactory(new StringTypeAdapterFactory())
                .build();
    }

    @Override
    public Predicate<Class<?>> applies() {
        return factory.applies();
    }

    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz) {
        return factory.getAdapter(clazz);
    }
}
