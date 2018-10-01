package fun.connor.lighter.example.handlers;

import fun.connor.lighter.declarative.*;
import fun.connor.lighter.example.domain.Foobar;
import fun.connor.lighter.example.persistance.FoobarRepository;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.handler.Response;
import fun.connor.lighter.handler.ResponseBuilder;


@ResourceController("/foobar")
public class FoobarHandler {

    private final FoobarRepository repository;

    public FoobarHandler(final FoobarRepository repository) {
        this.repository = repository;
    }


    @Get("{name:name}?count={count},anotherParam={anotherParam}")
    public Response<Foobar> getFoobarByName(String name, int count, ResponseBuilder<Foobar> resp) {

        Foobar foobar = repository.getByName(name);

        resp.status(200);
        resp.content(foobar);
        resp.putHeader("This-Is-A-HTTP-Header", "wwwwoooww");

        return resp.build();
    }

    @Get @Post
    public Response<Void> createFoobar(@Body Foobar foobar) {

        repository.create(foobar);

        return new Response<>(201);
    }
}
