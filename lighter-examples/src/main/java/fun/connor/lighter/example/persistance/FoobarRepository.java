package fun.connor.lighter.example.persistance;

import fun.connor.lighter.example.domain.Foobar;

public interface FoobarRepository {

    void create(Foobar foobar);

    Foobar getByName(String name);

    void update(Foobar foobar);

    void delete(Foobar foobar);
}
