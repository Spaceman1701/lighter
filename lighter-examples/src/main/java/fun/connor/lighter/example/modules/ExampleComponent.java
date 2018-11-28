package fun.connor.lighter.example.modules;

import dagger.Component;
//import fun.connor.lighter.generated.dependency.GeneratedReverseInjector;


/**
 * Example Dagger {@link Component} for injecting into the
 * {@link fun.connor.lighter.autoconfig.ReverseInjector}
 */
@Component(modules = ExampleDaggerModule.class)
public interface ExampleComponent {
//    void inject(GeneratedReverseInjector reverseInjector);
}
