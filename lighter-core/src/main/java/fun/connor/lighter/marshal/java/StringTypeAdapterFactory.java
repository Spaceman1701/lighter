package fun.connor.lighter.marshal.java;

import fun.connor.lighter.adapter.FilteringTypeAdaptorFactory;
import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeRequirement;
import fun.connor.lighter.handler.TypeMarshalException;
import fun.connor.lighter.http.MediaType;

import java.util.function.Predicate;

public class StringTypeAdapterFactory implements FilteringTypeAdaptorFactory {

    public Predicate<TypeRequirement> applies() {
        return req ->
            req.getClazz().isAssignableFrom(String.class) && req.getMediaType().equalsIgnoreCase(MediaType.TEXT_PLAIN);
    }


    @Override @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz, String contentType) {
        if (applies().test(new TypeRequirement(clazz, contentType))) {
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
