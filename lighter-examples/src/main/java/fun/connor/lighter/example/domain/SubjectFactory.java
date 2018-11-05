package fun.connor.lighter.example.domain;

import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.declarative.ProducesRequestGuard;
import fun.connor.lighter.example.persistance.PersonRepository;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.handler.RequestGuardFactory;

import javax.inject.Inject;
import java.util.Map;

@ProducesRequestGuard(Subject.class)
public class SubjectFactory implements RequestGuardFactory<Subject> {

    /**
     * Obviously, a very good security policy
     */
    private static final Name adminName = new Name("Ethan", "Hunter");

    private PersonRepository repository;

    @Inject
    public SubjectFactory(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public final Subject newInstance(Map<String, String> pathParams, Map<String, String> queryParams,
                                     Request request, TypeAdapterFactory adapterFactory) {
        TypeAdapter<Person> personTypeAdapter = adapterFactory.getAdapter(Person.class);
        Person person = personTypeAdapter.deserialize(request.getBody());

        if (person == null) {
            throw new IllegalArgumentException("cannot make a subject from this request!");
        }
        boolean isAdmin = person.getName().equals(adminName);
        return new Subject() {
            @Override
            public Object getId() {
                return person;
            }

            @Override
            public boolean isAdmin() {
                return isAdmin;
            }
        };
    }
}
