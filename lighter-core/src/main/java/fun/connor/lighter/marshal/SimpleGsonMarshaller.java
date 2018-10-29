package fun.connor.lighter.marshal;

import com.google.gson.Gson;
import fun.connor.lighter.handler.TypeMarshalException;
import fun.connor.lighter.handler.TypeMarshaller;

public class SimpleGsonMarshaller implements TypeMarshaller {

    private Gson gson;

    public SimpleGsonMarshaller() {
        gson = new Gson();
    }

    @Override @SuppressWarnings("unchecked")
    public <T> T marshal(String value, Class<T> clazz) throws TypeMarshalException {
        if (clazz.isPrimitive()) {
            return null; //TODO: handle primitives
        }
        if (clazz.isAssignableFrom(String.class)) {
            return (T) value;
        }
        return gson.fromJson(value, clazz);
    }
}
