package fun.connor.lighter.example.persistance;

import fun.connor.lighter.example.domain.Foobar;

public class FoobarRepoImpl implements FoobarRepository {
    @Override
    public void create(Foobar foobar) {

    }

    @Override
    public Foobar getByName(String name) {
        Foobar bar = new Foobar();
        bar.aField = "foo";
        return bar;
    }

    @Override
    public void update(Foobar foobar) {

    }

    @Override
    public void delete(Foobar foobar) {

    }
}
