package fun.connor.lighter.adapter;

/**
 * Factory for constructing new TypeAdapter instances. All TypeAdapters used by
 * Lighter will be constructed using this factory. The TypeAdapters created by this
 * factory must be capable of fulfilling the TypeAdapter interface requirements (see {@link TypeAdapter}
 * for the given type.
 * <br>
 * There is no requirement that every instance (or any instance) of TypeAdapter created by this
 * factory is unique.
 * <br>
 * Implementations of this class <strong>MUST</strong> be threadsafe.
 */
public interface TypeAdapterFactory {
    /**
     * Factory method. This method must provide a TypeAdapter capable for fulfilling the TypeAdapter interface
     * requirements for the given type. There is no requirement that implementations of this method return a unique
     * instance of any combination of parameters.
     * @param clazz the type which requires a TypeAdapter
     * @param mediaType The IANA Media Type for which serialized data must conform to
     * @param <T> The type which requires a TypeAdapter
     * @return a non-null TypeAdapter for the provided constraints
     */
    <T> TypeAdapter<T> getAdapter(Class<T> clazz, String mediaType);
}
