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

    @Override
    public boolean equals(Object other) {
        if (other != null && other.getClass().isAssignableFrom(Person.class)) {
            Person otherPerson = (Person) other;
            return otherPerson.getName().equals(name) && otherPerson.prefersFullName == prefersFullName;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + (prefersFullName ? 0 : 7);
    }

    public boolean prefersFullName() {
        return prefersFullName;
    }
}
