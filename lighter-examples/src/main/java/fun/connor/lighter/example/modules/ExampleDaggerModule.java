package fun.connor.lighter.example.modules;

import dagger.Module;
import dagger.Provides;
import fun.connor.lighter.example.persistance.FoobarRepoImpl;
import fun.connor.lighter.example.persistance.FoobarRepository;

@Module
public class ExampleDaggerModule {
    @Provides static FoobarRepository provideFoobarRepository() {
        return new FoobarRepoImpl();
    }
}
