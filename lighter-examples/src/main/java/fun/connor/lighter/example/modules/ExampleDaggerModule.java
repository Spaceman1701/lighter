package fun.connor.lighter.example.modules;


import dagger.Module;
import dagger.Provides;
import fun.connor.lighter.example.persistance.InMemoryPersonRepo;
import fun.connor.lighter.example.persistance.PersonRepository;

/**
 * An example Dagger {@link Module} which defines a provider for
 * {@link PersonRepository}
 */
@Module
public class ExampleDaggerModule {

    @Provides static PersonRepository providesPersonRepository() {
        return new InMemoryPersonRepo();
    }
}
