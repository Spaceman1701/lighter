package fun.connor.lighter.example.modules;

import dagger.Component;
import fun.connor.lighter.example.handlers.FoobarHandler;

@Component(modules = ExampleDaggerModule.class)
public interface ExampleComponent {
    FoobarHandler foobarHandler();
}
