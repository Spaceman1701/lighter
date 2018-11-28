package fun.connor.lighter.example.domain;

import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.declarative.ProducesRequestGuard;
import fun.connor.lighter.example.persistance.PersonRepository;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.handler.RequestGuardFactory;

import javax.inject.Inject;
import java.util.Map;

/**
 * {@link ProducesRequestGuard} factory for {@link Subject} objects. This class demonstrates
 * using the Request guard for authentication. While the implementation here is very simple,
 * it shows the general design of Request Guard based implementations. This class reads the
 * Authorization header from the raw request, parses the username and password strings, and checks
 * that they match the super-secure passphrase for the server. Finally, it constructs a {@link Subject}
 * implementation that reflects the results of this processing.
 * <p>
 *     This allows implementations to require user authentication without coupling them with authentication
 *     logic. In fact, consumers of the {@link Subject} object don't even have to know that the this
 *     factory exists! Authentication is complete decoupled from the business logic.
 * </p>
 */
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
