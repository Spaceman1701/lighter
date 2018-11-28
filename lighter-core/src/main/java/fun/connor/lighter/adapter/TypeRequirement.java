package fun.connor.lighter.adapter;

/**
 * Description of a serialization and deserialization requirement. Instances of this class
 * represent a requirement to be able to serialize an instance of a Java class into a specific
 * IANA Media type
 */
public class TypeRequirement {
    private final String mediaType;
    private final Class<?> clazz;

    /**
     * Construct a type requirement for the given class type and IANA media type string
     * @param clazz The Java class for which an instance must be serializable
     * @param mediaType The IANA media type which is required be the target of serialization
     */
    public TypeRequirement(final Class<?> clazz, final String mediaType) {
        this.clazz = clazz;
        this.mediaType = mediaType;
    }

    /**
     * @return The IANA Media type targeted by this requirement
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * @return The Java type targeted by this requirement
     */
    public Class<?> getClazz() {
        return clazz;
    }
}
