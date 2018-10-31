package fun.connor.lighter.example.persistance;

import fun.connor.lighter.example.domain.Name;
import fun.connor.lighter.example.domain.Person;

import java.util.HashMap;
import java.util.Map;

public class InMemoryPersonRepo implements PersonRepository {

    private Map<Name, Person> people;

    public InMemoryPersonRepo() {
        people = new HashMap<>();
    }

    @Override
    public void createPerson(Person person) {
        System.out.println("adding person: " + person.getName());
        people.put(person.getName(), person);
    }

    @Override
    public Person readPerson(Name name) {
        System.out.println("reading person:" + name);
        return people.get(name);
    }

    @Override
    public void updatePerson(Person person) {
        people.put(person.getName(), person);
    }

    @Override
    public Person deletePerson(Person person) {
        return people.remove(person.getName());
    }
}
