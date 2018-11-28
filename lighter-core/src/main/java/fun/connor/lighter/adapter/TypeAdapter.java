package fun.connor.lighter.adapter;

import fun.connor.lighter.handler.TypeMarshalException;

/**
 * Basic interface for classes that provide serialization and deserialization. TypeAdaptoes are normally
 * expected to support both serialization and deserialization of their specialized type. However, TypeAdapter
 * implementations can choose to throw a {@link TypeMarshalException} for any reason, including unsupported
 * operations.
 * <br><br>
 * TypeAdapters are expected to handle error cases. TypeAdapter implementations should never assume that serialized
 * data provided to them is correct.
 * <br><br>
 * TypeAdapters for type-parameterized objects like collections are expected to work for the erasure of the provided
 * type. For example, a TypeAdaptor for {@code List<String>} is expected to support any {@code List} instance, whether
 * or not the list contains strings.
 * <br><br>
 * In the future, this interface will contain methods for serializing and deserializing directly from input and
 * output streams.
 * @param <T> The type supported by this TypeAdaptor
 */
public interface TypeAdapter<T> {
    /**
     * Serialize an object to a String.
     * @param type an instance of T to serialize
     * @return the provided instance, serialized to a string
     * @throws TypeMarshalException if any unrecoverable error occurs during execution
     */
    String serialize(T type) throws TypeMarshalException;

    /**
     * Deserialize an object from a string. This method <strong>should never</strong> assume the serializedData
     * is a correct serialized instance of T.
     * @param serializedData An instance of T encoded as a String
     * @return The provided instance as a Java object
     * @throws TypeMarshalException if any unrecoverable error occurs during execution
     */
    T deserialize(String serializedData) throws TypeMarshalException;
}
