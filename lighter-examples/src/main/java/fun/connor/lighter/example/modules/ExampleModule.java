package fun.connor.lighter.example.modules;

import com.google.inject.AbstractModule;
import fun.connor.lighter.example.persistance.InMemoryPersonRepo;
import fun.connor.lighter.example.persistance.PersonRepository;

public class ExampleModule extends AbstractModule {

    @Override
    public void configure() {
        bind(PersonRepository.class).to(InMemoryPersonRepo.class).asEagerSingleton();
    }
}
