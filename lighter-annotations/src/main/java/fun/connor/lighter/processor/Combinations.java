package fun.connor.lighter.processor;


import java.util.ArrayList;
import java.util.List;

public class Combinations {
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
    }

    private Combinations() {}

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

