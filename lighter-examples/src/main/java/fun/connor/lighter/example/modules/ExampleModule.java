package fun.connor.lighter.example.modules;

import com.google.inject.AbstractModule;
import fun.connor.lighter.example.persistance.FoobarRepoImpl;
import fun.connor.lighter.example.persistance.FoobarRepository;

public class ExampleModule extends AbstractModule {

    @Override
    public void configure() {
        bind(FoobarRepository.class).to(FoobarRepoImpl.class);
    }
}
