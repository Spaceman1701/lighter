package fun.connor.lighter.adapter;

import java.util.function.Predicate;

public interface FilteringTypeAdaptorFactory extends TypeAdapterFactory {
    Predicate<TypeRequirement> applies();
}
