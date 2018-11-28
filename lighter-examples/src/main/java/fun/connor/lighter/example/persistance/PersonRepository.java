package fun.connor.lighter.example.persistance;

import fun.connor.lighter.example.domain.Name;
import fun.connor.lighter.example.domain.Person;

import java.util.List;

/**
 * DAO for {@link Person} instances
 */
public interface PersonRepository {
    /**
     * Create a new person in the repository
     * @param person the person to create
     */
    void createPerson(Person person);

    /**
     * Fetch a person from the repository by name.
     * @param name the name of the person to fetch
     * @return the person or {@code null} if the person does not exist
     */
    Person readPerson(Name name);

    /**
     * Fetch all {@link Person} instances from the repository
     * @return a list of all {@link Person} objects
     */
    List<Person> getAllPeople();

    /**
     * Update a person in the repository
     * @param person the person to update
     */
    void updatePerson(Person person);

    /**
     * Delete a person from the repository
     * @param person the person to delete
     * @return the actual deleted person
     */
    Person deletePerson(Person person);
}
