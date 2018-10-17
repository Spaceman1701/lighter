package fun.connor.lighter.handler;

public class TypeMarshalException extends RuntimeException {

    private final String serializedData;
    private final Class<?> expectedType;

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
