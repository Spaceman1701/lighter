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

    private PersonRepository repository;

    @Inject
    public SubjectFactory(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public final Subject newInstance(Map<String, String> pathParams, Map<String, String> queryParams,
                                     Request request, TypeAdapterFactory adapterFactory) {

        return null;
    }
}
