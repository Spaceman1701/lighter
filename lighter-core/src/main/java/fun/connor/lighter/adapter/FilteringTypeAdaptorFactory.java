package fun.connor.lighter.adapter;

import java.util.function.Predicate;

/**
 * Extension of the {@link TypeAdapterFactory} interface which allows the factory declare a filter
 * for the Java types and IANA Media Types it supports.
 */
public interface FilteringTypeAdaptorFactory extends TypeAdapterFactory {

    /**
     * @return a filtering predicate that returns <code>true</code> iff this factory
     * can support instances of the given {@link TypeRequirement}
     */
    Predicate<TypeRequirement> applies();
}
