package fun.connor.lighter.marshal.gson;

import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.handler.TypeMarshalException;

import java.io.IOException;

/**
 * {@link TypeAdapter} implementation for any Java object and Media Type application/json. Implemented
 * using GSON. This will be moved to its own package soon.
 * @param <T> The type this adapter applies to.
 */
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
            e.printStackTrace();
            throw new TypeMarshalException("failed to deserialize type", serializedData, Class.class);
        }
    }
}
