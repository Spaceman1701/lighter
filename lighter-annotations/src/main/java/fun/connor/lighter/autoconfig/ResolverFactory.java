package fun.connor.lighter.autoconfig;

import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.handler.TypeMarshaller;
import fun.connor.lighter.injection.InjectionObjectFactory;

@FunctionalInterface
public interface ResolverFactory {
    LighterRequestResolver newInstance(InjectionObjectFactory genericFactory, TypeAdapterFactory marshaller);
}
