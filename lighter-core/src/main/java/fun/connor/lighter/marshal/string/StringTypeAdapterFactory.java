package fun.connor.lighter.marshal.string;

import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.handler.TypeMarshalException;

import java.util.function.Predicate;

public class StringTypeAdapterFactory implements TypeAdapterFactory {

    public static Predicate<Class<?>> applies() {
        return clazz -> clazz.isAssignableFrom(String.class);
    }


    @Override @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz) {
        if (clazz.isAssignableFrom(String.class)) {
            return (TypeAdapter<T>) new TypeAdapter<String>() {
                @Override
                public String serialize(String type) throws TypeMarshalException {
                    return type;
                }

                @Override
                public String deserialize(String serializedData) throws TypeMarshalException {
                    return serializedData;
                }
            };
        }
        return null;
    }
}
