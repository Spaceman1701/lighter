package fun.connor.lighter.example.domain;

/**
 * Represents a person's name in the application domain. Simple POJO.
 */
public final class Name {
    private final String firstName;
    private final String lastName;

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean equals(Object o) {
        if (o != null && o.getClass() == Name.class) {
            Name otherName = (Name) o;
            return otherName.firstName.equals(firstName)
                    && otherName.lastName.equals(lastName);
        }
        return false;
    }

    public int hashCode() {
        return firstName.hashCode() * lastName.hashCode();
    }

    @Override
    public String toString() {
        return "Name: " + firstName + ", " + lastName;
    }
}
