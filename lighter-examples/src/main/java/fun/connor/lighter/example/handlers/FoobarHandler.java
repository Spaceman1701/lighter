package fun.connor.lighter.example.handlers;

import fun.connor.lighter.declarative.*;
import fun.connor.lighter.example.domain.Foobar;
import fun.connor.lighter.example.persistance.FoobarRepository;
import fun.connor.lighter.handler.RequestContext;
import fun.connor.lighter.handler.Response;
import fun.connor.lighter.handler.ResponseBuilder;


@ResourceController("/foobar")
public class FoobarHandler {

    private final FoobarRepository repository;

    public FoobarHandler(final FoobarRepository repository) {
        this.repository = repository;
    }


    @Get("/{name:name}")
    @QueryParams({"count:count"})
    public Response<Foobar<Integer>> getFoobarByName(String name, Integer count, RequestContext<Foobar<Integer>> context) {

        Foobar<Integer> foobar = repository.getByName(name);

        ResponseBuilder<Foobar<Integer>> resp = context.getResponseBuilder();
        resp.status(200);
        resp.content(foobar);
        resp.putHeader("This-Is-A-HTTP-Header", "wwwwoooww");

        return resp.build();
    }

//    @Get
//    @Post
//    public Response<Foobar> createFoobar(@Body Foobar foobar) {
//
//        return null;
//    }
}
