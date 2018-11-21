package fun.connor.lighter.marshal;

import fun.connor.lighter.adapter.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Creates adapters based on a delegation chain. Useful when creating
 * a single adaptor factory for multiple content types
 */
public class DelegatingAdaptorFactory implements FilteringTypeAdaptorFactory {


    @Override
    public Predicate<TypeRequirement> applies() {
        return delegateFactories.stream()
                .map(FilteringTypeAdaptorFactory::applies)
                .reduce(Predicate::or)
                .orElse(clazz -> false);
    }


    public static class Builder {
        private List<FilteringTypeAdaptorFactory> delegateFactories;

        private Builder() {
            delegateFactories = new ArrayList<>();
        }

        public Builder addDelegateFactory(Predicate<TypeRequirement> condition, TypeAdapterFactory factory) {
            delegateFactories.add(new BasicAdaptorFilter(condition, factory));
            return this;
        }

        public Builder addDelegateFactory(FilteringTypeAdaptorFactory factory) {
            delegateFactories.add(factory);
            return this;
        }

        public DelegatingAdaptorFactory build() {
            return new DelegatingAdaptorFactory(delegateFactories);
        }
    }

    private List<FilteringTypeAdaptorFactory> delegateFactories;

    private DelegatingAdaptorFactory(List<FilteringTypeAdaptorFactory> delegateFactories) {
        this.delegateFactories = delegateFactories;
    }


    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz, String contentType) {
        TypeRequirement requirement = new TypeRequirement(clazz, contentType);
        for (FilteringTypeAdaptorFactory factory : delegateFactories) {
            if (factory.applies().test(requirement)) {
                return factory.getAdapter(clazz, contentType);
            }
        }
        return null; //TODO: error case
    }

    public static Builder builder() {
        return new Builder();
    }
}
