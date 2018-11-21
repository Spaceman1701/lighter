package fun.connor.lighter.adapter;

public class TypeRequirement {
    private final String mediaType;
    private final Class<?> clazz;

    public TypeRequirement(final Class<?> clazz, final String mediaType) {
        this.clazz = clazz;
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
