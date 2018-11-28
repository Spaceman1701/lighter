package fun.connor.lighter.response;

/**
 * Functional interface for transforming applying transformations to responses.
 * Takes advantage of the type system to enforce rules about transformations. Combined
 * with {@link Response#with(ResponseDecorator)} these objects can be used to in a fluent
 * interface style.
 * @param <T> the input response content type
 * @param <R> the output response content type
 */
@FunctionalInterface
public interface ResponseDecorator<T, R> {
    /**
     * Apply a transformation to a {@link ResponseState}.
     * @param from the initial state
     * @return the state with the transformation applied
     */
    ResponseState<R> apply(ResponseState<T> from);
}
