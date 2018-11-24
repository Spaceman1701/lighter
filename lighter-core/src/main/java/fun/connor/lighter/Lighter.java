package fun.connor.lighter;


import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.autoconfig.RouteConfiguration;
import fun.connor.lighter.global.GlobalRequestTransformer;
import fun.connor.lighter.injection.InjectionObjectFactory;

public interface Lighter {
    void start();
    void stop();

    interface Builder {
        Builder addRouter(RouteConfiguration configuration);

        Builder injectionFactory(InjectionObjectFactory factory);

        Builder adapterFactory(TypeAdapterFactory factory);

        Builder port(int port);

        Builder hostName(String hostName);

        Builder showBanner(boolean show);

        Builder addResponseTransformer(GlobalRequestTransformer transformer);

        Lighter build();
    }
}
