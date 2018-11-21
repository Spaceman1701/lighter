package fun.connor.lighter.adapter;

public interface TypeAdapterFactory {
    <T> TypeAdapter<T> getAdapter(Class<T> clazz, String mediaType);
}
