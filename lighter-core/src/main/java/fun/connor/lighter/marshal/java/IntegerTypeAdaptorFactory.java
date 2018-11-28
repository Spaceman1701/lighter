package fun.connor.lighter.marshal.java;

import fun.connor.lighter.adapter.FilteringTypeAdaptorFactory;
import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeRequirement;
import fun.connor.lighter.handler.TypeMarshalException;
import fun.connor.lighter.http.MediaType;

import java.util.function.Predicate;

/**
 * {@link fun.connor.lighter.adapter.TypeAdapterFactory} and {@link TypeAdapter} for the
 * {@link java.lang.Integer}
 */
public class IntegerTypeAdaptorFactory implements FilteringTypeAdaptorFactory {

    @Override
    public Predicate<TypeRequirement> applies() {
        return req -> req.getClazz().isAssignableFrom(Integer.class)
                && req.getMediaType().equalsIgnoreCase(MediaType.TEXT_PLAIN);
    }

    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz, String contentType) {
        if (clazz.isAssignableFrom(Integer.class)) {
            return new TypeAdapter<T>() {
                @Override
                public String serialize(T type) throws TypeMarshalException {
                    return type.toString();
                }

                @Override @SuppressWarnings("unchecked")
                public T deserialize(String serializedData) throws TypeMarshalException {
                    return (T) Integer.valueOf(Integer.parseInt(serializedData));
                }
            };
        }
        return null;
    }
}
