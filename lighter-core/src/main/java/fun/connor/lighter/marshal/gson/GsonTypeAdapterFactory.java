package fun.connor.lighter.marshal.gson;

import com.google.gson.Gson;
import fun.connor.lighter.adapter.FilteringTypeAdaptorFactory;
import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeAdapterFactory;

import java.util.function.Predicate;

public class GsonTypeAdapterFactory implements FilteringTypeAdaptorFactory {

    private Gson gson;

    public GsonTypeAdapterFactory(Gson gson) {
        this.gson = gson;
    }

    public static GsonTypeAdapterFactory create() {
        return new GsonTypeAdapterFactory(new Gson());
    }

    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz) {
        System.out.println("asked to make adaptor for " + clazz.getSimpleName());
        com.google.gson.TypeAdapter<T> adapter = gson.getAdapter(clazz);
        return new GsonTypeAdapter<>(adapter);
    }

    public Predicate<Class<?>> applies() {
        return clazz -> true;
    }
}
