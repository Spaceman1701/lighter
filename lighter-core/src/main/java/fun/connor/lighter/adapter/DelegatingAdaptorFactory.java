package fun.connor.lighter.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Creates adapters based on a delegation chain. Useful when creating
 * a single adaptor factory for multiple content types
 * <p>
 *     A delegate factory will be selected if its filtering predicate
 *     is sufficient for the type adaption requirement. If more than one
 *     delegate factory can fulfill the requirement, the factory added
 *     to the builder first is chosen.
 * </p>
 */
public class DelegatingAdaptorFactory implements FilteringTypeAdaptorFactory {


    @Override
    public Predicate<TypeRequirement> applies() {
        return delegateFactories.stream()
                .map(FilteringTypeAdaptorFactory::applies)
                .reduce(Predicate::or)
                .orElse(clazz -> false);
    }


    /**
     * Fluent builder for constructing DelegatingAdaptorFactory instances
     */
    public static class Builder {
        private List<FilteringTypeAdaptorFactory> delegateFactories;

        private Builder() {
            delegateFactories = new ArrayList<>();
        }

        /**
         * Add a delegate factory with the given condition. The condition replaces any condition
         * on the {@link TypeAdapterFactory} instance.
         * <p>
         *     See {@link DelegatingAdaptorFactory} for details on delegate selection.
         * </p>
         * @param condition the condition for delegating to this factory
         * @param factory the factory to delegate to
         * @return self
         */
        public Builder addDelegateFactory(Predicate<TypeRequirement> condition, TypeAdapterFactory factory) {
            delegateFactories.add(new BasicAdaptorFilter(condition, factory));
            return this;
        }

        /**
         * Add a delegate {@link FilteringTypeAdaptorFactory}. This factory will be delegated to this
         * factory based on its filtering predicate.
         * <p>
         *     See {@link DelegatingAdaptorFactory} for details on delegate selection.
         * </p>
         * @param factory the factory
         * @return self
         */
        public Builder addDelegateFactory(FilteringTypeAdaptorFactory factory) {
            delegateFactories.add(factory);
            return this;
        }

        /**
         * Construct a new {@link DelegatingAdaptorFactory} with the configuration
         * of this builder.
         * @return the new factory instance
         */
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
        throw new IllegalArgumentException("unable to find TypeAdaptor for " + clazz.getSimpleName() + " and " +
                "Content-Type: " + contentType);
    }

    public static Builder builder() {
        return new Builder();
    }
}
