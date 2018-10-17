package fun.connor.lighter.handler;

public interface TypeMarshaller {
    <T> T marshal(String value, Class<T> clazz) throws TypeMarshalException;
}
