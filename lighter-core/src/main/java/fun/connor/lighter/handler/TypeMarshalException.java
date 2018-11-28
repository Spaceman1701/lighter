package fun.connor.lighter.handler;

/**
 * Type marshalling exception. This exception can be thrown
 * for any unrecoverable error when attempting to marshal a type. This includes
 * errors when serializing and deserializing data as well as missing data.
 */
public class TypeMarshalException extends RuntimeException {

    private final String serializedData;
    private final Class<?> expectedType;

    /**
     * Construct a new TypeMarshalException with the given message for the given data
     * @param message exception message
     * @param serializedData the serialized data, if present
     * @param expectedType the expected Java type
     */
    public TypeMarshalException(String message, String serializedData, Class<?> expectedType) {
        super(message);
        this.serializedData = serializedData;
        this.expectedType = expectedType;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
