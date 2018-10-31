package fun.connor.lighter.example.domain;

public class Person {
    private Name name;

    private boolean prefersFullName;

    public Person(Name name, boolean prefersFullName) {
        this.name = name;
        this.prefersFullName = prefersFullName;
    }

    public Name getName() {
        return name;
    }

    public String getPreferredName() {
        if (prefersFullName) {
            return name.getFirstName() + " " + name.getLastName();
        } else {
            return name.getFirstName();
        }
    }

    public boolean prefersFullName() {
        return prefersFullName;
    }
}
