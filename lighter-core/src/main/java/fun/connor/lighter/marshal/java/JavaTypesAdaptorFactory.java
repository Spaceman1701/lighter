package fun.connor.lighter.marshal.java;

import fun.connor.lighter.adapter.FilteringTypeAdaptorFactory;
import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeRequirement;
import fun.connor.lighter.marshal.DelegatingAdaptorFactory;

import java.util.function.Predicate;

public class JavaTypesAdaptorFactory implements FilteringTypeAdaptorFactory {

    private DelegatingAdaptorFactory factory;

    public JavaTypesAdaptorFactory() {
        factory = DelegatingAdaptorFactory.builder()
                .addDelegateFactory(new BooleanTypeAdapterFactory())
                .addDelegateFactory(new IntegerTypeAdaptorFactory())
                .addDelegateFactory(new StringTypeAdapterFactory())
                .addDelegateFactory(new UUIDTypeAdaptorFactory())
                .build();
    }

    @Override
    public Predicate<TypeRequirement> applies() {
        return factory.applies();
    }

    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz, String contentType) {
        return factory.getAdapter(clazz, contentType);
    }
}
