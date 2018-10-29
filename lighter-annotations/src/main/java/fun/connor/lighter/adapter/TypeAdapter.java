package fun.connor.lighter.adapter;

public interface TypeAdapter<T> {

    String serialize(T type);
    T deserialize(String serializedData, Class<T> type);
}
