package fun.connor.lighter.example.persistance;

import fun.connor.lighter.example.domain.Name;
import fun.connor.lighter.example.domain.Person;

public interface PersonRepository {
    void createPerson(Person person);
    Person readPerson(Name name);
    void updatePerson(Person person);
    Person deletePerson(Person person);
}
