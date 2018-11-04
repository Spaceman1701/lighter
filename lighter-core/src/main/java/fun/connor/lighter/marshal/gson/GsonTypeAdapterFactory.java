package fun.connor.lighter.marshal.gson;

import com.google.gson.Gson;
import fun.connor.lighter.adapter.FilteringTypeAdaptorFactory;
import fun.connor.lighter.adapter.TypeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class GsonTypeAdapterFactory implements FilteringTypeAdaptorFactory {

    private static final Logger log = LoggerFactory.getLogger(GsonTypeAdapterFactory.class);

    private Gson gson;

    public GsonTypeAdapterFactory(Gson gson) {
        this.gson = gson;
    }

    public static GsonTypeAdapterFactory create() {
        return new GsonTypeAdapterFactory(new Gson());
    }

    @Override
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz) {
        log.debug("asked to make adaptor for {}", clazz.getSimpleName());
        com.google.gson.TypeAdapter<T> adapter = gson.getAdapter(clazz);
        return new GsonTypeAdapter<>(adapter);
    }

    public Predicate<Class<?>> applies() {
        return clazz -> true;
    }
}
