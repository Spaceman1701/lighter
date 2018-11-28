package fun.connor.lighter.example.handlers;

import fun.connor.lighter.declarative.*;
import fun.connor.lighter.example.domain.Name;
import fun.connor.lighter.example.domain.Person;
import fun.connor.lighter.example.domain.Subject;
import fun.connor.lighter.example.persistance.PersonRepository;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.http.StatusCodes;
import fun.connor.lighter.response.Response;
import fun.connor.lighter.response.Responses;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Lighter {@link ResourceController} for {@link Person} related operations. This class demonstrates
 * several features of Lighter. First, the person handler has a dependency on the {@link PersonRepository}.
 * Lighter uses the {@link fun.connor.lighter.injection.InjectionObjectFactory} provided at configuration
 * time to construct them. Second, this class contains several different endpoint methods which each
 * demonstrate the usage of different annotations from the {@link fun.connor.lighter.declarative} package.
 */
@ResourceController("/person")
public class PersonHandler {

    private PersonRepository repository;

    /**
     * Create a new handler with a repository for persistence.
     * @param repository the repository
     */
    @Inject
    public PersonHandler(PersonRepository repository) {
        this.repository = repository;
    }


    @Post @QueryParams({"first_name:firstName", "last_name:lastName", "use_full_name:useFullName"})
    public Response<Person> createPerson
            (String firstName, String lastName, Optional<Boolean> useFullName, Request request) {
        boolean prefersFirstName = useFullName.orElse(false);

        Person person = new Person(new Name(firstName, lastName), prefersFirstName);
        System.out.println("created: " + person.getName());
        repository.createPerson(person);

        return Responses.json(person, StatusCodes.CREATED);
    }

    /**
     * Retrieve a person from the repository and response with a hello message
     * for that person. This method demonstrates Lighter's automatic path parameter
     * marshalling and Lighter's response API.
     * @param firstName The first name of the person
     * @param lastName The last name of the person
     * @param request the request
     * @return a response with a map containing the hello message.
     */
    @Get("/say_hello/{firstName}/{lastName}")
    public Response<Map<String, String>> sayHello(String firstName, String lastName, Request request) {
        Name name = new Name(firstName, lastName);
        System.out.println("Saying hello to: " + name);
        Person person = repository.readPerson(name);

        String result = "Hello, " + person.getPreferredName() + "!";
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("words", result);
        return Responses.json(resultMap);

    }

    /**
     * Update a person in the repository. This method demonstrates Lighter's {@link Body} binding
     * as well as the {@link fun.connor.lighter.handler.RequestGuard} feature. In this case, the
     * {@link Subject} RequestGuard is used to provide authentication information to the endpoint.
     * @param person the person to update
     * @param subject the subject which the response is being executed on the behalf of
     * @return a response with the actual updated person
     */
    @Put
    public Response<Person> updatePerson(@Body Person person, Subject subject) {
        if (!subject.isAdmin()) {
           return Responses.noContent(StatusCodes.OK);
        }

        repository.updatePerson(person);
        return Responses.json(person, StatusCodes.OK);
    }

    /**
     * Get all people in the repository. This method demonstrates Lighter's ability to
     * serialize arbitrary collections.
     * @return a response with the list of people in the repository
     */
    @Get("/all")
    public Response<List<Person>> getAll() {
        List<Person> people = repository.getAllPeople();
        return Responses.json(people, StatusCodes.OK);
    }
}
