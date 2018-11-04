package fun.connor.lighter.example.persistance;

import fun.connor.lighter.example.domain.Name;
import fun.connor.lighter.example.domain.Person;

import java.util.List;

public interface PersonRepository {
    void createPerson(Person person);
    Person readPerson(Name name);
    List<Person> getAllPeople();
    void updatePerson(Person person);
    Person deletePerson(Person person);
}
