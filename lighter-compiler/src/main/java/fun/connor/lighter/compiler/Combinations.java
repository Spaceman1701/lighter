package fun.connor.lighter.compiler;


import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for creating all pairwise combinations of a list (i.e. Cartesian product).
 * This is used in the compiler several places to perform efficient global validations
 */
public final class Combinations {
    /**
     * Simple tuple of two objects
     * @param <T> the type of the contained objects
     */
    public static class Pair<T> {

        public final T first;
        public final T second;

        private Pair(final T first, final T second) {
            this.first = first;
            this.second = second;
        }

        public static <T> Pair<T> of(T first, T second) {
            return new Pair<>(first, second);
        }

        public T first() {
            return first;
        }

        public T second() {
            return second;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair)) return false;
            Pair that = (Pair) o;
            return first.equals(that.first) && second.equals(that.second);
        }
    }

    private Combinations() {}

    /**
     * Perform the cartensian product of a list with itself. Creates exactly one instance
     * of each pair of objects in the list without duplicates. No two pairs with the same
     * elements are produced (order doesn't matter). There is no pair where both objects are
     * the same.
     * <br>
     * This method is used to check each object in a list against each other object in a list exactly once.
     * Each pair contains one of
     * @param data the list to generate combinations of
     * @param <T> the type of the list elements
     * @return the list of combinations
     */
    public static <T> List<Pair<T>> CombinationsOf(List<T> data) {
        List<Pair<T>> combinations = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            T first = data.get(i);
            for (int j = i + 1; j < data.size(); j++) {
                combinations.add(Pair.of(first, data.get(j)));
            }
        }
        return combinations;
    }
}

