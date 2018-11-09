package fun.connor.lighter.adapter;

import fun.connor.lighter.handler.TypeMarshalException;

public interface TypeAdapter<T> {

    String serialize(T type) throws TypeMarshalException;
    T deserialize(String serializedData) throws TypeMarshalException;
}
