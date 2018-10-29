package fun.connor.lighter.example.handlers;

import fun.connor.lighter.declarative.Get;
import fun.connor.lighter.declarative.QueryParams;
import fun.connor.lighter.declarative.ResourceController;
import fun.connor.lighter.example.domain.Foobar;
import fun.connor.lighter.example.persistance.FoobarRepository;
import fun.connor.lighter.handler.RequestContext;
import fun.connor.lighter.handler.Response;
import fun.connor.lighter.handler.ResponseBuilder;

import javax.inject.Inject;
import java.util.Optional;


@ResourceController("/foobar")
public class FoobarHandler {

    private final FoobarRepository repository;

    @Inject
    public FoobarHandler(final FoobarRepository repository) {
        this.repository = repository;
    }


    @Get("/{name:name}")
    @QueryParams({"count:count"})
    public Response<Foobar<Integer>> getFoobarByName(String name, Optional<Integer> count, RequestContext<Foobar<Integer>> context) {

        Foobar<Integer> foobar = repository.getByName(name);

        ResponseBuilder<Foobar<Integer>> resp = context.getResponseBuilder();
        resp.status(200);
        resp.content(foobar);

        return resp.build();
    }

//    @Get
//    @Post
//    public Response<Foobar> createFoobar(@Body Foobar foobar) {
//
//        return null;
//    }
}
