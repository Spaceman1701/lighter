package fun.connor.lighter.marshal.gson;

import com.google.gson.Gson;
import fun.connor.lighter.adapter.FilteringTypeAdaptorFactory;
import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeRequirement;
import fun.connor.lighter.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

/**
 * {@link fun.connor.lighter.adapter.TypeAdapterFactory} implementation for any Java object and
 * Media Type application/json. Implemented using GSON. This will be moved to its own package soon.
 */
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
    public <T> TypeAdapter<T> getAdapter(Class<T> clazz, String contentType) {
        log.debug("asked to make adaptor for {}, Content-Type: {}", clazz.getSimpleName(), contentType);
        com.google.gson.TypeAdapter<T> adapter = gson.getAdapter(clazz);
        return new GsonTypeAdapter<>(adapter);
    }

    /**
     * @return Predicate that returns true for any Java type and Media Type application/json
     */
    public Predicate<TypeRequirement> applies() {
        return req -> req.getMediaType().equalsIgnoreCase(MediaType.APPLICATION_JSON);
    }
}
