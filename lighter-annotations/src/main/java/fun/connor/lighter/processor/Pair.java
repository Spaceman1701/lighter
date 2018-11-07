package fun.connor.lighter.processor;

public final class Pair<T, K> {

    public final T first;
    public final K second;

    private Pair(final T first, final K second) {
        this.first = first;
        this.second = second;
    }

    public static <T, K> Pair<T, K> of(T first, K second) {
        return new Pair<>(first, second);
    }

    public T first() {
        return first;
    }

    public K second() {
        return second;
    }
}
