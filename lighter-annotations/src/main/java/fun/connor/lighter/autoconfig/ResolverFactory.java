package fun.connor.lighter.autoconfig;

import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.handler.TypeMarshaller;

@FunctionalInterface
public interface ResolverFactory {
    LighterRequestResolver newInstance(TypeMarshaller marshaller);
}
