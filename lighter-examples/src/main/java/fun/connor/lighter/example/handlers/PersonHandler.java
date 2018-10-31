package fun.connor.lighter.example.handlers;

import fun.connor.lighter.declarative.Get;
import fun.connor.lighter.declarative.Post;
import fun.connor.lighter.declarative.QueryParams;
import fun.connor.lighter.declarative.ResourceController;
import fun.connor.lighter.example.domain.Name;
import fun.connor.lighter.example.domain.Person;
import fun.connor.lighter.example.persistance.PersonRepository;
import fun.connor.lighter.handler.RequestContext;
import fun.connor.lighter.handler.Response;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ResourceController("/person")
public class PersonHandler {

    private PersonRepository repository;

    @Inject
    public PersonHandler(PersonRepository repository) {
        this.repository = repository;
    }


    @Post @QueryParams({"first_name:firstName", "last_name:lastName", "use_full_name:useFullName"})
    public Response<Person> createPerson
            (String firstName, String lastName, Optional<Boolean> useFullName, RequestContext<Person> context) {
        boolean prefersFirstName = useFullName.orElse(false);

        Person person = new Person(new Name(firstName, lastName), prefersFirstName);
        System.out.println("created: " + person.getName());
        repository.createPerson(person);

        return context.getResponseBuilder()
                .content(person)
                .status(201)
                .build();
    }

    @Get("/say_hello/{firstName}/{lastName}")
    public Response<Map<String, String>> sayHello
            (String firstName, String lastName, RequestContext<Map<String, String>> context) {
        Name name = new Name(firstName, lastName);
        System.out.println("Saying hello to: " + name);
        Person person = repository.readPerson(name);

        String result = "Hello, " + person.getPreferredName() + "!";
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("words", result);
        return context.getResponseBuilder()
                .content(resultMap)
                .status(200)
                .build();

    }
}
