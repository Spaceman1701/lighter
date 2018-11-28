package fun.connor.lighter.http;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * MIME Media-Type. Not even close to implemented. Parsing MIME-Types needs
 * to be fast and bug-free. For now, this can be used as a set of common Media-Type
 * constants
 */
//TODO: FINISH
public final class MediaType implements Serializable {

    private static final long serialVersionUID = -3566972279775986472L;

    private static String APPLICATION = "application";
    private static String WILDCARD = "*";

    public static final String ANY = "*/*";
    public static final MediaType ANY_TYPE = new MediaType(WILDCARD, WILDCARD);

    public static final String APPLICATION_JAVASCRIPT = "application/javascript";
    public static final MediaType APPLICATION_JAVASCRIPT_TYPE = new MediaType(APPLICATION, "javascript");

    public static final String APPLICATION_JSON = "application/json";
    public static final MediaType APPLICATION_JSON_TYPE = new MediaType(APPLICATION, "json");

    public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final MediaType APPLICATION_X_WWW_FORM_URLENCODED_TYPE = new MediaType(APPLICATION, "x-www-form-urlencoded");

    public static final String APPLICATION_XML = "application/xml";
    public static final MediaType APPLICATION_XML_TYPE = new MediaType(APPLICATION, "xml");

    public static final String APPLICATION_ZIP = "application/zip";
    public static final MediaType APPLICATION_ZIP_TYPE = new MediaType(APPLICATION, "zip");

    public static final String APPLICATION_PDF = "application/pdf";
    public static final MediaType APPLICATION_PDF_TYPE = new MediaType(APPLICATION, "pdf");

    public static final String APPLICATION_SQL = "application/sql";
    public static final MediaType APPLICATION_SQL_TYPE = new MediaType(APPLICATION, "sql");

    public static final String APPLICATION_GRAPHQL = "application/graphql";
    public static final MediaType APPLICATION_GRAPHQL_TYPE = new MediaType(APPLICATION, "graphql");

    public static final String APPLICATION_LD_JSON = "application/ld+json";
    public static final MediaType APPLICATION_LD_JSON_TYPE = new MediaType(APPLICATION, "ld+json");

    public static final String AUDIO_MPEG = "audio/mpeg";
    public static final MediaType AUDIO_MPEG_TYPE = new MediaType("audio", "mpeg");

    public static final String AUDIO_OGG = "audio/ogg";
    public static final MediaType AUDIO_OGG_TYPE = new MediaType("audio", "ogg");

    public static final String MUTLIPART_FORM_DATA = "multipart/form-data";
    public static final MediaType MULTIPART_FORM_DATA_TYPE = new MediaType("multipart", "form-data");

    public static final String TEXT_CSS = "text/css";
    public static final MediaType TEXT_CSS_TYPE = new MediaType("text", "css");

    public static final String TEXT_HTML = "text/html";
    public static final MediaType TEXT_HTML_TYPE = new MediaType("text", "html");

    public static final String TEXT_XML = "text/xml";
    public static final MediaType TEXT_XML_TYPE = new MediaType("text", "xml");

    public static final String TEXT_CSV = "text/csv";
    public static final MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");

    public static final String TEXT_PLAIN = "text/plain";
    public static final MediaType TEXT_PLAIN_TYPE = new MediaType("text", "plain");

    public static final String IMAGE_PNG = "image/png";
    public static final MediaType IMAGE_PNG_TYPE = new MediaType("image", "png");

    public static final String IMAGE_JPEG = "image/jpeg";
    public static final MediaType IMAGE_JPEG_TYPE = new MediaType("image", "jpeg");

    public static final String IMAGE_GIF = "image/gif";
    public static final MediaType IMAGE_GIF_TYPE = new MediaType("image", "gif");


    private final String type;
    private final String subType;
    private final Map<String, String> parameters;

    /**
     * Construct a Media type with parameters
     * @param type the type
     * @param subType the subtype
     * @param parameters the parameters
     */
    public MediaType(final String type, final String subType, final Map<String, String> parameters) {
        this.type = type;
        this.subType = subType;
        this.parameters = parameters;
    }

    /**
     * Construct a media type with no parameters
     * @param type the type
     * @param subType the subtype
     */
    public MediaType(final String type, final String subType) {
        this(type, subType, Collections.emptyMap());
    }


    /**
     * Parse a MediaType from a IANA Media type string
     * @param str the IANA String
     * @return the MediaType instance representing the string
     * @throws IllegalArgumentException if the the parameter is malformed
     */
    public static MediaType from(final String str) {
        return null;
    }

    /**
     * @return the IANA type of this Media Type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the IANA subtype of this Media type
     */
    public String getSubType() {
        return subType;
    }

    /**
     * @return the IANA parameters of this Media type
     */
    public Map<String, String> getParameters() {
        return new HashMap<>(parameters);
    }
}
