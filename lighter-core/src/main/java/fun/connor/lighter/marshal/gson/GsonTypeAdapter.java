package fun.connor.lighter.marshal.gson;

import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.handler.TypeMarshalException;

import java.io.IOException;

public class GsonTypeAdapter<T> implements TypeAdapter<T> {

    private com.google.gson.TypeAdapter<T> adapter;

    GsonTypeAdapter(com.google.gson.TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public String serialize(T type) throws TypeMarshalException {
        return adapter.toJson(type);
    }

    @Override
    public T deserialize(String serializedData) throws TypeMarshalException {
        try {
            return adapter.fromJson(serializedData);
        } catch (IOException e) {
            throw new TypeMarshalException("failed to deserialize type", serializedData, Class.class);
        }
    }
}
