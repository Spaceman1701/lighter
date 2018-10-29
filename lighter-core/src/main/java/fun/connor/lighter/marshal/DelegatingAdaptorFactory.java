package fun.connor.lighter.marshal;

import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeAdapterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Creates adapters based on a delegation chain. Useful when creating
 * a single adaptor factory for multiple content types
 */
public class DelegatingAdaptorFactory implements TypeAdapterFactory {

    private static class DelegateFactory implements TypeAdapterFactory {
        private TypeAdapterFactory factory;
        private Predicate<Class<?>> predicate;

        private DelegateFactory(Predicate<Class<?>> predicate, TypeAdapterFactory factory) {
            this.predicate = predicate;
            this.factory = factory;
        }

        private boolean test(Class<?> clazz) {
            return predicate.test(clazz);
        }

        @Override
        public <T> TypeAdapter<T> getAdapter(Class<T> clazz) {
            return factory.getAdapter(clazz);
        }
    }

    public static class Builder {
        private List<DelegateFactory> delegateFactories;

        private Builder() {
            delegateFactories = new ArrayList<>();
        }

        public Builder addDelegateFactory(Predicate<Class<?>> condition, TypeAdapterFactory factory) {
            delegateFactories.add(new DelegateFactory(condition, factory));
            return this;
        }

        public DelegatingAdaptorFactory build() {
            return new DelegatingAdaptorFactory(delegateFactories);
        }
    }

    private List<DelegateFactory> delegateFactories;

    private DelegatingAdaptorFactory(List<DelegateFactory> delegateFactories) {
        this.delegateFactories = delegateFactories;
    }


    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz) {
        for (DelegateFactory factory : delegateFactories) {
            if (factory.test(clazz)) {
                return factory.getAdapter(clazz);
            }
        }
        return null; //TODO: error case
    }

    public static Builder builder() {
        return new Builder();
    }
}
