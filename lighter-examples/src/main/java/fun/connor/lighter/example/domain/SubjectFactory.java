package fun.connor.lighter.example.domain;

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

    private static final String passphrase = "parc";

    private PersonRepository repository;

    @Inject
    public SubjectFactory(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public final Subject newInstance(Map<String, String> pathParams, Map<String, String> queryParams,
                                     Request request, TypeAdapterFactory adapterFactory) {

        String authHeader = request.getHeaderValue("Authorization");

        //Username:Password
        String[] authParts = authHeader.split(":");
        if (authParts.length != 2) {
            throw new IllegalArgumentException("cannot read authorization data from request");
        }

        boolean isAdmin = authParts[1].equals(passphrase);

        return new Subject() {
            @Override
            public Object getId() {
                return authParts[0];
            }

            @Override
            public boolean isAdmin() {
                return isAdmin;
            }
        };
    }
}
